
<script id="tpl-file-attach" type="text/x-handlebars-template">
    {literal}
    <li class="attach-file" file_id="{{file_id}}">

        {{#if _image_file}}
            <a href="./uploads/{{file_name}}"><img width="160" src="./uploads/{{file_name}}"></a>
        {{/if}}
        <input type="text" name="attach-comment-{{file_id}}" value="" placeholder="附件说明">
    </li>
    {/literal}
</script>