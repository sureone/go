

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


    $("#login-form").submit(function(e){
        e.preventDefault();
        $$.ajaxPostForm(
            //action
            $(this).attr('action'),
            //form id
            '#'+$(this).attr('id'),
            //addition params
            null,
            //callback
            function(sucess,data){

                window.location.href="./";
                
            }
        )
        return false;
    })

    $("#register-form").submit(function(e){
        e.preventDefault();
        $$.ajaxPostForm(
            //action
            $(this).attr('action'),
            //form id
            '#'+$(this).attr('id'),
            //addition params
            null,
            //callback
            function(sucess,data){
                window.location.href="./";
            }
        )
        return false;
    })

    $("#logout-form").submit(function(e){
        e.preventDefault();
        $$.ajaxPostForm(
            //action
            $(this).attr('action'),
            //form id
            '#'+$(this).attr('id'),
            //addition params
            null,
            //callback
            function(sucess,data){
               window.location.href="./";
            }
        )

        return false;

    })
});