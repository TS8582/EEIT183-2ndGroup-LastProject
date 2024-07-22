const total = document.querySelector('.total');
const price = document.querySelectorAll('.price');
const gamecount = document.querySelector('.gamecount');
let totalPrice = 0;
let totalGame = parseInt(gamecount.innerHTML.trim());
price.forEach(elm => {
    totalPrice += parseInt(elm.innerHTML);
});
total.innerHTML = totalPrice;

const remove = document.querySelectorAll('.remove');
remove.forEach(elm => {
    elm.addEventListener('click', e => {
        const thisprice = parseInt(elm.closest('.game').querySelector('.price').innerHTML.trim());
        totalGame -= 1;
        gamecount.innerHTML = totalGame;
        totalPrice -= thisprice;
        total.innerHTML = totalPrice;
        const gameId = parseInt(elm.closest('.game').querySelector('.gameId').innerHTML.trim());
        console.log(gameId);
        axios.get('/PlayCentric/gamecart/remove', {
            params: {
                gameId: gameId
            }
        })
            .then(res => {
                elm.closest('.game').remove();
            })
            .catch(err => {
                console.error(err);
            })
    })
});