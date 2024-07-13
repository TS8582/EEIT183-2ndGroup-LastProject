const aa = document.querySelector(`a[href="${window.location.pathname}"]`);
if (aa) {
    aa.classList.add('disabled');
}