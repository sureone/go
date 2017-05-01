<?php
/* Smarty version 3.1.30, created on 2017-04-23 07:31:15
  from "D:\go\ama\application\views\common\thing-attach.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58fc3c235acbb8_47449842',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'a39d0e36eaffb48329459f750f9953ef1ff45a6f' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\thing-attach.tpl',
      1 => 1492925473,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58fc3c235acbb8_47449842 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->compiled->nocache_hash = '718058fc3c235760a7_34681189';
?>
<div>
	
	<?php if ($_smarty_tpl->tpl_vars['attach']->value['image_width'] != '0') {?>
	
	<a href="./uploads/<?php echo $_smarty_tpl->tpl_vars['attach']->value['file_name'];?>
">
		<img src="./uploads/<?php echo $_smarty_tpl->tpl_vars['attach']->value['file_name'];?>
" style="max-width:700px;max-height:240px;"></a>
	<br/>
	<?php }?>
	<span class="attachment_order">附件<?php echo $_smarty_tpl->tpl_vars['attach']->value['file_no'];?>
:</span>&nbsp;<a href="./uploads/<?php echo $_smarty_tpl->tpl_vars['attach']->value['file_name'];?>
"><?php if ($_smarty_tpl->tpl_vars['attach']->value['file_comment'] != '') {
echo $_smarty_tpl->tpl_vars['attach']->value['file_comment'];
} else {
echo $_smarty_tpl->tpl_vars['attach']->value['file_name'];
}?></a>
</div><?php }
}
