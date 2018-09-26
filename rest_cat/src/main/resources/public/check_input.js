function checkInputs() {
    var catalogueURL = document.getElementById("cat_url").value;
    var targetSelect = document.getElementById("target").value;
    console.log(targetInt);
    var targetInt = parseInt(targetSelect)
    switch (targetInt) {
        case 1:
            var mappingSelect = document.getElementById("mappingSelect");
            if (mappingSelect.options.length < 2) {
                var option = document.createElement("option");
                option.text = "Mapping115";
                mappingSelect.add(option);
            }
            break;

        default:
            var mappingSelect = document.getElementById("mappingSelect");
            if (mappingSelect.options.length >= 2) {
                mappingSelect.remove(mappingSelect.selectedIndex);
            }

            break;
    }






//        console.log(mapping115);
//        if (mapping115 !== null) {
//            var option = document.createElement("option");
//            option.text = "Mapping115";
//            mappingSelect.add(option);
//        }





//    console.log(catalogueURL);
//    console.log(targetInt);
    if (catalogueURL !== null && targetInt > 0) {
        var startButt = document.getElementById("startBtn");
        startButt.disabled = false;
    }
}


function configure() {
// Plain JavaScript
    fileSelector.setAttribute('multiple', 'multiple');

// jQuery - change the file select
    var fileSelector = $('<input type="file" multiple="">');

}