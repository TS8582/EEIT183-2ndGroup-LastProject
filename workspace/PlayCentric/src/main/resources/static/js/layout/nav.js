document.getElementById('mobile-menu-button').addEventListener('click', function() {
	var mobileMenu = document.getElementById('mobile-menu');
	if (mobileMenu.classList.contains('hidden')) {
		mobileMenu.classList.remove('hidden');
	} else {
		mobileMenu.classList.add('hidden');
	}
});
const alltag = document.querySelectorAll('.tag');
alltag.forEach(elm => {
	elm.addEventListener('mouseenter', () => {
		elm.querySelector('.dropmenu').classList.remove('hidden');
	})
	elm.addEventListener('mouseleave', () => {
		elm.querySelector('.dropmenu').classList.add('hidden');
	})
});

const alltagm = document.querySelectorAll('.tag-m');
alltagm.forEach(elm => {
	elm.addEventListener('click', (e) => {

		if (elm.querySelector('.dropmenu-m').classList.contains('hidden')) {
			alltagm.forEach(el2 => {
				el2.querySelector('.dropmenu-m').classList.add('hidden');
			});
			elm.querySelector('.dropmenu-m').classList.remove('hidden');
		}
		else {
			elm.querySelector('.dropmenu-m').classList.add('hidden');
		}
	})
});
document.addEventListener('DOMContentLoaded', function() {
	const addPfMemberBtn = document.querySelector('#addPfMember');

	if (addPfMemberBtn) {
		addPfMemberBtn.addEventListener('click', function() {
			const memberId = '6'; // 之後要換上當下的會員身分 替換成實際的伴遊者ID
			const url = `http://localhost:8080/PlayCentric/playFellow/memId/${memberId}`;
			window.location.href = url;
		});
	}
});