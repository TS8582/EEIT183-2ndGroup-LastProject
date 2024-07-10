package com.playcentric.service.prop;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.ImageLib;
import com.playcentric.model.prop.Props;
import com.playcentric.model.prop.PropsRepository;
import com.playcentric.model.prop.type.PropType;

import jakarta.persistence.EntityNotFoundException;

@Service 
public class PropService {

    @Autowired
    private PropsRepository propsRepo;
    
    // 遊戲ID找全部道具
    public List<Props> findPropsByGameId(int gameId) {
        return propsRepo.findPropsByGameId(gameId);
    }
    
    // 查單筆
    public Props findById(int id) {
        return propsRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("道具 ID " + id + " 未找到"));
    }
    
    // 新增道具
    public void save(Props prop) {
        propsRepo.save(prop);
    } 
    
    // 刪除道具
    public void deleteById(int id) {
        propsRepo.deleteById(id);
    }
    
    // 修改道具
    public Props updateById(int id, String propName, String propRarity, String propDescription, int propTypeId, PropType propType, int propImageId, ImageLib imageLib) {
        Props prop = propsRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("道具 ID " + id + " 未找到"));
        prop.setPropName(propName);
        prop.setPropRarity(propRarity);
        prop.setPropDescription(propDescription); 
        prop.setPropTypeId(propTypeId);
        prop.setPropType(propType);
        prop.setPropImageId(propImageId);
        prop.setImageLib(imageLib); 
        prop.setUpdatedTime(LocalDateTime.now()); 
        return propsRepo.save(prop); 
    }
}
