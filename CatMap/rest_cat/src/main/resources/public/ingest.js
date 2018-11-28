var folderName;
function ingest() {


// Get the modal
    var modal = document.getElementById('myModal');
    modal.style.display = "block";

// Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];




    span.onclick = function () {
        modal.style.display = "none";
    };

    var loginBtn = document.getElementById("loginBtn");

    loginBtn.onclick = function () {
        modal.style.display = "none";
        var ingestBtn = document.getElementById("ingestBtn");
        ingestBtn.disabled = false;
        document.getElementById("loader").style.display = "block";
        var token = getToken();

        var source_rec_url = document.getElementById("source_rec_url").value;
        var ingest_cat_url = document.getElementById("ingest_cat_url").value;
        var uname = document.getElementById("uname").value;
        var requestParams = '{"sourceRecURL":"' + source_rec_url + '","ingestCatalogueURL":"' + ingest_cat_url + '","token":"' + token + '","username":"' + uname + '","namedGraphLabelParam":"ingest' + Date.now() + '"}';

        var innerHTML = window.location.href.split('/');
        innerHTML.pop();
        innerHTML = innerHTML.join('/');
        var ingestRecordsURL = innerHTML + '/ingest_records/';

        var ingestRecordsRequest = new XMLHttpRequest();
        ingestRecordsRequest.open('POST', ingestRecordsURL, false);
        ingestRecordsRequest.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        ingestRecordsRequest.send(requestParams);

        if (ingestRecordsRequest.status === 200) {
            var json = JSON.parse(ingestRecordsRequest.responseText);
            console.log(json)
        }
        console.log(ingestRecordsRequest.responseText);
//        document.cookie = '';
        document.getElementById("loader").style.display = "none";

        
//        window.open(ingest_cat_url + '#!/navigation', '_blank');
    };

}


function enableBtn() {
    var source_rec_url = document.getElementById("source_rec_url").value;
    var ingest_cat_url = document.getElementById("ingest_cat_url").value;
    if (source_rec_url !== null && source_rec_url.length > 0 && ingest_cat_url !== null && ingest_cat_url.length > 0) {
        var ingestBtn = document.getElementById("ingestBtn");
        ingestBtn.disabled = false;
    }
}



function getToken() {

    var psw = document.getElementById("psw").value;
    var uname = document.getElementById("uname").value;
    var token = 'aaaaaaaaaaaaaaa';
    var nodeService = 'http://v4e-lab.isti.cnr.it:8080/NodeService/user/login?pwd=' + psw + '&username=' + uname;
    var request = new XMLHttpRequest();
    request.open('GET', nodeService, false);
    request.send(null);
    if (request.status === 200) {
        var json = JSON.parse(request.responseText);
        if (json.status === 'SUCCEED') {
            token = json.token;
        }
    }
    return token;
}
