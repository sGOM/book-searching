let arr = [];
let currentFocus = -1;

$(document).ready(function () {
    /* 검색 창에서 키보드 입력이 있을 때마다, 자동 완성 배열 최신화 */
    $("#mysearch").keyup(async function () {
        let keyword = $("#mysearch").val();

        try {
            let res = await $.ajax({
                url: 'http://localhost:8080/books/search',
                type: 'GET',
                data: {keyword: keyword},
            });

            arr = res;

            if (arr.length <= currentFocus) {
                currentFocus = 0;
            }
            let inp = document.getElementById("mysearch");
            /*execute a function when someone writes in the text field:*/

            /* 기존에 존재하는 자동 완성 리스트 제거 */
            closeAllLists();
            /* 입력 값이 없다면 함수 종료 */
            if (!inp.value) return false;

            /* 새로 생성한 자동 완성 리스트 (DIV) */
            let autoCompleteList = createAutoCompleteList(this.id);
            /* 새로 생성한 자동 완성 리스트를 화면에 추가 */
            this.parentNode.appendChild(autoCompleteList);
            /* 자동 완성 리스트의 요소들을 추가 */
            addAutoCompleteElement(autoCompleteList, inp);

            /* 이벤트 리스너 + 자동완성 리스트 생성 로직 */
            updateAutoCompleteList();
        } catch (err) {
            console.log('Error:', err);
        }
    });
});

function updateAutoCompleteList() {
    let inp = document.getElementById("mysearch");
    /*execute a function when someone writes in the text field:*/

    /* key down (Up, Down Arrow나 Enter) 이벤트 발생 시 */
    inp.addEventListener("keydown", function (e) {
        /* 자동 완성 리스트 */
        let autoCompleteList = document.getElementById(this.id + "autocomplete-list");
        /* 자동 완성 리스트 내부에 모든 DIV를 가져옴 */
        if (autoCompleteList) {
            autoCompleteList = autoCompleteList.getElementsByTagName("div");
            /* 입력 키보드에 따라 다른 동작으로 Focus를 변경 */
            if (e.key === "ArrowDown") {
                currentFocus++;
                changeFocus(autoCompleteList);
            } else if (e.key === "ArrowUp") {
                currentFocus--;
                changeFocus(autoCompleteList);
            } else if (e.key === "Enter") {
                e.preventDefault();
                if (currentFocus > -1) {
                    autoCompleteList[currentFocus].click();
                }
            }
        }

    });
}

/* 자동 완성 리스트 생성 (element는 addAutoCompleteElement에서 생성) */
function createAutoCompleteList(id) {
    let autoCompleteList = document.createElement("DIV");
    autoCompleteList.setAttribute("id", id + "autocomplete-list");
    autoCompleteList.setAttribute("class", "autocomplete-items");
    return autoCompleteList;
}

/* 자동 완서 리스트에 element 추가 */
function addAutoCompleteElement(autoCompleteList, inp) {
    let inputVal = inp.value
    for (let i = 0; i < arr.length; i++) {
        /* element에 대응되는 DIV 생성 */
        let b = document.createElement("DIV");
        // TODO : 해당 부분 로직 변경 필요
        /* 입력 값의 길이 만큼 각 element의 글자 강조 */
        b.innerHTML = "<strong>" + arr[i].substr(0, inputVal.length) + "</strong>";
        b.innerHTML += arr[i].substr(inputVal.length);
        /* 해당 element를 선택시에 input 태그에 선택한 값이 입력되도록 해당 태그를 추가 */
        b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
        /* 클릭 시 */
        b.addEventListener("click", function (e) {
            /* 검색 창을 해당 값으로 변경 */
            inp.value = this.getElementsByTagName("input")[0].value;
            /* 자동 완성 리스트 삭제 */
            closeAllLists();
        });
        /* 자동 완성 리스트에 추가 */
        autoCompleteList.appendChild(b);
    }
}

/* Focus를 변경 */
function changeFocus(autoCompleteList) {
    /* 자동 완성 리스트가 없으면 종료 */
    if (!autoCompleteList) return false;
    /* 기존 Focus 삭제 */
    removeFocus(autoCompleteList);
    if (currentFocus >= autoCompleteList.length) currentFocus = 0;
    if (currentFocus < 0) currentFocus = (autoCompleteList.length - 1);
    /* currentFocus에 해당하는 element에 class(css) 추가 */
    autoCompleteList[currentFocus].classList.add("autocomplete-active");
}

/* Focus 삭제 */
function removeFocus(autoCompleteList) {
    /* 모든 리스트를 순회하면 Focus 삭제 */
    for (let i = 0; i < autoCompleteList.length; i++) {
        autoCompleteList[i].classList.remove("autocomplete-active");
    }
}

/* el가 mysearch이 아닌 경우, 열려 있는 모든 리스트 삭제 */
function closeAllLists(el) {
    let inp = document.getElementById("mysearch");
    /*close all autocomplete lists in the document,
    except the one passed as an argument:*/
    let autoCompleteList = document.getElementsByClassName("autocomplete-items");
    for (let i = 0; i < autoCompleteList.length; i++) {
        if (el !== autoCompleteList[i] && el !== inp) {
            autoCompleteList[i].parentNode.removeChild(autoCompleteList[i]);
        }
    }
}
/* Click 이벤트가 mysearch 내부에서 발생하지 않은 경우, 열려 있는 모든 리스트 삭제 */
document.addEventListener("click", function (e) {
    closeAllLists(e.target);
});