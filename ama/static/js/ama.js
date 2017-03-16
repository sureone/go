

$(document).ready(function () {
// <div class="modal-backdrop fade in"></div>
    function showLoginModal(){
        $(".login-modal").addClass('in');
        $(".login-modal").parent().append('<div class="modal-backdrop fade in"></div>');
    }
    function closeLoginModal(){
        $(".login-modal").removeClass('in');
        $(".modal-backdrop").remove();
    }
    $(document).delegate(".login-required", "click", function(){
        showLoginModal();
        return false;
    })

    $(document).delegate(".login-modal .c-close", "click", function(){
        closeLoginModal();
        return false;
    })


    $("#login-form").submit(function(){
         var param = $(this).serializeFormStandard();
         alert(JSON.stringify(param));
         return false;
    })
});