$(function(){
    $.get("/get_address_data",
        function(response){
            console.log("does it exist?")
            console.log(response.address);
        }
    )

});