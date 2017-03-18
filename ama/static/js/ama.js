
$(document).ready(function () {
// <div class="modal-backdrop fade in"></div>

    var threads=[{
        thingid:1,
        title:'18 year old female, fed via NG tube AMA',
        text:'i am an 18 year old girl, fed via an NG tube (and have been for over 2 years) due to illness. normally people have a lot of questions about my tube, and currently i am in hospital with no entertainment so i thought i would answer any questions people have!',
        cdate:new Date(),
        author:'chloegbih',
        likes:200,
        dislikes:160,
        score:78   
    },{
        thingid:2,
        title:'Journey down the rabbit hole with fellow Alice in Wonderland fans as you exchange awesome gifts related to her magic, and her world. Join Reddit Gifts for this exchange, and explore your imagination!',
        text:'',
        cdate:new Date(),
        author:'mathamatazz',
        likes:200,
        dislikes:160,
        score:96
    },{
        thingid:3,
        title:'18 year old female, fed via NG tube AMA',
        text:'i am an 18 year old girl, fed via an NG tube (and have been for over 2 years) due to illness. normally people have a lot of questions about my tube, and currently i am in hospital with no entertainment so i thought i would answer any questions people have!',
        cdate:new Date(),
        author:'chloegbih',
        likes:200,
        dislikes:160,
        score:78   
    },{
        thingid:4,
        title:'I was tried and convicted of manslaughter for killing one of my closest friends, AMA',
        text:'',
        cdate:new Date(),
        author:'mathamatazz',
        likes:200,
        dislikes:160,
        score:96
    }];
    function showLoginModal(){
        $(".login-modal").addClass('in');
        $(".login-modal").parent().append('<div class="modal-backdrop fade in"></div>');
    }
    function closeLoginModal(){
        $(".login-modal").removeClass('in');
        $(".modal-backdrop").remove();
    }

    $(document).delegate("#siteTable .thing .expando-button.collapsed", "click", function(e){


        var idx = $(e.target).parent().closest('.thing').attr('data-idx');

        $(e.target).removeClass("collapsed");
        $(e.target).addClass("expanded");


        $(".expando-"+idx).css('display','block');



        var tpl = Handlebars.compile($("#tpl-thread-text").html());


        $(".expando-"+idx).html(tpl(threads[idx]));
    })


    $(document).delegate("#siteTable .thing .expando-button.expanded", "click", function(e){

        
        var idx = $(e.target).parent().closest('.thing').attr('data-idx');

        $(e.target).removeClass("expanded");
        $(e.target).addClass("collapsed");


        $(".expando-"+idx).css('display','none');

        $(".expando-"+idx).html('');
    })


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


    function renderThreads(){

        var tpl = Handlebars.compile($("#tpl-thread-item").html());
        var container = $("#siteTable");
        container.html('');
        for(var i=0;i<threads.length;i++){
            var t = threads[i];
            t['idx']=i;
            t['no']=i+1;
            var html = tpl(t);
            container.append(html);

        }

    }

    renderThreads();


});