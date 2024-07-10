let allData = document.querySelectorAll('tbody tr');
const search = document.querySelector('#search');
const type = document.querySelector('#type');
const tbody = document.querySelector('tbody');

trHover();

type.addEventListener('change', (e) => {
    let url = `http://localhost:8080/PlayCentric/anno/getOneTypeAnno`;
    if (type.value != 0) {
        axios.get(url, {
            params: {
                typeId: type.value
            }
        })
            .then(res => {
                tbody.innerHTML = '';
                res.data.forEach(elm => {
                    htmlMaker(elm);
                });
                allData = document.querySelectorAll('tbody tr');
                trHover();
            })
            .catch(err => {
                console.error(err);
            })
    }
    if (type.value == 0) {
        url = 'http://localhost:8080/PlayCentric/anno/getAllAnno';
        axios.get(url)
            .then(res => {
                tbody.innerHTML = '';
                res.data.forEach(elm => {
                    htmlMaker(elm);
                });
                allData = document.querySelectorAll('tbody tr');
                trHover();
            })
            .catch(err => {
                console.error(err);
            })
    }
})
function htmlMaker(data) {
    tbody.innerHTML += `<tr>
                            <td class="border p-3 title">
                              <a href="/PlayCentric/anno/showOneAnno?annoId=${data.annoId}"
                               class="text-center">${data.title}</a>
                               </td>
                              <td
                                 class="border p-3 title text-center">
                                  ${data.announcementType.annoTypeName}
                                  </td>
                                  <td class="text-center border p-3 createAt">${data.createAt}</td>
                                  <td class="text-center border p-3 lastEditAt">${data.lastEditAt}</td>
                                <td class="text-center border p-3">
                                   <a href="/PlayCentric/anno/getUpdatAnnoannoId=${data.annoId}"
                                 class="btn btn-sky rounded-md me-2 block md:inline">修改</a>
                                 <a href="/PlayCentric/anno/deleteAnno?annoId=${data.annoId}"
                                  class="btn btn-danger rounded-md me-2 block md:inline">刪除</a>
                                    </td>
                                </tr>
			`;

}

function trHover() {
    allData.forEach(elm => {
        elm.addEventListener('mouseenter', () => {
            elm.classList.add('bg-gray-300');
        })
        elm.addEventListener('mouseleave', () => {
            elm.classList.remove('bg-gray-300');
        })
    });
}

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