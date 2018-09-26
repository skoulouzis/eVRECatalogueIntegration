function ingest() {
    var source_rec_url = document.getElementById("source_rec_url").value;
    var ingest_cat_url = document.getElementById("ingest_cat_url").value;


    var innerHTML = window.location.href.split('/');
    innerHTML.pop();
    innerHTML = innerHTML.join('/');

    var elem = document.getElementById("ingestBar");
    var ingestBtn = document.getElementById("ingestBtn");
    var width = 10;
    var id = setInterval(frame, 10);
    var catalogueURL = document.getElementById("cat_url").value;

    var url = innerHTML + '/list_records/?catalogue_url=' + catalogueURL + '&limit=80';

    var request = new XMLHttpRequest();
    request.open('GET', url, false);  // `false` makes the request synchronous
    request.send(null);
    var numOfRec = 0;
    if (request.status === 200) {
        json = JSON.parse(request.responseText);
        numOfRec = json.length;
    }

    var mappingURL = 'https://raw.githubusercontent.com/skoulouzis/CatMap/master/etc/Mapping115.x3ml'
    var mappingName = mappingURL.substring(mappingURL.lastIndexOf("/") + 1, mappingURL.lastIndexOf("."));


    var numOfRes = 0;

    ingestBtn.disabled = false;
    var count = 0;
    function frame() {
        var ingestBtn = document.getElementById("ingestBtn");
        ingestBtn.disabled = true;

        if (width >= 100) {
            clearInterval(id);
            document.getElementById("ingestBtn").disabled = false;
            done = false;
            currProgress = 0;
            var win = window.open(ingest_cat_url, '_blank');
            win.focus();
        } else {
            numOfRes++;
            width = Math.round((((numOfRes - 1) / 3) / numOfRec) * 100);
            elem.style.width = width + '%';
            elem.innerHTML = width * 1 + '%';
            count++;
        }
    }
}


function enableBtn() {
    var source_rec_url = document.getElementById("source_rec_url").value;
    var ingest_cat_url = document.getElementById("ingest_cat_url").value;
    if (source_rec_url !== null && source_rec_url.length > 0 && ingest_cat_url !== null && ingest_cat_url.length > 0) {
        var ingestBtn = document.getElementById("ingestBtn");
        ingestBtn.disabled = false;
    }
}
