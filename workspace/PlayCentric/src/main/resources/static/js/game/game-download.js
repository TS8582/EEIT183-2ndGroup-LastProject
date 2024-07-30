let download = document.querySelector('.download');
let gameIdD = download.closest('div').querySelector('.gameId').innerHTML.trim();
console.log(download.closest('div').querySelector('.gameId'));
download.addEventListener('click', downloadFile)
function downloadFile() {
    axios({
        url: `/PlayCentric/personal/game/download?gameId=${gameIdD}`,
        method: 'GET',
        responseType: 'blob' // 指定為blob類型
    }).then((response) => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'GuessNumber.exe'); // 設置下載的文件名
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }).catch((error) => {
        console.error('Error downloading file:', error);
    });
}