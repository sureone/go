<?php
/* Smarty version 3.1.30, created on 2017-03-27 05:53:35
  from "D:\work\go\ama\application\views\common\page-header.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58d88cbf3c0210_78752644',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '2cba0286b045df067f392a1b6c88c5e91e74ffac' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\page-header.tpl',
      1 => 1490586688,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58d88cbf3c0210_78752644 (Smarty_Internal_Template $_smarty_tpl) {
?>
    <base href="http://127.0.0.1/ama/index.php">
	<meta charset="utf-8">
	<title>Ask Me Anything</title>
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
 src="./static/js/common.js?v=6"><?php echo '</script'; ?>
>
    <?php echo '<script'; ?>
 src="./static/js/jquery.timeago.js?v=1"><?php echo '</script'; ?>
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