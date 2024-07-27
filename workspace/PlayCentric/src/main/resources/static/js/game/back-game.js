let allData = document.querySelectorAll('tbody tr');
const search = document.querySelector('#search');
const isShow = document.querySelectorAll('.isShow');

isShow.forEach(elm => {
    const handleClick = () => {
        if (elm.innerHTML === '上架') {
            down(elm);
        } else {
            up(elm);
        }
    };

    elm.addEventListener('click', handleClick, { once: true });

    function up(elm) {
        elm.classList.remove('mybtn-danger');
        elm.classList.add('mybtn-disabled');
        elm.innerHTML = '處理中';
        const gameId = elm.closest('td').querySelector('.gameId').innerHTML.trim();
        axios.get('/PlayCentric/game/isShow', { params: { gameId: gameId } })
            .then(res => {
                console.log(res);
                elm.classList.remove('mybtn-disabled');
                elm.classList.add('mybtn-green');
                elm.innerHTML = '上架';

                // 重新綁定點擊事件
                elm.addEventListener('click', handleClick, { once: true });
            })
            .catch(err => {
                elm.classList.remove('mybtn-disabled');
                elm.classList.add('mybtn-danger');
                elm.innerHTML = '下架';
                console.error(err);

                // 重新綁定點擊事件
                elm.addEventListener('click', handleClick, { once: true });
            });
    }

    function down(elm) {
        elm.classList.remove('mybtn-green');
        elm.classList.add('mybtn-disabled');
        elm.innerHTML = '處理中';
        const gameId = elm.closest('td').querySelector('.gameId').innerHTML.trim();
        axios.get('/PlayCentric/game/isShow', { params: { gameId: gameId } })
            .then(res => {
                console.log(res);
                elm.classList.remove('mybtn-disabled');
                elm.classList.add('mybtn-danger');
                elm.innerHTML = '下架';

                // 重新綁定點擊事件
                elm.addEventListener('click', handleClick, { once: true });
            })
            .catch(err => {
                elm.classList.remove('mybtn-disabled');
                elm.classList.add('mybtn-green');
                elm.innerHTML = '上架';
                console.error(err);

                // 重新綁定點擊事件
                elm.addEventListener('click', handleClick, { once: true });
            });
    }
});



// 滑鼠移過去資料列變色
allData.forEach(elm => {
    elm.addEventListener('mouseenter', () => {
        elm.classList.add('bg-gray-300');
    })
    elm.addEventListener('mouseleave', () => {
        elm.classList.remove('bg-gray-300');
    })
});

//簡易模糊查詢
search.addEventListener('keyup', () => {
    allData.forEach(data => {
        if (!data.innerText.includes(search.value)) {
            data.classList.add('hidden');
        }
        else {
            data.classList.remove('hidden');
        }
    });
})