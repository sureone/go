<?php
/* Smarty version 3.1.30, created on 2017-04-22 05:58:29
  from "D:\go\ama\application\views\common\file-attach.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58fad4e542b092_40347613',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '92480ae619bca4be4858d7e757663330f94640b0' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\file-attach.tpl',
      1 => 1492833408,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58fad4e542b092_40347613 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->compiled->nocache_hash = '1756458fad4e5407e04_77023570';
?>

<?php echo '<script'; ?>
 id="tpl-file-attach" type="text/x-handlebars-template">
    
    <li class="attach-file" file_id="{{file_id}}">

        {{#if _image_file}}
            <a href="./uploads/{{file_name}}"><img width="160" src="./uploads/{{file_name}}"></a>
        {{/if}}
        <input type="text" name="attach-comment-{{file_id}}" value="" placeholder="附件说明">
    </li>
    
<?php echo '</script'; ?>
><?php }
}
