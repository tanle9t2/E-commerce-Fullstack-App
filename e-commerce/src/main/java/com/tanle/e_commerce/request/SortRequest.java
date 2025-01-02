package com.tanle.e_commerce.request;

import com.tanle.e_commerce.utils.filter.SortDirection;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SortRequest {
    private String field;
    private SortDirection sortDirection;
}
