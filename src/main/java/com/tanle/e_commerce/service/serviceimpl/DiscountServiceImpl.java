package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.DiscountRepository;
import com.tanle.e_commerce.dto.DiscountDTO;
import com.tanle.e_commerce.entities.Discount;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.payload.MessageResponse;
import com.tanle.e_commerce.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {
    @Autowired
    private DiscountRepository discountRepository;

    @Override
    public ResponseEntity<DiscountDTO> findById(Integer id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found disocunt"));
        return new ResponseEntity<>(discount.converDTO(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<DiscountDTO>> findAll() {
        List<DiscountDTO> discountsDTO = discountRepository.findAll()
                .stream()
                .map(Discount::converDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(discountsDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<DiscountDTO>> findByProduct(Integer productId) {
        List<DiscountDTO> discounts = discountRepository.findByProduct(productId)
                .stream()
                .map(Discount::converDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(discounts, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<DiscountDTO> creatDiscount(Discount discount) {
        Random random = new Random();
        discount.setCreateAt(LocalDateTime.now());
        //sample expiry
        discount.setExpiry(LocalDateTime.now().plusDays(random.nextInt(10)));
        Discount result = discountRepository.save(discount);
        return new ResponseEntity<>(result.converDTO(), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<DiscountDTO> updateDiscount(Discount discount) {
        Discount result = discountRepository.save(discount);

        return new ResponseEntity<>(result.converDTO(), HttpStatus.OK);
    }

    @Override
    @Transactional
    public MessageResponse deleteDiscount(Integer id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found discount need to be deleted"));

        discountRepository.delete(discount);
        return MessageResponse.builder()
                .message("Successfully delete discount")
                .status(HttpStatus.OK)
                .build();
    }
}
