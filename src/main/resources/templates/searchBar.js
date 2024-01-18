import { searchBooksRequest } from './request.js';
import { createElement } from './createElement.js';

let suggestions = [];
let currentFocus = -1;

$(document).ready(function () {
    $("#mysearch").keyup(async function () {
        const keyword = $("#mysearch").val();



            if (suggestions.length <= currentFocus) {
                currentFocus = 0;
            }
            suggestions = await searchBooksRequest(keyword, autocompleteSearchSize);

            closeAllLists();

            if (!keyword) return false;

            const autoCompleteList = createElement("ul", ["autocomplete-items"], "autocomplete-list");

            this.parentNode.appendChild(autoCompleteList);
            addAutoCompleteElements(autoCompleteList, this);

            updateAutoCompleteList();
        } catch (err) {
            console.log('Error:', err);
        }
    });
});

function updateAutoCompleteList() {
    const inp = document.getElementById("mysearch");

    inp.addEventListener("keydown", function (e) {
        const autoCompleteList = document.getElementById("mysearch-autocomplete-list");

        if (autoCompleteList) {
            const items = autoCompleteList.getElementsByTagName("div");

            if (["ArrowDown", "ArrowUp"].includes(e.key)) {
                e.preventDefault();
                currentFocus += (e.key === "ArrowDown") ? 1 : -1;
                changeFocus(items);
            } else if (e.key === "Enter") {
                e.preventDefault();
                if (currentFocus > -1) {
                    items[currentFocus].click();
                }
            }
        }
    }
}

function addAutoCompleteElements(autoCompleteList, input) {
    suggestions.forEach(suggestion => {
        const item = createElement("li", ["autocomplete-item"]);

        const titleDiv = createElement("div", ["title"]);
        titleDiv.innerHTML = suggestion.highlightTitle;
        const authorDiv = createElement("div", ["author"]);
        authorDiv.innerHTML = suggestion.author;

        item.appendChild(titleDiv);
        item.appendChild(authorDiv);

        const hiddenInput = createElement("input", null, null, "hidden");
        hiddenInput.value = suggestion.title;
        item.appendChild(hiddenInput);

        item.addEventListener("click", function () {
            input.value = this.querySelector("input").value;
            closeAllLists();
        });

        autoCompleteList.appendChild(item);
    });
}

function changeFocus(items) {
    removeFocus(items);

    if (currentFocus >= items.length) currentFocus = 0;
    if (currentFocus < 0) currentFocus = (items.length - 1);

    items[currentFocus].classList.add("autocomplete-item-active");
}

function removeFocus(items) {
    Array.from(items).forEach(item => item.classList.remove("autocomplete-item-active"));
}

function closeAllLists(el) {
    const input = document.getElementById("inputSearchText");
    const autoCompleteLists = document.getElementsByClassName("autocomplete-items");

    Array.from(autoCompleteLists).forEach(list => {
        if (el !== list && el !== input) {
            list.parentNode.removeChild(list);
        }
    });
}

document.addEventListener("click", function (e) {
    closeAllLists(e.target);
});
