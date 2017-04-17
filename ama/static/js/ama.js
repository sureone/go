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

            var score_elm = $("#vote-"+thingid+" .score.unvoted");
            if($(elm).hasClass('upmod')){
                $(elm).removeClass('upmod').addClass("up");
                score_elm.html(parseInt(score_elm.text())-1);

            }else if($(elm).hasClass('downmod')){
                $(elm).removeClass('downmod').addClass("down");

                score_elm.html(parseInt(score_elm.text())+1);
            }else if($(elm).hasClass('down')){
                $("#vote-"+thingid+" .upmod").removeClass("upmod").addClass("up");
                $(elm).removeClass('down').addClass("downmod");

                score_elm.html(parseInt(score_elm.text())-1);
            }else if($(elm).hasClass('up')){

                $("#vote-"+thingid+" .downmod").removeClass("downmod").addClass("down");
                $(elm).removeClass('up').addClass("upmod");

                score_elm.html(parseInt(score_elm.text())+1);
            }
            console.log(data);
        },
        error : function() {
        }
    });
}

function deleteit(thingid){
    var map={
        action:'delete-link',
        thingid:thingid
    };
    var postdata = JSON.stringify(map);
    $.ajax({
        dataType : 'json',
        type : 'POST',
        url : "./api",
        data : postdata,
        success : function(data) {
            window.location.href="./";
         
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

        var thing = $(e.target).parent().closest('.thing');
        var id = thing.attr('data-thingid');

    

        $(e.target).removeClass("collapsed");
        $(e.target).addClass("expanded");


        $(".expando-"+id).css('display','block');

    })


 
    $(document).delegate("#siteTable .thing .expando-button.expanded", "click", function(e){

        
        var thing = $(e.target).parent().closest('.thing');
        var id = thing.attr('data-thingid');

     
       
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

    // $(".out.md").each(function(){
    //     var md = $(this).text();
    //     var h = markdown.toHTML(md);
    //     $(this).html(h);
    // })


});