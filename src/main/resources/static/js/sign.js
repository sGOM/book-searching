// 정규 표현식 정의
const usernameRegex = /^[a-zA-Z0-9]{6,12}$/;
const passwordRegex = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\S+$).{8,16}$/;
const nicknameRegex = /^.{3,20}$/;

document.addEventListener('DOMContentLoaded', function() {
    // submit 버튼 클릭 이벤트 핸들러
    let signInForm = document.getElementById('sign-in-form')
    if (signInForm) {
        signInForm.addEventListener('submit', function(event) {
            // 입력 필드 값 검사
            const username = document.querySelector('input[name="username"]').value;
            const password = document.querySelector('input[name="password"]').value;

            if (testRegex(usernameRegex, username, '아이디는 6~12자의 알파벳과 숫자로만 입력해야 합니다.') &&
                testRegex(passwordRegex, password, '비밀번호는 8~16자의 알파벳, 숫자, 특수문자로만 입력해야 합니다.')){

                const requestData = {
                    username: username,
                    password: password
                };

                requestSignIn(requestData);
            }
        });
    }

    let signUpForm = document.getElementById('sign-up-form')
    if (signUpForm) {
        // submit 버튼 클릭 이벤트 핸들러
        signUpForm.addEventListener('submit', function(event) {
            // 입력 필드 값 검사
            const username = document.querySelector('input[name="username"]').value;
            const password = document.querySelector('input[name="password"]').value;
            const nickname = document.querySelector('input[name="nickname"]').value;

            if (testRegex(usernameRegex, username, '아이디는 6~12자의 알파벳과 숫자로만 입력해야 합니다.') &&
                testRegex(passwordRegex, password, '비밀번호는 8~16자의 알파벳, 숫자, 특수문자로만 입력해야 합니다.') &&
                testRegex(nicknameRegex, nickname, '이름은 3~20자 이하여야 합니다.')){

                const requestData = {
                    username: username,
                    password: password,
                    nickname: nickname,
                    role: 'ROLE_USER'
                };

                requestSignUp(requestData);
            }
        });
    }
});

function testRegex(regex, value, errorMessage) {
    if (!regex.test(value)) {
        alert(errorMessage);
        event.preventDefault();
        return false;
    }
    return true;
}
