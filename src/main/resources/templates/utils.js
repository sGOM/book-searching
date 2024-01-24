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

export { createElement };
