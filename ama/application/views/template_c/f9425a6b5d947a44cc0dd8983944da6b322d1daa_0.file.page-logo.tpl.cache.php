<?php
/* Smarty version 3.1.30, created on 2017-04-18 08:57:09
  from "D:\work\go\ama\application\views\common\page-logo.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58f5b8c53ec2b3_35548153',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'f9425a6b5d947a44cc0dd8983944da6b322d1daa' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\page-logo.tpl',
      1 => 1492498622,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58f5b8c53ec2b3_35548153 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->compiled->nocache_hash = '264858f5b8c53e8430_84329545';
?>
<span class="hover pagename"><a href="./"><?php if (isset($_smarty_tpl->tpl_vars['user_name']->value)) {
echo $_smarty_tpl->tpl_vars['user_name']->value;
} else { ?><img src="./static/images/logo.gif" alt="后园小亭" height="20"><?php }?></a></span><?php }
}
