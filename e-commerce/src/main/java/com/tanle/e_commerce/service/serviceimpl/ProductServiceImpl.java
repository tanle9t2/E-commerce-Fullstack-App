package com.tanle.e_commerce.service.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tanle.e_commerce.Repository.Jpa.*;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.*;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.ProductMapper;
import com.tanle.e_commerce.mapper.SKUMapper;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.request.ProductCreationRequest;
import com.tanle.e_commerce.service.ProductService;
import com.tanle.e_commerce.specification.ProductSpecification;
import com.tanle.e_commerce.utils.Patcher;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.json.JsonPatch;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SKURepository skuRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private OptionValueRepository optionValueRepository;
    @Autowired
    private SKUMapper skuMapper;
    @Autowired
    private Patcher patcher;


    private PageResponse<ProductDTO> getResult(Page<Product> products) {
        if (products.getNumberOfElements() == 0) {
            return new PageResponse<>(Collections.emptyList(),
                    products.getNumber(), products.getNumberOfElements()
                    , products.getTotalElements(), HttpStatus.OK);
        }
        List<ProductDTO> productDTOS = products.getContent().stream()
                .map(p -> productMapper.asInput(p))
                .collect(Collectors.toList());
        return new PageResponse<>(productDTOS, products.getNumber(), products.getNumberOfElements()
                , products.getTotalElements(), HttpStatus.OK);
    }

    @Override
    public PageResponse<ProductDTO> findAll(int page, int size, String direction, String... field) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), field);
        Page<Product> products = productRepository.findAll(pageable);

        return getResult(products);
    }

    @Override
    public PageResponse<ProductDTO> findByTenant(int tenantId, int page, int size, String direction, String... field) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), field);
        Page<Product> products = productRepository.findAll(ProductSpecification.withOwnerId(tenantId), pageable);
        List<ProductDTO> productDTOS = getResult(products).getData();

        return getResult(products);
    }

    @Override
    public PageResponse<ProductDTO> findByName(int page, int size, String nameProduct) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAllByName(nameProduct, pageable);
        return getResult(products);
    }

    @Override
    @Transactional
    public ProductDTO findById(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundExeption("Product id: "
                + id + " not found"));

        return productMapper.asInput(product);
    }

    @Override
    public PageResponse<ProductDTO> findByCategory(String categoryId, Pageable pageable) {
        Page<Product> page = productRepository.findProductByCategory(categoryId, pageable);
        return getResult(page);
    }

    @Override
    public List<ProductDTO> findByOption(Integer optionId) {
        return null;
    }

    @Override
    @Transactional
    public ProductDTO update(Product product) {
        Product result = productRepository.save(product);
        return result.converDTO();
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundExeption("Product id: "
                + id + " not found"));
        List<SKU> skus = skuRepository.getSKUSByProduct(product);
        skuRepository.deleteAll(skus);
        productRepository.delete(product);
    }

    @Override
    @Transactional
    public ProductDTO save(ProductCreationRequest productCreationRequest) {
        Product product = productCreationRequest.getProduct();
        product.setCreatedAt(LocalDateTime.now());
        //set options
        product.setOptions(productCreationRequest.getOptions());
        //set Category
        Category category = categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found category"));
        product.setCategory(category);
        Product productDB = productRepository.save(product);

        //create SKU
        List<SKU> skus = productCreationRequest.getSkus().stream()
                .map(s -> skuMapper.convertEntity(s, productDB))
                .collect(Collectors.toList());
        productDB.addSKU(skus);

        productRepository.save(product);
        return productDB.converDTO();
    }

    @Override
    @Transactional
    public ProductDTO save(Product product) {
        Product productDB = productRepository.save(product);
        return productDB.converDTO();
    }

    @Override
    @Transactional
    public ProductDTO update(Integer id, JsonPatch jsonPatch, String skuNo) throws JsonProcessingException {
        Product productDB = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found option value"));

        ProductDTO productDTO = productMapper.asInput(productDB);
        ProductDTO productDTOPatch = patcher.patch(jsonPatch, productDTO, ProductDTO.class);
        productMapper.update(productDB, productDTOPatch);
        productDB.setCreatedAt(LocalDateTime.now());
        productRepository.save(productDB);

        if ((productDTOPatch.getPrice() != 0 || productDTOPatch.getStock() != 0) && skuNo != null) {
            SKU skuDB = skuRepository.findBySkuNo(skuNo)
                    .orElseThrow(() -> new ResourceNotFoundExeption("Not found SKU"));
            skuDB.setPrice(productDTOPatch.getPrice());
            skuDB.setStock(productDTOPatch.getStock());
        }
        return productDTOPatch;
    }

    @Override
    public PageResponse<ProductDTO> findByTenant(int page, int size, Integer tenantId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findProductByTenant(tenantId, pageable);
        return getResult(products);
    }

    @Override
    @Transactional
    public ProductDTO addOption(Integer productId, ProductCreationRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found product"));
        //save options
        product.getOptions().addAll(request.getOptions());
        productRepository.save(product);
        //save skus
        List<SKU> skus = request.getSkus().stream()
                .map(s -> skuMapper.convertEntity(s, product))
                .collect(Collectors.toList());
        product.addSKU(skus);
        productRepository.save(product);
        return product.converDTO();
    }

    @Override
    @Transactional
    public MessageResponse deleteOption(List<Integer> skusId, List<Integer> optionId
            , List<LinkedHashMap<Integer, List<Integer>>> optionValueId, int productId) {
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new ResourceNotFoundExeption("Not found product"));

        List<SKU> skusDelete = new ArrayList<>();
        List<Option> optionDelete = new ArrayList<>();
        List<OptionValue> optionValuesDelete = new ArrayList<>();

        //remove SKU
        List<SKU> skusDB = product.getSkus();
        skusId.forEach(id -> {
            SKU sku = skusDB.stream()
                    .filter(s -> s.getId() == id)
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundExeption("Not found SKU"));
            skusDelete.add(sku);
        });
        product.removeSKU(skusDelete);
        //remove Option
        if (optionId != null) {
            List<Option> optionDB = product.getOptions();
            optionId.forEach(id -> {
                Option option = optionDB.stream()
                        .filter(o -> o.getId() == id)
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundExeption("Not found Option"));
                optionDelete.add(option);
            });
        }
        product.removeOption(optionDelete);
        //remove OptionValue
        if (optionValueId != null) {
            for (var item : optionValueId) {
                Option option = product.getOptions().get(item.get("optionId").get(0));
                List<Integer> ids = item.get("optionValuesId");
                //filter option value;
                List<OptionValue> optionValues = new ArrayList<>();
                ids.forEach(id -> optionValues.add(option.getOptionValues().get(id)));
                //remove option value from option Entity
                option.getOptionValues().removeAll(optionValues);
                //add option value to delete
                optionValuesDelete.addAll(optionValues);
            }
        }

        productRepository.save(product);
        optionValueRepository.deleteAll(optionValuesDelete);
        skuRepository.deleteAll(skusDelete);
        optionRepository.deleteAll(optionDelete);
        return MessageResponse.builder()
                .data(null)
                .status(HttpStatus.OK)
                .message("Successfully delete option")
                .build();
    }

    @Override
    @Transactional
    public ProductDTO updatePrice(Integer productId, Map<String, Integer> data) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found product"));
        int skuId = data.get("skuId");
        SKU sku = product.getSkus().stream()
                .filter(s -> s.getId() == skuId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found Sku"));
        sku.setPrice(data.get("price"));
        productRepository.save(product);
        return productMapper.asInput(product);
    }

    @Override
    @Transactional
    public ProductDTO updateStock(Integer productId, Map<String, Integer> data) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found product"));
        int skuId = data.get("skuId");
        SKU sku = product.getSkus().stream()
                .filter(s -> s.getId() == skuId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found Sku"));
        sku.setStock(data.get("stock"));
        productRepository.save(product);
        return productMapper.asInput(product);
    }

    @Override
    public boolean userOwnEntity(Integer integer, String username) {
        return false;
    }
}
