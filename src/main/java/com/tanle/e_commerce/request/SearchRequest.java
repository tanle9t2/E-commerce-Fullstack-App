package com.tanle.e_commerce.request;

import com.tanle.e_commerce.utils.filter.SpecSearchCriterial;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static com.tanle.e_commerce.utils.AppConstant.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {
    private List<SpecSearchCriterial> filterAndConditions;
    private List<SpecSearchCriterial> filterOrConditions;
    private List<SortRequest> sortRequests;
    private int page;
    private int size;

    public List<SpecSearchCriterial> getFilterAndConditions() {
        if(filterAndConditions == null) return new ArrayList<>();
        return filterAndConditions;
    }

    public void setFilterAndConditions(List<SpecSearchCriterial> filterAndConditions) {
        this.filterAndConditions = filterAndConditions;
    }
    public List<SpecSearchCriterial> getFilterOrConditions() {
        if(filterOrConditions == null) return new ArrayList<>();
        return filterOrConditions;
    }
    public void setFilterOrConditions(List<SpecSearchCriterial> filterOrConditions) {
        this.filterOrConditions = filterOrConditions;
    }

    public List<SortRequest> getSortRequests() {
        if(sortRequests == null) return new ArrayList<>();
        return sortRequests;
    }

    public void setSortRequests(List<SortRequest> sortRequests) {
        this.sortRequests = sortRequests;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    public Pageable getPageable() {
        return PageRequest.of(Objects.requireNonNullElse(this.page,0)
                ,Objects.requireNonNullElse(this.size,Integer.parseInt(PAGE_SIZE)));
    }
}
