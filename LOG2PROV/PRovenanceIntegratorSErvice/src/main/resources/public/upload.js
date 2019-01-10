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
    var serviceArray = JSON.parse(xhr.responseText);

    var table = document.getElementById('output_table');
    serviceArray.forEach(function(element) {
    console.log('bang!');
          var row = table.insertRow(0);
          row.insertCell(0).innerHTML = element.name;
          row.insertCell(1).innerHTML = element.endpoint;
          row.insertCell(2).innerHTML = element.method;
          row.insertCell(3).innerHTML = element.startTime;
          row.insertCell(4).innerHTML = element.endTime;
    });

    document.getElementById('demo_output').style.display === "block";
};

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