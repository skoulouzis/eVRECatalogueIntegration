function ingest() {
    var source_rec_url = document.getElementById("source_rec_url").value;
    var ingest_cat_url = document.getElementById("ingest_cat_url").value;
    if (source_rec_url !== null && source_rec_url.length > 0 && ingest_cat_url !== null && ingest_cat_url.length > 0) {
        var ingestBtn = document.getElementById("ingestBtn");
        ingestBtn.disabled = false;
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