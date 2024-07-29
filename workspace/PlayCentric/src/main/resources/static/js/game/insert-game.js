const isUsePhoto = document.querySelector('#isUsePhoto');
let filelist = [];
const imgs = document.querySelector('.imgs');
const photos = document.querySelector('#photos');
const showDiscount = document.querySelector('.showDiscount');
const discountChooser = document.querySelector('#discount-chooser');
let imgChooser = document.querySelector('.img-choose');
chooserAct();
let imgcount = 0;

const importdata = document.querySelector('#import');
if (importdata) {
    importdata.addEventListener('click', e => {
        document.querySelector('#gameName').value = '猜數字小遊戲';
        document.querySelector('#price').value = '188';
        document.querySelector('#developer').value = '彭琮畫';
        document.querySelector('#publisher').value = '彭琮畫';
        document.querySelector('#description').value =
            `
        這是一款簡單但充滿樂趣的猜數字小遊戲，這款遊戲不僅適合所有年齡層的玩家，也是一個很好的訓練思維和邏輯能力的工具。這款遊戲的核心功能包括設定數字範圍、猜對數字後顯示成功信息，以及猜錯數字後顯示目前已猜出的數字範圍。

遊戲的基本概念非常簡單：玩家需要在指定的數字範圍內猜出一個隨機生成的數字。玩家可以設置數字範圍，例如從1到100或1到1000，根據自己的需求和挑戰的難度來選擇。遊戲開始後，系統會隨機選擇一個數字，並等待玩家進行猜測。

在遊戲開始之前，玩家可以選擇設定一個數字範圍，這使得遊戲的難度和趣味性可以根據玩家的喜好進行調整。這一功能的實現讓玩家有更多的自主性，也提高了遊戲的重玩價值。當玩家猜對了系統隨機選擇的數字時，遊戲會立即顯示一條成功信息，這樣玩家會有一種成就感和滿足感。

如果玩家的猜測不正確，遊戲會告訴玩家當前已猜出的數字範圍。這樣的提示不僅幫助玩家逐步縮小猜測範圍，也增加了遊戲的互動性和趣味性。玩家可以根據這些提示逐步逼近正確答案，從而增加遊戲的挑戰性和娛樂性。

遊戲的流程也非常簡單易懂。首先，玩家設置一個數字範圍。這個範圍可以根據玩家的興趣和挑戰程度來設定，例如從1到100或1到1000。設置範圍後，系統會隨機選擇一個數字，然後玩家開始進行猜測。每次猜測後，系統會根據玩家的猜測給出相應的提示。如果玩家猜錯，系統會給出相應的提示，告訴玩家猜測的數字是太大還是太小。玩家可以根據這些提示逐步調整自己的猜測範圍。當玩家最終猜對了數字，系統會顯示一條成功信息，並統計玩家所花費的猜測次數。玩家可以根據這些數據來評估自己的表現，並在下次遊戲中挑戰更短的猜測次數。

這款猜數字小遊戲通過簡單的規則和有趣的互動，為玩家提供了一個休閒娛樂的好選擇。同時，設定數字範圍和反饋提示的功能，不僅增加了遊戲的挑戰性和趣味性，也讓玩家在遊戲過程中能夠不斷調整策略，提升自己的猜測能力和邏輯思維能力。無論是自己娛樂還是與朋友家人一起分享，這款遊戲都能帶來無盡的樂趣和挑戰。

總的來說，這款猜數字小遊戲是一個既簡單又具有挑戰性的遊戲。通過設置數字範圍、猜對數字顯示成功信息、以及猜錯數字顯示目前已猜出的數字範圍，這款遊戲不僅讓玩家享受猜測的樂趣，也提供了一個訓練邏輯思維的好機會。無論是用來打發時間還是進行腦力挑戰，這款遊戲都是一個不錯的選擇。希望大家能夠在遊戲中找到樂趣，挑戰自我，不斷提升自己的猜測技巧。
        `;
    })
}



let oneimg = document.querySelectorAll('.one-img');
if (oneimg.length > 0) {
    imgcount = oneimg.length;
    if (imgcount >= 6) {
        const chooser = document.querySelector('.img-choose');
        chooser.remove();
    }
    oneimg.forEach(img => {
        imgcount = oneimg.length;
        const closer = img.querySelector('.remove-img');
        closer.addEventListener('click', e => {
            if (imgcount == 6) {
                newImgChooser();
                chooserAct();
            }
            imgcount = imgcount - 1;
            closer.closest('.one-img').remove();
        })
    });
}


// 選擇優惠後跳出選擇折扣
discountChooser.addEventListener('change', e => {
    const rateset = document.querySelector('.rateset');
    const rateinput = document.querySelector('#discountRate');
    if (discountChooser.value != 0) {
        rateset.classList.remove('hidden');
        rateinput.disabled = false;
    }
    else {
        rateset.classList.add('hidden');
        rateinput.value = 0;
        rateinput.disabled = true;
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
        doAlert('分類尚未選擇');
        e.preventDefault();
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
                        class="remove-img">
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
                        chooserAct();
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
    imgChooser = document.querySelector('.img-choose');
    imgChooser.addEventListener('click', e => {
        photos.click();
    })
}

function updatefile() {
    let trans = new DataTransfer();

    filelist.forEach(file => trans.items.add(file));

    photos.files = trans.files;
}