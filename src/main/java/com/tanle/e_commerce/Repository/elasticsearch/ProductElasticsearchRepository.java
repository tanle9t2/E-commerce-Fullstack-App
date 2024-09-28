package com.tanle.e_commerce.Repository.elasticsearch;

import com.tanle.e_commerce.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDTO,Integer> {
}
