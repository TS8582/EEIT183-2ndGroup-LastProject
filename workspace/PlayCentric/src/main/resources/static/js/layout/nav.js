document.getElementById('mobile-menu-button').addEventListener('click', function () {
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