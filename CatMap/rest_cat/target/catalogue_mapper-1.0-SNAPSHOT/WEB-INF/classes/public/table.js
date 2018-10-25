var data = {
    k: ['Catalogue Type', 'Num. Of Records']
}

function Table() {
//sets attributes
    this.header = [];
    this.data = [[]];
    this.catalogueURL = ""
    this.tableClass = ''
}

Table.prototype.setHeader = function (keys) {
//sets header data
    this.header = keys;
    return this;
}

Table.prototype.setData = function (data) {
//sets the main data
    this.data = data;
    return this;
}

Table.prototype.setCatalogueURL = function (catalogueURL) {
    this.catalogueURL = catalogueURL;
    return this;
}



Table.prototype.setTableClass = function (tableClass) {
//sets the table class name
    this.tableClass = tableClass;
    return this;
}
Table.prototype.build = function (container) {
    var innerHTML = window.location.href.split('/');
    innerHTML.pop();
    innerHTML = innerHTML.join('/');

    if (this.catalogueURL !== null || this.catalogueURL.length > 0) {
        document.getElementById("loader").style.display = "inline";



        var table = document.getElementById("tbl");
        if (table.rows.length >= 2) {
            table.deleteRow(1);
        }
        var row = table.insertRow(1);
        var cell1 = row.insertCell(0);
        var cell2 = row.insertCell(1);
//        console.log(table.rows.length);




        const request = new XMLHttpRequest();
        var limit = document.getElementById("recLimit").value;
//        const url = innerHTML + '/list_records/?catalogue_url=' + this.catalogueURL + '&limit=' + limit;
        const url = innerHTML + '/list_records/?catalogue_url=' + this.catalogueURL;
        console.log(url)
        request.open("GET", url);

        request.send();

        request.onload = function () {
            if (request.responseText !== null || request.responseText.length > 0) {
                
                json = JSON.parse(request.responseText);
                cell1.innerHTML = "CKAN";
                cell2.innerHTML = json.length;
            }
            document.getElementById("loader").style.display = "none";
        };


    }
    return this
}
function analyzeCatalogue() {
    var catUrl = document.getElementById("cat_url").value;
    var targetInt = document.getElementById("target").value;
    if (catUrl !== null) {
        if (targetInt > 0) {
            var startButt = document.getElementById("startBtn");
            startButt.disabled = false;
        }
        var table = new Table()
        table.setHeader(data.k)
                .setData(data.v)
                .setTableClass('sean')
                .setCatalogueURL(catUrl)
                .build()
    }
}



  