package com.playcentric.service.prop.MemberPropInventoryService;

import java.io.Console;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.prop.MemberPropInventory.MemberPropInventory;
import com.playcentric.model.prop.MemberPropInventory.MemberPropInventoryRepository;
import com.playcentric.model.prop.MemberPropInventory.MemberPropInventoryForFrontDto;
import com.playcentric.model.prop.MemberPropInventory.MemberPropInventoryId;

@Service
public class MemberPropInventoryService {

    @Autowired
    private MemberPropInventoryRepository memberPropInventoryRepo;
	private Optional<MemberPropInventory> byId;

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
    
    // 減少倉庫道具數量
    public Optional<MemberPropInventory> findMemberPropByIdAndUpdateQuantity(int memId, int propId, int quantity) {
//        MemberPropInventoryId id = new MemberPropInventoryId(memId, propId); 
//        Optional<MemberPropInventory> propOptional = memberPropInventoryRepo.findById(id);
//    	上面雙主鍵不知為何找不到~
    	
        Optional<MemberPropInventory> propOptional = memberPropInventoryRepo.findByIdMemIdAndIdPropId(memId,propId);
        System.out.println("memId:"+memId);
        System.out.println("propId:"+propId);

        System.out.println(propOptional+"倉庫搜尋資料");
        // 檢查記錄是否存在
        if (propOptional.isPresent()) {
            MemberPropInventory prop = propOptional.get();

            // 減少數量
            int newQuantity = prop.getPropQuantity() - quantity;
            System.out.println(quantity+"販賣數量");
           
            
            // 確保數量不會小於0
            if (newQuantity < 0) {
                newQuantity = 0;
            }
            
            prop.setPropQuantity(newQuantity);

            // 保存變更
            memberPropInventoryRepo.save(prop);

            // 返回更新後的對象
            return Optional.of(prop);
        } else {
            // 返回空的 Optional 表示沒有找到相應的道具
            return Optional.empty();
        }
    

    }
}
