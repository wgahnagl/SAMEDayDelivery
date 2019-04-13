$(document).on('change', '#password2', function () {
    var password2 = $('#password2');
    var password1 = $('#password1');
    console.log("changed, babe");

   if(password2.val() !== password1.val()){
       password2.addClass("error");
       password1.addClass("error");
       $('#accountCreateForm').submit(function (e) {
           e.preventDefault();
       })
   }else{
       console.log("aaa oh no")
       password1.removeClass("error");
       password2.removeClass("error");
       $('#accountCreateForm').unbind()
   }
});
