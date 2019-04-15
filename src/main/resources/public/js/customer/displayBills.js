$(function() {
    $.get("/get_billing_data",
        function (response) {
        console.log(response);
            var bills = JSON.parse(response);
            if (response !== "[]") {
                $('#displayBills').show();
                $('#noBills').hide();
            }
            bills.forEach(function(bill){
                var timestamp = bill["ship_timestamp"];
                var sender_last_name = bill["sender_last_name"];
                var sender_first_name = bill["sender_first_name"];
                var type = bill["type"];
                var price = bill["price"];

                var expected_Delivery = bill["expected_delivery"];
                var delivery_Timestamp = bill["delivery_timestamp"];
                var reciever_pays = bill["reciever_pays"];
                var weight = bill["weight"];
                var id = bill["id"];
                var paid_flag = bill["paid_flag"];

                $('#bills').append(
                    ' <div class="card"  style="padding: 20px; background-color: dodgerblue; margin-bottom: 20px">\n'+
                    '           <div class="row">\n' +
                    '                <div class="col-sm-6">\n' +
                    '                    <label for="senderName">Sender First Name</label>\n' +
                    '                    <input type="text" class="form-control"  readonly value="'+sender_first_name+'">\n' +
                    '                </div>\n' +
                    '                <div class="col-sm-6">\n' +
                    '                    <label for="senderName">Sender Last Name</label>\n' +
                    '                    <input type="text" class="form-control"  readonly value="'+sender_last_name+'">\n' +
                    '                </div>\n' +
                    '            </div>\n' +
                    '            <div class="row">\n' +
                    '                <div class="col-sm-3">\n' +
                    '                    <label for="type">Type</label>\n' +
                    '                    <input type="text" class="form-control" id="type" readonly value="'+type+'">\n' +
                    '                </div>\n' +
                    '                <div class="col-sm-6">\n' +
                    '                    <label for="price">Price</label>\n' +
                    '                    <input type="text" class="form-control" id="price" readonly value="'+price+'">\n' +
                    '                </div>\n' +
                    '                <div class="col-sm-3">\n' +
                    '                    <label for="delivery">Expected Delivery</label>\n' +
                    '                    <input type="text" class="form-control" id="delivery" readonly value="'+expected_Delivery+'">\n' +
                    '                </div>\n' +
                    '            </div>' +
                    '</div>');
            });

        }
    );
});