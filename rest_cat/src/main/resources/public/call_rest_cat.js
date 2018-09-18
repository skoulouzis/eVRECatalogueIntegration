function getResponse() {
    const Http = new XMLHttpRequest();
    const url = 'http://localhot:8083/rest';
    Http.open("GET", url);
    Http.send();
    Http.onreadystatechange = (e) => {
        console.log(Http.responseText)
    }
}