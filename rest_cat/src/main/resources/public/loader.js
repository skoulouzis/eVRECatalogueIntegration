var myVar;
function myFunction() {
    var catalogueURL = document.getElementById("cat_url").value;
    
    if (catalogueURL !== null && catalogueURL.length > 0) {
        document.getElementById("myDiv").style.display = "none";
        document.getElementById("loader").style.display = "inline";
        myVar = setTimeout(showPage, 2000);
    }

}

function showPage() {
    document.getElementById("loader").style.display = "none";
    document.getElementById("myDiv").style.display = "block";
}