const influxPort = 8086;
const prometheusPort = 9090;
const minFrameLength = 30000;

function getData(){
    //make connection to influxDB and retrieve data
    document.getElementById('dataBtn').disabled = true;
    var innerHTML = window.location.href.split('/');
    innerHTML.pop();
    innerHTML = innerHTML.join('/');

    var serviceArray = new Array();
    var table = document.getElementById('output_table');
    for(var i = 1; i < table.rows.length; i++){
        var row = table.rows.item(i);

        if(row.cells[0].childNodes[0].checked){
            var service = JSON.parse(row.getAttribute("data-rest"));
            serviceArray.push(service);
        }
    }

    connectDB(serviceArray);
}

function connectDB(serviceArray){
    var resultData = new Object();

    serviceArray.forEach(element => {
        resultData[element.name] = retrieveData(element);
    });
}


function retrieveData(restService){
    var urlBase = restService.endpoint.replace(/:[0-9]+\/.*/, ':' + prometheusPort) + "/api/v1/query_range?";

    var difference = restService.endTime - restService.startTime;
    if(difference * 3 < minFrameLength){
        difference += (minFrameLength - difference*3)/2
        difference = Math.round(difference + 0.5);
    }

    var startSec = Math.round((restService.startTime - difference)/1000);
    var endSec =  Math.round((restService.endTime + difference)/1000);

    var CPUdata = retrieveCPU(urlBase, startSec, endSec);
}

function retrieveCPU(urlBase, start, end){
    var queryURL = urlBase + 'query=sum(rate(container_cpu_usage_seconds_total%7Bname%3D~%22.%2B%22%7D%5B50s%5D))%20by%20(name)%20*%20100';
    queryURL += '&start=' + start;
    queryURL += '&end=' + start;
    queryURL += '&step=' + 1;
    queryURL += '&timeout=' + '5s';

    var http = new XMLHttpRequest();
    http.open("GET", queryURL);
    http.send();
    
    http.onreadystatechange = (e) => {
        console.log(http.responseText);
        return JSON.parse(http.responseText);
    }
}