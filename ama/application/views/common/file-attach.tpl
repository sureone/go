
<script id="tpl-file-attach" type="text/x-handlebars-template">
    {literal}
    <li class="attach-file new" file_id="{{file_id}}">
        {{#if _image_file}}
        	<a href="./uploads/{{file_name}}"><img width="160" src="./uploads/{{file_name}}"></a>
        {{/if}}
        <a href="javascript:removeNewAttach({{file_id}})">删除附件</a>
        <a href="javascript:changeAttachOrder({{file_id}},-1)">向上</a>
        <a href="javascript:changeAttachOrder({{file_id}},1)">向下</a>
        
        <input type="text" name="attach-comment-{{file_id}}" value="" placeholder="附件说明({{file_name}})">
    </li>
    {/literal}
</script>