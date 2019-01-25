const dbPort = 8086;



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

    //connectDB(serviceArray);
}

function connectDB(serviceArray){
    var resultData = new Object();

    serviceArray.forEach(element => {
        resultData[element.name] = retrieveData(element);
    });
}


function retrieveData(restService){
    var dbURL = new URL(restService.endpoint.split(":")[0] + ":" + dbPort);
    dbURL.searchParams.append('db', 'mydb');

    dbURL.searchParams.append('q', memoryQuery(restService.startTime, restService.endTime));
    // dbURL.searchParams.set('q', cpuQuery(start, end));
    // dbURL.searchParams.set('q', fileQuery(start, end));
    // dbURL.searchParams.set('q', networkQuery(start, end));

    dbURL.searchParams.append('epoch', 'ms'); 
}

function memoryQuery(start, end){
    var difference = end - start; 
    
}