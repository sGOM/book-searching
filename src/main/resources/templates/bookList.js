import { createElement } from "./createElement.js";

window.searchResult = null;

document.addEventListener("DOMContentLoaded", function () {
    function setSearchResult(newSearchResult) {
        const booksListWrap = document.getElementById("booksListWrap");
        const booksSecTit = booksListWrap.querySelector(".booksSecTit");
        booksSecTit.textContent = "검색 결과: " + newSearchResult.length;

        removeListItems(booksList);
        createBookListItems(newSearchResult);
    }

    Object.defineProperty(window, "searchResult", {
        set: function(newSearchResult) {
            setSearchResult(newSearchResult);
        }
    });
});

function removeListItems(listElement) {
    Array.from(listElement).forEach(list => {
        list.parentNode.removeChild(list);
    });
}

function createBookListItems(dataList) {
    const booksList = document.getElementById("booksList");

    Array.from(dataList).forEach(data => {
        const booksItem = createElement("li");
        booksItem.appendChild(createItemUnit(data));
        booksList.appendChild(booksItem);
    });
}

function createItemUnit(data) {
    const itemUnitDiv = createElement("div", ["item-unit"]);

    const imgDiv = createElement("div", ["item-img"]);

    const itemInfoDiv = createItemInfoDiv(data);

    const itemBtnColDiv = createElement("div", ["item-btn-col"]);

    const buyBtnA = createElement("a", ["btn-c", "btn-sBlue"]);
    const btnWrapSpan = createElement("span", ["btn-wrap"]);
    const btnTxtEm = createElement("em", ["txt"]);
    btnTxtEm.textContent = "바로구매";
    btnWrapSpan.appendChild(btnTxtEm);
    buyBtnA.appendChild(btnWrapSpan);

    itemBtnColDiv.appendChild(buyBtnA);

    itemUnitDiv.appendChild(imgDiv);
    itemUnitDiv.appendChild(itemInfoDiv);
    itemUnitDiv.appendChild(itemBtnColDiv);

    return itemUnitDiv;
}

function createItemInfoDiv(data) {
    const itemInfoDiv = createElement("div", ["item-info"]);

    // info title row
    const infoTitleDiv = createElement("div", ["info-row", "info-title"]);
    const bookTitleA = createElement("a", ["book-title"]);
    bookTitleA.textContent = data.title;
    infoTitleDiv.appendChild(bookTitleA);

    // info pub group row
    const infoPubGrpDiv = createElement("div", ["info-row", "info-pub-grp"]);
    const infoAuthSpan = createElement("span", ["auth-pub", "info-auth"]);
    infoAuthSpan.innerHTML = data.author;
    infoPubGrpDiv.appendChild(infoAuthSpan);

    // info price row
    const infoPriceDiv = createElement("div", ["info-row", "info-price"]);
    const priceNumStrong = createElement("strong", ["txt-num"]);
    const priceNumEm = createElement("em", ["book-price"]);
    priceNumStrong.appendChild(priceNumEm);
    infoPriceDiv.appendChild(priceNumStrong);

    // add rows into the itemInfoDiv
    itemInfoDiv.appendChild(infoTitleDiv);
    itemInfoDiv.appendChild(infoPubGrpDiv);
    itemInfoDiv.appendChild(infoPriceDiv);

    return itemInfoDiv;
}
