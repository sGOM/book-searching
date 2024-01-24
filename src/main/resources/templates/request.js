import { createBooksListWrapHTML } from './bookList.js';

export async function requestAutocompleteSuggestions(keyword) {
    try {
        const res = await $.ajax({
            url: 'http://localhost:8080/books/search',
            type: 'GET',
            data: {
                keyword: keyword,
                page: 1,
                size: 10
            },
        });
        return res;
    } catch (err) {
        console.log('Error:', err);
    }
}

export async function requestBookSearch(paramsObject) {
    let params = createQueryString(paramsObject);

    //window.history.replaceState({ params: params }, '', '/books/search?' + params);

    try {
        const res = await $.ajax({
            url: 'http://localhost:8080/books/search?' + params,
            type: 'GET',
            success: function (data) {
                $('#booksListWrap').html(createBooksListWrapHTML(data));
            }
        });
    } catch (err) {
        console.log('Error:', err);
    }
}

function createQueryString(paramsObject) {
    const queryParams = Object.keys(paramsObject).map(key => `${encodeURIComponent(key)}=${encodeURIComponent(paramsObject[key])}`);
    return queryParams.join('&');
}
