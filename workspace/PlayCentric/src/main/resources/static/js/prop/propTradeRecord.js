$(document).ready(function () {
  const userId = "";
  // 初始化 DataTable
  let myTable = $("#TradeRecordTable").DataTable({
    language: {
      info: "顯示第 _START_ 到第 _END_ 筆，共 _TOTAL_ 筆記錄", // 設置顯示的信息
    },
  });

  const selGameSub = document.querySelector("#selGameSub");
  selGameSub.addEventListener("click", async (event) => {
    event.preventDefault(); // 取消 form 預設的送出 (form 內有 submit 要寫，不然表單會送出)
    let SelectGame = document.querySelector("#SelectGame");
    let selectedOption = SelectGame.options[SelectGame.selectedIndex];
    const selectedGameId = selectedOption.value;
    console.log("gameId:" + selectedGameId);

    // 清空表格
    myTable.clear().draw();

    try {
      // 根據遊戲Id找買單
      const buyOrdersResponse = await axios.get(
        "http://localhost:8080/PlayCentric/prop/findAllpropBuyOrder",
        {
          params: { gameId: selectedGameId },
        }
      );

      for (const buyOrder of buyOrdersResponse.data) {
        console.log("找到買單:", buyOrder);

        try {
          // 根據買單中的orderId去找賣單
          const sellOrderResponse = await axios.get(
            "http://localhost:8080/PlayCentric/prop/findPropSellOrderByOrderId",
            {
              params: { orderId: buyOrder.orderId },
            }
          );

          const sellOrder = sellOrderResponse.data;
          console.log("找到對應買單的賣單:", sellOrder);

          try {
            const propName = await fetchPropNameById(sellOrder.propId);

            // 將賣單和買單數據一起添加到表格中
            myTable.row
              .add([
                buyOrder.orderId,
                sellOrder.propId,
                propName || "", // 確保有道具名稱
                sellOrder.sellerMemId,
                buyOrder.buyerMemId, // 買家會員編號
                buyOrder.orderTime, // 購買時間
                buyOrder.quantity, // 數量
                buyOrder.price, // 金額
                buyOrder.payment.paymentName, // 付款方式
              ])
              .draw();
          } catch (err) {
            console.error("獲取道具名稱時出錯:", err);
          }
        } catch (err) {
          console.error("獲取賣單時出錯:", err);
        }
      }
    } catch (err) {
      console.error("獲取買單時出錯:", err);
    }
  });

  // 0.25秒後（等待遊戲讀取完）自動點擊 selGameSub 按鈕一次
  setTimeout(() => {
    selGameSub.click();
  }, 250);
});

// 根據PropId找PropName
async function fetchPropNameById(propId) {
  try {
    const res = await axios.get(
      "http://localhost:8080/PlayCentric/prop/findById",
      {
        params: { id: propId },
      }
    );
    const propName = res.data.propName; // 假設道具名稱字段為 propName
    return propName;
  } catch (err) {
    console.error("獲取道具名稱時出錯:", err);
    throw err; // 如果需要，可以選擇拋出錯誤以便在調用此函數時處理
  }
}
