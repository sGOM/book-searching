async function requestAutocompleteSuggestions(keyword) {
    try {
        const res = await $.ajax({
            url: 'http://localhost:8080/search/auto-complete',
            type: 'GET',
            data: {
                keyword: keyword
            },
        });
        return res;
    } catch (err) {
        console.log('Error:', err);
    }
}

async function requestBookSearch(paramsObject) {
    let params = createQueryString(paramsObject);

    window.history.replaceState({ params: params }, '', '/search?' + params);

    try {
        const res = await $.ajax({
            url: 'http://localhost:8080/search?' + params,
            type: 'GET',
            success: function(res) {
                    document.open();
                    document.write(res);
                    document.close();
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
