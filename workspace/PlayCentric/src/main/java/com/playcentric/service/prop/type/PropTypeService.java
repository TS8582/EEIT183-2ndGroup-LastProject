package com.playcentric.service.prop.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.prop.type.PropType;
import com.playcentric.model.prop.type.PropTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PropTypeService {

    @Autowired
    PropTypeRepository propTypeRepo;

    public PropType findById(int propTypeId) {
        Optional<PropType> optionalPropType = propTypeRepo.findById(propTypeId);
        return optionalPropType.orElse(null); 
    }
    // 根據gameId獲取道具類型
    public List<PropType> findAllPropTypesByGameId(int gameId) {
        return propTypeRepo.findAllByGameId(gameId);
    }
}
