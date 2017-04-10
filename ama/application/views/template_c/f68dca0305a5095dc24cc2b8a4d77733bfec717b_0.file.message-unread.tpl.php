<?php
/* Smarty version 3.1.30, created on 2017-04-10 11:25:24
  from "D:\work\go\ama\application\views\message-unread.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58eb4f8464de54_68898819',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'f68dca0305a5095dc24cc2b8a4d77733bfec717b' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\message-unread.tpl',
      1 => 1491799236,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
    'file:common/page-header.tpl' => 1,
    'file:common/header-bottom-right.tpl' => 1,
    'file:common/message.tpl' => 1,
    'file:common/login-modal.tpl' => 1,
    'file:common/comment-reply-edit.tpl' => 1,
  ),
),false)) {
function content_58eb4f8464de54_68898819 (Smarty_Internal_Template $_smarty_tpl) {
?>
<html lang="en">
<head>
	<?php $_smarty_tpl->_subTemplateRender("file:common/page-header.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</head>

<body class="<?php if ($_smarty_tpl->tpl_vars['logined']->value == "true") {?>loggedin<?php }?> <?php echo $_smarty_tpl->tpl_vars['page']->value;?>
-page">


<div id="header">
<div id="header-bottom-left">
	<span class="hover pagename"><a href="./">信息</a></span>
	<ul class="tabmenu ">
		<li><a href="./v/message/compose" class="choice">傳送一個私人訊息</a></li>
		<li class="selected"><a href="./v/message/inbox" class="choice">收件匣</a></li>
		<li><a href="./v/message/sent" class="choice">发件箱</a></li>
</div>
<?php $_smarty_tpl->_subTemplateRender("file:common/header-bottom-right.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</div>
<div class="content">
	<div class="menuarea">
		<div class="spacer">
			<ul class="flat-list hover">
				<li><a href="./v/message/inbox/" class="choice">所有</a></li>
				<li class="selected"><span class="separator">|</span><a href="./v/message/unread/" class="choice">未讀</a></li>
				<li><span class="separator">|</span><a href="./v/message/messages/" class="choice">信息</a></li>
				<li><span class="separator">|</span><a href="./v/message/comments/" class="choice">留言回覆</a></li>
				<li><span class="separator">|</span><a href="./v/message/selfreply/" class="choice">貼文回覆</a></li>
				<li><span class="separator">|</span><a href="./v/message/mentions/" class="choice">用戶名被提及</a></li>
			</ul>
		</div>
	</div>

	<div class="spacer">
		<div id="siteTable" class="sitetable linklisting">

			<?php
$_from = $_smarty_tpl->smarty->ext->_foreach->init($_smarty_tpl, $_smarty_tpl->tpl_vars['things']->value, 'entry');
if ($_from !== null) {
foreach ($_from as $_smarty_tpl->tpl_vars['entry']->value) {
?>
				<?php $_smarty_tpl->_subTemplateRender("file:common/message.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, true);
?>

			<?php
}
}
$_smarty_tpl->smarty->ext->_foreach->restore($_smarty_tpl);
?>


		</div>
	</div>
</div>

<div id="footer"></div>
<?php $_smarty_tpl->_subTemplateRender("file:common/login-modal.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>


<?php $_smarty_tpl->_subTemplateRender("file:common/comment-reply-edit.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</body>


<?php echo '<script'; ?>
 type="text/javascript" src="./static/js/message-inbox.js?v=8"><?php echo '</script'; ?>
>
</html><?php }
}
