const influxPort = 8086;
const prometheusPort = 9090;
const minFrameLength = 30000;

function getData() {
    //make connection to influxDB and retrieve data
    document.getElementById('dataBtn').disabled = true;
    var innerHTML = window.location.href.split('/');
    innerHTML.pop();
    innerHTML = innerHTML.join('/');

    var serviceArray = new Array();
    var table = document.getElementById('output_table');
    var workflow = JSON.parse(table.getAttribute("workflow"));

    for (var i = 1; i < table.rows.length; i++) {
        var row = table.rows.item(i);

        if (row.cells[0].childNodes[0].checked) {
            var service = JSON.parse(row.getAttribute("data-rest"));
            serviceArray.push(service);
        }
    }

    serviceArray.sort((a, b) => (a.startTime > b.endTime) ? 1 : (a.startTime > b.endTime) ? -1 : 0);

    var resultObj = new Object();
    resultObj.services = serviceArray;
    resultObj.workflow = workflow;

    connectDB(resultObj);
}

function connectDB(resultObj) {
    var resultData = new Object();

    // some function for retrieving the current selected service

    resultObj.services.forEach(element => {
        resultData[element.name] = new Object();
        resultData[element.name].startTime = element.startTime;
        resultData[element.name].endTime = element.endTime;
        retrieveData(element.endpoint, resultObj.workflow, resultData[element.name]);
    });
}


function plotData(serviceObject) {
    document.getElementById('plot_div').style.display = "block";
    console.log(serviceObject.cpu);
    g = new Dygraph(document.getElementById("cpu_out"), serviceObject.cpu, { labels: ["x", "A"] });
    g = new Dygraph(document.getElementById("mem_out"), serviceObject.cpu, { labels: ["x", "A"] });
    g = new Dygraph(document.getElementById("net_out"), serviceObject.cpu, { labels: ["x", "A"] });
    g = new Dygraph(document.getElementById("fle_out"), serviceObject.cpu, { labels: ["x", "A"] });
}

function retrieveData(endpoint, workflow, serviceObject) {
    serviceObject.data = new Object();
    var urlBase = endpoint.replace(/:[0-9]+(?:\/.*)?/, ':' + prometheusPort) + "/api/v1/query_range?";

    var difference = workflow.endTime - workflow.startTime;
    if (difference * 3 < minFrameLength) {
        difference += (minFrameLength - difference * 3) / 2
        difference = Math.round(difference + 0.5);
    }

    var startSec = Math.round((workflow.startTime - difference) / 1000);
    var endSec = Math.round((workflow.endTime + difference) / 1000);
    retrieveMetrics(urlBase, startSec, endSec, serviceObject);
}

function retrieveMetrics(urlBase, start, end, serviceObject) {
    var queryURL = urlBase + 'query=sum(rate(container_cpu_usage_seconds_total%7Bname%3D~%22.%2B%22%7D%5B30m%5D))%20by%20(name)%20*%20100';
    queryURL += '&start=' + start;
    queryURL += '&end=' + end;
    queryURL += '&step=' + 1;
    queryURL += '&timeout=' + '5s';

    console.log(queryURL);
    $.get(queryURL, (data, status) => {
        data.data.result.forEach(element => {
            var expr = /service/;
            if (expr.test(element.metric.name)) {
                console.log("values: " + element.values);
                serviceObject.cpu = element.values.map(([x,y]) => [new Date(x*1000), parseFloat(y)]);
                plotData(serviceObject);
            }
        });
    });
}