<?php
/* Smarty version 3.1.30, created on 2017-04-22 06:42:35
  from "D:\go\ama\application\views\common\thing-attach.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58fadf3bc8d076_79697519',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'a39d0e36eaffb48329459f750f9953ef1ff45a6f' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\thing-attach.tpl',
      1 => 1492835903,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58fadf3bc8d076_79697519 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->compiled->nocache_hash = '372058fadf3bc814e7_85332373';
?>
<div>
<a href="./uploads/<?php echo $_smarty_tpl->tpl_vars['entry']->value['file_name'];?>
"><?php if ($_smarty_tpl->tpl_vars['entry']->value['file_comment'] != '') {
echo $_smarty_tpl->tpl_vars['entry']->value['file_comment'];
} else {
echo $_smarty_tpl->tpl_vars['entry']->value['file_name'];
}?></a>
<?php if ($_smarty_tpl->tpl_vars['entry']->value['image_width'] != '0') {?>
<img src="./uploads/<?php echo $_smarty_tpl->tpl_vars['entry']->value['file_name'];?>
">
<?php }?>
</div><?php }
}
