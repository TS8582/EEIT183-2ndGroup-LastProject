//package com.playcentric.service.prop.MemberPropInventoryService;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.playcentric.model.prop.MemberPropInventory.MemberPropInventory;
//import com.playcentric.model.prop.MemberPropInventory.MemberPropInventoryRepository;
//
//@Service
//public class MemberPropInventoryService {
//
//    @Autowired
//    private MemberPropInventoryRepository memberPropInventoryRepo;
//
//    // 修改买家仓库道具数量
//    public void updateInventoryQuantity(int buyerMemId, int propId, int quantity) {
//        // 获取买家库存道具
//        MemberPropInventory buyerInventory = memberPropInventoryRepo.findByMemberIdAndPropId(buyerMemId, propId)
//            .orElseGet(() -> {
//                MemberPropInventory newInventory = new MemberPropInventory();
//                newInventory.setMember(buyer);
//                newInventory.setProps(prop);
//                newInventory.setPropQuantity(quantity);
//                return newInventory;
//            });
//
//        // 更新买方库存数量
//        int newBuyerQuantity = buyerInventory.getPropQuantity() + quantity;
//        buyerInventory.setPropQuantity(newBuyerQuantity);
//
//        // 保存更新后的库存道具
//        memberPropInventoryRepo.save(buyerInventory);
//    }
//}
