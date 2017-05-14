function getLink(currSelected){

    $(".get-link-menu").addClass("show");

    var currRow = $(currSelected).closest("tr").attr("class");
    var currName = $("." + currRow).find(".circuitNames");
    var currOwner = $("." + currRow).find(".circuitOwners");

    sendOwnerName(currName.html(), currOwner.html());
}

function sendOwnerName(pCircuitName, pCircuitOwner){
    $.ajax({
            url: "/profile/getLink",
            method: "GET",
            data: profileLinkObject(pCircuitName, pCircuitOwner),
            contentType: "application/json",
            success: function(data){
                console.log(data);
                setText(data);
                return false;
            }
        });
}

function setText(link){

    console.log(link);
    var textField = $('#shareable-link');
    textField.val(link);
}

function hideLinkMenu(){
    $(".get-link-menu").removeClass("show");
}




function profileLinkObject(pCircuitName, pCircuitOwner){
    return {circuitName:pCircuitName, circuitOwner:pCircuitOwner}
}