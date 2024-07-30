
function alertAndRedirect() {
	Swal.fire({
		title: '請登入會員',
		icon: 'warning',
		confirmButtonText: '確定'
	}).then((result) => {
		if (result.isConfirmed) {
			window.location.href = "http://localhost:8080/PlayCentric/member/login";
		}
	});
}

document.addEventListener('DOMContentLoaded', function() {

	const swiper = new Swiper('.swiper-container', {
		slidesPerView: 2,
		spaceBetween: 120,
		loop: true,
		autoplay: {
			delay: 3000,
			disableOnInteraction: false,
		},
		pagination: {
			el: '.swiper-pagination',
			clickable: true,
		},
		navigation: {
			nextEl: '.swiper-button-next',
			prevEl: '.swiper-button-prev',
		},
	});
});






document.addEventListener('DOMContentLoaded', function() {
	const modal = document.getElementById('exampleModal');
	let selectedPfGameId = null;  // 用於存儲選中的 pfGameId

	modal.addEventListener('show.bs.modal', function(event) {
		const button = event.relatedTarget;
		const playFellowId = button.getAttribute('data-playfellowid');
		const modalBodyContent = document.getElementById('modal-body-content');

		modalBodyContent.innerHTML = '';

		axios.get('/PlayCentric/playFellow/showGame', {
			params: {
				playFellowId: playFellowId
			}
		})
			.then(function(response) {
				const data = response.data;
				if (data.length > 0) {
					data.forEach(gameDTO => {
						const gameInfo = `
		                        <div class="game-info">
		                            <div class="game-title" data-pfgameid="${gameDTO.pfGameId}">
		                               🌟 ${gameDTO.gameName}
		                            </div>
		                            <div class="game-details">
		                                <p>$ ${gameDTO.amount} 元 / ${gameDTO.pricingCategory}</p>
		                            </div>
		                        </div>
		                    `;
						modalBodyContent.innerHTML += gameInfo;
					});

					document.querySelectorAll('.game-title').forEach(title => {
						title.addEventListener('click', function() {
							document.querySelectorAll('.game-details').forEach(detailsDiv => {
								if (detailsDiv !== this.nextElementSibling) {
									detailsDiv.style.display = 'none';
								}
							});

							const detailsDiv = this.nextElementSibling;
							if (detailsDiv.style.display === 'none' || detailsDiv.style.display === '') {
								detailsDiv.style.display = 'block';
							} else {
								detailsDiv.style.display = 'none';
							}

							selectedPfGameId = this.getAttribute('data-pfgameid');
						});
					});
				} else {
					modalBodyContent.innerHTML = '<p>系統錯誤，請稍後再試</p>';
				}
			})
			.catch(function(error) {
				console.error(error);
				modalBodyContent.innerHTML = '<p>系統錯誤，請稍後再試</p>';
			});
	});

	document.getElementById('orderButton').addEventListener('click', function() {
		if (selectedPfGameId) {
			window.location.href = `http://localhost:8080/PlayCentric/playFellow/playFellowCart?pfGameId=${selectedPfGameId}`;
		} else {
			alert('請先選擇一個遊戲');
		}
	});

});











document.addEventListener('DOMContentLoaded', function() {
	const birthdays = document.querySelectorAll('.member-birthday');

	birthdays.forEach(function(birthdayElem) {
		const birthdayText = birthdayElem.textContent.trim();
		if (birthdayText) {
			const birthDate = new Date(birthdayText);
			const age = calculateAge(birthDate);
			birthdayElem.textContent = `${age}`;
		}
	});

	function calculateAge(birthDate) {
		const today = new Date();
		let age = today.getFullYear() - birthDate.getFullYear();
		const monthDiff = today.getMonth() - birthDate.getMonth();

		if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
			age--;
		}

		return age;
	}
});


$(document).ready(function() {
	swiper = new Swiper('.main-swiper', {
		loop: true,
		autoplay: {
			delay: 1699,
		},
		pagination: {
			el: '.swiper-pagination',
			clickable: true,
		},
		navigation: {
			nextEl: '.swiper-button-next',
			prevEl: '.swiper-button-prev',
		},
	});

	// 初始化 Isotope for PlayFellow
	var $playfellowContainer = $('#playfellow-container').isotope({
		itemSelector: '.item',
		layoutMode: 'fitRows'
	});

	// 過濾按鈕的點擊事件 for PlayFellow
	$('.filter-button').click(function() {
		// 切換 active 樣式
		$('.filter-button').removeClass('active');
		$(this).addClass('active');

		// 過濾項目
		var filterValue = $(this).attr('data-filter');
		if (filterValue === '*') {
			// 顯示所有項目
			$playfellowContainer.isotope({
				filter: '*'
			});
		} else {
			// 顯示過濾後的項目
			$playfellowContainer.isotope({
				filter: filterValue
			});
		}
	});
});


async function loadImage(imageId, imgElement) {
	try {
		const response = await axios.get(`/PlayCentric/api/images/${imageId}`,
			{ responseType: 'arraybuffer' });

		const uint8Array = new Uint8Array(response.data);
		const base64Image = arrayBufferToBase64(uint8Array);

		const imageUrl = `data:image/jpeg;base64,${base64Image}`;
		if (imgElement) {
			imgElement.src = imageUrl;
		}
	} catch (error) {
		console.error(error);
	}
}

function arrayBufferToBase64(buffer) {
	let binary = '';
	const bytes = new Uint8Array(buffer);
	const len = bytes.byteLength;
	for (let i = 0; i < len; i++) {
		binary += String.fromCharCode(bytes[i]);
	}
	return btoa(binary);
}


document.addEventListener('DOMContentLoaded', () => {
	const imageElements = document.querySelectorAll('[data-image-id]');
	const observerOptions = {
		root: null,
		rootMargin: '0px',
		threshold: 0.5
	};

	const observer = new IntersectionObserver((entries, observer) => {
		entries.forEach(entry => {
			if (entry.isIntersecting) {
				const img = entry.target;
				const imageId = img.getAttribute('data-image-id');
				loadImage(imageId, img);
				observer.unobserve(img);
			}
		});
	}, observerOptions);

	imageElements.forEach(img => {
		observer.observe(img);
	});
});

document.addEventListener('DOMContentLoaded', () => {
	const swiper = new Swiper('.gameSwiper', {
		slidesPerView: 9,
		spaceBetween: 10,
		loop: true,
		autoplay: {
			delay: 10000,
			disableOnInteraction: false,
		},
		lazy: {
			loadPrevNext: true, // 加載相鄰的幻燈片
		},
		pagination: {
			el: '.swiper-pagination',
			clickable: true,
		},
		navigation: {
			nextEl: '.swiper-button-next',
			prevEl: '.swiper-button-prev',
		},
	});


});
document.addEventListener('DOMContentLoaded', function() {

	const swiper = new Swiper('.swiper-container', {
		slidesPerView: 2,
		spaceBetween: 120,
		loop: true,
		autoplay: {
			delay: 3000,
			disableOnInteraction: false,
		},
		pagination: {
			el: '.swiper-pagination',
			clickable: true,
		},
		navigation: {
			nextEl: '.swiper-button-next',
			prevEl: '.swiper-button-prev',
		},
	});
});
