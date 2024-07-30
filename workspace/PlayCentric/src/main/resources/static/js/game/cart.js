function addToCart() {
    const cart = document.querySelectorAll('.cart');
    cart.forEach(elm => {
        if (!elm.classList.contains('okk')) {
            elm.addEventListener('click', e => {
                let gameId = elm.closest('.gameitem').querySelector('.gameId').innerHTML.trim();
                let memId = document.querySelector('.memId').innerHTML.trim();

                axios.get('/PlayCentric/personal/api/gamecart/insert', {
                    params: {
                        gameId: gameId,
                        memId: memId
                    }
                })
                    .then(res => {
                        elm.innerHTML = '已加入購物車';
                        elm.classList.remove('mybtn-green');
                        elm.classList.add('mybtn-disabled');
                        elm.classList.remove('mybtn');
                        doAlert('成功加入購物車');
                    })
                    .catch(err => {
                        if (err.response && err.response.status === 401) {
                            window.location.href = '/PlayCentric/member/homeShowErr/' + err.response.data;
                        }
                        console.error(err);
                    })
            }, { once: true })
            elm.classList.add('okk');
        }

    });
}
addToCart();