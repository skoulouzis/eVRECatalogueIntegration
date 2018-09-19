function move() {
    var elem = document.getElementById("myBar");
    var startButt = document.getElementById("startBtn");
    startButt.disabled = true;
    var width = 10;
    var id = setInterval(frame, 10);
    var catalogueURL = document.getElementById("cat_url").value;
    var url = 'http://localhost:8080/rest/list_records/?catalogue_url=' + catalogueURL + '&limit=200';


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
    var generator_url = 'https://raw.githubusercontent.com/skoulouzis/CatMap/master/etc/CERIF-generator-policy-v5___21-08-2018124405___12069.xml'
    const convertURL = 'http://localhost:8080/rest/convert?catalogue_url=' + catalogueURL + '&mapping_url=' + mappingURL + '&generator_url=' + generator_url + '&limit=200';
    var request = new XMLHttpRequest();
    request.open('GET', convertURL, false);  // `false` makes the request synchronous
    request.send(null);
    console.log(request.responseText);

    var resultsURL = 'http://localhost:8080/rest/list_results/?mapping_name=' + mappingName + '&limit=200';
    var request = new XMLHttpRequest();
    request.open('GET', resultsURL, false);  // `false` makes the request synchronous
    request.send(null);
    var numOfRes = 0;
    if (request.status === 200) {
        json = JSON.parse(request.responseText);
        numOfRes = json.length;
    }
    console.log(numOfRes);

    var count = 0;
    function frame() {
        var startButt = document.getElementById("startBtn");
        startButt.disabled = true;

        if (width >= 100) {
            clearInterval(id);
            document.getElementById("startBtn").disabled = false;
            done = false;
            currProgress = 0;
            alert("The results are stored in " + window.location.protocol + '//' + window.location.hostname + '/' + mappingName);
        } else {

            if ((count % 50) === 0 || count <= 0) {
                var resultsURL = 'http://localhost:8080/rest/list_results/?mapping_name=' + mappingName;
                var request = new XMLHttpRequest();
                request.open('GET', resultsURL, false);  // `false` makes the request synchronous
                request.send(null);
                if (request.status === 200) {
                    json = JSON.parse(request.responseText);
                    numOfRes = json.length;
                }
//                console.log(count)
            }
            width = Math.round((((numOfRes - 1) / 3) / numOfRec) * 100);
            elem.style.width = width + '%';
            elem.innerHTML = width * 1 + '%';
            count++;
        }
    }
}