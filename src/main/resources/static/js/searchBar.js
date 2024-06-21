let suggestions = [];
let currentFocus = -1;
const tempListSearchSize = 24;

async function searchBarKeyUpHandler(e) {
    const inputSearchText = document.getElementById('inputSearchText');
    const keyword = inputSearchText.value;

    if (!["ArrowUp", "ArrowDown", "ArrowLeft", "ArrowRight", "Enter"].includes(e.key)) {

        const data = await requestAutocompleteSuggestions(keyword);
        suggestions = data;

        currentFocus = -1;

        closeAllLists();

        if (!keyword) return false;

        const autoCompleteList = createElement("ul", ["autocomplete-items"], "autocomplete-list");

        inputSearchText.parentNode.appendChild(autoCompleteList);
        addAutoCompleteElements(autoCompleteList, this);
    } else if (["Enter"].includes(e.key)) {
        closeAllLists();
        requestBookSearch({
            keyword: keyword,
            page: 1,
            size: tempListSearchSize
        });
    } else if (["ArrowUp", "ArrowDown"].includes(e.key)) {
        updateFocus(e);
        const focusedItem = document.getElementsByClassName('autocomplete-item-active')[0];
        if (focusedItem) {
            const focusedItemTitleElement = focusedItem.querySelector("div.title");

            if (focusedItemTitleElement) {
                inputSearchText.value = focusedItemTitleElement.textContent;
            }
        }
    }
}

function clearSearchBar() {
    document.getElementById('inputSearchText').value = '';
}

function updateFocus(e) {
    const autoCompleteList = document.getElementById("autocomplete-list");

    if (autoCompleteList) {
        const autoCompleteList = document.getElementById("autocomplete-list");
        const autocompleteItems = autoCompleteList.querySelectorAll("li.autocomplete-item");

        if (["ArrowDown", "ArrowUp"].includes(e.key)) {
            e.preventDefault();
            currentFocus += (e.key === "ArrowDown") ? 1 : -1;
            changeFocus(autocompleteItems);
        } else if (e.key === "Enter") {
            e.preventDefault();
            if (currentFocus > -1) {
                autocompleteItems[currentFocus].click();
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

        item.addEventListener("click", async function () {
            const searchKeyword = this.querySelector("input").value;
            input.value = searchKeyword;
            requestBookSearch({
                keyword: searchKeyword,
                page: 1,
                size: tempListSearchSize
            });
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

function closeAllLists() {
    const input = document.getElementById("inputSearchText");
    const autoCompleteLists = document.getElementsByClassName("autocomplete-items");

    const listsArray = Array.from(autoCompleteLists);

    listsArray.forEach(list => {
        while (list.firstChild) {
            list.removeChild(list.firstChild);
        }

        list.parentNode.removeChild(list);
    });
}

document.addEventListener("click", function () {
    closeAllLists();
});
