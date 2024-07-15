package com.playcentric.model.playfellow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PfGameDTO {
    private Integer pfGameId; // 伴遊遊戲編號
    private Integer playFellowId; // 伴遊者id
    private Integer gameId; // 遊戲id
    private String gameName; // 遊戲名稱
    private String pricingCategory; // 計費方式(小時or計次)單位初始只能1
    private Integer amount; // 單價金額 沒小數
    private Byte pfGameStatus; // 狀態 1:開啟 2:關閉
    private String pfNickname; // 暱稱
    private String base64Image; // 照片的 base64 編碼
}
