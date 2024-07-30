function doAlert(message) {
  var div = document.createElement('div');

  div.classList.add('rounded-2', 'position-fixed', 'top-50', 'start-50', 'translate-middle', 'bg-dark', 'text-light', 'p-2', 'fade-alert', 'z-3', 'user-select-none','fs-3');
  div.textContent = message;

  document.body.appendChild(div);

  var fadeAlert = document.querySelector('.fade-alert');
  fadeAlert.classList.add('show-alert');
  setTimeout(function () {
    fadeAlert.classList.remove('show-alert');

    setTimeout(function () {
      fadeAlert.remove();
    }, 1500);
  }, 1500);
}

//使用javascript，須配合css/utils/doAlertJs.css使用，標籤透明度為0.75