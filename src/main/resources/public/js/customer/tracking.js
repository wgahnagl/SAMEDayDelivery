$(function() {
    $.get("/get_customer_packages",
        function (response) {
            console.log(response);
        }
    );
}