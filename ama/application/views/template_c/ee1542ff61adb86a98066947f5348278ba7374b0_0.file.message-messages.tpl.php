<?php
/* Smarty version 3.1.30, created on 2017-03-31 15:57:31
  from "D:\go\ama\application\views\message-messages.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58de604bd5a769_75239481',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'ee1542ff61adb86a98066947f5348278ba7374b0' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\message-messages.tpl',
      1 => 1490966687,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
    'file:common/page-header.tpl' => 1,
    'file:common/header-bottom-right.tpl' => 1,
    'file:common/message.tpl' => 1,
    'file:common/login-modal.tpl' => 1,
  ),
),false)) {
function content_58de604bd5a769_75239481 (Smarty_Internal_Template $_smarty_tpl) {
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
                <li><span class="separator">|</span><a href="./v/message/unread/" class="choice">未讀</a></li>
                <li class="selected"><span class="separator">|</span><a href="./v/message/messages/" class="choice">信息</a></li>
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

</body>


<?php echo '<script'; ?>
 type="text/javascript" src="./static/js/user-home.js?v=8"><?php echo '</script'; ?>
>
</html><?php }
}
