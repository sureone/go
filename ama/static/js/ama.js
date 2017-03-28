function voteit(url,elm,v,thingid){
    var map={
        action:'vote-link',
        thingid:thingid,
        idata:v
    };
    var postdata = JSON.stringify(map);
    $.ajax({
        dataType : 'json',
        type : 'POST',
        url : url,
        data : postdata,
        success : function(data) {
            console.log(data);
        },
        error : function() {
        }
    });
}

function saveit(url,elm,thingid){
    var map={
        action:'save-link',
        thingid:thingid
    };
    var postdata = JSON.stringify(map);
    $.ajax({
        dataType : 'json',
        type : 'POST',
        url : url,
        data : postdata,
        success : function(data) {
            console.log(data);
        },
        error : function() {
        }
    });
}
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

    $(document).delegate("#siteTable .thing .expando-button.collapsed", "click", function(e){


        var id = $(e.target).parent().closest('.thing').attr('data-thingid');

        $(e.target).removeClass("collapsed");
        $(e.target).addClass("expanded");


        $(".expando-"+id).css('display','block');

    })


 
    $(document).delegate("#siteTable .thing .expando-button.expanded", "click", function(e){

        
        var id = $(e.target).parent().closest('.thing').attr('data-thingid');

       
        $(e.target).removeClass("expanded");
        $(e.target).addClass("collapsed");


        $(".expando-"+id).css('display','none');
    })


    $(document).delegate(".login-required", "click", function(){
        if(g_logined==true) return true;
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



    jQuery("time.timeago").timeago();


});