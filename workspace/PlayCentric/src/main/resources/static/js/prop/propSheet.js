// 初始化 DataTable 並設置語言選項
var table;
// 存儲道具種類的映射
const propTypesMap = {};

$(document).ready(function () {
  // 初始化 DataTable 並設置語言選項
  table = $("#myTable").DataTable({
    language: {
      info: "顯示第 _START_ 到第 _END_ 筆，共 _TOTAL_ 筆記錄", // 設置顯示的信息
    },
  });

  // 加載所有遊戲
  loadGames();

  // 提交按鈕點擊事件
  $("#submitBtn").click(function (event) {
    event.preventDefault(); // 取消表單的默認提交行為
    const selectedGameId = $("#SelectGame").val(); // 獲取選中的遊戲ID
    fetchPropTypesByGameId(selectedGameId, function (propTypes) {
      populatePropTypeSelect(propTypes, "add"); // 填充道具種類選單
      propTypes.forEach(function (propType) {
        propTypesMap[propType.propTypeId] = propType.propType; // 填充 propTypesMap
      });
      findAllPropsByGameId(selectedGameId); // 查找所有道具
    });
  });

  // 新增道具表單提交事件
  $("#savePropForm").submit(function (event) {
    event.preventDefault(); // 防止表單的默認提交行為

    let formData = new FormData();
    formData.append("propName", $("#propName").val()); // 道具名稱
    formData.append("propRarity", $("#propRarity").val()); // 道具稀有度
    formData.append("propDescription", $("#propDescription").val()); // 道具描述
    formData.append("propTypeId", $("#propTypeId").val()); // 道具種類ID
    formData.append("gameId", $("#SelectGame").val()); // 遊戲ID
    formData.append("propImage", $("#propImage")[0].files[0]); // 道具圖片文件
    saveProp(formData); // 保存道具
  });

  // 修改道具表單提交事件
  $("#editPropForm").submit(function (event) {
    event.preventDefault(); // 防止表單的默認提交行為

    let propId = $(this).data("prop-id"); // 从表单的 data 属性中获取 propId
    let formData = new FormData();
    formData.append("propId", propId); // 添加 ID
    formData.append("propName", $("#editPropName").val()); // 道具名稱
    formData.append("propRarity", $("#editPropRarity").val()); // 道具稀有度
    formData.append("propDescription", $("#editPropDescription").val()); // 道具描述
    formData.append("propTypeId", $("#editPropTypeId").val()); // 道具種類ID
    formData.append("gameId", $("#SelectGame").val()); // 遊戲ID
    formData.append("propImage", $("#editPropImage")[0].files[0]); // 道具圖片文件
    updateProp(formData); // 保存道具
  });

  // 監聽刪除按鈕點擊事件
  $("#myTable").on("click", ".btn-danger", function () {
    if (window.confirm("確定要刪除嗎？")) {
      let propId = $(this).data("prop-id");
      deleteProp(propId); // 確認刪除道具
    } else {
      // 用戶取消刪除操作
      console.log("用戶取消刪除操作");
    }
  });

  // 監聽修改按鈕點擊事件
  $("#myTable").on("click", ".btn-primary", function () {
    let propId = $(this).data("prop-id");
    $("#editPropForm").data("prop-id", propId); // 将 propId 存储在表单的 data 属性中
    editPropWindow(propId); // 打開修改模態框並傳遞道具ID
  });
});

// 加載所有遊戲
function loadGames() {
  axios
    .get("http://localhost:8080/PlayCentric/prop/findAllGame")
    .then(function (res) {
      const games = res.data;
      const selectGame = $("#SelectGame");
      console.log("Games data:", games); // 打印数据内容

      selectGame.empty();
      games.forEach(function (game) {
        const option = $("<option></option>")
          .val(game.gameId)
          .text(game.gameName);
        selectGame.append(option);
      });

      if (games.length > 0) {
        selectGame.val(games[0].gameId);
        $("#submitBtn").click();
      }
    })
    .catch(function (error) {
      console.error("獲取遊戲數據時出錯:", error);
    });
}

// 根據 gameId 獲取所有道具種類
function fetchPropTypesByGameId(gameId, callback) {
  axios
    .get("http://localhost:8080/PlayCentric/prop/findAllPropTypesByGameId", {
      params: { gameId: gameId },
    })
    .then((res) => {
      callback(res.data);
      const propTypes = res.data;
      propTypes.forEach(function (propType) {
        propTypesMap[propType.propTypeId] = propType.propType;
      });
    })
    .catch((error) => {
      console.error("獲取道具種類數據時出錯:", error);
    });
}

// 填充道具種類選單
function populatePropTypeSelect(propTypes, mode, selectedTypeId) {
  let propTypeSelect;
  if (mode === "edit") {
    propTypeSelect = $("#editPropTypeId");
  } else {
    propTypeSelect = $("#propTypeId");
  }
  propTypeSelect.empty();
  propTypes.forEach(function (propType) {
    const option = $("<option></option>")
      .val(propType.propTypeId)
      .text(propType.propType);
    if (mode === "edit" && propType.propTypeId === selectedTypeId) {
      option.attr("selected", "selected");
    }
    propTypeSelect.append(option);
  });
}

// 格式化日期時間
function formatDateTime(dateTimeString) {
  if (!dateTimeString) {
    return "";
  }
  const date = new Date(dateTimeString);
  const year = date.getFullYear();
  const month = ("0" + (date.getMonth() + 1)).slice(-2);
  const day = ("0" + date.getDate()).slice(-2);
  const hours = ("0" + date.getHours()).slice(-2);
  const minutes = ("0" + date.getMinutes()).slice(-2);
  const seconds = ("0" + date.getSeconds()).slice(-2);
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// 根據 gameId 查找所有道具
function findAllPropsByGameId(gameId) {
  axios
    .get("http://localhost:8080/PlayCentric/prop/findAllPropsByGameId", {
      params: { gameId: gameId },
    })
    .then((res) => {
      const props = res.data;
      table.clear();
      props.forEach(function (prop) {
        const formattedCreatedTime = formatDateTime(prop.createdTime);
        const formattedUpdatedTime = formatDateTime(prop.updatedTime);

        const row = [
          prop.propId, // 道具ID
          prop.propName, // 道具名稱
          `<img src="${window.location.origin}/PlayCentric/prop/image?id=${prop.propImageId}" alt="${prop.propImageId}" width="60px">`, // 道具圖片
          propTypesMap[prop.propTypeId] || "未知", // 道具種類名稱
          prop.propRarity, // 道具稀有度
          prop.propDescription, // 道具描述
          formattedCreatedTime, // 格式化后的創建時間
          formattedUpdatedTime, // 格式化后的更新時間
          `<button class="btn btn-primary" data-prop-id="${prop.propId}">修改</button>`, // 修改按鈕
          `<button class="btn btn-danger" data-prop-id="${prop.propId}">刪除</button>`, // 刪除按鈕
        ];
        table.row.add(row);
      });
      table.draw();
    })
    .catch((error) => {
      console.error("獲取道具數據時出錯:", error);
    });
}

// 保存道具
function saveProp(formData) {
  axios
    .post("http://localhost:8080/PlayCentric/prop/saveProp", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
    .then((res) => {
      console.log("道具已成功保存", res);
      $("#savePropModal").modal("hide");
      findAllPropsByGameId($("#SelectGame").val());
    })
    .catch((error) => {
      console.error("保存道具時出錯", error);
    });
}

// 修改道具
function updateProp(formData) {
  axios
    .put("http://localhost:8080/PlayCentric/prop/update", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
    .then((res) => {
      console.log("道具已成功修改", res);
      $("#editPropModal").modal("hide");
      findAllPropsByGameId($("#SelectGame").val());
    })
    .catch((error) => {
      console.error("修改道具時出錯", error);
    });
}

// 刪除道具
function deleteProp(propId) {
  axios
    .delete("http://localhost:8080/PlayCentric/prop/deleteProp", {
      params: { id: propId }, // 傳遞正確的 propId
    })
    .then((res) => {
      console.log("道具已成功刪除", res);
      findAllPropsByGameId($("#SelectGame").val());
    })
    .catch((error) => {
      console.error("刪除道具時出錯", error);
    });
}

// 顯示新增道具的模態框
function savePropWindow() {
  $("#savePropForm")[0].reset(); // 使用原生 JavaScript 方法重置表單
  fetchPropTypesByGameId($("#SelectGame").val(), function (propTypes) {
    populatePropTypeSelect(propTypes, "add"); // 填充道具種類選單
  });
  $("#savePropModal").modal("show");
}

// 顯示修改道具的模態框並加載道具數據
function editPropWindow(propId) {
  axios
    .get("http://localhost:8080/PlayCentric/prop/findById", {
      params: { id: propId },
    })
    .then((res) => {
      const prop = res.data;
      $("#editPropForm")[0].reset(); // 使用原生 JavaScript 方法重置表單
      $("#editPropId").val(prop.propId);
      $("#editPropName").val(prop.propName);
      $("#editPropRarity").val(prop.propRarity);
      $("#editPropDescription").val(prop.propDescription);
      fetchPropTypesByGameId($("#SelectGame").val(), function (propTypes) {
        populatePropTypeSelect(propTypes, "edit", prop.propTypeId); // 填充道具種類選單
      });
      $("#editPropModal").modal("show");
    })
    .catch((error) => {
      console.error("加載道具數據時出錯:", error);
    });
}
