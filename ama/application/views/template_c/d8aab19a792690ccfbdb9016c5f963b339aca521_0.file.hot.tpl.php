<?php
/* Smarty version 3.1.30, created on 2017-03-31 02:23:01
  from "D:\work\go\ama\application\views\hot.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58dda1652cc3b5_73039947',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'd8aab19a792690ccfbdb9016c5f963b339aca521' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\hot.tpl',
      1 => 1490919774,
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
function content_58dda1652cc3b5_73039947 (Smarty_Internal_Template $_smarty_tpl) {
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
		<li class="selected"><a href="./v/hot" class="choice">熱門</a></li>
		<li><a href="./v/new" class="choice">最新</a></li>
		<li><a href="./v/rising" class="choice">好評上升中</a></li>
		<li><a href="./v/controversial" class="choice">具爭議的</a></li>
		<li><a href="./v/top" class="choice">頭等</a></li>
		<li><a href="./v/gilded" class="choice">精選</a></li>
		<li><a href="./v/wiki" class="choice">wiki</a></li>
		<li><a href="./v/ads" class="choice">宣傳過的</a></li></ul>
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
 type="text/javascript" src="./static/js/hot.js?v=8"><?php echo '</script'; ?>
>
</html><?php }
}
