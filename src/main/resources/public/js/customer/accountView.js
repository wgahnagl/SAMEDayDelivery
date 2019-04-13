$(function(){
    $.get("/get_address_data",
        function(response){
            console.log("does it exist?");
            console.log(response.address);
        }
    );

    $.get("/get_bank_account_data",
        function(response){
            console.log("does it exist?");
            console.log(response.address);
        }
    );

    $.get("/get_credit_card_data",
        function(response){
            console.log("does it exist?");
            console.log(response.address);
        }
    );
});