const total = document.querySelector('.total');
const price = document.querySelectorAll('.price');
const gamecount = document.querySelector('.gamecount');
const gopay = document.querySelector('.gopay');
const pay = document.querySelector('.pay');
const remove = document.querySelectorAll('.remove');
const showPoints = document.querySelector('.showPoints');
const choosepay = document.querySelector('.choosepay');
const cancel = document.querySelector('.cancel');
let totalPrice = 0;
let totalGame = parseInt(gamecount.innerHTML.trim());
const nogame = document.querySelector('.nogame');

if (totalGame === 0) {
    nogame.classList.remove('hidden');
}

//取消結帳按鈕
cancel.addEventListener('click', e => {
    pay.classList.add('hidden');
    gopay.classList.add('mybtn', 'mybtn-green');
    gopay.classList.remove('mybtn-disabled');
    remove.forEach(elm => {
        elm.classList.add('mybtn-remove', 'mybtn');
        elm.classList.remove('vis-hidden');
    });
})

// 按下結帳按鈕顯示選擇付款方式
function gopayfunc() {
    pay.classList.remove('hidden');
    gopay.classList.remove('mybtn', 'mybtn-green');
    gopay.classList.add('mybtn-disabled');
    remove.forEach(elm => {
        elm.classList.remove('mybtn-remove', 'mybtn');
        elm.classList.add('vis-hidden');
    });
}

if (gopay) {
    gopay.addEventListener('click', gopayfunc)
}

// 計算總價
price.forEach(elm => {
    totalPrice += parseInt(elm.innerHTML);
});
total.innerHTML = totalPrice;

//提交表單前
document.querySelector('form').addEventListener('submit', e => {
    const mypoint = document.querySelector('.mypoint').innerHTML;
    if (choosepay.value === 1 && parseInt(mypoint) < totalPrice) {
        doAlert('PC錢包餘額不足');
        e.preventDefault();
    }
})


// 錢包餘額顯示
choosepay.addEventListener('change', e => {
    if (e.target.value !== '1') {
        showPoints.classList.add('hidden');
    }
    else {
        showPoints.classList.remove('hidden');
    }
})


// 從購物車內移除
remove.forEach(elm => {
    elm.addEventListener('click', e => {
        const gameId = parseInt(elm.closest('.game').querySelector('.gameId').innerHTML.trim());

        axios.get('/PlayCentric/personal/api/gamecart/remove', {
            params: {
                gameId: gameId
            }
        })
            .then(res => {
                if (res.data == 'OK') {
                    const thisprice = parseInt(elm.closest('.game').querySelector('.price').innerHTML.trim());
                    totalGame -= 1;
                    if (totalGame === 0) {
                        gopay.classList.remove('mybtn', 'mybtn-green');
                        gopay.classList.add('mybtn-disabled');
                        gopay.removeEventListener('click', gopayfunc);
                        nogame.classList.remove('hidden');
                    }
                    gamecount.innerHTML = totalGame;
                    totalPrice -= thisprice;
                    total.innerHTML = totalPrice;
                    elm.closest('.game').remove();
                }
                else {
                    doAlert('帳號已登出，請重新登入再嘗試。');
                }

            })
            .catch(err => {
                if (err.response && err.response.status === 401) {
                    window.location.href = '/PlayCentric/member/homeShowErr/' + err.response.data;
                }
                console.error(err);
            })
    })
});
