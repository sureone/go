<?php
/* Smarty version 3.1.30, created on 2017-04-23 09:17:52
  from "D:\go\ama\application\views\common\comment-reply-edit.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58fc5520625642_91606284',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '8e352f3f10d8bdc0f289664303560737af49f0b6' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\comment-reply-edit.tpl',
      1 => 1492931870,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
    'file:common/markhelp.tpl' => 1,
  ),
),false)) {
function content_58fc5520625642_91606284 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->compiled->nocache_hash = '327658fc5520619ac7_30963306';
?>

<?php echo '<script'; ?>
 id="tpl-comment-edit" type="text/x-handlebars-template">
    
    <form action="./api" class="usertext cloneable warn-on-unload" onsubmit="handleFormSubmit(this);return false;" id="form-comment-{{thingid}}">
    
        <input type="hidden" name="action" value="submit-new-comment">
         
        <input type="hidden" name="main" value="{{mainid}}">
        
        <input type="hidden" name="parent" value="{{thingid}}">
        
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
            <?php $_smarty_tpl->_subTemplateRender("file:common/markhelp.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

        </div>
    </form>
<?php echo '</script'; ?>
><?php }
}
