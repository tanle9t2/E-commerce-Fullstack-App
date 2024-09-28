package com.tanle.e_commerce.request;

import com.tanle.e_commerce.dto.OrderDTO;
import com.tanle.e_commerce.payload.PageResponse;
import com.tanle.e_commerce.utils.filter.SpecSearchCriterial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private List<SpecSearchCriterial> filterAndConditions;
    private List<SpecSearchCriterial> filterOrConditions;
    private List<SortRequest> sortRequests;
    private int page;
    private int size;


}
