function createElement(elementTag, classNameList, id, type) {
    const el = document.createElement(elementTag);

    if (Array.isArray(classNameList) && classNameList.length > 0) {
        el.classList.add(...classNameList);
    }

    if (id) {
        el.id = id;
    }

    if (type) {
        el.type = type;
    }

    return el;
}

function generateUrl(baseUrl, params) {
    let url = baseUrl;
    let queries = Object.keys(params).map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`);

    return url + '?' + queries.join('&');
}
