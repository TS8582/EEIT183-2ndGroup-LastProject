function doAlert(message) {
  // 創建一個新的 div 元素，並設置其內容和樣式
  $('body').append(`
    <div class="rounded-2 position-fixed top-50 start-50 translate-middle bg-dark text-light p-2 alert-log user-select-none fs-3" 
         style="z-index: 9999; opacity: 0.9;">
      ${message}
    </div>
  `);

  // 獲取剛創建的 alert 元素
  const $alertLog = $('.alert-log');

  // 初始隱藏 alert
  $alertLog.hide();

  // 使用淡入效果顯示 alert
  $alertLog.fadeIn(500);

  // 設置定時器來淡出並移除 alert
  setTimeout(function () {
    // 淡出 alert
    $alertLog.fadeOut(1500, function() {
      // 淡出完成後移除 alert 元素
      $(this).remove();
    });
  }, 1500);
}
//使用jquery，標籤為不透明