var currProgress = 0;
//is the task complete
var done = false;
//total progress amount
var total = 100;

//function to update progress
function startProgress() {
//get the progress element
    var prBar = document.getElementById("progressBar");
//get the start button
    var startButt = document.getElementById("startBtn");
//get the textual element
    var val = document.getElementById("numValue");
//disable the button while the task is unfolding
    startButt.disabled = true;
//update the progress level
    prBar.value = currProgress;
//update the textual indicator
    val.innerHTML = Math.round((currProgress / total) * 100) + "%";
//increment the progress level each time this function executes
    currProgress++;
//check whether we are done yet
    if (currProgress > 100)
        done = true;
//if not done, call this function again after a timeout
    if (!done)
        setTimeout("startProgress()", 100);
//task done, enable the button and reset variables
    else
    {
        document.getElementById("startBtn").disabled = false;
        done = false;
        currProgress = 0;
    }
}


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
        } else {

            if ((count % 15) === 0 || count <= 0) {
                var resultsURL = 'http://localhost:8080/rest/list_results/?mapping_name=' + mappingName;
                var request = new XMLHttpRequest();
                request.open('GET', resultsURL, false);  // `false` makes the request synchronous
                request.send(null);
                if (request.status === 200) {
                    json = JSON.parse(request.responseText);
                    numOfRes = json.length;
                }
            }
            console.log(numOfRes)
            width = Math.round((((numOfRes - 1) / 3) / numOfRec) * 100);
            elem.style.width = width + '%';
            elem.innerHTML = width * 1 + '%';
            count++;
        }
    }
}