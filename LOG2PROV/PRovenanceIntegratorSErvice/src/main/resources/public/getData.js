function getData(){
    //make connection to influxDB and retrieve data
    document.getElementById('dataBtn').disabled = true;
    var innerHTML = window.location.href.split('/');
    innerHTML.pop();
    innerHTML = innerHTML.join('/');

    var serviceArray = new Array();
    var table = document.getElementById('output_table');
    for(var i = 1; i < table.rows.length; i++){
        var row = table.rows.item(i);

        if(row.cells[0].childNodes[0].checked){
            var service = JSON.parse(row.getAttribute("data-rest"));
            serviceArray.push(service);
        }
    }

    console.log(JSON.stringify(serviceArray));
    var xhr = new XMLHttpRequest();
//    hr.open("POST", innerHTML + '/connectDB', false);
//    xhr.send(JSON.stringify(serviceArray));
}