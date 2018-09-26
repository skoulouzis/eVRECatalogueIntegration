function checkInputs() {
    var catalogueURL = document.getElementById("cat_url").value;
    var targetInt = document.getElementById("target").value;
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