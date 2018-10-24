function checkInputs() {
    var catalogueURL = document.getElementById("cat_url").value;
    var targetSelect = document.getElementById("target").value;

    var startButt = document.getElementById("startBtn");
    startButt.disabled = true;

    var targetInt = parseInt(targetSelect)
    switch (targetInt) {
        case 1:
            var mappingSelect = document.getElementById("mappingSelect");
            if (mappingSelect.options.length < 2) {
                var option = document.createElement("option");
                option.text = "Mapping115";
                option.value = 1;
                mappingSelect.add(option);
                option = document.createElement("option");
                option.text = "Mapping120";
                option.value = 2;
                mappingSelect.add(option);
                
                mappingSelect.selectedIndex = 1
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
    mappingInt = parseInt(mappingSelect)
    console.log(mappingInt);


    if (catalogueURL !== null && targetInt >= 1 && mappingInt >= 1) {
        startButt.disabled = false;
        return true;
    } else {
        startButt.disabled = true;
        return false;
    }
}