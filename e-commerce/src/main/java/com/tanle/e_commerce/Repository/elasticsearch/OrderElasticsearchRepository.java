package com.tanle.e_commerce.Repository.elasticsearch;

import com.tanle.e_commerce.dto.OrderDTO;
import com.tanle.e_commerce.entities.Order;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderElasticsearchRepository extends ElasticsearchRepository<OrderDTO,Integer> {

}
