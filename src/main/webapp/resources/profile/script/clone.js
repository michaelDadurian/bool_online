
/*Called when clone button is clicked on the profile page
    currSelected = the row containing the circuit name and owner*/
function cloneCircuit(currSelected){

    /*Confirm clone*/
    if(confirm("Are you sure you want to clone this circuit?")){
        /*Get Circuit name and owner from the row that was selected*/
        var currRow = $(currSelected).closest("tr").attr("class");
        var currName = $("." + currRow).find(".circuitNames");
        var currOwner = $("." + currRow).find(".circuitOwners");


        sendCircuitInfo(currName.html(), currOwner.html());
    }

}

/*AJAX call to send Circuit name and owner to ProfileController
    pCircuitName = name of Circuit
    pCircuitOwner = owner of Circuit*/
function sendCircuitInfo(pCircuitName, pCircuitOwner){

    $.ajax({
            url: "/profile/cloneCircuit",
            method: "GET",
            data: profileCloneObject(pCircuitName, pCircuitOwner),
            contentType: "application/json",

            /*If successful, receives success message and alerts the user*/
            success: function(data){
                console.log(data);
                alert(data);
                return false;
            }
        });
}

/*JSON object to send to ProfileController*/
function profileCloneObject(pCircuitName, pCircuitOwner){
    return {circuitName:pCircuitName, circuitOwner:pCircuitOwner}
}