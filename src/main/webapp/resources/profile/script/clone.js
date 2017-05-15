function cloneCircuit(currSelected){

    if(confirm("Are you sure you want to clone this circuit?")){
        var currRow = $(currSelected).closest("tr").attr("class");
        var currName = $("." + currRow).find(".circuitNames");
        var currOwner = $("." + currRow).find(".circuitOwners");

        sendCircuitInfo(currName.html(), currOwner.html());
    }

}


function sendCircuitInfo(pCircuitName, pCircuitOwner){

    $.ajax({
            url: "/profile/cloneCircuit",
            method: "GET",
            data: profileCloneObject(pCircuitName, pCircuitOwner),
            contentType: "application/json",
            success: function(data){
                console.log(data);
                alert(data);
                return false;
            }
        });
}


function profileCloneObject(pCircuitName, pCircuitOwner){
    return {circuitName:pCircuitName, circuitOwner:pCircuitOwner}
}