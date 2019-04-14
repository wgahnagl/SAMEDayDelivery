$(function(){
    $.get("/get_address_data",
        function(response){
            console.log(response);
            var address = JSON.parse(response);
            if(response !== "{}"){
                $('#displayAddress').show();
                $('#addAddress').hide();
            }

            var zip = address['zipcode'];
            var country = address['country'];
            var province = address['province'];
            var addr_line1 = address['addr_line1'];
            var addr_line2 = address['addr_line2'];
            var city = address['city'];

            $('#zip').val(zip);
            $('#country').val(country);
            $('#state').val(province);
            $('#address1').val(addr_line1);
            $('#address2').val(addr_line2);
            $('#city').val(city);
        }
    );

    $.get("/get_bank_account_data",
        function(response){
            console.log(response);
        }
    );

    $.get("/get_credit_card_data",
        function(response){
            var creditCards = JSON.parse(response);
            if(response !== "[]"){
                $('#displayAddress').show();
                $('#addAddress').hide();
            }

            for(var card in creditCards){
                var num = card["card_num"];
                var name = card["card_name"];
                var expiration = card["card_expiration"];
                var cvv = card["card_cvv"];
            }

        }
    );
});