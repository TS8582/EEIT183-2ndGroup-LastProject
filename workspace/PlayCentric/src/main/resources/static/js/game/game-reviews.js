let starAmount = 0;
const reviews = document.querySelector('#reviews');
const reviewsContainer = document.querySelector('.reviewsContainer');
const goReviews = document.querySelector('.goReviews');
let gameId = document.querySelector('.gameId').innerHTML.trim();
const addReviews = document.querySelector('.addReviews');

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
                    const html = `
              <div 
                    class="existReviews mt-2 p-2 border-2 border-sky-200 bg-white rounded-md h-auto">
                    <div class="flex items-center space-x-2 mb-2">
                        <img class="h-16 w-16 rounded-full" th:src="${data.member.photoUrl}" alt="">
                        <span th:text="${data.member.nickname}"></span>
                        <div class="existStar select-none">
                            <div class="star-container">
                                <div class="filled" th:style="'width:' + ${data.reviewsScore * 20} + '%;'">★★★★★
                                </div>
                                <div class="unfilled">★★★★★</div>
                            </div>
                        </div>
                    </div>
                    <div>
                        <span th:text="${data.reviewsContent}"></span>
                    </div>
                </div>
        `;
                    reviewsContainer.prepend(html);
                    console.log(res);
                })
                .catch(err => {
                    console.error(err);
                })
        }
    }, { once: true })
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