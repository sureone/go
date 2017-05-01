<?php
/* Smarty version 3.1.30, created on 2017-04-23 09:09:26
  from "D:\go\ama\application\views\common\file-attach.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58fc5326a38a68_67955034',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '92480ae619bca4be4858d7e757663330f94640b0' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\file-attach.tpl',
      1 => 1492931325,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58fc5326a38a68_67955034 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->compiled->nocache_hash = '1180658fc5326a34be5_93472219';
?>

<?php echo '<script'; ?>
 id="tpl-file-attach" type="text/x-handlebars-template">
    
    <li class="attach-file new" file_id="{{file_id}}">
        {{#if _image_file}}
        	<a href="./uploads/{{file_name}}"><img width="160" src="./uploads/{{file_name}}"></a>
        {{/if}}
        <a href="javascript:removeNewAttach({{file_id}})">删除附件</a>
        <a href="javascript:changeAttachOrder({{file_id}},-1)">向上</a>
        <a href="javascript:changeAttachOrder({{file_id}},1)">向下</a>
        
        <input type="text" name="attach-comment-{{file_id}}" value="" placeholder="附件说明({{file_name}})">
    </li>
    
<?php echo '</script'; ?>
><?php }
}
