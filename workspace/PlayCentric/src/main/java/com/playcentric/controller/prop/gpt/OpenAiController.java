package com.playcentric.controller.prop.gpt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.playcentric.model.prop.GptProperties;
import com.playcentric.model.prop.buyOrder2.PropBuyOrder2;
import com.playcentric.service.game.GameService;
import com.playcentric.service.prop.buyOrder.PropBuyOrderService2;

@Controller
public class OpenAiController {

    @Autowired
    private GptProperties gptProperties; 
	

    @Autowired
    private PropBuyOrderService2 propBuyOrderService2; // 自動注入 PropBuyOrderService2 服務

    @Autowired
    private GameService gameService;

    @GetMapping("/getCompletion")
    @ResponseBody
    public String getCompletion(@RequestParam("prompt") String prompt, @RequestParam("gameId") Integer gameId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.openai.com/v1/chat/completions"; // 使用正確的端點

        // 設置請求頭
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + gptProperties.getOpenaiToken()); // 設置授權標頭
        headers.set("Content-Type", "application/json"); // 設置內容類型為 JSON

        // 創建請求體
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4"); // 使用新的模型

        // 如果 prompt 關鍵字提到 "熱門"，讀取資料庫買單資訊，加入 prompt
        if (prompt.contains("熱")) {
            List<PropBuyOrder2> propBuyOrders = propBuyOrderService2.findPropBuyOrders(gameId);
            StringBuilder promptBuilder = new StringBuilder(prompt);
            promptBuilder.append(" 根據以下訂單資料，請計算並回答以下問題：");
            for (int i = 0; i < propBuyOrders.size(); i++) {
                PropBuyOrder2 propBuyOrder = propBuyOrders.get(i);
                promptBuilder.append("\n訂單編號")
                    .append(i + 1)
                    .append(" 名稱: ").append(propBuyOrder.getProps().getPropName())
                    .append(" 數量: ").append(propBuyOrder.getQuantity())
                    .append(" 總價: ").append(propBuyOrder.getPrice());
            }
            promptBuilder.append("\n最熱賣的商品是什麼？該商品總共賣出了多少個？該商品總銷售金額是多少？");
            prompt = promptBuilder.toString();
            System.out.println(prompt);
        } else {
            prompt += " 根據遊戲:" + gameService.findById(gameId).getGameName() + "回答，請用繁體中文回答";
        }

        // 創建消息對象
        Map<String, String> message = new HashMap<>();
        message.put("role", "user"); // 設置角色為用戶
        message.put("content", prompt); // 設置消息內容

        requestBody.put("messages", new Map[] { message }); // 將消息對象添加到請求體中

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers); // 創建請求實體

        try {
            // 發送請求並獲取響應
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // 檢查響應狀態碼
            if (response.getStatusCode().is2xxSuccessful()) {
                // 返回響應體
                return response.getBody();
            } else {
                // 打印並返回錯誤信息
                System.err.println("Error: " + response.getStatusCode() + ": " + response.getBody());
                return "Error: " + response.getStatusCode() + ": " + response.getBody();
            }
        } catch (Exception e) {
            // 處理異常情況
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
