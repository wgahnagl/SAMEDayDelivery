$(function(){
    $.get("/get_address_data",
        function(response){
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
            var bankAccount = JSON.parse(response);
            if(response !== "null"){
                $('#displayBankAccount').show();
                $('#addBankAccount').hide();
            }
            var acct_num = bankAccount["acct_num"];
            var routing_num = bankAccount["routing_num"];
            $('#bankRoutingNum').val(routing_num);
            $('#bankAccountNumber').val(acct_num);
        }
    );

    $.get("/get_credit_card_data",
        function(response){
            var creditCards = JSON.parse(response);
            if(response !== "[]"){
                $('#displayCreditCard').show();
                $('#addCreditCard').hide();
            }
            creditCards.forEach(function(card){
                var num = card["card_num"];
                var name = card["card_name"];
                var expiration = card["card_expiration"];
                var cvv = card["card_cvv"];

                $('#cardsHolder').append(
                    ' <div class="card"  style="padding: 20px; background-color: dodgerblue; margin-bottom: 20px">\n'+
                    '           <div class="row">\n' +
                    '                <div class="col-sm-12">\n' +
                    '                    <label for="zip">Card Number</label>\n' +
                    '                    <input type="text" class="form-control" id="cardNumber" readonly value="'+num+'">\n' +
                    '                </div>\n' +
                    '            </div>\n' +
                    '            <div class="row">\n' +
                    '                <div class="col-sm-3">\n' +
                    '                    <label for="zip">Expiration</label>\n' +
                    '                    <input type="text" class="form-control" id="cardExpiration" readonly value="'+expiration+'">\n' +
                    '                </div>\n' +
                    '                <div class="col-sm-6">\n' +
                    '                    <label for="zip">Name</label>\n' +
                    '                    <input type="text" class="form-control" id="cardName" readonly value="'+name+'">\n' +
                    '                </div>\n' +
                    '                <div class="col-sm-3">\n' +
                    '                    <label for="zip">CVV</label>\n' +
                    '                    <input type="text" class="form-control" id="cardCVV" readonly value="'+cvv+'">\n' +
                    '                </div>\n' +
                    '            </div>' +
                    '</div>');
            });
        }
    );
});