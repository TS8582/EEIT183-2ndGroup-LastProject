package com.playcentric.service.prop.MemberPropInventoryService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.prop.MemberPropInventory.MemberPropInventory;
import com.playcentric.model.prop.MemberPropInventory.MemberPropInventoryRepository;
import com.playcentric.model.prop.MemberPropInventory.MemberPropInventoryForFrontDto;

@Service
public class MemberPropInventoryService {

    @Autowired
    private MemberPropInventoryRepository memberPropInventoryRepo;

    public List<MemberPropInventoryForFrontDto> memberPropsbyMemId(int memId) {
        List<MemberPropInventory> memberPropInventories = memberPropInventoryRepo.findByIdMemId(memId);
        return memberPropInventories.stream()
                                    .map(this::convertToDto)
                                    .collect(Collectors.toList());
    }

    private MemberPropInventoryForFrontDto convertToDto(MemberPropInventory inventory) {
        return new MemberPropInventoryForFrontDto(
            inventory.getProps(),
            inventory.getId().getMemId(),
            inventory.getPropQuantity()
        );
    }
}
