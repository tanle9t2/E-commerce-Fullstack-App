package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.DiscountDTO;
import com.tanle.e_commerce.entities.Discount;
import com.tanle.e_commerce.payload.MessageResponse;
import com.tanle.e_commerce.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DiscountController {
    @Autowired
    private DiscountService discountService;

    @RequestMapping("/disocunt")
    public ResponseEntity<List<DiscountDTO>> getDiscounts() {
        return discountService.findAll();
    }
    @RequestMapping("/discount/{discountId}")
    public ResponseEntity<DiscountDTO> getDiscount(@PathVariable int discountId) {
        return discountService.findById(discountId);
    }
    @RequestMapping(value = "/discount",method = RequestMethod.POST)
    public ResponseEntity<DiscountDTO> createDiscount(@RequestBody Discount discount) {
        return discountService.creatDiscount(discount);
    }
    @RequestMapping(value = "/discount",method = RequestMethod.PUT)
    public ResponseEntity<DiscountDTO> updateDiscount(@RequestBody Discount discount) {
        return discountService.updateDiscount(discount);
    }
    @RequestMapping(value = "/discount/{discountId}",method = RequestMethod.DELETE)
    public MessageResponse deleteDiscount(@PathVariable int discountId) {
        return discountService.deleteDiscount(discountId);
    }
    @RequestMapping("/discounts")
    public ResponseEntity<List<DiscountDTO>> getDiscountByProduct(@RequestParam("productid") String productId) {
        return discountService.findByProduct(Integer.parseInt(productId));
    }
}
