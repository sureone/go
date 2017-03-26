function handleFormSubmit(form){

    $$.ajaxPostForm(
        //action
        $(form).attr('action'),
        //form id
        '#'+$(form).attr('id'),
        //addition params
        null,
        //callback
        function(sucess,data){
            window.location.href=window.location.href;    
            // console.log(data);
        }
    )

    
}