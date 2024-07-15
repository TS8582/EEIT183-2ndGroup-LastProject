let allData = document.querySelectorAll('tbody tr');
const search = document.querySelector('#search');

allData.forEach(elm => {
    elm.addEventListener('mouseenter', () => {
        elm.classList.add('bg-gray-300');
    })
    elm.addEventListener('mouseleave', () => {
        elm.classList.remove('bg-gray-300');
    })
});

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