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
    var workflow = JSON.parse(table.getAttribute("workflow"));

    for(var i = 1; i < table.rows.length; i++){
        var row = table.rows.item(i);

        if(row.cells[0].childNodes[0].checked){
            var service = JSON.parse(row.getAttribute("data-rest"));
            serviceArray.push(service);
        }
    }
    serviceArray.sort((a,b) => (a.startTime > b.endTime) ? 1 : (a.startTime > b.endTime) ? -1 : 0);

    var resultObj = new Object();
    resultObj.services = serviceArray;
    resultObj.workflow = workflow;

    connectDB(resultObj);
}

function connectDB(resultObj){
    var resultData = new Object();

    resultObj.services.forEach(element => {
        resultData[element.name] = new Object();
        resultData[element.name].data = retrieveData(element.endpoint, resultObj.workflow);
        resultData[element.name].startTime = element.startTime;
        resultData[element.name].endTime = element.endTime;
    });

    console.log(JSON.stringify(resultData));
}


function retrieveData(endpoint, workflow){
    var urlBase = endpoint.replace(/:[0-9]+(?:\/.*)?/, ':' + prometheusPort) + "/api/v1/query_range?";

    var difference = workflow.endTime - workflow.startTime;
    if(difference * 3 < minFrameLength){
        difference += (minFrameLength - difference*3)/2
        difference = Math.round(difference + 0.5);
    }

    var startSec = Math.round((workflow.startTime - difference)/1000);
    var endSec =  Math.round((workflow.endTime + difference)/1000);

    var CPUdata = retrieveCPU(urlBase, startSec, endSec);

    return "datadatadata";
}

function retrieveCPU(urlBase, start, end){
    console.log(start + "-" + end);
    var queryURL = urlBase + 'query=sum(rate(container_cpu_usage_seconds_total%7Bname%3D~%22.%2B%22%7D%5B30m%5D))%20by%20(name)%20*%20100';
    queryURL += '&start=' + start;
    queryURL += '&end=' + end;
    queryURL += '&step=' + 1;
    queryURL += '&timeout=' + '5s';

    console.log(queryURL);

    // var http = new XMLHttpRequest();
    // http.open("GET", queryURL);
    // http.send();
    
    // http.onreadystatechange = (e) => {
    //     console.log(http.responseText);
    //     return JSON.parse(http.responseText);
    // }
}