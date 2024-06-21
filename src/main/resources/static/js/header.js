document.addEventListener('DOMContentLoaded', async function() {
    let res = await getTokenAndRequestUserAccount();
    let headerRight = document.getElementById('header-right');

    if (res === null) {
        headerRight.innerHTML = `
            <button class="sign-in-btn" onclick="window.location.href='users/sign-in'">
                로그인
            </button>
        `;
    } else {
        headerRight.innerHTML = `
            <span class="header-nickname">
                ${res.username} 님
            </span>
        `;
    }
});

async function getTokenAndRequestUserAccount() {
    let token = StorageManager.getLocalStorage('token');
    let res = null;
    if (token !== null) {
        res = await requestUserAccount(token);
        if (res === null) {
            StorageManager.removeLocalStorage('token');
        }
    }

    return res;
}
