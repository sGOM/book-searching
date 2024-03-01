async function requestAutocompleteSuggestions(keyword) {
    try {
        const res = await $.ajax({
            url: '/search/auto-complete',
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
            url: '/search?' + params,
            type: 'GET',
            success: function(res) {
                const resElement = new DOMParser().parseFromString(res, 'text/html');
                const booksSecWrapFromRes = resElement.getElementById('booksSecWrap');
                const booksSecWrapDiv = document.getElementById('booksSecWrap');
                booksSecWrapDiv.replaceWith(booksSecWrapFromRes);
            }
        });
    } catch (err) {
        console.log('Error:', err);
    }
}

async function requestPaymentConfirm(req) {
    const res = await $.ajax({
        url: "/payment/confirm",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(req),
        complete: function(xhr) {
            if (xhr.status !== 200) {
                const failure = res.failure;
                window.location.href = `/checkout-fail?message=${failure.message}&code=${failure.code}`;
            }
            alert('결제 성공!');
            history.go(-2);
        }
    });

    return res;
}

function createQueryString(paramsObject) {
    const queryParams = Object.keys(paramsObject).map(key => `${encodeURIComponent(key)}=${encodeURIComponent(paramsObject[key])}`);
    return queryParams.join('&');
}
