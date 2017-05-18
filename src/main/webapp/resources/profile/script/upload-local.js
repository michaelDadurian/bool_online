/*Called when user clicks on Upload button*/
$(document).on('change', '#loader',function(){
	var fileToLoad = $("#loader")[0].files[0];

    var fileReader = new FileReader();
    fileReader.onload = function(fileLoadedEvent)
    {

        /*Turn text file into JSON object*/
        obj = JSON.parse(fileLoadedEvent.target.result);
        obj.circuitContent = JSON.stringify(obj.circuitContent);
        obj.constraints = JSON.stringify(obj.constraints);

        /*Get file name to set Circuit name*/
        var fileName = fileToLoad.name;
        /*Remove file extension*/
        fileName = fileName.substring(0, fileName.length-4);

        /*Set name of Circuit to name of File*/
        obj.name = fileName;

        var circuitFileObject = {
            owner: obj.owner,
            shared: obj.shared,
            name: obj.name,
            circuitContent: obj.circuitContent,
            quizletConstraints: JSON.stringify(obj.quizletConstraints),
            tags: obj.tags
        };


        console.log(circuitFileObject);
        /*AJAX call to send uploaded Circuit to profile
            Upon success, receives "SUCCESS" and alerts the user*/
        $.ajax({
                    url: "/profile/uploadLocal",
                    method: "POST",
                    data: JSON.stringify(circuitFileObject),
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