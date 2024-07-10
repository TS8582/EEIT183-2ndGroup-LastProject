console.log(window.location.pathname);
let prop = document.querySelector('.prop');
switch (window.location.pathname) {
    case '/PlayCentric/back/anno':
        const anno = document.querySelector('.anno');
        anno.classList.add('left-selected');
        break;
    case '/PlayCentric/back/game':
        const game = document.querySelector('.game');
        game.classList.add('left-selected');
        break;
    case '/PlayCentric/member/memManage':
        const mem = document.querySelector('.mem');
        mem.classList.add('left-selected');
        break;
    case '/PlayCentric/prop/propSheet':
        prop.classList.add('left-selected');
        break;
    case '/PlayCentric/prop/propTradeRecord':
        prop.classList.add('left-selected');
        break;
    case '/PlayCentric/prop/propSellOrder':
        prop.classList.add('left-selected');
        break;
    case '//PlayCentric/playFellow/cms':
        const playf = document.querySelector('.playf');
        playf.classList.add('left-selected');
        break;
    case '/PlayCentric/events/listEvents':
        const event = document.querySelector('.event');
        event.classList.add('left-selected');
        break;
    case '/PlayCentric/forum/page':
        const forum = document.querySelector('.forum');
        forum.classList.add('left-selected');
        break;

    default:
        break;
}