package com.tanle.e_commerce.utils.filter;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.criteria.*;

import java.time.LocalDateTime;
import java.util.List;

public enum FilterOperation {

    EQUAL("eq") {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
//            Object value = request.getFieldType().parse(request.getValue().toString());
            Expression<?> key = this.getPath(root, request);
            if(request.isOrPredicate()) {
                return cb.and(cb.or(cb.equal(key, request.getValue())), predicate);
            }
            return cb.and(cb.equal(key, request.getValue()), predicate);
        }
    },
    NOT_EQUAL("neq") {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
//            Object value = request.getFieldType().parse(request.getValue().toString());
            Expression<?> key = this.getPath(root, request);
            return cb.and(cb.notEqual(key, request.getValue()), predicate);
        }
    },
    LIKE("like") {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
//            Object value = request.getFieldType().parse(request.getValue().toString());
            Expression<?> key = this.getPath(root, request);
            return cb.and(cb.notEqual(key, "%" + request.getValue() + "%"), predicate);
        }
    },
    JOIN("jn") {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
            return null;
        }
    },
    GREATER_THAN("gt") {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
            return null;
        }
    },
    GREATER_THAN_OR_EQUAL_TO("gte") {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
            return null;
        }
    },
    LESS_THAN("lt") {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
            return null;
        }
    },
    LESSTHAN_OR_EQUAL_TO("lte") {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
            return null;
        }
    },
    IN("in") {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
            if(request.getValues() !=null) {
                List<Object> values = request.getValues();
                CriteriaBuilder.In<Object> inclause = cb.in(this.getPath(root,request));
                for (Object o : values) {
                    inclause.value(o);
                }
                return cb.and(inclause,predicate);
            }

            List<String> fields= request.getFields();
            Predicate p = cb.equal(cb.literal(Boolean.TRUE),Boolean.FALSE);
            for(String field : fields) {
                Expression<String> expression = root.get(field);
                p = cb.or(cb.like(expression,"%" + request.getValue() + "%"),p);
            }
            return cb.and(p,predicate);
        }
    },
    NOT_IN("nin") {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
            return null;
        }
    },
    BETWEEN("btn") {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
            if (request.getValue() instanceof Double) {
                Expression<Double> key = this.getPath(root, request);
                Double fromPrice = (Double) request.getValue();
                Double toPrice = (Double) request.getValueTo();
                if (toPrice != null && fromPrice !=null) {
                    return cb.and(cb.and(
                            cb.greaterThanOrEqualTo(key, fromPrice)),
                            cb.lessThanOrEqualTo(key, toPrice),predicate);
                }
            }
            Expression<LocalDateTime> key = this.getPath(root, request);
            LocalDateTime fromDate = (LocalDateTime) request.getValue();
            LocalDateTime toDate = (LocalDateTime) request.getValueTo();
            if (fromDate != null && toDate != null) {
                return cb.and(cb.and(
                                cb.greaterThanOrEqualTo(key, fromDate)),
                        cb.lessThanOrEqualTo(key, toDate),predicate);
            }
            return predicate;
        }
    },
    CONTAINS("like") {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
            return null;
        }
    },
    NOT_CONTAINS("notLike") {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
            return null;
        }
    },
    IS_NULL("isnull") {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
            return null;
        }
    },
    IS_NOT_NULL("isnotnull") {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
            return null;
        }
    },
    START_WITH("startwith") {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
            return null;
        }
    },
    END_WITH("endwith") {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
            return null;
        }
    },
    IS_EMPTY("isempty") {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
            return null;
        }
    },
    IS_NOT_EMPTY("isnotempty") {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate) {
            return null;
        }
    };


    private String value;

    FilterOperation(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    public static FilterOperation fromValue(String value) {
        for (FilterOperation op : FilterOperation.values()) {
            if (String.valueOf(op.value).equalsIgnoreCase(value)) {
                return op;
            }
        }
        return null;
    }

    public abstract <T> Predicate build(Root<T> root, CriteriaBuilder cb, SpecSearchCriterial request, Predicate predicate);

    public <T, V> Path<V> getPath(Root<T> root, SpecSearchCriterial request) {
        String[] keys = request.getField().split("\\.");
        Path<V> path = root.get(keys[0]);
        for (int i = 1; i < keys.length; i++) {
            path = path.get(keys[i]);
        }
        return path;
    }

}