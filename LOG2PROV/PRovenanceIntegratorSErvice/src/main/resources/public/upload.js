function getFormData(fileID, formData) {
    var x = document.getElementById(fileID);

    if ('files' in x) {
        for (var i = 0; i < x.files.length; i++) {
            var file = x.files[i];
            formData.append("files", file);
        }
    }

    return formData;
}


function uploadAll() {
    var formData = new FormData();
    formData = getFormData("provUpload", formData);
    formData = getFormData("serviceLogUpload", formData);
    document.getElementById('uploadBtn').disabled = true;
    var innerHTML = window.location.href.split('/');
    innerHTML.pop();
    innerHTML = innerHTML.join('/');
    var xhr = new XMLHttpRequest();
    xhr.open("POST", innerHTML + '/uploadFile', false);
    xhr.send(formData);

    console.log(xhr.responseText);
    var resultObject = JSON.parse(xhr.responseText);
    drawTimeline(resultObject.services);
    drawTable(resultObject);
};

function drawTimeline(serviceArray) {
    google.charts.load('current', { 'packages': ['timeline'] });
    google.charts.setOnLoadCallback(drawChart);
    function drawChart() {
        var container = document.getElementById('timeline');
        var chart = new google.visualization.Timeline(container);
        var dataTable = new google.visualization.DataTable();

        dataTable.addColumn({ type: 'string', id: 'Service' });
        dataTable.addColumn({ type: 'date', id: 'Start' });
        dataTable.addColumn({ type: 'date', id: 'End' });

        for (var i = 0; i < serviceArray.length; i++) {
            var ser = serviceArray[i];
            console.log('nnd: ' + ser.name)
            dataTable.addRow([ser.name , new Date(ser.startTime), new Date(ser.endTime)]);
        }

        var options = {
               'chartArea': {'width': '100%', 'height': '100%'}
        };

        chart.draw(dataTable, options);
    }
}

function drawTable(resultObject) {
    var serviceArray = resultObject.services;
    var table = document.getElementById('output_table');
    table.setAttribute('workflow', JSON.stringify(resultObject.workflow));
    var startTime, endTime;

    for (var i = 0; i < serviceArray.length; i++) {
        var element = serviceArray[i];
        var row = table.insertRow(i + 1);
        row.setAttribute('data-rest', JSON.stringify(element));

        row.insertCell(0).innerHTML = "<input type=\"checkbox\">";
        row.insertCell(1).innerHTML = element.name;
        row.insertCell(2).innerHTML = element.endpoint;
        row.insertCell(3).innerHTML = element.method;
        row.insertCell(4).innerHTML = printTime(new Date(element.startTime));
        row.insertCell(5).innerHTML = printTime(new Date(element.endTime));
    }

    document.getElementById('demo_output').style.display = "block";
}

function printTime(timestamp) {
    return timestamp.getFullYear() + "-" +
        timestamp.getMonth() + "-" +
        timestamp.getDate() + " " +
        timestamp.getHours() + ":" +
        timestamp.getMinutes() + ":" +
        timestamp.getSeconds() + "." +
        timestamp.getMilliseconds();
}

function move(docID, to, ctxName, json) {
    var elem = document.getElementById('bar' + docID);
    var width = 0;
    var id = setInterval(frame, 4);
    function frame() {
        if (width >= 100) {
            clearInterval(id);
            document.getElementById("myP" + docID).className = "w3-text-green w3-animate-opacity";
            document.getElementById("myP" + docID).innerHTML = 'Successfully created ' + to + ' ' + ctxName + ' context';

            if (docID === '3') {
                document.getElementById('uploadBtn').disabled = false;
                document.getElementById('source').value = JSON.stringify(json, undefined, 2);
            }


        } else {
            width++;
            elem.style.width = width + '%';
            var num = width * 1 / to;
            num = num.toFixed(0);
            document.getElementById('demo' + docID).innerHTML = num;
        }
    }
}