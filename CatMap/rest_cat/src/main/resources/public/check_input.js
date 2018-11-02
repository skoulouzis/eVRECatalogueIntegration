function checkInputs() {
    var catalogueURL = document.getElementById("cat_url").value;
    var targetSelect = document.getElementById("target").value;

    var innerHTML = window.location.href.split('/');
    innerHTML.pop();
    innerHTML = innerHTML.join('/');

    var resultsURL = innerHTML + '/catalogue_type/?catalogue_url=' + catalogueURL;
    if (catalogueURL !== null || catalogueURL !== "") {
        console.log(catalogueURL);
        var request = new XMLHttpRequest();
        request.open('GET', resultsURL, false);
        request.send(null);
        var catType = request.responseText;
//    console.log(catType);
//    var catType = 'CSW';

        var startButt = document.getElementById("startBtn");
        startButt.disabled = true;

        var targetInt = parseInt(targetSelect);
        switch (targetInt) {
            case 1:
                var mappingSelect = document.getElementById("mappingSelect");
                if (mappingSelect.options.length < 2 && catType === 'CKAN') {
                    var option = document.createElement("option");
                    option.text = "Mapping115";
                    option.value = 1;
                    mappingSelect.add(option);
                    option = document.createElement("option");
                    option.text = "Mapping120";
                    option.value = 2;
                    mappingSelect.add(option);

                    mappingSelect.selectedIndex = 1;
                }
                if (mappingSelect.options.length < 2 && catType === 'CSW') {
                    var option = document.createElement("option");
                    option.text = "Mapping61";
                    option.value = 3;
                    mappingSelect.add(option);
                    option = document.createElement("option");
                    mappingSelect.selectedIndex = 1;
                }
                break;

            default:
                var mappingSelect = document.getElementById("mappingSelect");

                if (mappingSelect.options.length >= 2) {
                    mappingSelect.remove(mappingSelect.selectedIndex);
                }
                break;
        }
        mappingSelect = document.getElementById("mappingSelect").value;
        mappingInt = parseInt(mappingSelect);
        console.log(mappingInt);
    }



    if (catalogueURL !== null && targetInt >= 1 && mappingInt >= 1) {
        startButt.disabled = false;
        return true;
    } else {
        startButt.disabled = true;
        return false;
    }
}