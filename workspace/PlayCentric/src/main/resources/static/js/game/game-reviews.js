let starAmount = 0;
const reviews = document.querySelector('#reviews');
const reviewsContainer = document.querySelector('.reviewsContainer');
const goReviews = document.querySelector('.goReviews');
let gameId = document.querySelector('.gameId').innerHTML.trim();
const addReviews = document.querySelector('.addReviews');
const editReviews = document.querySelector('.editReviews');
const filled = document.querySelector('#staryo');



function edit(elm) {
    if (elm.innerHTML.trim() == '修改') {

    }
    else {

    }
}

const myGameReviews = document.querySelector('.myGameReviews');

//提交評論
if (goReviews) {
    goReviews.addEventListener('click', e => {
        const content = reviews.value;
        if (starAmount != 0 && content != '') {
            axios.post('/PlayCentric/personal/api/gameReviews/add', {
                gameId: gameId,
                reviewsScore: starAmount,
                reviewsContent: content
            })
                .then(res => {
                    addReviews.remove();
                    const data = res.data;
                    let title = document.createElement('h2');
                    title.classList.add('text-2xl', 'text-center', 'font-semibold');
                    if (location.pathname.includes('gameReviews/show') && myGameReviews) {
                        title.innerHTML = myGameReviews.innerHTML;
                        myGameReviews.remove();
                    }
                    else {
                        title.innerHTML = '評論';
                    }
                    let existReviews1 = document.createElement('div');
                    existReviews1.classList.add('existReviews');

                    const html = `
                    <div class="flex items-center space-x-2 mb-2">
                    <img class="h-16 w-16 rounded-full" src="${data.member.photoUrl}" alt="">
                    <span>${data.member.nickname}</span>
                    <div class="existStar select-none">
                    <div class="star-container">
                    <div class="filled" style="width:${data.reviewsScore * 20}%;">★★★★★
                    </div>
                    <div class="unfilled">★★★★★</div>
                    </div>
                    </div>
                    </div>
                    <div>
                    <span>${data.reviewsContent}</span>
                    </div>
                    `;
                    let myReviewTitle = document.createElement('h2');
                    myReviewTitle.classList.add('text-xl', 'font-semibold', 'text-center');
                    myReviewTitle.innerHTML = '我的評論';
                    existReviews1.innerHTML = html;
                    existReviews1.prepend(myReviewTitle);
                    let noReview = document.querySelector('.noReview');
                    let h22 = reviewsContainer.querySelector('h2');
                    if (noReview) {
                        noReview.remove();
                    }
                    if (h22) {
                        h22.remove();
                    }
                    reviewsContainer.prepend(existReviews1);
                    reviewsContainer.prepend(title);
                    if (filled) {
                        let starnum = document.querySelector('#starnum');
                        let totalreviews = document.querySelector('#totalReviews').innerHTML;
                        let totalScore = document.querySelector('#totalScore').innerHTML;
                        let allcount = parseFloat(parseInt(totalreviews) + 1);
                        let allscore = parseFloat(parseInt(totalScore) + starAmount);
                        let avgScore = (allscore / allcount).toFixed(1);
                        let result = avgScore * 20;
                        starnum.innerHTML = avgScore;
                        filled.setAttribute('style', `width:${result}%;`);
                    }
                })
                .catch(err => {
                    console.error(err);
                })
        }
        else {
            doAlert('請給評分及評論！');
        }
    })
}



// 跳頁到更多評論
const moreReviews = document.querySelector('.moreReviews');
if (moreReviews) {
    moreReviews.addEventListener('click', e => {
        window.location.href = `/PlayCentric/gameReviews/show?gameId=${gameId}`;
    }, { once: true })
}

// 新增評論選星星
const star = document.querySelector('.star');
if (star) {
    chooseStar();
}

function chooseStar() {
    const starContainer = star.querySelectorAll('.star-container');
    starContainer.forEach((elm, i) => {
        elm.addEventListener('mouseenter', e => {
            if (i < (starAmount - 1)) {
                for (let g = i + 1; g < starContainer.length; g++) {
                    starContainer[g].classList.remove('color-gold');
                }
            }
            for (let j = 0; j <= i; j++) {
                starContainer[j].classList.add('color-gold');
            }
        })

        elm.addEventListener('mouseleave', e => {
            for (let j = 0; j <= i; j++) {
                starContainer[j].classList.remove('color-gold');
            }
            if (starAmount != 0) {
                for (let j = 0; j <= (starAmount - 1); j++) {
                    starContainer[j].classList.add('color-gold');
                }
            }
        })

        elm.addEventListener('click', e => {
            starContainer.forEach(el => {
                el.classList.remove('color-gold');
            });
            for (let j = 0; j <= i; j++) {
                starContainer[j].classList.add('color-gold');
                starAmount = i + 1;
            }
        })

    });
}