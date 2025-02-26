package com.tanle.e_commerce.service.serviceimpl;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.UpdateByQueryRequest;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.json.JsonData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LazilyParsedNumber;
import com.tanle.e_commerce.Repository.Jpa.ProductRepository;
import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.ProductMapper;
import com.tanle.e_commerce.mapper.SKUMapper;
import com.tanle.e_commerce.service.CategoryService;
import com.tanle.e_commerce.service.ProductAsycnService;

import com.tanle.e_commerce.service.ProductService;
import com.tanle.e_commerce.service.SKUService;
import com.tanle.e_commerce.utils.Status;
import org.elasticsearch.client.RequestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQuery;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductAsynServiceImpl implements ProductAsycnService {
    @Autowired
    private ElasticsearchOperations elasticsearchRestTemplate;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SKUService skuService;
    private final String INDEX_NAME = "product-index";
    private static final Logger LOG = LoggerFactory.getLogger(ProductAsycnService.class);

    @Override
    public void create(int entityId) {
        IndexCoordinates indexCoordinates = IndexCoordinates.of(INDEX_NAME);
        // Check if the index exists
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(indexCoordinates);
        if (!indexOperations.exists()) {

            indexOperations.create();
            System.out.println("Index created: " + INDEX_NAME);
        }
        Product product = productRepository.findById(entityId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found"));
        elasticsearchRestTemplate.save(productMapper.toDocument(product));
        LOG.info("Create Product - {}", product.getId());
    }


    @Override
    public void update(int entityId, JsonObject payload) {
        Map<String, Object> updateField = getUpdatedFiled(payload);
        Document document = Document.create();
        for (Map.Entry<String, Object> entry : updateField.entrySet()) {
            if (entry.getKey().equals("product_category_id")) {
                CategoryDTO categoryDTO = categoryService.findById((Integer) entry.getValue());
                document.put("category", categoryDTO);
            } else {
                document.put(entry.getKey(), entry.getValue());
            }
        }
        UpdateQuery updateQuery = UpdateQuery.builder(String.valueOf(entityId))
                .withDocument(document)
                .build();
        // Execute the update request using the Elasticsearch template
        elasticsearchRestTemplate.update(updateQuery, IndexCoordinates.of(INDEX_NAME));
        LOG.info("update Product - {}", entityId);
    }

    @Override
    public void delete(int entityId) {
        elasticsearchRestTemplate.delete(String.valueOf(entityId), IndexCoordinates.of(INDEX_NAME));
        LOG.info("Delete Product - {}", entityId);
    }

    @Override
    public void deleteSku(int entityId, int skuId) {
        ProductDTO product = elasticsearchRestTemplate.get(String.valueOf(entityId),
                ProductDTO.class, IndexCoordinates.of("product-index"));

        for (SKUDTO skudto : product.getSkus()) {
            if (skudto.getSkuId() == skuId) {
                product.getSkus().remove(skudto);
                elasticsearchRestTemplate.update(product);
                break;
            }
        }
        LOG.info("Delete Product - {}", entityId);

    }

    @Override
    public void createSku(int entityId, int skuId) {
        ProductDTO product = elasticsearchRestTemplate.get(String.valueOf(entityId),
                ProductDTO.class, IndexCoordinates.of("product-index"));
        SKUDTO skudto = skuService.findById(skuId);
        product.getSkus().add(skudto);
        elasticsearchRestTemplate.update(product);
    }

    @Override
    public void updateCategory(int entityId, JsonObject payload) {
        List<UpdateQuery> updateQueries = new ArrayList<>();
        Map<String, Object> updatedField = getUpdatedFiled(payload);
        Document document = Document.create();
        document.put("category", updatedField);
        BoolQuery query = QueryBuilders.bool()
                .should(
                        builder -> builder.term(
                                prefix -> prefix.field("category.id").value(entityId))
                )
                .build();
        NativeQuery searchQuery = new NativeQueryBuilder()
                .withQuery(query._toQuery())
                .build();

        SearchHits<ProductDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, ProductDTO.class);

        for (SearchHit<ProductDTO> hit : searchHits.getSearchHits()) {
            ProductDTO product = hit.getContent();
            UpdateQuery updateQuery = UpdateQuery.builder(String.valueOf(product.getId()))
                    .withDocument(document) // Partial update document
                    .build();
            updateQueries.add(updateQuery);
        }
        elasticsearchRestTemplate.bulkUpdate(updateQueries, IndexCoordinates.of(INDEX_NAME));
        LOG.info("Bulk update successful");
    }

    @Override
    public void updateSKU(int entityId, int skuId, JsonObject payload) {
        ProductDTO product = elasticsearchRestTemplate.get(String.valueOf(entityId),
                ProductDTO.class, IndexCoordinates.of("product-index"));

        Map<String, Object> updatedFields = getUpdatedFiled(payload);

        for (SKUDTO s : product.getSkus()) {
            if (s.getSkuId() == skuId) {
                updatedFields.forEach((key, value) -> {
                    try {
                        if (key.equals("product_id")) {
                            String prevId = (payload.get("after").getAsJsonObject().get("product_id").toString());
                            ProductDTO p = elasticsearchRestTemplate.get(prevId,
                                    ProductDTO.class, IndexCoordinates.of("product-index"));
                            p.getSkus().add(s);
                            product.getSkus().remove(s);
                            elasticsearchRestTemplate.update(product);
                            elasticsearchRestTemplate.update(p);
                            return;
                        } else {
                            // Build the setter method name
                            String setterName = "set" + Arrays.stream(key.split("_"))
                                    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                                    .collect(Collectors.joining()); // Find the method in the class
                            // Find the setter method in the entity class

                            Method setterMethod = findSetterMethod(SKUDTO.class, setterName, value);
                            if (setterMethod != null) {
                                // Invoke the setter method with the correct value
                                setterMethod.invoke(s, value);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace(); // Handle exception as needed
                    }
                });

                elasticsearchRestTemplate.update(product);
                break;
            }
        }
        LOG.info("update sku - {}", entityId);
    }

    private Object convertValue(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }

        if (targetType.isAssignableFrom(value.getClass())) {
            return value;
        }

        if (targetType == Integer.class || targetType == int.class) {
            return Integer.parseInt(value.toString());
        }

        if (targetType == Double.class || targetType == double.class) {
            return Double.parseDouble(value.toString());
        }

        if (targetType == Long.class || targetType == long.class) {
            return Long.parseLong(value.toString());
        }

        if (targetType == String.class) {
            return value.toString();
        }

        // Add more conversions here as needed

        throw new IllegalArgumentException("Unsupported conversion from " + value.getClass() + " to " + targetType);
    }

    private Method findSetterMethod(Class<?> clazz, String setterName, Object value) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(setterName)) {
                Class<?> c = method.getParameterTypes()[0];
                // Check if the setter method accepts the type of value we're passing
                if (method.getParameterCount() == 1 && method.getParameterTypes()[0].isAssignableFrom(convertValue(value, c).getClass())) {
                    return method;
                }
            }
        }
        return null;
    }

    private Object convertJsonElement(JsonElement element) {
        if (element.isJsonPrimitive()) {
            if (element.getAsJsonPrimitive().isString()) {
                return element.getAsString(); // Return as String
            } else if (element.getAsJsonPrimitive().isNumber()) {
                // Get the number as a primitive value and ensure proper type conversion
                Number number = element.getAsNumber();
                if (number instanceof Integer) {
                    return number.intValue();  // Explicitly return as Integer
                } else if (number instanceof Long) {
                    return number.longValue(); // Explicitly return as Long
                } else if (number instanceof Double) {
                    return number.doubleValue(); // Explicitly return as Double
                } else if (number instanceof Float) {
                    return number.floatValue(); // Explicitly return as Float
                } else if (number instanceof LazilyParsedNumber) {
                    // If it's a LazilyParsedNumber, convert to the appropriate type
                    try {
                        return number.intValue(); // Or number.intValue() based on your requirement
                    } catch (Exception e) {
                        return number.doubleValue(); // Fallback to double
                    }
                }
                return number;  // Default case: return the number as is
            } else if (element.getAsJsonPrimitive().isBoolean()) {
                return element.getAsBoolean(); // Return as Boolean
            }
        } else if (element.isJsonObject()) {
            return element.getAsJsonObject(); // Return as JsonObject
        } else if (element.isJsonArray()) {
            return element.getAsJsonArray(); // Return as JsonArray
        }
        return null;
    }

    private Map<String, Object> getUpdatedFiled(JsonObject payload) {
        JsonObject afterObject = payload.getAsJsonObject("after");
        JsonObject beforeObject = payload.getAsJsonObject("before");
        Map<String, Object> updateField = new HashMap<>();
        // Loop through the entries of the "after" JsonObject
        for (Map.Entry<String, JsonElement> entry : beforeObject.entrySet()) {
            String key = entry.getKey();
            JsonElement beforeValue = entry.getValue();
            // Check if the "after" object has the same key
            if (afterObject.has(key)) {
                JsonElement afterValue = afterObject.get(key);
                // Compare the values
                if (!beforeValue.equals(afterValue)) {
                    updateField.put(key, convertJsonElement(afterValue));
                }
            }
        }
        return updateField;
    }
}

