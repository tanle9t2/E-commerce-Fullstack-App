package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.DiscountDTO;
import com.tanle.e_commerce.entities.Discount;
import com.tanle.e_commerce.payload.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DiscountService {
    ResponseEntity<DiscountDTO> findById(Integer id);
    ResponseEntity<List<DiscountDTO>> findAll();
    ResponseEntity<List<DiscountDTO>> findByProduct(Integer productId);
    ResponseEntity<DiscountDTO> creatDiscount(Discount discount);
    ResponseEntity<DiscountDTO> updateDiscount(Discount discount);

    MessageResponse deleteDiscount(Integer id);

}
