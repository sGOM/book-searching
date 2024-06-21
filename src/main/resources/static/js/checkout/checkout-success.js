// 쿼리 파라미터 값이 결제 요청할 때 보낸 데이터와 동일한지 반드시 확인하세요.
// 클라이언트에서 결제 금액을 조작하는 행위를 방지할 수 있습니다.
const urlParams = new URLSearchParams(window.location.search);

// 서버로 결제 승인에 필요한 결제 정보를 보내세요.
async function confirm() {
    const amount = StorageManager.getLocalStorage('amount');
    const items = StorageManager.getLocalStorage('items');
    StorageManager.removeLocalStorage('amount');
    StorageManager.removeLocalStorage('items');

    if (parseInt(urlParams.get("amount")) !== amount) {
        alert('결제 정보가 일치하지 않아, 이전 페이지로 이동합니다.');
        history.go(-2);
    }

    const requestData = {
        paymentKey: urlParams.get("paymentKey"),
        orderId: urlParams.get("orderId"),
        amount: urlParams.get("amount"),
        items: items
    };

    requestPaymentConfirm(requestData);
}

confirm();

const paymentKeyElement = document.getElementById("paymentKey");
const orderIdElement = document.getElementById("orderId");
const amountElement = document.getElementById("amount");

orderIdElement.textContent = "주문번호: " + urlParams.get("orderId");
amountElement.textContent = "결제 금액: " + urlParams.get("amount");
paymentKeyElement.textContent = "paymentKey: " + urlParams.get("paymentKey");
