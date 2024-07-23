const startAt = document.querySelector('#startAt');
const endAt = document.querySelector('#endAt');
startAt.addEventListener(('change'), checkTime);
endAt.addEventListener(('change'), checkTime);

function checkTime() {
    const now = new Date();
    const start = new Date(startAt.value);
    const end = new Date(endAt.value);
    const msg = document.querySelector('#msg');
    if (start < now) {
        msg.innerHTML = '開始時間設定錯誤';
        startAt.value = '';
    }
    else if (end < start) {
        msg.innerHTML = '結束時間不得在開始時間之前';
        endAt.value = '';
    }
    else if (end < now) {
        msg.innerHTML = '結束時間設定錯誤';
        endAt.value = '';
    }
}