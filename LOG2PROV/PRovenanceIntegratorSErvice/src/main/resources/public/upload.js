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
    document.getElementById('source').value = '';

    var innerHTML = window.location.href.split('/');
    innerHTML.pop();
    innerHTML = innerHTML.join('/');
    var xhr = new XMLHttpRequest();
    xhr.open("POST", innerHTML + '/uploadFile', false);
    xhr.send(formData);

    var serviceArray = JSON.parse(xhr.responseText);

    document.getElementById('source').value = "Services found:";
    serviceArray.forEach(function(element) {
        document.getElementById('source').value += '\n';
        document.getElementById('source').value += JSON.stringify(element, null, "\t");
    });
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