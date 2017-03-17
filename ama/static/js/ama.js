

$(document).ready(function () {
// <div class="modal-backdrop fade in"></div>

    var things=[{
        title:'18 year old female, fed via NG tube AMA',
        text:'i'm an 18 year old girl, fed via an NG tube (and have been for over 2 years) due to illness. normally people have a lot of questions about my tube, and currently i am in hospital with no entertainment so i thought i would answer any questions people have!',
        cdate:new Date(),
        author:'chloegbih',
        ups:200,
        downs:160       
    }];
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