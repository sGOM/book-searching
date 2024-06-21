async function requestAutocompleteSuggestions(keyword) {
    try {
        const res = await $.ajax({
            url: '/api/search/auto-complete',
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

async function requestPaymentConfirm(req) {
    const res = await $.ajax({
        url: "/api/payment/confirm",
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

async function requestSignIn(req) {
    const res = await $.ajax({
        url: "/api/users/sign-in",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(req),
        success: function(response) {
            alert('로그인 성공!');
            StorageManager.setLocalStorage('token', response.token);
            window.history.back();
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert('로그인 실패 : ' + errorThrown);
            console.error("Request failed: ", textStatus, errorThrown);
        }
    });

    return res;
}

async function requestSignUp(req) {
    const res = await $.ajax({
        url: "/api/users/sign-up",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(req),
        success: function(response) {
            alert('회원가입 성공!');
            window.location.href = '/users/login';
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert('회원가입 실패 : ' + errorThrown);
            console.error("Request failed: ", textStatus, errorThrown);
        }
    });

    return res;
}

async function requestUserAccount(accessToken) {
    const res = await $.ajax({
        url: "/api/users/me",
        type: "GET",
        headers: {
            Authorization: `Bearer ${accessToken}`
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error("Request failed: ", textStatus, errorThrown);
            return null;
        }
    });

    return res;
}
