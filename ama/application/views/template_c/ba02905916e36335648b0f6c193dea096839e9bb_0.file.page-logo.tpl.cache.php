<?php
/* Smarty version 3.1.30, created on 2017-04-19 02:01:23
  from "D:\go\ama\application\views\common\page-logo.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58f6a8d38ec422_71454798',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'ba02905916e36335648b0f6c193dea096839e9bb' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\page-logo.tpl',
      1 => 1492558661,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58f6a8d38ec422_71454798 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->compiled->nocache_hash = '2473558f6a8d3853e84_78349887';
?>
<span class="hover pagename"><a href="./"><?php if (isset($_smarty_tpl->tpl_vars['user_name']->value)) {
echo $_smarty_tpl->tpl_vars['user_name']->value;
} else { ?><img src="./static/images/logo.gif" alt="后园小亭" height="20"><?php }?></a></span><?php }
}
