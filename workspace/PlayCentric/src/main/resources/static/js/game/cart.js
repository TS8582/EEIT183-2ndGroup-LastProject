function addToCart() {
    const cart = document.querySelectorAll('.cart');
    cart.forEach(elm => {
        if (!elm.classList.contains('okk')) {
            elm.addEventListener('click', e => {
                const gameId = elm.closest('.gameitem').querySelector('.gameId').innerHTML.trim();
                const memId = document.querySelector('.memId').innerHTML.trim();

                axios.get('/PlayCentric/gamecart/insert', {
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
                        console.error(err);
                    })
            }, { once: true })
        }
        elm.classList.add('okk');

    });
}
addToCart();