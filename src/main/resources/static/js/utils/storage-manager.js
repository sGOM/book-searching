window.StorageManager = {
    setLocalStorage(key, value) {
        localStorage.setItem(key, JSON.stringify(value));
    },

    getLocalStorage(key) {
        const storedValue = localStorage.getItem(key);
        return storedValue ? JSON.parse(storedValue) : null;
    },

    removeLocalStorage(key) {
        localStorage.removeItem(key);
    },

    setCookie(name, value, days) {
        let expires = '';
        if (days) {
            const date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            expires = `expires=${date.toUTCString()};`;
        }
        document.cookie = `${name}=${value};${expires}path=/`;
    },

    getCookie(name) {
        const cookieArray = document.cookie.split(';');
        for (const cookie of cookieArray) {
            const [cookieName, cookieValue] = cookie.split('=');
            if (cookieName.trim() === name) {
                return cookieValue;
            }
        }
        return null;
    },

    removeCookie(name) {
        document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
    },
};
