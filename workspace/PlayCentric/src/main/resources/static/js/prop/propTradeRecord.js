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
          // 根據會員ID獲取會員名稱
          const memberName = await fetchMemberNameById(buyOrder.buyerMemId);

          // 格式化購買時間
          const orderTime = new Date(buyOrder.orderTime).toLocaleString("zh-TW", {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
            hour: "2-digit",
            minute: "2-digit",
            second: "2-digit",
            hour12: false
          }).replace(/\//g, '-').replace(',', '');

          // 检查并获取付款方式名称
          const paymentName = buyOrder.payment ? buyOrder.payment.paymentName : 'N/A';

          myTable.row
            .add([
              buyOrder.buyOrderId,
              buyOrder.props.propId,
              buyOrder.props.propName,
              memberName, // 買家會員名稱
              orderTime, // 購買時間
              buyOrder.quantity, // 數量
              buyOrder.price, // 金額
              buyOrder.paymentId, // 付款方式
            ])
            .draw();
        } catch (err) {
          console.error("獲取會員名稱時出錯:", err);
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

// 根據會員ID獲取會員名稱
async function fetchMemberNameById(memId) {
  try {
    const res = await axios.get(
      "http://localhost:8080/PlayCentric/prop/front/buyProp/findMenNameByMemId",
      {
        params: { memId: memId },
      }
    );
    const memName = res.data.account; // 假設會員名稱字段為 memName
    return memName;
  } catch (err) {
    console.error("獲取會員名稱時出錯:", err);
    throw err; // 如果需要，可以選擇拋出錯誤以便在調用此函數時處理
  }
}
