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
    formData = getFormData("sysLogUpload", formData);




    var innerHTML = window.location.href.split('/');
    innerHTML.pop();
    innerHTML = innerHTML.join('/');
    var xhr = new XMLHttpRequest();
    xhr.open("POST", innerHTML + '/uploadFile');
    xhr.send(formData);

    xhr.onload = function () {
        if (xhr.responseText !== null || xhr.responseText.length > 0) {
            json = JSON.parse(xhr.responseText);
//            console.log(json);
//            console.log(json['workflowContext']);
            move('1', 1, 'workflow');

            move('2', json['systemContext'].length, 'system');

            var totalServices = 0;
            for (var key in json['systemContext']) {
                var systemContext = json['systemContext'][key];
                totalServices += systemContext['services'].length;
            }

            move('3', totalServices, 'services');
            var cont = syntaxHighlight(json);
//            console.log(cont)
//            document.getElementById("res").innerHTML = cont;

        }

    };


}

function move(docID, to, ctxName) {
    var elem = document.getElementById('bar' + docID);
    var width = 0;
    var id = setInterval(frame, 2);
    function frame() {
        if (width >= 100) {
            clearInterval(id);
            document.getElementById("myP" + docID).className = "w3-text-green w3-animate-opacity";
            document.getElementById("myP" + docID).innerHTML = 'Successfully created ' + to + ' ' + ctxName + ' context';
        } else {
            width++;
            elem.style.width = width + '%';
            var num = width * 1 / to;
            num = num.toFixed(0);
            document.getElementById('demo' + docID).innerHTML = num;
        }
    }
}

function syntaxHighlight(json) {
    if (typeof json != 'string') {
        json = JSON.stringify(json, undefined, 2);
    }
    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        return '<span class="' + cls + '">' + match + '</span>';
    });
}