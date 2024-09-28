package com.tanle.e_commerce.utils.filter;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterBuider {
    private FilterBuider() {

    }

    private static List<String> split(String filter, String delimiter) {
        return Stream.of(filter.split(delimiter))
                .collect(Collectors.toList());
    }

    public static List<SpecSearchCriterial> getFilters(String field, String search) {
        List<SpecSearchCriterial> specSearchCriterials = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            List<String> values = split(search, ",");

            if(values!= null) {
               values.forEach(value -> specSearchCriterials.add(
                      SpecSearchCriterial.builder()
                              .value(value)
                              .field(field)
                              .orPredicate(true)
                              .operator(FilterOperation.EQUAL)
                              .build()
               ));
            }
        }
        return specSearchCriterials;
    }
}
