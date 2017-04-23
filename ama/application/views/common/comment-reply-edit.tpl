
<script id="tpl-comment-edit" type="text/x-handlebars-template">
    {literal}
    <form action="./api" class="usertext cloneable warn-on-unload" onsubmit="handleFormSubmit(this);return false;" id="form-comment-{{thingid}}">
    {/literal}
        <input type="hidden" name="action" value="submit-new-comment">
        {literal} 
        <input type="hidden" name="main" value="{{mainid}}">
        
        <input type="hidden" name="parent" value="{{thingid}}">
        {/literal}
        <div class="usertext-edit md-container" style="">
            <div class="md">
                <textarea rows="1" cols="1" name="content" class="" data-event-action="comment" data-type="link"></textarea>
            </div>
            <div class="bottom-area">
                <span class="attach-toggle toggle" style="">
                    <a class="option active " href="#" tabindex="100" onclick="return toggle(this, attachon, attachoff)">添加附件</a>
                    <a class="option " href="#">不添加附件</a>
                </span>
                <span class="help-toggle toggle" style="">
                    <a class="option active " href="#" tabindex="100" onclick="return toggle(this, helpon, helpoff)">格式說明</a>
                    <a class="option " href="#">隱藏說明</a>
                </span>
                <a class="reddiquette" target="_blank" tabindex="100">內容政策</a>
                <span class="error TOO_LONG field-text" style="display:none"></span>
                <span
                    class="error RATELIMIT field-ratelimit" style="display:none">
                </span>
                <span class="error NO_TEXT field-text" style="display:none"></span>
                <span class="error TOO_OLD field-parent" style="display:none"></span>
                <span class="error THREAD_LOCKED field-parent" style="display:none"></span>
                <span class="error DELETED_COMMENT field-parent" style="display:none"></span>
                <span class="error USER_BLOCKED field-parent" style="display:none"></span>
                <span class="error USER_MUTED field-parent" style="display:none"></span>
                <span class="error MUTED_FROM_SUBREDDIT field-parent" style="display:none"></span>

                <div class="usertext-buttons">
                    <button type="submit" onclick="" class="save">保存</button>
                    <button type="button" onclick="return cancel_usertext(this);" class="cancel" style="">取消</button>
                </div>
            </div>
            {include file="common/markhelp.tpl" }
        </div>
    </form>
</script>