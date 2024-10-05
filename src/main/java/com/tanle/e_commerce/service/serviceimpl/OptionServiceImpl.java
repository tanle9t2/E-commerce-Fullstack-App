package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.OptionRepository;
import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.entities.OptionValue;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Service
public class OptionServiceImpl implements OptionService {

    @Autowired
    private OptionRepository optionRepository;
    @Override
    @Transactional
    public MessageResponse updateOption(Map<String, List<Option>> data) {
        List<Option> options = data.get("options");
        for (var option : options) {
            Option optionDB = optionRepository.findById(option.getId())
                    .orElseThrow(()-> new ResourceNotFoundExeption("Not found option"));
            optionDB.setName(option.getName());
            if(option.getOptionValues() != null) {
                List<OptionValue> optionValues = option.getOptionValues();
                for (var optionValue : optionValues) {
                    OptionValue op = optionDB.getOptionValues().stream()
                            .filter(o -> o.getId() == optionValue.getId())
                            .findFirst()
                            .get();
                    op.setName(optionValue.getName());
                }
            }
            optionRepository.save(optionDB);
        }
        return MessageResponse.builder()
                .message("Successfully update option")
                .status(HttpStatus.OK)
                .data(null)
                .build();
    }
}
