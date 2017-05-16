/*AJAX call to send Circuit name, owner and row to be deleted to ProfileController
    pCircuitName = name of Circuit
    pCircuitOwner = owner of Circuit
    pCurrRow = row that was selected*/
function deleteProfileObject(pCircuitName, pCircuitOwner, pCurrRow){

    $.ajax({
            url: "/profile/delete",
            method: "GET",
            data: profileObject(pCircuitName, pCircuitOwner, pCurrRow),
            contentType: "application/json",
            /*Upon success, receives the row to be deleted and calls updateTable*/
            success: function(data){
                console.log(data);
                updateTable(data)
                return false;
            }
        });


}
/*Deletes a row from table display
    pCurrRow = row to be deleted*/
function updateTable(pCurrRow){
    if (pCurrRow == null || pCurrRow == ""){

    }else{
       $("." + pCurrRow).remove();
    }
}

/*Called when delete button is clicked on a row
    currSelected = row containing Circuit name and owner that will be deleted*/
function deleteProfileRow(currSelected){

    /*Get the row containing Circuit name and owner*/
    var currRow = $(currSelected).closest("tr").attr("class");
    var currName = $("." + currRow).find(".circuitNames");
    var currOwner = $("." + currRow).find(".circuitOwners");

    /*If confirmed, sends Circuit name, owner, and row to deleteProfileObject*/
    if (confirm("Delete Circuit?")){
        deleteProfileObject(currName.html(), currOwner.html(), currRow);
    }


}

/*JSON object to send to ProfileController*/
function profileObject(pCircuitName, pCircuitOwner, pCurrRow){
    return {circuitName:pCircuitName, circuitOwner:pCircuitOwner, currRow:pCurrRow}
}

$(".delete-button").mousedown(function(e){deleteProfileRow(this)});