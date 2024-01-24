import { createElement } from './utils.js';

export function createBooksListWrapHTML(data) {
    const booksInfo = data.booksInfo;
    const pageInfo = data.pageInfo;
    const elements = [];

    elements.push(createBooksSecTopDiv(pageInfo.totalElements));
    elements.push(createBooksSecAreaDiv(booksInfo));
    elements.push(createBooksPaginationDiv(pageInfo));

    return elements;
}

function createBooksSecTopDiv(totalElements) {
    const booksSecTopDiv = createElement("div", ["booksSecTop"]);
    const booksSecTitDiv = createElement("div", ["booksSecTit"]);

    booksSecTitDiv.textContent = "검색 결과: " + totalElements;

    booksSecTopDiv.appendChild(booksSecTitDiv);

    return booksSecTopDiv;
}

function createBooksSecAreaDiv(booksInfo) {
    const booksSecAreaDiv = createElement("div", ["booksSecArea"]);
    const booksListUl = createElement("ul", ["bLi"], "booksList");

    fillBooksList(booksListUl, booksInfo);

    booksSecAreaDiv.appendChild(booksListUl);

    return booksSecAreaDiv;
}

function fillBooksList(booksListUl, booksInfo) {
    Array.from(booksInfo).forEach(bookInfo => {
        const booksItem = createElement("li");
        booksItem.appendChild(createItemUnit(bookInfo));
        booksListUl.appendChild(booksItem);
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

function createBooksPaginationDiv(pageInfo) {
    const barNumberList = pageInfo.barNumberList;
    const currentPageNum = pageInfo.page;

    const booksPaginationDiv = createElement("div", ["booksPagination"]);
    const uiPaginationDiv = createElement("div", ["ui-pagination"]);

    Array.from(barNumberList).forEach(barNum => {
        let paginationBtn;
        if (barNum == currentPageNum) {
            paginationBtn = createElement("strong", ["num"]);
            paginationBtn.innerHTML = barNum;
        } else {
            paginationBtn = createElement("a", ["num"]);
            paginationBtn.title = barNum;
            paginationBtn.innerHTML = barNum;
        }
        uiPaginationDiv.appendChild(paginationBtn);
    });

    booksPaginationDiv.appendChild(uiPaginationDiv);

    return booksPaginationDiv;
}
