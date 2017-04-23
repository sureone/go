function handleFormSubmit(form){

    var attches=[];
    if($(".attach-file").length>0){
        for(var i=0;i<$(".attach-file").length;i++){
            var fileid = $($(".attach-file")[i]).attr("file_id");
            var comment = $("input[name=attach-comment-"+fileid+"]").val();
            attches.push({file_id:fileid,file_comment:comment});
        }
    }
    $$.ajaxPostForm(
        //action
        $(form).attr('action'),
        //form id
        '#'+$(form).attr('id'),
        //addition params
       {attaches:attches},
        //callback
        function(sucess,data){
            window.location.href=window.location.href;    
            // console.log(data);
        }
    )

    
}