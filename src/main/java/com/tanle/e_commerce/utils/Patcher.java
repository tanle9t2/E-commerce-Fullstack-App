package com.tanle.e_commerce.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.json.*;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Patcher {
    @Autowired
    private final ObjectMapper objectMapper;
    private final Validator validator;

    /**
     * Performs a JSON Patch operation.
     *
     * @param patch      JSON Patch document
     * @param targetBean object that will be patched
     * @param beanClass  class of the object the will be patched
     * @param <T>
     * @return patched object
     */

    public <T> T patch(JsonPatch patch, T targetBean, Class<T> beanClass) throws JsonProcessingException {
        // Convert targetBean to a JsonObject
        JsonNode targetNode = objectMapper.valueToTree(targetBean);
        // Apply the patch using JsonNode
        JsonNode patchedNode = applyPatch(patch, targetNode);
        // Convert the patched JsonNode back to the target bean and validate
        return convertAndValidate(patchedNode, beanClass);
    }


    /**
     * Performs a JSON Merge Patch operation
     *
     * @param mergePatch JSON Merge Patch document
     * @param targetBean object that will be patched
     * @param beanClass  class of the object the will be patched
     * @param <T>
     * @return patched object
     */
    public <T> T mergePatch(JsonMergePatch mergePatch, T targetBean, Class<T> beanClass) {
        JsonValue target = objectMapper.convertValue(targetBean, JsonValue.class);
        JsonValue patched = applyMergePatch(mergePatch, target);
        return convertAndValidate(patched, beanClass);
    }

//    private JsonValue applyPatch(JsonPatch patch, JsonStructure target) {
//        try {
//            return patch.apply(target);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    private JsonNode applyPatch(JsonPatch patch, JsonNode targetNode) {
        try {
            // Convert JsonNode to a String and then back to a JsonObject to apply the patch
            String jsonString = objectMapper.writeValueAsString(targetNode);
            JsonObject jsonObject = javax.json.Json.createReader(new StringReader(jsonString)).readObject();

            JsonValue patched = patch.apply(jsonObject);
            return objectMapper.readTree(patched.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private <T> T convertAndValidate(JsonNode jsonNode, Class<T> beanClass) throws JsonProcessingException {
        T bean = objectMapper.treeToValue(jsonNode, beanClass);
        validate(bean);
        return bean;
    }
    private JsonValue applyMergePatch(JsonMergePatch mergePatch, JsonValue target) {
        try {
            return mergePatch.apply(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T convertAndValidate(JsonValue jsonValue, Class<T> beanClass) {
        T bean = objectMapper.convertValue(jsonValue, beanClass);
        validate(bean);
        return bean;
    }

    private <T> void validate(T bean) {
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new RuntimeException("test");
        }
    }
    public static <T> void patch(T existingIntern, T incompleteIntern)
            throws IllegalAccessException {
        //GET THE COMPILED VERSION OF THE CLASS
        Class<?> internClass= existingIntern.getClass();
        Field[] internFields=internClass.getDeclaredFields();
        for(Field field : internFields){
            //CANT ACCESS IF THE FIELD IS PRIVATE
            field.setAccessible(true);
            //CHECK IF THE VALUE OF THE FIELD IS NOT NULL, IF NOT UPDATE EXISTING INTERN
            Object value=field.get(incompleteIntern);
            if(value!=null){
                field.set(existingIntern,value);
            }
            //MAKE THE FIELD PRIVATE AGAIN
            field.setAccessible(false);
        }

    }
}
