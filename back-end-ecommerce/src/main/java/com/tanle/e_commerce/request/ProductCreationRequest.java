package com.tanle.e_commerce.request;

import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.entities.OptionValue;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.SKU;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductCreationRequest {
    private Product product;
    private List<OptionRequest> options;
    private List<SKUDTO> skus;
    private List<MultipartFile> images;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionRequest {
        private String name;
        private List<String> value;
        public List<OptionValue> convertOptionValue() {
            return value.stream()
                    .map(v -> OptionValue.builder()
                            .name(v)
                            .build())
                    .collect(Collectors.toList());
        }
    }
}
