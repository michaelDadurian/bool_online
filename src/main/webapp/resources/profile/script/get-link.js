/*Called when getLink button is clicked on a row
    currSelected = row containing Circuit Name and Owner*/
function getLink(currSelected){

    /*Display link-menu containing the shareable link*/
    $(".get-link-menu").addClass("show");

    var menu = $(".get-link-menu");

    $(".get-link-menu").offset({top: window.height/2 - menu.height()/2, left: window.width/2 - menu.width()/2})

    /*Get Circuit name and Owner from the row*/
    var currRow = $(currSelected).closest("tr").attr("class");
    var currName = $("." + currRow).find(".circuitNames");
    var currOwner = $("." + currRow).find(".circuitOwners");


    sendOwnerName(currName.html(), currOwner.html());
}

/*AJAX call to send Circuit name and Owner to ProfileController
    pCircuitName = name of Circuit
    pCircuitOwner = owner of Circuit*/
function sendOwnerName(pCircuitName, pCircuitOwner){
    $.ajax({
            url: "/profile/getLink",
            method: "GET",
            data: profileLinkObject(pCircuitName, pCircuitOwner),
            contentType: "application/json",
            /*Upon success, receives a link to workspace*/
            success: function(data){
                console.log(data);
                /*Send link to setText()*/
                setText(data);
                return false;
            }
        });
}

/*Sets the text field of link-menu to the text received from ProfileController
    link = shareable link text received from ProfileController*/
function setText(link){

    console.log(link);
    /*Get text field of link-menu by ID*/
    var textField = $('#shareable-link');
    /*Set value*/
    textField.val(link);
}

/*Hides the link-menu*/
function hideLinkMenu(){
    $(".get-link-menu").removeClass("show");
}


/*JSON object to send to ProfileController*/
function profileLinkObject(pCircuitName, pCircuitOwner){
    return {circuitName:pCircuitName, circuitOwner:pCircuitOwner}
}