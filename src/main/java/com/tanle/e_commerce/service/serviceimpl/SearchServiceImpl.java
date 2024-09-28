package com.tanle.e_commerce.service.serviceimpl;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.ObjectBuilder;
import com.tanle.e_commerce.Repository.Jpa.SearchRepository;
import com.tanle.e_commerce.Repository.elasticsearch.ProductElasticsearchRepository;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.payload.PageResponse;
import com.tanle.e_commerce.request.SearchRequest;
import com.tanle.e_commerce.request.SortRequest;
import com.tanle.e_commerce.service.SearchService;
import com.tanle.e_commerce.utils.filter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private ProductElasticsearchRepository productElasticsearchRepository;

    @Override
    public PageResponse<Product> findBySpecs(Map<String, String> filter, int page, int size) {
        List<SpecSearchCriterial> andFilter = new ArrayList<>();
        List<SpecSearchCriterial> orFilter = new ArrayList<>();
        //filter
        String keyword = filter.get("keyword");
        Double minPrice = filter.get("minPrice") == null ? null : Double.parseDouble(filter.get("minPrice"));
        Double maxPrice = filter.get("maxPrice") == null ? null : Double.parseDouble(filter.get("maxPrice"));
        List<Object> categoryIds = filter.get("category") == null ? null :
                List.of(filter.get("category").split(","));
        SpecSearchCriterial categorySpec = SpecSearchCriterial.builder()
                .field("category.id")
                .operator(FilterOperation.IN)
                .values(categoryIds)
                .build();
        SpecSearchCriterial priceSpec = SpecSearchCriterial.builder()
                .field("price")
                .operator(FilterOperation.BETWEEN)
                .value(minPrice)
                .valueTo(maxPrice)
                .build();
        SpecSearchCriterial keywordSpec = SpecSearchCriterial.builder()
                .fields(List.of("name", "description"))
                .value(keyword)
                .operator(FilterOperation.IN)
                .build();
        andFilter.addAll(List.of(keywordSpec, priceSpec, categorySpec));
        //Sort
        String sortField = filter.get("sortBy");
        String direction = filter.get("order");
        SortRequest sortRequest = new SortRequest(sortField, SortDirection.fromString(direction));

        SearchRequest searchRequest = SearchRequest.builder()
                .filterAndConditions(andFilter)
                .sortRequests(List.of(sortRequest))
                .page(page)
                .size(size)
                .build();

        FilterSpecification specification = new FilterSpecification<>(searchRequest);
        Page<Product> products = searchRepository.findAll(specification, searchRequest.getPageable());

        if (products.getNumberOfElements() == 0) {
            return new PageResponse<>(Collections.emptyList(),
                    products.getNumber(), products.getNumberOfElements()
                    , products.getTotalElements(), HttpStatus.OK);
        }
        return new PageResponse<>(products.getContent(), products.getNumber(), products.getNumberOfElements()
                , products.getTotalElements(), HttpStatus.OK);
    }

    @Override
    public PageResponse<ProductDTO> searchProduct(Map<String, String> condition, int page, int size) {
        String keyword = condition.get("keyword");
        String order = condition.get("order");
        Double minPrice =  condition.get("minPrice") == null
                ? 0 : Double.parseDouble(condition.get("minPrice"));
        Double maxPrice = condition.get("maxPrice") == null
                ? Double.MAX_VALUE :Double.parseDouble(condition.get("maxPrice"));
        var boolQuery = QueryBuilders.bool();
        if (condition.get("category") != null) {
            String[] categoryIds = condition.get("category").toString().split(",");
            var categoryQuery = QueryBuilders.bool();
            for (var id : categoryIds) {
                categoryQuery.should(builder -> builder.term(t -> t.field("category.id").value(id)));
            }
            boolQuery.must(categoryQuery.build()._toQuery());
        }
        BoolQuery shouldQuery = QueryBuilders.bool().should(
                        builder -> builder.matchPhrasePrefix(
                                prefix -> prefix.field("name").query(keyword)
                                        .maxExpansions(2).slop(1).boost(2.0F))
                )
                .should(builder ->
                        builder.multiMatch(m ->
                                m.fields("name").query(keyword).fuzziness("AUTO").prefixLength(1)))
                .should(
                        builder -> builder.matchPhrasePrefix(
                                prefix -> prefix.field("description").query(keyword).maxExpansions(2).slop(1))
                )
                .should(
                        builder -> builder.matchPhrasePrefix(
                                prefix -> prefix.field("category.name").query(keyword).maxExpansions(2).slop(1))
                )
                .build();
        boolQuery.must(shouldQuery._toQuery());
        NativeQuery query = new NativeQueryBuilder()
                .withQuery(boolQuery.build()._toQuery())
                .withFilter(f -> f.range(
                        r -> r.field("price").gte(JsonData.of(minPrice)).lte(JsonData.of(maxPrice))))
                .withPageable(PageRequest.of(page,size))
                .withSort(s -> {
                    if(condition.get("order") != null) {
                        SortOrder sortOrder = condition.get("order").equals("desc") ? SortOrder.Desc : SortOrder.Asc;
                        return s.field(f -> f.field("price").order(sortOrder));
                    }
                    return s.field(f -> f.field("_score").order(SortOrder.Desc));
                })
                .build();
        Long totalProduct = elasticsearchOperations
                .count(query,ProductDTO.class);
        var products = elasticsearchOperations.search(query,ProductDTO.class)
                .stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());

        return new PageResponse<>(products,page,products.size(),totalProduct,HttpStatus.OK);
    }

    @Override
    public ProductDTO test(String id) {
       ProductDTO productDTO= elasticsearchOperations.get(id,ProductDTO.class);
       return productDTO;
    }
}
