<?php
/* Smarty version 3.1.30, created on 2017-04-16 16:24:13
  from "D:\go\ama\application\views\common\page-header.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58f37e8ddebe11_35653682',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '9add0006f06d4c3a3cdd978f02ffd7fb276e3103' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\page-header.tpl',
      1 => 1492352526,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58f37e8ddebe11_35653682 (Smarty_Internal_Template $_smarty_tpl) {
?>
    <base href="http://127.0.0.1/ama/index.php">
	<meta charset="utf-8">
    <?php if (isset($_smarty_tpl->tpl_vars['page_title']->value)) {?>
	<title><?php echo $_smarty_tpl->tpl_vars['page_title']->value;?>
</title>
    <?php } else { ?>
    <title>问我任何事 Ask Me Anything </title>
    <?php }?>
	<style type="text/css">
	</style>
	<link rel="stylesheet" href="./static/css/common.css?v=2" type="text/css" />
    <?php echo '<script'; ?>
 type="text/javascript" src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.min.js"><?php echo '</script'; ?>
>
    <?php echo '<script'; ?>
 type="text/javascript" src="http://apps.bdimg.com/libs/handlebars.js/1.3.0/handlebars.min.js"><?php echo '</script'; ?>
>
    <?php echo '<script'; ?>
 src="./static/js/form2json.js"><?php echo '</script'; ?>
>
    <?php echo '<script'; ?>
 src="./static/js/common.js?v=7"><?php echo '</script'; ?>
>
    <?php echo '<script'; ?>
 src="./static/js/jquery.timeago.js?v=1"><?php echo '</script'; ?>
>
    <?php echo '<script'; ?>
 src="./static/js/markdown.js?v=1"><?php echo '</script'; ?>
>
    <?php echo '<script'; ?>
 type="text/javascript" src="./static/js/ama.js?v=8"><?php echo '</script'; ?>
>

    <?php echo '<script'; ?>
 type="text/javascript">
    	var g_logined = <?php echo $_smarty_tpl->tpl_vars['logined']->value;?>
;
    <?php echo '</script'; ?>
><?php }
}
