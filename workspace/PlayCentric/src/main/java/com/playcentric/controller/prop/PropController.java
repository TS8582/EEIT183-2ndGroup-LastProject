package com.playcentric.controller.prop;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.playcentric.model.ImageLib;
import com.playcentric.model.game.primary.Game;
import com.playcentric.model.prop.GameSimpleDto;
import com.playcentric.model.prop.Props;
import com.playcentric.model.prop.type.PropType;
import com.playcentric.service.game.GameService;
import com.playcentric.service.prop.PropService;
import com.playcentric.service.prop.type.PropTypeService;
import com.playcentric.service.ImageLibService;

@Controller
@SessionAttributes(names = "games")
public class PropController {

    @Autowired
    private PropService propService;

    @Autowired
    private GameService gameService;

    @Autowired
    private PropTypeService propTypeService;

    @Autowired
    private ImageLibService imageLibService; // 確保已經注入 ImageLibService

    // 進入道具頁面
    @GetMapping("/prop/propSheet")
    public String showPropPage() {
        return "prop/propSheet";
    }

    // 找尋所有遊戲返回至選單
    @GetMapping("/prop/findAllGame")
    @ResponseBody
    public List<GameSimpleDto> findAllGame() {
        List<Game> games = gameService.findAll();
        return games.stream()
                .map(game -> new GameSimpleDto(game.getGameId(), game.getGameName()))
                .collect(Collectors.toList());
    }

    // 根據遊戲ID找尋所有道具
    @GetMapping("/prop/findAllPropsByGameId")
    @ResponseBody
    public List<Props> findAllPropsByGameId(@RequestParam int gameId) {
        return propService.findPropsByGameId(gameId);
    }
    
    //讀取圖片
    @GetMapping("/prop/image")
    public ResponseEntity<byte[]> getMethodName(@RequestParam Integer id) {
		ImageLib imageLib = imageLibService.findImageById(id);
		byte[] imageFile = imageLib.getImageFile();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		
		return new ResponseEntity<byte[]>(imageFile, headers, HttpStatus.OK);
    }

    // 根據遊戲ID找尋所有道具類型
    @GetMapping("/prop/findAllPropTypesByGameId")
    @ResponseBody
    public List<PropType> findAllPropTypesByGameId(@RequestParam int gameId) {
        return propTypeService.findAllPropTypesByGameId(gameId);
    }

    // 新增道具
    @PostMapping("/prop/saveProp")
    @ResponseBody
    public String save(@RequestParam("propName") String propName, @RequestParam("propRarity") String propRarity,
                       @RequestParam("propDescription") String propDescription, @RequestParam("propTypeId") Integer propTypeId,
                       @RequestParam("gameId") Integer gameId, @RequestParam("propImage") MultipartFile propImage)
            throws IOException {

        Props prop = new Props();
        prop.setGameId(gameId);
        prop.setPropName(propName);
        prop.setPropRarity(propRarity);
        prop.setPropDescription(propDescription);
        prop.setPropTypeId(propTypeId);
        Game game = gameService.findById(gameId);
        if (game == null) {
            throw new IllegalArgumentException("無效的 gameId: " + gameId);
        }
        prop.setGame(game);

        PropType propType = propTypeService.findById(propTypeId);
        if (propType == null) {
            throw new IllegalArgumentException("無效的 propTypeId: " + propTypeId);
        }
        prop.setPropType(propType);

        ImageLib imageLib = new ImageLib();
        imageLib.setImageFile(propImage.getBytes());

        // 保存圖片
        imageLibService.saveImage(imageLib);

        // 關聯圖片
        prop.setPropImageId(imageLib.getImageId());

        // 設置創建時間
        prop.setCreatedTime(LocalDateTime.now());

        // 保存道具
        propService.save(prop);

        return "上傳OK!!";
    }

    // 刪除道具
    @DeleteMapping("/prop/deleteProp")
    public ResponseEntity<String> deleteProp(@RequestParam Integer id) {
        propService.deleteById(id);
        return ResponseEntity.ok("道具已成功刪除");
    }

    // 查找單筆道具
    @GetMapping("/prop/findById")
    public ResponseEntity<Props> findById(@RequestParam Integer id) {
        Props prop = propService.findById(id);
        if (prop != null) {
            return ResponseEntity.ok(prop);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 修改道具
    @PutMapping("/prop/update")
    @ResponseBody
    public ResponseEntity<Props> updateProp(@RequestParam Integer propId, @RequestParam String propName,
                                            @RequestParam String propRarity, @RequestParam String propDescription,
                                            @RequestParam Integer propTypeId, @RequestParam MultipartFile propImage) throws IOException {
        PropType propType = propTypeService.findById(propTypeId);
        if (propType == null) {
            throw new IllegalArgumentException("無效的 propTypeId: " + propTypeId);
        }

        ImageLib imageLib = new ImageLib();
        imageLib.setImageFile(propImage.getBytes());
        imageLibService.saveImage(imageLib);

        Props updatedProp = propService.updateById(propId, propName, propRarity, propDescription, propTypeId, propType, imageLib.getImageId(), imageLib);
        return ResponseEntity.ok(updatedProp);
    }
}
