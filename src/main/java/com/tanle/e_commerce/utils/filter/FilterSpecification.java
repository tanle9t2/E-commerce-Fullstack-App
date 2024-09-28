package com.tanle.e_commerce.utils.filter;

import com.tanle.e_commerce.request.SearchRequest;
import com.tanle.e_commerce.request.SortRequest;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class FilterSpecification<T> implements Specification<T> {
    private SearchRequest searchRequest;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate finalPredicate;
        Predicate andPredicate = criteriaBuilder.equal(criteriaBuilder.literal(Boolean.TRUE),Boolean.TRUE);
        Predicate orPredicarte = criteriaBuilder.equal(criteriaBuilder.literal(Boolean.TRUE),Boolean.TRUE);

        for (SpecSearchCriterial filter : searchRequest.getFilterAndConditions()) {
            andPredicate = filter.getOperator().build(root,criteriaBuilder,filter,andPredicate);
        }
        for (SpecSearchCriterial filter : searchRequest.getFilterOrConditions()) {
            orPredicarte = filter.getOperator().build(root,criteriaBuilder,filter,orPredicarte);
        }
        List<Order> orders = new ArrayList<>();
        for (SortRequest sortRequest : searchRequest.getSortRequests()) {
            orders.add(sortRequest.getSortDirection().build(root,criteriaBuilder,sortRequest));
        }
        query.orderBy(orders);
        finalPredicate = criteriaBuilder.and(
                criteriaBuilder.and(andPredicate),
                criteriaBuilder.or(orPredicarte)
        );
        return finalPredicate;
    }


}
