const result = document.querySelector('.anno');
const type = document.querySelector('.type');
type.addEventListener('change', (e) => {
    let url = `http://localhost:8080/PlayCentric/anno/getOneTypeAnno`;
    if (type.value != 0) {
        axios.get(url, {
            params: {
                typeId: type.value
            }
        })
            .then(res => {
                result.innerHTML = '';
                res.data.forEach(elm => {
                    htmlMaker(elm);
                });
            })
            .catch(err => {
                console.error(err);
            })
    }
    if (type.value == 0) {
        url = 'http://localhost:8080/PlayCentric/anno/getAllAnno';
        axios.get(url)
            .then(res => {
                result.innerHTML = '';
                res.data.forEach(elm => {
                    htmlMaker(elm);
                });
            })
            .catch(err => {
                console.error(err);
            })
    }
})

function htmlMaker(data) {
    result.innerHTML += `
				<div class="h-auto col-span-12 md:col-span-6 text-left text-2xl font-bold border-dotted md:border-b-2 border-sky-300">
					<a href="/PlayCentric/anno/showOneAnno?annoId=${data.annoId}"
						>[${data.announcementType.annoTypeName}]${data.title}</a>
						</div>
						<div class="h-auto col-span-12 md:col-span-6 text-right text-2xl border-dotted border-b-2 border-sky-300">
							<span>${data.createAt}</span>
						</div>
			`;

}