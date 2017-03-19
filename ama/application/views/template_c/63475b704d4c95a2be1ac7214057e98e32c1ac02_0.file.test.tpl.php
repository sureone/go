<?php
/* Smarty version 3.1.30, created on 2017-03-19 13:35:44
  from "D:\go\ama\application\views\test.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58ce7b201e9294_03690921',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '63475b704d4c95a2be1ac7214057e98e32c1ac02' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\test.tpl',
      1 => 1489926901,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58ce7b201e9294_03690921 (Smarty_Internal_Template $_smarty_tpl) {
?>
hello temple
<?php
$__section_t_0_saved = isset($_smarty_tpl->tpl_vars['__smarty_section_t']) ? $_smarty_tpl->tpl_vars['__smarty_section_t'] : false;
$__section_t_0_loop = (is_array(@$_loop=$_smarty_tpl->tpl_vars['testary']->value) ? count($_loop) : max(0, (int) $_loop));
$__section_t_0_total = $__section_t_0_loop;
$_smarty_tpl->tpl_vars['__smarty_section_t'] = new Smarty_Variable(array());
if ($__section_t_0_total != 0) {
for ($__section_t_0_iteration = 1, $_smarty_tpl->tpl_vars['__smarty_section_t']->value['index'] = 0; $__section_t_0_iteration <= $__section_t_0_total; $__section_t_0_iteration++, $_smarty_tpl->tpl_vars['__smarty_section_t']->value['index']++){
?>
name=<?php echo $_smarty_tpl->tpl_vars['testary']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_t']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_t']->value['index'] : null)]['name'];?>

old=<?php echo $_smarty_tpl->tpl_vars['testary']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_t']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_t']->value['index'] : null)]['old'];?>

<?php
}
}
if ($__section_t_0_saved) {
$_smarty_tpl->tpl_vars['__smarty_section_t'] = $__section_t_0_saved;
}
}
}
