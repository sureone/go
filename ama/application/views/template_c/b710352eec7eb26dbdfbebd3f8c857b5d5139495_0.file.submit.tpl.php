<?php
/* Smarty version 3.1.30, created on 2017-03-27 05:53:53
  from "D:\work\go\ama\application\views\submit.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58d88cd167b814_37311096',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'b710352eec7eb26dbdfbebd3f8c857b5d5139495' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\submit.tpl',
      1 => 1490586688,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
    'file:common/page-header.tpl' => 1,
    'file:common/header-bottom-right.tpl' => 1,
    'file:common/side.tpl' => 1,
    'file:common/markhelp.tpl' => 1,
    'file:common/login-modal.tpl' => 1,
  ),
),false)) {
function content_58d88cd167b814_37311096 (Smarty_Internal_Template $_smarty_tpl) {
echo '<?php
';?>defined('BASEPATH') OR exit('No direct script access allowed');
<?php echo '?>';?><!DOCTYPE html>

<html lang="en">
<head>
    <?php $_smarty_tpl->_subTemplateRender("file:common/page-header.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

    <style type="text/css">
        
        .infobar {
            background-color: #FFB6C1;
        }
        
    </style>
</head>

<body class="listing-page <?php if ($_smarty_tpl->tpl_vars['logined']->value == "true") {?>loggedin<?php }?> <?php echo $_smarty_tpl->tpl_vars['page']->value;?>
-page">


<div id="header">
    <div id="header-bottom-left">
        <span class="hover pagename"><a href="./">AMA</a></span>
        <ul class="tabmenu ">
            <li class="selected"><a href="./v/submit" class="choice">发表</a></li>
    </div>
    <?php $_smarty_tpl->_subTemplateRender("file:common/header-bottom-right.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</div>
<?php $_smarty_tpl->_subTemplateRender("file:common/side.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>


<div class="content">
    <form class="submit content warn-on-unload" onsubmit="handleFormSubmit(this); return false;" action="./api" id="newlink" method="post">
        <input name="action" value="submit-new-link" type="hidden">
        
        <div class="formtabs-content">
            <div class="spacer">
                <div id="text-desc" class="infobar">
                    你正要發表以文字為主的文章，請暢所欲言。發文時必須註明標題，但不一定要在文字欄位中長篇大論。使用「如果你...請幫我加分」作為標題，是違反銀河法規的。
                </div>
            </div>
            <div class="spacer">
                <div class="roundfield " id="title-field">
                    <span class="title required-roundfield">標題</span>
                    <div class="roundfield-content">
                        <textarea name="title" rows="2" required=""></textarea>
                        <div class="error NO_TEXT field-title" style="display:none"></div>
                        <div class="error TOO_LONG field-title" style="display:none"></div>
                    </div>
                </div>
            </div>
            <div class="spacer">
                <div class="roundfield " id="text-field">
                    <span class="title ">文字</span> 
                    <span class="little gray roundfield-description">(非必填項目)</span>
                    <div class="roundfield-content"><input name="kind" value="self" type="hidden">

                        <div class="usertext">
                            <input type="hidden" name="thing_id" value="">
                            <div class="usertext-edit md-container" style="">
                                <div class="md">
                                    <textarea rows="1" cols="1" name="content" class=""></textarea>
                                </div>
                                
                                <?php $_smarty_tpl->_subTemplateRender("file:common/markhelp.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

                            </div>
                        </div>
                        <span class="error NO_SELFS field-sr" style="display:none"></span>
                    </div>
                </div>
            </div>

            
            <div class="spacer">
                <div class="roundfield "><span class="title ">選項</span>

                    <div class="roundfield-content">
                        <input class="nomargin" type="checkbox" checked="checked" name="sendreplies" id="sendreplies" data-send-checked="true">
                        <label for="sendreplies">將回覆寄到我的收件匣</label></div>
                </div>
            </div>

        <div class="roundfield info-notice">please be mindful of reddit's 
            <a href="https://www.reddit.com/help/contentpolicy" target="_blank">內容政策</a>
             and practice 
            <a href="https://www.reddit.com/wiki/reddiquette" target="_blank">良好的 reddit 站規</a>.
        </div>
        <div id="items-required">*required</div>
        <input name="resubmit" value="" type="hidden">

        <div class="spacer">
            <button class="btn" name="submit" value="form" type="submit">送出</button>
            <span class="status"></span>
            <span class="error RATELIMIT field-ratelimit" style="display:none"></span>
            <span class="error INVALID_OPTION field-sr" style="display:none"></span>
            <span class="error IN_TIMEOUT field-sr" style="display:none"></span>
        </div>
    </form>
</div>

<div id="footer"></div>
<?php $_smarty_tpl->_subTemplateRender("file:common/login-modal.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</body>


<?php echo '<script'; ?>
 type="text/javascript" src="./static/js/submit.js?v=8"><?php echo '</script'; ?>
>
</html><?php }
}
