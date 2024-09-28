package com.tanle.e_commerce.service.serviceimpl;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.tanle.e_commerce.Repository.Jpa.OrderJpaRepository;
import com.tanle.e_commerce.Repository.Jpa.ProductRepository;
import com.tanle.e_commerce.Repository.elasticsearch.OrderElasticsearchRepository;
import com.tanle.e_commerce.Repository.elasticsearch.ProductElasticsearchRepository;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.Order;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.mapper.OrderMapper;
import com.tanle.e_commerce.mapper.ProductMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tanle.e_commerce.utils.AppConstant.INTERVAL_IN_MILLISECONDE;
import static com.tanle.e_commerce.utils.AppConstant.MODIFICATION_DATE;

@Service
public class ElasticsearchSynchronizerService {
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private ProductElasticsearchRepository productElasticsearchRepository;
    @Autowired
    private OrderElasticsearchRepository orderElasticsearchRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderMapper orderMapper;
    private static final Logger LOG = LoggerFactory.getLogger(ElasticsearchSynchronizerService.class);


    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void sync() {
        LOG.info("Start Syncing - {}", LocalDateTime.now());
        this.syncProduct();
        this.syncOrder();
        LOG.info(" End Syncing - {}", LocalDateTime.now());
    }
    private void syncProduct() {
        Specification<Product> productSpecification = (root, criteriaQuery, criteriaBuilder) ->
                getModificationDatePredicate(criteriaBuilder, root);
        List<Product> products = new ArrayList<>();
        if (productElasticsearchRepository.count() == 0) {
            products.addAll( productRepository.findAll());
        } else {
            List<Product> t = productRepository.findAll(productSpecification);
            products.addAll(t);
        }
        for(Product product: products) {
            LOG.info("Syncing Product - {}", product.getId());
            elasticsearchOperations.save(productMapper.asInput(product));
        }
    }
    private void syncOrder() {
        Specification<Order> orderSpecification = (root, criteriaQuery, criteriaBuilder) ->
                getModificationDatePredicate(criteriaBuilder, root);
        List<Order> orders = new ArrayList<>();
        if (orderElasticsearchRepository.count() == 0) {
            orders.addAll( orderJpaRepository.findAll());
        } else {
            orders.addAll(orderJpaRepository.findAll(orderSpecification));
        }
        for(Order order : orders) {
            LOG.info("Syncing Product - {}", order.getId());
            elasticsearchOperations.save(orderMapper.convertDTO(order));
        }
    }
    private static Predicate getModificationDatePredicate(CriteriaBuilder cb, Root<?> root) {
        Expression<Timestamp> currentTime;
        currentTime = cb.currentTimestamp();
        Expression<Timestamp> currentTimeMinus = cb.literal(new Timestamp(System.currentTimeMillis() -
                (INTERVAL_IN_MILLISECONDE)));
        return cb.between(root.<Date>get(MODIFICATION_DATE),
                currentTimeMinus,
                currentTime
        );
    }
}
