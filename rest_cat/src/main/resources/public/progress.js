var currProgress = 0;
//is the task complete
var done = false;
//total progress amount
var total = 100;

//function to update progress
function startProgress() {
//get the progress element
    var prBar = document.getElementById("progressBar");
//get the start button
    var startButt = document.getElementById("startBtn");
//get the textual element
    var val = document.getElementById("numValue");
//disable the button while the task is unfolding
    startButt.disabled = true;
//update the progress level
    prBar.value = currProgress;
//update the textual indicator
    val.innerHTML = Math.round((currProgress / total) * 100) + "%";
//increment the progress level each time this function executes
    currProgress++;
//check whether we are done yet
    if (currProgress > 100)
        done = true;
//if not done, call this function again after a timeout
    if (!done)
        setTimeout("startProgress()", 100);
//task done, enable the button and reset variables
    else
    {
        document.getElementById("startBtn").disabled = false;
        done = false;
        currProgress = 0;
    }
}


function move() {
    var elem = document.getElementById("myBar");
    var width = 10;
    var id = setInterval(frame, 10);
    function frame() {
        var startButt = document.getElementById("startBtn");
        startButt.disabled = true;
        if (width >= 100) {
            clearInterval(id);
            document.getElementById("startBtn").disabled = false;
            done = false;
            currProgress = 0;
        } else {
            width++;
            elem.style.width = width + '%';
            elem.innerHTML = width * 1 + '%';
        }
    }
}