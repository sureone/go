function handleFormSubmit(form){

     $(form).find(".status").html("消息发送中。。。");
    $$.ajaxPostForm(
        //action
        $(form).attr('action'),
        //form id
        '#'+$(form).attr('id'),
        //addition params
        null,
        //callback
        function(sucess,data){

            console.log(data);

            $(form).find(".status").html("消息已送达。");
        }
    )

    
}