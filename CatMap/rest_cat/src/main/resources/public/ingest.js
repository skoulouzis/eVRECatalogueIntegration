var folderName;
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


    var folderName = new URL(source_rec_url).pathname;
    var resultsURL = innerHTML + '/list_results/?folder_name=' + folderName;
//    console.log(resultsURL);
    var listResultsRequest = new XMLHttpRequest();
    listResultsRequest.open('GET', resultsURL, false);  // `false` makes the request synchronous
    listResultsRequest.send(null);
    var numOfRec = 0;
    if (listResultsRequest.status === 200) {
        json = JSON.parse(listResultsRequest.responseText);
        numOfRec = json.length;
    }


    var datasetName = "envri";
    var ingestRecordsURL = innerHTML + '/ingest_records/?source_rec_url=' + source_rec_url + '&ingest_cat_url=' + ingest_cat_url + '&dataset_name=' + datasetName;
    console.log(ingestRecordsURL);
    var ingestRecordsRequest = new XMLHttpRequest();
    ingestRecordsRequest.open('GET', ingestRecordsURL, false);
    ingestRecordsRequest.send(null);


    var countRDFRecordsURL = innerHTML + '/count_rdf_records/?catalogue_url=' + ingest_cat_url;
    console.log(countRDFRecordsURL);
    var countRDFRecordsRequest = new XMLHttpRequest();
    countRDFRecordsRequest.open('GET', countRDFRecordsURL, false);  // `false` makes the request synchronous
    countRDFRecordsRequest.send(null);

    var numOfRes = 0;
    if (countRDFRecordsRequest.status === 200) {
        json = JSON.parse(countRDFRecordsRequest.responseText);
        numOfRes = parseInt(json);
        console.log(numOfRes);
    }


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
            width = Math.round((((numOfRes - 1) / 2) / numOfRec) * 100);
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
