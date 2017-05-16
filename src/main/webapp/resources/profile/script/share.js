/*Called when user clicks on Submit button on share-menu*/
function confirmEdit(){

    /*Gets the values entered in the share-menu*/
    var sharedText = $('#shared-text').val();
    var circuitOwner = $('.circuit-owner').val();
    var circuitName = $('.circuit-name').val();

    var checkBoxPublic = document.getElementById('public');
    var checkBoxPrivate = document.getElementById('private');

    /*Send "public" or "private" based on the check box*/
    if (checkBoxPublic.checked){
        $('.circuit-tags').val("public");
    }else if (checkBoxPrivate.checked){
        $('.circuit-tags').val("private");
    }


    var circuitTags = $('.circuit-tags').val();
    console.log(circuitTags);


    /*AJAX call to send Circuit name, owner, and updated shared and tag fields*/
    $.ajax({
                url: "/profile/submitEdit",
                method: "GET",
                data: profileShareObject(circuitName, circuitOwner, sharedText, circuitTags),
                contentType: "application/json",
                /*Upon success, receive "SUCCESS" and hide the share-menu*/
                success: function(data){
                    console.log(data);
                    hideShareMenu();
                    return false;
                }
            });
}

/*Displays the current name and shared property in the text field of the share-menu
    circuitInfo = JSON String containing all Circuit data received from Controller*/
function editShare(circuitInfo){
    /*Get JSON object*/
    var circuitInfoObject = JSON.parse(circuitInfo);
    var textField = $('#shared-text');

    /*Display share-menu*/
    $(".share-menu").addClass("show");


    /*Display Circuit name*/
    $('#circuitNameDisplay').text(circuitInfoObject.pCircuitName);

    /*Set text field to current shared property*/
    textField.val(circuitInfoObject.pCircuitShared);
}

/*Hides share-menu*/
function hideShareMenu(){
    $(".share-menu").removeClass("show");
}

/*AJAX call to send Circuit name and Owner to ProfileController
    pCircuitName = name of Circuit
    pCircuitOwner = owner of Circuit*/
function sendData(pCircuitName, pCircuitOwner){
    $.ajax({
            url: "/profile/share",
            method: "GET",
            data: profileShareObject(pCircuitName, pCircuitOwner),
            contentType: "application/json",
            /*Upon success, receives a JSON string containing all Circuit information*/
            success: function(data){
                console.log(data);
                /*Get JSON object*/
                var circuitInfoObject = JSON.parse(data);

                $('.circuit-name').val(circuitInfoObject.pCircuitName);
                $('.circuit-owner').val(circuitInfoObject.pCircuitOwner);
                $('.circuit-tags').val(circuitInfoObject.pCircuitTags);

                editShare(data);
                return false;
            }
        });
}

/*Called when user clicks on share button in a row containing Circuit name and Owner
    currSelected = row that was selected*/
function getCircuit(currSelected){

    /*Get the row containing the Circuit name and owner*/
    var currRow = $(currSelected).closest("tr").attr("class");
    var currName = $("." + currRow).find(".circuitNames");
    var currOwner = $("." + currRow).find(".circuitOwners");

    sendData(currName.html(), currOwner.html());
}

function profileShareObject(pCircuitName, pCircuitOwner, pCircuitShared, pCircuitTags){
    return {circuitName:pCircuitName, circuitOwner:pCircuitOwner, circuitShared:pCircuitShared, circuitTags:pCircuitTags}
}

