function move() {
    var innerHTML = window.location.href.split('/');
    innerHTML.pop();
    innerHTML = innerHTML.join('/');

    var elem = document.getElementById("myBar");
    var startButt = document.getElementById("startBtn");
    startButt.disabled = true;
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



    var generator_url = 'https://raw.githubusercontent.com/skoulouzis/CatMap/master/etc/CERIF-generator-policy-v5___21-08-2018124405___12069.xml'
    const convertURL = innerHTML + '/convert?catalogue_url=' + catalogueURL + '&mapping_url=' + mappingURL + '&generator_url=' + generator_url + '&limit=80';
    var request = new XMLHttpRequest();
    request.open('GET', convertURL, false);  // `false` makes the request synchronous
    request.send(null);


    var resultsURL = innerHTML + '/list_results/?mapping_name=' + mappingName + '&limit=80';
    var request = new XMLHttpRequest();
    request.open('GET', resultsURL, false);  // `false` makes the request synchronous
    request.send(null);



    var numOfRes = 0;
    if (request.status === 200) {
        json = JSON.parse(request.responseText);
        numOfRes = json.length;
    }


    var count = 0;
    function frame() {
        var startButt = document.getElementById("startBtn");
        startButt.disabled = true;

        var downloadBtn = document.getElementById("downloadBtn");
        downloadBtn.disabled = true;


        if (width >= 100) {
            clearInterval(id);
            document.getElementById("startBtn").disabled = false;
            done = false;
            currProgress = 0;
//            alert("The results are stored in " + window.location.protocol + '//' + window.location.hostname + '/' + mappingName);
            var rec_loc = document.getElementById("rec_loc");
            if (rec_loc !== null) {
                rec_loc.href = window.location.protocol + '//' + window.location.hostname + '/' + mappingName;
                rec_loc.style.display = "inline";
            }



//            document.getElementById("source_rec_url").value = window.location.protocol + '//' + window.location.hostname + '/' + mappingName;
            downloadBtn.disabled = false;

        } else {

            if ((count % 10) === 0 || count <= 0) {
                var resultsURL = innerHTML + '/list_results/?mapping_name=' + mappingName;
                var request = new XMLHttpRequest();
                request.open('GET', resultsURL, false);  // `false` makes the request synchronous
                request.send(null);
                if (request.status === 200) {
                    json = JSON.parse(request.responseText);
                    numOfRes = json.length;
                }
                console.log(count);
            }
            width = Math.round((((numOfRes - 1) / 3) / numOfRec) * 100);
            elem.style.width = width + '%';
            elem.innerHTML = width * 1 + '%';
            count++;
        }
    }
}

function download() {
    var mappingURL = 'https://raw.githubusercontent.com/skoulouzis/CatMap/master/etc/Mapping115.x3ml'
    var mappingName = mappingURL.substring(mappingURL.lastIndexOf("/") + 1, mappingURL.lastIndexOf("."));
    var webdav = window.location.protocol + '//' + window.location.hostname + '/' + mappingName;

    var win = window.open(webdav, '_blank');
    win.focus();
}