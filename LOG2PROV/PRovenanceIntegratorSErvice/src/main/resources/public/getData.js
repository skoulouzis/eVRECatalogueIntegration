const prometheusPort = 9090;
const minFrameLength = 30000;
const db_connector_endpoint = 'http://192.168.99.100:8080/metrics';

function getData() {
    document.getElementById('dataBtn').disabled = true;
    var innerHTML = window.location.href.split('/');
    innerHTML.pop();
    innerHTML = innerHTML.join('/');

    var serviceArray = new Array();
    var table = document.getElementById('output_table');
    var workflow = JSON.parse(table.getAttribute("workflow"));

    // TODO: change to forEach
    // serviceArray.sort((a, b) => (a.startTime > b.endTime) ? 1 : (a.startTime > b.endTime) ? -1 : 0);
    var name;
    for (var i = 1; i < table.rows.length; i++) {
        var row = table.rows.item(i);

        if (!row.cells[0].childNodes[0].checked) {
            name = row.getAttribute("data-rest");
            wfObject.services = wfObject.services.filter(x => x.name !== name);
        }
    }

    var promiseArray = connectDB(wfObject);
    Promise.all(promiseArray)
    .then(values => {
        var resultData = new Object();
        values.forEach((value, i) => {
            resultData[promiseArray[i].resourceID] = value.data;
        });

        visualizeData(formatData(resultData));
        console.log(JSON.stringify(wfObject));
    })
    .catch(err => {
        console.log(err.stack);
    });
}

function connectDB(wfObject) {
    var promiseArray = new Array();
    var set = new Set();

    wfObject.services.forEach(element => {
        if(!set.has(element.resource)){
            set.add(element.resource);
            
            var prm = retrieveData(element.endpoint, wfObject.workflow);
            prm.resourceID = element.resource;
            promiseArray.push(prm)
        }
    });

    return promiseArray;
}

function formatData (resultData){
    var plotData = new Object();
    plotData.resources = [];

    for (var key in resultData) {
        if (resultData.hasOwnProperty(key)) {
            plotData.resources.push(key);
            if(!plotData.cpu){
                plotData.cpu = resultData[key].cpu;
                plotData.mem = resultData[key].mem;
                plotData.net_in = resultData[key].net_in;
                plotData.net_out = resultData[key].net_out;
            }else{
                for(var i = 0; i < plotData.cpu.length; i++){
                    plotData.cpu[i].push(resultData[key].cpu[i][1]);
                    plotData.mem[i].push(resultData[key].mem[i][1]);
                    plotData.net_in[i].push(resultData[key].net_in[i][1]);
                    plotData.net_out[i].push(resultData[key].net_out[i][1]);
                }
            }
            
        }
    }

    function matchTypes(x, div){
        x[0] = new Date(x[0] * 1000);
    
        for(var i = 1; i < x.length; i++){
            x[i] = parseFloat(x[i]) / div;
        }
        
        return x;
    }

    if(plotData.cpu){
        plotData.cpu = plotData.cpu.map(x => matchTypes(x,1));
        plotData.mem = plotData.mem.map(x => matchTypes(x,1000000));
        plotData.net_in = plotData.net_in.map(x => matchTypes(x,1));
        plotData.net_out = plotData.net_out.map(x => matchTypes(x,1));
    }

    return plotData;
}

function retrieveData(endpoint, workflow) {
    var endpointURL = endpoint.replace(/:[0-9]+(?:\/.*)?/, ':' + prometheusPort);

    var difference = workflow.endTime - workflow.startTime;
    if (difference * 3 < minFrameLength) {
        difference += (minFrameLength - difference * 3) / 2
        difference = Math.round(difference + 0.5);
    }
    var startSec = Math.round((workflow.startTime - difference) / 1000);
    var endSec = Math.round((workflow.endTime + difference) / 1000);

    var url = new URL(db_connector_endpoint);
    url.searchParams.append('endpoint', endpointURL);
    url.searchParams.append('startTime', startSec);
    url.searchParams.append('endTime', endSec);

    return axios.get(url);
}