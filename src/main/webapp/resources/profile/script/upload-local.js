$("#loader").change(function(){
	var fileToLoad = $("#loader")[0].files[0];
    //var fileToLoad = document.getElementById("loader").files[0];
    console.log(fileToLoad);
    console.log("helooooo");
 
    var fileReader = new FileReader();
    fileReader.onload = function(fileLoadedEvent) 
    {
        obj = JSON.parse(fileLoadedEvent.target.result);


    };

    fileReader.readAsText(fileToLoad, "UTF-8");
 });

