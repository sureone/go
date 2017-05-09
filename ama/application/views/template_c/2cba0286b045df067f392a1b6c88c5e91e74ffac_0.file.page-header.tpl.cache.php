<?php
/* Smarty version 3.1.30, created on 2017-05-08 11:42:05
  from "D:\work\go\ama\application\views\common\page-header.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_59103d6d21a9c5_96939839',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '2cba0286b045df067f392a1b6c88c5e91e74ffac' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\page-header.tpl',
      1 => 1494233935,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_59103d6d21a9c5_96939839 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->compiled->nocache_hash = '2804059103d6d18a120_83751391';
?>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
	<meta charset="utf-8">
    <base href="http://127.0.0.1/ama/index.php">
    <?php if (isset($_smarty_tpl->tpl_vars['page_title']->value)) {?>
	<title><?php echo $_smarty_tpl->tpl_vars['page_title']->value;?>
</title>
    <?php } else { ?>
    <title>波普网络</title>
    <?php }?>
    <link rel="stylesheet" href="./static/css/common.css?v=2" type="text/css" />
    <?php echo '<script'; ?>
 type="text/javascript" src="https://apps.bdimg.com/libs/jquery/1.9.1/jquery.min.js"><?php echo '</script'; ?>
>
    <?php echo '<script'; ?>
 type="text/javascript" src="https://apps.bdimg.com/libs/handlebars.js/1.3.0/handlebars.min.js"><?php echo '</script'; ?>
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
 src="./static/js/markdown.js?v=3"><?php echo '</script'; ?>
>
    <?php echo '<script'; ?>
 type="text/javascript" src="./static/js/ama.js?v=9"><?php echo '</script'; ?>
>

    <?php echo '<script'; ?>
 type="text/javascript">
    	var g_logined = <?php echo $_smarty_tpl->tpl_vars['logined']->value;?>
;
    <?php echo '</script'; ?>
>
    <?php if ($_smarty_tpl->tpl_vars['page']->value == "hot" || $_smarty_tpl->tpl_vars['page']->value == "new") {?>
    <style type="text/css">
        .link .score {
            text-align: center;
            color: #ec4a36;
            background: #dbd0b6;
            border: 1px solid #eddeaa;
            font-size: small;
            padding-left: 2px;
            padding-right: 2px;
            font-weight: normal;
            margin-right: 4px;
        }
        .thing{
            display: inline-block;
            margin: 0 0px 0px 0; 
            padding: 2px;
            min-width:240px;
        }

        .listing-page .linklisting .thing {
            position: relative;
            margin: 0 0px 0px 0;
        }
        .link .title {
          
         
            text-overflow: ellipsis;
            overflow: hidden;
            white-space: nowrap;
            max-width: 700px;
        }

        <?php if (isset($_smarty_tpl->tpl_vars['ismobile']->value)) {?>
        .side{
            display: none;
        }
        <?php }?>
    </style>
    <?php }?>
      
	<style>
	  <?php if (isset($_smarty_tpl->tpl_vars['ismobile']->value)) {?>
        #header,.side{
            display: none;
        }
		.formtabs-content {
			width: 100%;
			border-top: 4px solid #5f99cf;
			padding-top: 10px;
		}
        <?php }?>
	</style>
<?php }
}
