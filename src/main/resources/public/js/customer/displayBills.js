$(function() {
    $.get("/get_billing_data",
        function (response) {
        console.log(response);
            var address = JSON.parse(response);
            if (response !== "[]") {
                $('#displayBills').show();
                $('#noBills').hide();
            }

        }
    );
});