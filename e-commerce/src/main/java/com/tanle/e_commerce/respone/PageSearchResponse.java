package com.tanle.e_commerce.respone;

import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.dto.ProductDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageSearchResponse {
    private List<ProductDocument> data;
    private List<FilterSearch> filter;
    private int page;
    private int totalElement;
    private Long count;
    private HttpStatus status;

    @Data
    public static class FilterSearch {
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
    }
    @Data
    private static class FilterItems {
        private Long id;
        private String name;
        private Long value;
    }
}
