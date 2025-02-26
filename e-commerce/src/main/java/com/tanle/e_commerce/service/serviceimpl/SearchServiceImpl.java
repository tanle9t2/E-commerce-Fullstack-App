package com.tanle.e_commerce.service.serviceimpl;

import co.elastic.clients.elasticsearch._types.SortMode;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.LongTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import com.tanle.e_commerce.Repository.Jpa.SearchRepository;
import com.tanle.e_commerce.Repository.elasticsearch.ProductElasticsearchRepository;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.dto.ProductDocument;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.request.SearchRequest;
import com.tanle.e_commerce.request.SortRequest;
import com.tanle.e_commerce.respone.PageSearchResponse;
import com.tanle.e_commerce.service.SearchService;
import com.tanle.e_commerce.utils.filter.*;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
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
    public PageSearchResponse searchProduct(Map<String, String> condition, int page, int size) {
        String keyword = condition.get("keyword");
        String order = condition.get("order");
        String sortBy = condition.get("sortBy");
        Double minPrice = condition.get("minPrice") != null ? Double.parseDouble(condition.get("minPrice")) : 0.0;
        Double maxPrice = condition.get("maxPrice") != null ? Double.parseDouble(condition.get("maxPrice")) : null;
        var boolQuery = QueryBuilders.bool();

        if (condition.get("category") != null) {
            String[] categoryIds = condition.get("category").split(",");
            var categoryQuery = QueryBuilders.bool();
            for (var id : categoryIds) {
                categoryQuery.should(builder -> builder.term(t -> t.field("category.id").value(id)));
            }
            boolQuery.must(categoryQuery.build()._toQuery());
        }
        if (condition.get("location") != null) {
            String[] locations = condition.get("location").split(",");
            var categoryQuery = QueryBuilders.bool();
            for (var location : locations) {
                categoryQuery.should(builder -> builder.match(t -> t.field("tenantDocument.location").query(location)));
            }
            boolQuery.must(categoryQuery.build()._toQuery());
        }
        if (keyword != null && !keyword.isEmpty()) {
            BoolQuery shouldQuery = QueryBuilders.bool()
                    .should(builder -> builder.matchPhrasePrefix(
                            prefix -> prefix.field("name").query(keyword).maxExpansions(2).slop(1).boost(2.0F)))
                    .should(builder -> builder.multiMatch(m -> m.fields("name").query(keyword).fuzziness("AUTO").prefixLength(1)))
                    .should(builder -> builder.matchPhrasePrefix(
                            prefix -> prefix.field("description").query(keyword).maxExpansions(2).slop(1)))
                    .should(builder -> builder.matchPhrasePrefix(
                            prefix -> prefix.field("category.name").query(keyword).maxExpansions(2).slop(1)))
                    .build();

            boolQuery.must(shouldQuery._toQuery());
        }

        NativeQueryBuilder queryBuilder = new NativeQueryBuilder()
                .withQuery(boolQuery.build()._toQuery())
                .withPageable(PageRequest.of(page, size));

        // Apply price range filter only if maxPrice is not null
        if (maxPrice != null) {
            queryBuilder.withFilter(f -> f.range(r -> r.field("minPrice").gte(JsonData.of(minPrice)).lte(JsonData.of(maxPrice))));
        } else {
            queryBuilder.withFilter(f -> f.range(r -> r.field("minPrice").gte(JsonData.of(minPrice))));
        }

        queryBuilder.withSort(s -> {
            if (sortBy != null) {
                if (sortBy.equals("price")) {
                    SortOrder sortOrder = order.equals("desc") ? SortOrder.Desc : SortOrder.Asc;
                    return s.field(f -> f
                            .field("minPrice")
                            .order(sortOrder)
                    );
                } else if (sortBy.equals("newest")) {
                    SortOrder sortOrder = order.equals("desc") ? SortOrder.Desc : SortOrder.Asc;
                    return s.field(f -> f.field("createdAt").order(sortOrder));
                }
            }
            return s.field(f -> f.field("_score").order(SortOrder.Desc));
        });
        queryBuilder.withAggregation("category", Aggregation.of(a ->
                a.terms(t -> t.field("category.id").size(10))
                        .aggregations("name", subAgg ->
                                subAgg.terms(t -> t
                                        .field("category.name.keyword")
                                        .size(1) // Since one ID corresponds to one name
                                )
                        )
        )).withAggregation("location", Aggregation.of(a ->
                a.terms(t -> t.field("tenantDocument.location.keyword").size(10)))
        );
        NativeQuery query = queryBuilder.build();
        Long totalProduct = elasticsearchOperations.count(query, ProductDocument.class);
        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(query, ProductDocument.class);
        var products = searchHits
                .stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());

        AggregationsContainer<?> aggregationsContainer = searchHits.getAggregations();
        List<PageSearchResponse.FilterSearch> filterSearches = new ArrayList<>();
        for (ElasticsearchAggregation aggregation : ((ElasticsearchAggregations) aggregationsContainer).aggregations()) {
            Aggregate aggregate = aggregation.aggregation().getAggregate();
            PageSearchResponse.FilterSearch filterSearch = new PageSearchResponse.FilterSearch();
            filterSearch.setFilterName(aggregation.aggregation().getName());
            // Iterate through buckets
            if (aggregate.isLterms()) {
                for (var bucket : ((LongTermsAggregate) aggregate._get()).buckets().array()) {
                    for (var sub : bucket.aggregations().entrySet()) {
                        filterSearch.addFilterItem(bucket.key()
                                , ((StringTermsAggregate) sub.getValue()._get()).buckets().array().get(0).key().stringValue()
                                , ((StringTermsAggregate) sub.getValue()._get()).buckets().array().get(0).docCount());
                    }
                }
            } else if (aggregate.isSterms()) {
                for (var bucket : ((StringTermsAggregate) aggregate._get()).buckets().array()) {
                    filterSearch.addFilterItem(null,bucket.key().stringValue(), bucket.docCount());
                }
            }

            filterSearches.add(filterSearch);
        }

        return PageSearchResponse.builder()
                .data(products)
                .count(totalProduct)
                .totalElement(products.size())
                .filter(filterSearches)
                .status(HttpStatus.OK)
                .build();
    }


    @Override
    public ProductDTO test(String id) {
        ProductDTO productDTO = elasticsearchOperations.get(id, ProductDTO.class);
        return productDTO;
    }
}
