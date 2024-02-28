function openModal(txtBookInfo) {
    bookInfo = textToObject(txtBookInfo)

    setPaymentWidget(bookInfo);

    document.getElementById('checkout-modal-wrapper').style.display = 'block';
}

function closeModal() {
    document.getElementById('checkout-modal-wrapper').style.display = 'none';
}

function textToObject(txtObj) {
    const start = txtObj.indexOf('[') + 1;
    const end = txtObj.lastIndexOf(']');
    const data = txtObj.slice(start, end);

    const pairs = data.split(", ");
    const result = {};

    for (let i = 0; i < pairs.length; i++) {
        let parts = pairs[i].split("=");
        result[parts[0]] = parts[1];
    }

    return result;
}

function generateOrderID() {
  const timestamp = new Date().getTime().toString();

  // 랜덤 문자열 생성 함수
  const generateRandomString = () => {
    return Math.random().toString(36).substring(2, 8);
  };

  const randomString = generateRandomString();

  const orderID = `${timestamp}-${randomString}`;

  return orderID;
}

function setPaymentWidget(bookInfo) {
    const button = removeAllEventListeners(document.getElementById("payment-button"));
    const coupon = removeAllEventListeners(document.getElementById("coupon-box"));

    let amount = Math.floor(bookInfo.price);
    const items = [];
    items.push({id: bookInfo.isbn, quantity: 1});
    StorageManager.setLocalStorage('amount', amount);
    StorageManager.setLocalStorage('items', items);

    // ------  결제위젯 초기화 ------
    // TODO: 구매자의 고유 아이디를 불러와서 customerKey로 설정하세요. 이메일・전화번호와 같이 유추가 가능한 값은 안전하지 않습니다.
    // @docs https://docs.tosspayments.com/reference/widget-sdk#sdk-설치-및-초기화
    const clientKey = "test_ck_jkYG57Eba3G7GdKlLJL3pWDOxmA1";
    //const customerKey = ;
    //const paymentWidget = PaymentWidget(clientKey, customerKey); // 회원 결제
    const paymentWidget = PaymentWidget(clientKey, PaymentWidget.ANONYMOUS); // 비회원 결제

    // ------  결제 UI 렌더링 ------
    // @docs https://docs.tosspayments.com/reference/widget-sdk#renderpaymentmethods선택자-결제-금액-옵션
    // @docs https://docs.tosspayments.com/guides/payment-widget/admin#멀티-결제-ui
    paymentMethodWidget = paymentWidget.renderPaymentMethods(
        "#payment-method",
        { value: amount },
        { variantKey: "DEFAULT" }
    );

    // ------  이용약관 UI 렌더링 ------
    // @docs https://docs.tosspayments.com/reference/widget-sdk#renderagreement선택자-옵션
    paymentWidget.renderAgreement("#agreement", { variantKey: "AGREEMENT" });

    // ------  결제 금액 업데이트 ------
    // @docs https://docs.tosspayments.com/reference/widget-sdk#updateamount결제-금액
    coupon.addEventListener("change", function () {
        if (coupon.checked) {
            paymentMethodWidget.updateAmount(amount - 5000);
        } else {
            paymentMethodWidget.updateAmount(amount);
        }
    });

    // ------ '결제하기' 버튼 누르면 결제창 띄우기 ------
    // @docs https://docs.tosspayments.com/reference/widget-sdk#requestpayment결제-정보
    button.addEventListener("click", function () {
        // 결제를 요청하기 전에 orderId, amount를 서버에 저장하세요.
        // 결제 과정에서 악의적으로 결제 금액이 바뀌는 것을 확인하는 용도입니다.
        try {
            paymentWidget.requestPayment({
                orderId: generateOrderID(),
                orderName: bookInfo.title + " 1권",
                successUrl: window.location.origin + "/payment/checkout-success",
                failUrl: window.location.origin + "/payment/checkout-fail"
            });
        } catch (e) {
            console.log(e);
        }
    });
}

function removeAllEventListeners(element) {
    const clonedElement = element.cloneNode(true);
    element.parentNode.replaceChild(clonedElement, element);

    return clonedElement;
}
