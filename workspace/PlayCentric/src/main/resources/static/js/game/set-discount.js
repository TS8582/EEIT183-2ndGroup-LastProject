document.querySelector('form').addEventListener('submit', e => {
    const now = new Date();
    const startAt = new Date(document.querySelector('#startAt').value);
    const endAt = new Date(document.querySelector('#endAt').value);
    const msg = document.querySelector('#msg');
    if (startAt < now) {
        msg.innerHTML = '開始日期設定錯誤';
        e.preventDefault();
    }
    else if (endAt < startAt) {
        msg.innerHTML = '結束日期不得在開始日期之前';
        e.preventDefault();
    }
})