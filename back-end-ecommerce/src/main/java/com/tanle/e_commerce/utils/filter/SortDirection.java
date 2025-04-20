package com.tanle.e_commerce.utils.filter;

import com.tanle.e_commerce.request.SortRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public enum SortDirection {
    ASC("asc") {
        @Override
        public <T> Order build(Root<T> root, CriteriaBuilder cb, SortRequest request) {
            return cb.asc(root.get(request.getField()));
        }
    },
    DESC("desc") {
        @Override
        public <T> Order build(Root<T> root, CriteriaBuilder cb,SortRequest request) {
            return cb.desc(root.get(request.getField()));
        }
    };
    private String value;
    SortDirection(String value) {
        this.value = value;
    }

    public static SortDirection fromString(String value) {
        for (SortDirection sortDirection : SortDirection.values()) {
            if(String.valueOf(sortDirection.value).equalsIgnoreCase(value)) {
                return sortDirection;
            }
        }
        return null;
    }

    public abstract <T> Order build(Root<T> root, CriteriaBuilder cb, SortRequest request);

}
