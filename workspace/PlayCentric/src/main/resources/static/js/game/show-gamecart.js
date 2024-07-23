const total = document.querySelector('.total');
const price = document.querySelectorAll('.price');
const gamecount = document.querySelector('.gamecount');
const gopay = document.querySelector('.gopay');
const pay = document.querySelector('.pay');
const remove = document.querySelectorAll('.remove');

// 按下結帳按鈕顯示選擇付款方式
gopay.addEventListener('click', e => {
    pay.classList.remove('hidden');
    gopay.classList.remove('mybtn', 'mybtn-green');
    gopay.classList.add('mybtn-disabled');
    remove.forEach(elm => {
        elm.classList.remove('mybtn-remove', 'mybtn');
        elm.classList.add('vis-hidden');
    });
}, { once: true })

// 計算總價
let totalPrice = 0;
let totalGame = parseInt(gamecount.innerHTML.trim());
price.forEach(elm => {
    totalPrice += parseInt(elm.innerHTML);
});
total.innerHTML = totalPrice;

// 從購物車內移除
remove.forEach(elm => {
    elm.addEventListener('click', e => {
        const gameId = parseInt(elm.closest('.game').querySelector('.gameId').innerHTML.trim());

        axios.get('/PlayCentric/gamecart/remove', {
            params: {
                gameId: gameId
            }
        })
            .then(res => {
                if (res.data == 'OK') {
                    const thisprice = parseInt(elm.closest('.game').querySelector('.price').innerHTML.trim());
                    totalGame -= 1;
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
                console.error(err);
            })
    })
});
