function doAlert(message) {
  $('body').append(`<div class="rounded-2 position-absolute top-50 start-50 translate-middle bg-dark text-light p-2 alert-log">${message}</div>`)
  $('.alert-log').hide();
  $('.alert-log').fadeIn(500);
  setTimeout(function () {
    $('.alert-log').fadeOut(1500);
    setTimeout(function () {
      $('.alert-log').remove();
    }, 1500)
  }, 1500)
}

//使用jquery，標籤為不透明