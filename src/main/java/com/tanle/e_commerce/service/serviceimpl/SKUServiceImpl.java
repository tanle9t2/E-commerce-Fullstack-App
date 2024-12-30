package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.ProductRepository;
import com.tanle.e_commerce.Repository.Jpa.SKURepository;
import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.entities.OptionValue;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.SKU;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.SKUMapper;
import com.tanle.e_commerce.service.SKUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SKUServiceImpl implements SKUService {
    @Autowired
    private SKURepository skuRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SKUMapper skuMapper;


    @Override
    @Transactional
    public SKUDTO findById(Integer skuId) {
        SKU sku = skuRepository.findById(skuId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found SKU"));
        return skuMapper.convertDTO(sku);
    }

    @Override
    @Transactional
    public List<SKUDTO> createSKU(List<SKUDTO> skus, Product product) {
        List<Option> options = product.getOptions();
        List<SKU> skuDB = new ArrayList<>();
        for (SKUDTO skuDTO : skus) {
            List<OptionValue> opDB = new ArrayList<>();
            List<Integer> tierIndex = skuDTO.getOptionValueIndex();
            for (int i = 0; i < tierIndex.size(); i++) {
                opDB.add(options.get(i).getOptionValues().get(tierIndex.get(i)));
            }
            SKU sku = SKU.builder()
                    .skuNo(skuDTO.getSkuNo())
                    .stock(skuDTO.getStock())
                    .price(skuDTO.getPrice())
                    .createAt(LocalDateTime.now())
                    .optionValues(opDB)
                    .product(product)
                    .build();
            skuDB.add(sku);
        }
        skuRepository.saveAll(skuDB);
        return skuDB.stream()
                .map(s -> skuMapper.convertDTO(s))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<SKUDTO> updateSKU(Integer productId, List<SKUDTO> skudtos) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found product"));

        List<SKU> skus = skudtos.stream()
                .map(s -> skuMapper.convertEntity(s, product))
                .collect(Collectors.toList());
        product.getSkus().addAll(skus);
        skuRepository.saveAll(skus);

        return skus.stream()
                .map(s -> skuMapper.convertDTO(s))
                .collect(Collectors.toList());
    }
}
