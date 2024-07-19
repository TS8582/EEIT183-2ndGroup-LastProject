const cart = document.querySelectorAll('.cart');
cart.forEach(elm => {
    elm.addEventListener('click', e => {
        const gameId = elm.closest('div').querySelector('.gameId').innerHTML.trim();
        const memId = elm.closest('div').querySelector('.memId').innerHTML.trim();

        axios.get('/PlayCentric/gamecart/insert', {
            params: {
                gameId: gameId,
                memId: memId
            }
        })
            .then(res => {
                elm.innerHTML = '下載';
                doAlert('成功加入購物車');
                console.log(res.data)
            })
            .catch(err => {
                console.error(err);
            })
    }, { once: true })
});