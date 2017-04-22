function handleUploadFileDone(result){
    console.log(result);
    
    var tpl = Handlebars.compile($("#tpl-file-attach").html());
   
    if(result.is_image==1){
        result['_image_file']=1;
    }
    var h = tpl(result);

    $("#attaches").append(h);

}

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


            window.location.href="./";    
            console.log(data);

        }
    )

	
}