package com.tanle.e_commerce.respone;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FilterSearchResponse {

    private String filterName;
    private List<FilterItems> filterItems;

    public void addFilterItem(Long id, String name, Long value) {
        if (filterItems == null)
            filterItems = new ArrayList<>();

        FilterItems newFilterItem = new FilterItems();
        newFilterItem.setId(id);
        newFilterItem.setName(name);
        newFilterItem.setValue(value);

        this.filterItems.add(newFilterItem);
    }

    @Data
    private static class FilterItems {
        private Long id;
        private String name;
        private Long value;
    }
}



