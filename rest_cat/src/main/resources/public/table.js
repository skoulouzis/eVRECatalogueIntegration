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
    this.header = keys
    return this
}

Table.prototype.setData = function (data) {
//sets the main data
    this.data = data
    return this
}

Table.prototype.setCatalogueURL = function (catalogueURL) {
    this.catalogueURL = catalogueURL
    return this
}



Table.prototype.setTableClass = function (tableClass) {
//sets the table class name
    this.tableClass = tableClass
    return this
}
Table.prototype.build = function (container) {
    if (this.catalogueURL !== null) {

//default selector
        container = container || '.table-container'

        //creates table
        var table = $('<table style="margin:0;" border="1" class="w3-table \n\
w3-bordered w3-hoverable w3-hover-grey w3-light-grey w3-margin-top w3-centered \n\
w3-card-4 divpost" width="10%"></table>').addClass(this.tableClass)

        var tr = $('<tr></tr>') //creates row
        var th = $('<th></th>') //creates table header cells
        var td = $('<td></td>') //creates table cells

        var header = tr.clone() //creates header row

        //fills header row
        this.header.forEach(function (d) {
            header.append(th.clone().text(d))
        })

        //attaches header row
        table.append($('<thead></thead>').append(header))

        //creates 
        var tbody = $('<tbody></tbody>')
//
//

        const request = new XMLHttpRequest();

        const url = 'http://localhost:8080/rest/list_records/?catalogue_url=' + this.catalogueURL + '&limit=200';
        request.open("GET", url);
        request.send();

        request.onload = function () {
            json = JSON.parse(request.responseText);
            console.log(json)

            var row = tr.clone() //creates a row
            row.append(td.clone().text('CKAN'))
            row.append(td.clone().text(json.length))
            tbody.append(row)

            $(container).append(table.append(tbody)) //puts entire table in the container

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



  