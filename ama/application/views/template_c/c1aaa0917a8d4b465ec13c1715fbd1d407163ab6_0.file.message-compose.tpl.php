<?php
/* Smarty version 3.1.30, created on 2017-04-01 03:03:06
  from "D:\work\go\ama\application\views\message-compose.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58defc4adf5414_27173169',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'c1aaa0917a8d4b465ec13c1715fbd1d407163ab6' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\message-compose.tpl',
      1 => 1491007681,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
    'file:common/page-header.tpl' => 1,
    'file:common/header-bottom-right.tpl' => 1,
    'file:common/markhelp.tpl' => 1,
    'file:common/login-modal.tpl' => 1,
  ),
),false)) {
function content_58defc4adf5414_27173169 (Smarty_Internal_Template $_smarty_tpl) {
?>
<html lang="en">
<head>
	<?php $_smarty_tpl->_subTemplateRender("file:common/page-header.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</head>

<body class="listing-page <?php if ($_smarty_tpl->tpl_vars['logined']->value == "true") {?>loggedin<?php }?> <?php echo $_smarty_tpl->tpl_vars['page']->value;?>
-page">


<div id="header">
<div id="header-bottom-left">
	<span class="hover pagename"><a href="./">信息</a></span>
	<ul class="tabmenu ">
		<li class="selected"><a href="./v/message/compose" class="choice">傳送一個私人訊息</a></li>
		<li><a href="./v/message/inbox" class="choice">收件匣</a></li>
		<li><a href="./v/message/sent" class="choice">发件箱</a></li>
</div>
<?php $_smarty_tpl->_subTemplateRender("file:common/header-bottom-right.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</div>
<div class="content">
	
		<h1>傳送一個私人訊息</h1>
	    <form class="submit content warn-on-unload" onsubmit="handleFormSubmit(this); return false;" action="./api" id="newlink" method="post">
        <input name="action" value="submit-new-message" type="hidden">
        
        <div class="formtabs-content">
	        <div class="spacer">
	        	<div class="roundfield ">
	        		<span class="title ">至</span> 
	        		<span class="little gray roundfield-description">(username)</span>
	        		<div class="roundfield-content">
	        			<input type="text" name="recipients" value="" onchange="admincheck(this)">
	        			<span class="error NO_USER field-to" style="display:none"></span>
	        			<span class="error USER_DOESNT_EXIST field-to" style="display:none">
	        			</span>
	        			<span class="error SUBREDDIT_NOEXIST field-to" style="display:none"></span><span class="error USER_BLOCKED field-to" style="display:none"></span>
	        			<span class="error USER_BLOCKED_MESSAGE field-to" style="display:none"></span>
	        			<span class="error USER_MUTED field-to" style="display:none"></span>
	        			<span class="error MUTED_FROM_SUBREDDIT field-to" style="display:none"></span>
	        		</div>
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
                    <div class="roundfield-content">
                        <input name="kind" value="self" type="hidden">

                        <div class="usertext">
                            <div class="usertext-edit md-container" style="">
                                <div class="md">
                                    <textarea rows="1" cols="1" name="content" class=""></textarea>
                                </div>
                                <div class="bottom-area">
					                <span class="help-toggle toggle" style="">
					                    <a class="option active " href="#" tabindex="100" onclick="return toggle(this, helpon, helpoff)">格式說明</a>
					                    <a class="option " href="#">隱藏說明</a>
					                </span>
					                <a href="/help/contentpolicy" class="reddiquette" target="_blank" tabindex="100">內容政策</a>
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
					            </div>
                                <?php $_smarty_tpl->_subTemplateRender("file:common/markhelp.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

                            </div>
                        </div>
                        <span class="error NO_SELFS field-sr" style="display:none"></span>
                    </div>
                </div>
            </div>

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
 type="text/javascript" src="./static/js/message-compose.js?v=8"><?php echo '</script'; ?>
>
</html><?php }
}
