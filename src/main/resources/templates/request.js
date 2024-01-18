export async function searchBooksRequest(keyword, size) {
    try {
        const res = await $.ajax({
            url: 'http://localhost:8080/books/search',
            type: 'GET',
            data: {
                keyword: keyword,
                size: size
            },
        });

        return res;
    } catch (err) {
        console.log('Error:', err);
    }
}
