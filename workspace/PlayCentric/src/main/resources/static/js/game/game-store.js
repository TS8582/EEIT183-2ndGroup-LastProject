let pgnum = 0;
let totalPages = document.querySelector('#pages').value;
const minPrice = document.querySelector('#minPrice');
const maxPrice = document.querySelector('#maxPrice');
let typeId = [];
const main = document.querySelector('main');
const spin = document.querySelector('.spin');
const typeselector = document.querySelectorAll('input[name="typeId"]');
let scrolltrigger = true;
let runcount = 0;

const gameName = document.querySelector('#gameName');
gameName.addEventListener('input', e => {
    const reg = new RegExp(gameName.value, 'i');
    const name = document.querySelectorAll('.name');
    name.forEach(myname => {
        if (reg.test(myname.innerHTML)) {
            myname.closest('.gameitem').classList.remove('hidden');
        }
        else {
            myname.closest('.gameitem').classList.add('hidden');
        }
    });
})

// 滑到底部載入事件
let scrollTimer = null;
let firstScroll = true; // 判斷是否是第一次滑到底部的標記

window.addEventListener('scroll', function () {
    if (firstScroll) {
        // 如果是第一次滑到底部，直接觸發加載內容
        handleScroll();
        firstScroll = false; // 將標記設為 false，表示不再是第一次滑到底部
    } else {
        // 如果有正在運行的定時器，先清除
        if (scrollTimer) {
            clearTimeout(scrollTimer);
        }

        // 設置定時檢查滾動位置
        scrollTimer = setTimeout(function () {
            handleScroll();
        }, 500); // 每隔一秒檢查一次滾動位置
    }
});

async function handleScroll() {
    var windowHeight = window.innerHeight;
    var documentHeight = document.documentElement.scrollHeight;
    var scrollTop = window.scrollY || window.pageYOffset || document.body.scrollTop + (document.documentElement && document.documentElement.scrollTop || 0);

    // 檢查是否滾動到底部
    if (windowHeight + scrollTop >= documentHeight && scrolltrigger && pgnum < totalPages - 1) {
        if (typeId.length === 0 && minPrice.value === '' && maxPrice.value === '') {
            spin.classList.remove('hidden');
            scrolltrigger = false;
            await nofilterplus();
            scrolltrigger = true; // 等待完成後重設 scrolltrigger
        } else if (typeId.length !== 0 && minPrice.value === '' && maxPrice.value === '') {
            spin.classList.remove('hidden');
            scrolltrigger = false;
            await typefilterplus();
            scrolltrigger = true; // 等待完成後重設 scrolltrigger
        } else if (minPrice.value !== '' && maxPrice.value !== '' && typeId.length === 0) {
            spin.classList.remove('hidden');
            scrolltrigger = false;
            await pricefilterplus();
            scrolltrigger = true; // 等待完成後重設 scrolltrigger
        } else if (minPrice.value !== '' && maxPrice.value !== '' && typeId.length !== 0) {
            spin.classList.remove('hidden');
            scrolltrigger = false;
            await typeAndPriceFilterPlus();
            scrolltrigger = true; // 等待完成後重設 scrolltrigger
        }
    }
}

async function myEvent() {
    try {
        if (typeId.length > 0 && minPrice.value === '' && maxPrice.value === '') {
            main.innerHTML = '';
            spin.classList.remove('hidden');
            scrolltrigger = false;
            await typefilter();
            scrolltrigger = true;
        } else if (typeId.length > 0 && minPrice.value !== '' && maxPrice.value !== '') {
            if (!isNaN(minPrice.value) && !isNaN(maxPrice.value) && minPrice.value > 0 && maxPrice.value > 0) {
                main.innerHTML = '';
                spin.classList.remove('hidden');
                scrolltrigger = false;
                await typeAndPriceFilter();
                scrolltrigger = true;
            }
            else {

            }
        } else if (minPrice.value !== '' && maxPrice.value !== '' && typeId.length === 0) {
            if (!isNaN(minPrice.value) && !isNaN(maxPrice.value) && minPrice.value > 0 && maxPrice.value > 0) {
                main.innerHTML = '';
                spin.classList.remove('hidden');
                scrolltrigger = false;
                await pricefilter();
                scrolltrigger = true;
            }
            else {

            }
        } else if (minPrice.value == '' && maxPrice.value == '' && typeId.length === 0) {
            main.innerHTML = '';
            spin.classList.remove('hidden');
            scrolltrigger = false;
            await nofilter();
            scrolltrigger = true;
        }
        else {
        }
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

minPrice.addEventListener('input', handlePriceFilter);
maxPrice.addEventListener('input', handlePriceFilter);

async function handlePriceFilter() {
    pgnum = 0; // 重置 pgnum 變數
    // 清空主要內容區域
    myEvent();
}

typeselector.forEach(typecheck => {
    typecheck.addEventListener('change', async e => {
        typeId = Array.from(typeselector)
            .filter(input => input.checked)
            .map(input => parseInt(input.value));
        pgnum = 0;
        myEvent();
    });
});

//標籤選擇
const typeTag = document.querySelectorAll('.mybtn-tag');
typeTag.forEach(tag => {
    tag.addEventListener('click', e => {
        const checkbox = tag.querySelector('input');
        if (tag.classList.contains('mybtn-tag')) {
            checkbox.checked = true;
            typeId.push(parseInt(checkbox.value));
            tag.classList.remove('mybtn-tag');
            tag.classList.add('tag-selected');
        } else {
            checkbox.checked = false;
            typeId = typeId.filter(item => item !== parseInt(checkbox.value));
            tag.classList.add('mybtn-tag');
            tag.classList.remove('tag-selected');
        }
        checkbox.dispatchEvent(new Event('change'));
    });
});

async function typefilter() {

    try {
        const response = await axios.get('/PlayCentric/game/getGamePageByType', {
            params: {
                pg: pgnum,
                typeId: typeId
            },
            paramsSerializer: function (params) {
                return Object.keys(params).map(key => {
                    if (Array.isArray(params[key])) {
                        return params[key].map(val => `${key}=${val}`).join('&');
                    }
                    return `${key}=${params[key]}`;
                }).join('&');
            }
        });
        runcount += 1;
        await wait(300);
        totalPages = response.data.totalPages;
        main.innerHTML = ''; // 清空主要內容區域
        spin.classList.add('hidden');
        for (const game of response.data.content) {
            if (runcount > 1) {
                break;
            }
            htmlmaker(game);
            await wait(300);
        }
    } catch (error) {
        console.error('Error fetching data:', error);
    } finally {
        runcount -= 1;
    }
}

async function typefilterplus() {

    try {
        const res = await axios.get('/PlayCentric/game/getGamePageByType', {
            params: {
                pg: pgnum + 1,
                typeId: typeId
            },
            paramsSerializer: function (params) {
                return Object.keys(params).map(key => {
                    if (Array.isArray(params[key])) {
                        return params[key].map(val => `${key}=${val}`).join('&');
                    }
                    return `${key}=${params[key]}`;
                }).join('&');
            }
        });
        totalPages = res.data.totalPages;
        spin.classList.add('hidden');
        for (const elm of res.data.content) {
            htmlmaker(elm);
            await wait(300);
        }
        pgnum += 1;
    } catch (err) {
        console.error(err);
    } finally {
    }
}

// async function pricefilter() {

//     try {
//         const response = await axios.get('/PlayCentric/game/getGamePageByPrice', {
//             params: {
//                 pg: pgnum,
//                 minPrice: minPrice.value,
//                 maxPrice: maxPrice.value
//             }
//         });
//         runcount += 1;
//         await wait(300);
//         totalPages = response.data.totalPages;
//         main.innerHTML = ''; // 清空主要內容區域
//         spin.classList.add('hidden');
//         for (const game of response.data.content) {
//             if (runcount > 1) {
//                 break;
//             }
//             htmlmaker(game);
//             await wait(300);
//         }
//     } catch (error) {
//         console.error('Error fetching data:', error);
//     } finally {
//         runcount -= 1;
//     }
// }

// async function pricefilterplus() {

//     try {
//         const res = await axios.get('/PlayCentric/game/getGamePageByPrice', {
//             params: {
//                 pg: pgnum + 1,
//                 minPrice: minPrice.value,
//                 maxPrice: maxPrice.value
//             }
//         });
//         totalPages = res.data.totalPages;
//         spin.classList.add('hidden');
//         for (const elm of res.data.content) {
//             htmlmaker(elm);
//             await wait(300);
//         }
//         pgnum += 1;
//     } catch (err) {
//         console.error(err);
//     } finally {
//     }
// }

// async function typeAndPriceFilter() {

//     try {
//         const res = await axios.get('/PlayCentric/game/getGamePageByPriceAndType', {
//             params: {
//                 pg: pgnum,
//                 typeId: typeId,
//                 minPrice: minPrice.value,
//                 maxPrice: maxPrice.value
//             },
//             paramsSerializer: function (params) {
//                 return Object.keys(params).map(key => {
//                     if (Array.isArray(params[key])) {
//                         return params[key].map(val => `${key}=${val}`).join('&');
//                     }
//                     return `${key}=${params[key]}`;
//                 }).join('&');
//             }
//         });
//         runcount += 1;
//         await wait(300);
//         totalPages = res.data.totalPages;
//         main.innerHTML = '';
//         spin.classList.add('hidden');
//         for (const elm of res.data.content) {
//             if (runcount > 1) {
//                 break;
//             }
//             htmlmaker(elm);
//             await wait(300);
//         }
//     } catch (err) {
//         console.error(err);
//     } finally {
//         runcount -= 1;
//     }
// }

// async function typeAndPriceFilterPlus() {

//     try {
//         const res = await axios.get('/PlayCentric/game/getGamePageByPriceAndType', {
//             params: {
//                 pg: pgnum + 1,
//                 typeId: typeId,
//                 minPrice: minPrice.value,
//                 maxPrice: maxPrice.value
//             },
//             paramsSerializer: function (params) {
//                 return Object.keys(params).map(key => {
//                     if (Array.isArray(params[key])) {
//                         return params[key].map(val => `${key}=${val}`).join('&');
//                     }
//                     return `${key}=${params[key]}`;
//                 }).join('&');
//             }
//         });
//         totalPages = res.data.totalPages;
//         spin.classList.add('hidden');
//         for (const elm of res.data.content) {
//             htmlmaker(elm);
//             await wait(300);
//         }
//         pgnum += 1;
//     } catch (err) {
//         console.error(err);
//     } finally {
//     }
// }

async function nofilter() {

    try {
        const res = await axios.get('/PlayCentric/game/getGamePage', { params: { pg: pgnum } });
        runcount += 1;
        await wait(300);
        totalPages = res.data.totalPages;
        main.innerHTML = ''; // 清空主要內容區域
        spin.classList.add('hidden');
        for (const elm of res.data.content) {
            if (runcount > 1) {
                break;
            }
            htmlmaker(elm);
            await wait(300);
        }
    } catch (err) {
        console.error(err);
    } finally {
        runcount -= 1;
    }
}

async function nofilterplus() {

    try {
        const res = await axios.get('/PlayCentric/game/getGamePage', { params: { pg: pgnum + 1 } });
        totalPages = res.data.totalPages;
        spin.classList.add('hidden');
        for (const elm of res.data.content) {
            htmlmaker(elm);
            await wait(300);
        }
        pgnum += 1;
    } catch (err) {
        console.error(err);
    } finally {
    }
}

function htmlmaker(game) {
    // 創建外層 div
    let gameItem = document.createElement('div');
    gameItem.classList.add('h-auto', 'shadow-md', 'border-2', 'rounded-lg', 'flex', 'flex-col', 'p-2', 'bg-white', 'border-sky-300', 'shadow-sky-500', 'fade-in-up', 'gameitem');

    // 圖片區域
    let imageContainer = document.createElement('div');
    imageContainer.classList.add('py-2', 'h-64', 'flex', 'justify-center', 'rounded-md');
    if (game.imageLibs.length > 0) {
        let img = document.createElement('img');
        img.classList.add('w-auto', 'h-full', 'rounded-md');
        img.src = `/PlayCentric/imagesLib/image${game.imageLibs[0].imageId}`;
        img.alt = '';
        let imglink = document.createElement('a');
        imglink.href = `/PlayCentric/game/showGame?gameId=${game.gameId}`;
        imglink.appendChild(img);
        imageContainer.appendChild(imglink);
    }
    else {
        let img = document.createElement('img');
        img.classList.add('w-full', 'h-full', 'rounded-md');
        let imglink = document.createElement('a');
        imglink.classList.add('w-full', 'h-full');
        imglink.href = `/PlayCentric/game/showGame?gameId=${game.gameId}`;
        imglink.appendChild(img);
        imageContainer.appendChild(imglink);
    }

    // 遊戲信息區域
    let gameInfo = document.createElement('div');
    gameInfo.classList.add('h-1/3', 'flex', 'text-black', 'flex-col', 'items-center', 'p-2');

    // 遊戲名稱
    let gameTitle = document.createElement('div');
    gameTitle.classList.add('flex', 'justify-center');
    let titleSpan = document.createElement('span');
    titleSpan.classList.add('text-xl', 'p-2', 'rounded-md', 'select-none', 'name');
    titleSpan.textContent = game.gameName;
    let namelink = document.createElement('a');
    namelink.href = `/PlayCentric/game/showGame?gameId=${game.gameId}`;
    namelink.appendChild(titleSpan);
    gameTitle.appendChild(namelink);

    // 遊戲價格
    let gamePrice = document.createElement('div');
    gamePrice.classList.add('flex', 'justify-center', 'my-2', 'space-x-3');
    if (game.rate != null) {
        let discountSpan = document.createElement('span');
        discountSpan.classList.add('price-tag', 'font-semibold');
        discountSpan.textContent = `-${100 - game.rate}%`;
        gamePrice.appendChild(discountSpan);

        let discountedPriceSpan = document.createElement('span');
        discountedPriceSpan.classList.add('price-tag', 'font-semibold');
        discountedPriceSpan.textContent = `NT$${game.discountedPrice}`;
        gamePrice.appendChild(discountedPriceSpan);
    } else {
        let priceSpan = document.createElement('span');
        priceSpan.classList.add('price-tag', 'text-black', 'font-semibold');
        priceSpan.textContent = `NT$${game.price}`;
        gamePrice.appendChild(priceSpan);
    }

    // 加入購物車按鈕
    let addToCartBtn;
    const memId = document.querySelector('.memId').innerHTML.trim();
    if (memId !== '') {
        if (game.inCart) {
            addToCartBtn = document.createElement('span');
            addToCartBtn.textContent = '已加入購物車';
            addToCartBtn.classList.remove('mybtn', 'mybtn-green');
            addToCartBtn.classList.add('mybtn-disabled');

        }
        else if (game.haveGame) {
            addToCartBtn = document.createElement('span');
            addToCartBtn.classList.add('mybtn', 'mybtn-green', 'cart');
            addToCartBtn.textContent = '下載';
        }
        else if (!game.inCart) {
            addToCartBtn = document.createElement('span');
            addToCartBtn.classList.add('mybtn', 'mybtn-green', 'cart');
            addToCartBtn.textContent = '加入購物車';
        }
    }
    else {
        addToCartBtn = document.createElement('a');
        addToCartBtn.classList.add('mybtn', 'mybtn-green', 'cart');
        addToCartBtn.textContent = '加入購物車';
        addToCartBtn.href = '/PlayCentric/member/login';
    }

    addToCart();

    let gameIdContainer = document.createElement('div');
    gameIdContainer.classList.add('hidden', 'gameId');
    gameIdContainer.innerHTML = game.gameId;

    // 添加所有元素到遊戲信息區域
    gameInfo.appendChild(gameTitle);
    gameInfo.appendChild(gamePrice);
    gameInfo.appendChild(addToCartBtn);
    gameInfo.appendChild(gameIdContainer);

    // 將圖片區域和遊戲信息區域添加到外層 div
    gameItem.appendChild(imageContainer);
    gameItem.appendChild(gameInfo);

    // 添加外層 div 到主要內容區域
    main.appendChild(gameItem);
    gameName.dispatchEvent(new Event('input'));
}


function wait(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}