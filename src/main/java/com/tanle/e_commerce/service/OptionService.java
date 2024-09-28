package com.tanle.e_commerce.service;

import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.payload.MessageResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface OptionService {
    MessageResponse updateOption(Map<String, List<Option>> data);
}
