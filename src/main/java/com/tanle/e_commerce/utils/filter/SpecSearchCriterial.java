package com.tanle.e_commerce.utils.filter;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SpecSearchCriterial<T> {
    private String field;
    private FilterOperation operator;
    private Object value;
    private Object valueTo;
    private boolean orPredicate;
    private List<String> fields;
    private List<Object> values;

    public SpecSearchCriterial(String field, FilterOperation operator, Object value) {
        this(field,operator,value,null,false,null,null);
    }
}
