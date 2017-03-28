<?php
/* Smarty version 3.1.30, created on 2017-03-28 02:43:29
  from "D:\go\ama\application\views\user-submitted.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58d9b1b1cc4782_73548779',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '6b5732f2df205d386e69acd1ad1f198460632586' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\user-submitted.tpl',
      1 => 1490661799,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
    'file:common/page-header.tpl' => 1,
    'file:common/header-bottom-right.tpl' => 1,
    'file:common/side.tpl' => 1,
    'file:common/thread.tpl' => 1,
    'file:common/login-modal.tpl' => 1,
  ),
),false)) {
function content_58d9b1b1cc4782_73548779 (Smarty_Internal_Template $_smarty_tpl) {
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
	<span class="hover pagename"><a href="./">AMA</a></span>
	<ul class="tabmenu ">
		<li><a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['userid']->value;?>
/home" class="choice">总览</a></li>
		<li><a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['userid']->value;?>
/replies" class="choice">留言</a></li>
		<li class="selected"><a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['userid']->value;?>
/submitted" class="choice">已发表</a></li>
		<li><a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['userid']->value;?>
/saved" class="choice">收藏</a></li>
		<li><a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['userid']->value;?>
/upvoted" class="choice">推</a></li>
		<li><a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['userid']->value;?>
/downvoted" class="choice">嘘</a></li></ul>
</div>
<?php $_smarty_tpl->_subTemplateRender("file:common/header-bottom-right.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</div>
<?php $_smarty_tpl->_subTemplateRender("file:common/side.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>


<div class="content">
	<div class="spacer">
		<div id="siteTable" class="sitetable linklisting">

			<?php
$_from = $_smarty_tpl->smarty->ext->_foreach->init($_smarty_tpl, $_smarty_tpl->tpl_vars['things']->value, 'entry');
if ($_from !== null) {
foreach ($_from as $_smarty_tpl->tpl_vars['entry']->value) {
?>
				<?php $_smarty_tpl->_subTemplateRender("file:common/thread.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, true);
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
