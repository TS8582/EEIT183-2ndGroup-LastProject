const isUsePhoto = document.querySelector('#isUsePhoto');
let filelist = [];
const imgs = document.querySelector('.imgs');
const photos = document.querySelector('#photos');
const showDiscount = document.querySelector('.showDiscount');
const discountChooser = document.querySelector('#discount-chooser');
let imgChooser = document.querySelector('.img-choose');
chooserAct();
let imgcount = 0;

discountChooser.addEventListener('change', e => {
    const rateset = document.querySelector('.rateset');
    if (discountChooser.value !== (0 || '')) {
        rateset.classList.remove('hidden');
    }
    else {
        const rateinput = document.querySelector('#discountRate');
        rateset.classList.add('hidden');
        rateinput.value = 0;
    }
})

//不允許不選分類
document.querySelector('form').addEventListener('submit', e => {
    let typeCheck = document.querySelectorAll(`input[name='typeId[]']`);
    let isCheck = false;
    typeCheck.forEach(elm => {
        if (elm.checked) {
            isCheck = true;
        }
    });
    updatefile();

    if (isCheck == false) {
        alert('分類尚未選擇');
        e.preventDefault();
    }

})

// 是否選擇圖片
isUsePhoto.addEventListener('change', e => {
    if (e.target.checked) {
        imgs.classList.add('grid', 'grid-cols-2', 'gap-3', 'w-full');
        imgs.classList.remove('hidden');
    }
    else {
        imgs.classList.add('hidden');
        imgs.classList.remove('grid', 'grid-cols-2', 'gap-3', 'w-full');
    }
})


// 選擇檔案事件
photos.addEventListener('change', e => {
    if (e.target.files.length + imgcount > 6) {
        const message = document.createElement('div');
        message.classList.add('col-span-2', 'message');
        message.innerText = '檔案數量超過上限，請重新嘗試。';
        imgs.append(message);
    }
    else {
        const message = document.querySelector('.message');
        if (message) {
            message.innerHTML = '';
        }
        imgChooser.remove();
        imgHtmlMaker(Array.from(e.target.files));
        newImgChooser();
        imgChooser = document.querySelector('.img-choose');
        chooserAct();
        e.target.value = '';
    }
    if (imgcount == 6) {
        imgChooser.remove();
    }
})


// 顯示選擇圖片
function imgHtmlMaker(data) {
    imgcount += data.length;
    for (const file of data) {
        filelist.push(file);
        const newimg = document.createElement('div');
        newimg.classList.add('one-img');
        const html = `
                    <div
                        class="absolute -top-2 -right-2 bg-black text-white rounded-full px-3 py-1 select-none cursor-pointer hover:bg-opacity-50 remove-img">
                        X
                    </div>

                    <div class="w-full h-full flex justify-center items-center select-none">
                        <img class="img-view" src="" alt="">
                    </div>
                `;
        newimg.innerHTML += html;
        if (data[0]) {
            const reader = new FileReader();
            reader.onload = function (e) {
                const closer = newimg.querySelector('.remove-img');
                closer.addEventListener('click', e => {
                    if (imgcount == 6) {
                        newImgChooser();
                    }
                    imgcount = imgcount - 1;
                    const index = filelist.indexOf(file);
                    filelist.splice(index, 1);
                    closer.closest('.one-img').remove();
                })
                newimg.querySelector(`.img-view`).src = e.target.result;
            }
            reader.readAsDataURL(file);
        }
        imgs.append(newimg);
    }
}
// 新的選圖按鈕
function newImgChooser() {
    const newImgChooser = document.createElement('div');
    newImgChooser.classList.add('img-choose');
    const html = `
             <div class="w-full h-full flex justify-center items-center  cursor-pointer">
                        <span class="text-3xl select-none">+</span>
                    </div>
            `;
    newImgChooser.innerHTML = html;
    imgs.append(newImgChooser);
}
// 重置選擇圖片事件
function chooserAct() {
    imgChooser.addEventListener('click', e => {
        photos.click();
    })
}

function updatefile() {
    let trans = new DataTransfer();

    filelist.forEach(file => trans.items.add(file));

    photos.files = trans.files;
}