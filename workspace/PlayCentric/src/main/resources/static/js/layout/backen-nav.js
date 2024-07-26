const urlPath = window.location.pathname
const tab = document.querySelector(`a[href="${urlPath}"]`);
if (tab) {
    tab.classList.add('disabled');
    tab.classList.add('disabled-tab');
}
if (urlPath.includes('/events/listEvents') || urlPath.includes('eventSignup/manage')) {
    document.querySelector('#event-dropdown').classList.add('disabled-tab')
}
