<?php
/* Smarty version 3.1.30, created on 2017-04-17 10:31:34
  from "D:\work\go\ama\application\views\common\page-header.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58f47d6687f332_17638287',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '2cba0286b045df067f392a1b6c88c5e91e74ffac' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\page-header.tpl',
      1 => 1492416008,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58f47d6687f332_17638287 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->compiled->nocache_hash = '2443958f47d66877634_91859586';
?>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
	<meta charset="utf-8">
    <base href="http://127.0.0.1/ama/index.php">
    <?php if (isset($_smarty_tpl->tpl_vars['page_title']->value)) {?>
	<title><?php echo $_smarty_tpl->tpl_vars['page_title']->value;?>
</title>
    <?php } else { ?>
    <title>问吧 Ask Me Anything </title>
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
 src="./static/js/markdown.min.js?v=1"><?php echo '</script'; ?>
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
