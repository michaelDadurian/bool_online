$(document).on('change', '#loader',function(){
    console.log("sadas")
	var fileToLoad = $("#loader")[0].files[0];
    //var fileToLoad = document.getElementById("loader").files[0];

    var fileReader = new FileReader();
    fileReader.onload = function(fileLoadedEvent)
    {

        obj = JSON.parse(fileLoadedEvent.target.result);
        obj.circuitContent = JSON.stringify(obj.circuitContent);
        obj.constraints = JSON.stringify(obj.constraints);

        var fileName = fileToLoad.name;
        fileName = fileName.substring(0, fileName.length-4);
        obj.name = fileName;

        console.log(obj);
        $.ajax({
                    url: "/profile/uploadLocal",
                    method: "GET",
                    data: obj,
                    contentType: "application/json",
                    success: function(data){
                        console.log(data);
                        alert("Success");
                        return false;
                    }
                });

    };

    fileReader.readAsText(fileToLoad, "UTF-8");
 });

 function profileLoadObject(pOwner, pShared, pName, pContent, pConstraints, pTags){
     return {owner:pOwner, shared:pShared, circuitContent:pContent, constraints:pConstraints, tags:pTags}
 }