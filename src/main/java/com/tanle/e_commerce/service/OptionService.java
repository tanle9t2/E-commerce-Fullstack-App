package com.tanle.e_commerce.service;

import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.respone.MessageResponse;

import java.util.List;
import java.util.Map;

public interface OptionService {
    MessageResponse updateOption(Map<String, List<Option>> data);
}
