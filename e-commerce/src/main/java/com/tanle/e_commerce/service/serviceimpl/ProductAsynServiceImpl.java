package com.tanle.e_commerce.service.serviceimpl;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.UpdateByQueryRequest;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.json.JsonData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LazilyParsedNumber;
import com.tanle.e_commerce.Repository.Jpa.ProductRepository;
import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.dto.ProductDocument;
import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.ProductMapper;
import com.tanle.e_commerce.mapper.SKUMapper;
import com.tanle.e_commerce.service.CategoryService;
import com.tanle.e_commerce.service.ProductAsycnService;

import com.tanle.e_commerce.service.ProductService;
import com.tanle.e_commerce.service.SKUService;
import com.tanle.e_commerce.utils.Status;
import org.elasticsearch.client.RequestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQuery;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductAsynServiceImpl implements ProductAsycnService {
    @Autowired
    private ElasticsearchOperations elasticsearchRestTemplate;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SKUService skuService;
    private final String INDEX_NAME = "product-index";
    private static final Logger LOG = LoggerFactory.getLogger(ProductAsycnService.class);

    @Override
    public void create(int entityId) {
        IndexCoordinates indexCoordinates = IndexCoordinates.of(INDEX_NAME);
        // Check if the index exists
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(indexCoordinates);
        if (!indexOperations.exists()) {

            indexOperations.create();
            System.out.println("Index created: " + INDEX_NAME);
        }
        Product product = productRepository.findById(entityId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found"));
        elasticsearchRestTemplate.save(productMapper.toDocument(product));
        LOG.info("Create Product - {}", product.getId());
    }


    @Override
    public void update(ProductDocument productDocument) {
        ProductDocument oldProduct = elasticsearchRestTemplate.get(String.valueOf(productDocument.getId()),
                ProductDocument.class, IndexCoordinates.of("product-index"));
        if (oldProduct.getCategory().getId() != productDocument.getCategory().getId()) {
            CategoryDTO categoryDTO = categoryService.findById(productDocument.getCategory().getId());
            productDocument.setCategory(categoryDTO);
        }
        updateNonNullFields(productDocument, oldProduct);
        // Execute the update request using the Elasticsearch template
        elasticsearchRestTemplate.update(oldProduct);
        LOG.info("update Product - {}", productDocument.getId());
    }

    @Override
    public void delete(int entityId) {
        elasticsearchRestTemplate.delete(String.valueOf(entityId), IndexCoordinates.of(INDEX_NAME));
        LOG.info("Delete Product - {}", entityId);
    }

    @Override
    public void deleteSku(int entityId, int skuId) {
        ProductDocument product = elasticsearchRestTemplate.get(String.valueOf(entityId),
                ProductDocument.class, IndexCoordinates.of("product-index"));
        for (SKUDTO skudto : product.getSkus()) {
            if (skudto.getSkuId() == skuId) {
                product.getSkus().remove(skudto);
                elasticsearchRestTemplate.update(product);
                break;
            }
        }
        LOG.info("Delete Product - {}", entityId);

    }

    @Override
    public void createSku(int entityId, int skuId) {
        ProductDocument product = elasticsearchRestTemplate.get(String.valueOf(entityId),
                ProductDocument.class, IndexCoordinates.of("product-index"));
        SKUDTO skudto = skuService.findById(skuId);
        product.getSkus().add(skudto);
        elasticsearchRestTemplate.update(product);
    }

    @Override
    public void updateCategory(int entityId, CategoryDTO categoryDTO) {
        List<UpdateQuery> updateQueries = new ArrayList<>();
        BoolQuery query = QueryBuilders.bool()
                .should(
                        builder -> builder.term(
                                prefix -> prefix.field("category.id").value(categoryDTO.getId()))
                )
                .build();
        NativeQuery searchQuery = new NativeQueryBuilder()
                .withQuery(query._toQuery())
                .build();
        SearchHits<ProductDocument> searchHits = elasticsearchRestTemplate.search(searchQuery, ProductDocument.class);
        if (searchHits.getSearchHits().isEmpty()) return;

        CategoryDTO oldCategory = searchHits.getSearchHits().get(0).getContent().getCategory();
        if (oldCategory.getLeft() != categoryDTO.getLeft() || oldCategory.getRight() != categoryDTO.getRight()) {
            List<String> path = List.of(categoryService.getSinglePath(categoryDTO.getId()).split(" > "));
            oldCategory.setPathCategory(path);
        }
        updateNonNullFields(categoryDTO, oldCategory);
        Document document = Document.create();
        document.put("category", oldCategory);
        for (SearchHit<ProductDocument> hit : searchHits.getSearchHits()) {
            ProductDocument product = hit.getContent();
            UpdateQuery updateQuery = UpdateQuery.builder(String.valueOf(product.getId()))
                    .withDocument(document) // Partial update document
                    .build();
            updateQueries.add(updateQuery);
        }
        elasticsearchRestTemplate.bulkUpdate(updateQueries, IndexCoordinates.of(INDEX_NAME));
        LOG.info("Bulk update successful");
    }

    @Override
    public void updateSKU(Integer productId, SKUDTO skudto) {
        ProductDocument product = elasticsearchRestTemplate.get(String.valueOf(productId),
                ProductDocument.class, IndexCoordinates.of("product-index"));
        SKUDTO skuOld = product.getSkus().stream()
                .filter(s -> s.getSkuId() == skudto.getSkuId())
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found sku"));
        if (skudto.getSkuStock() != skuOld.getSkuStock()) {
            product.setStock(product.getStock() - skudto.getSkuStock());
        }
        updateNonNullFields(skudto, skuOld);
        elasticsearchRestTemplate.update(product);
        LOG.info("update sku - {}", product);
    }

    private <T> void updateNonNullFields(T source, T target) {
        for (Field field : source.getClass().getDeclaredFields()) {
            field.setAccessible(true); // Allow access to private fields
            try {
                Object value = field.get(source);
                if (value != null) {  // Only update non-null values
                    field.set(target, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }
    }


}

