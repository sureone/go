<?php
/* Smarty version 3.1.30, created on 2017-04-10 11:23:39
  from "D:\work\go\ama\application\views\header-bottom-left.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58eb4f1b0eae38_23863774',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '60fa3e11c1f4314b315f8259b36371c39276f334' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\header-bottom-left.tpl',
      1 => 1491816210,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58eb4f1b0eae38_23863774 (Smarty_Internal_Template $_smarty_tpl) {
?>
<div id="header-bottom-left">
    <span class="hover pagename"><a href="./">AMA</a></span>
    <ul class="tabmenu ">
        <li <?php if ($_smarty_tpl->tpl_vars['page']->value == "hot") {?>class="selected"<?php }?>><a href="./v/hot" class="choice">熱門</a></li>
        <li <?php if ($_smarty_tpl->tpl_vars['page']->value == "new") {?>class="selected"<?php }?>><a href="./v/news" class="choice">最新</a></li>
        <li <?php if ($_smarty_tpl->tpl_vars['page']->value == "rising") {?>class="selected"<?php }?>><a href="./v/rising" class="choice">好評上升中</a></li>
        <li <?php if ($_smarty_tpl->tpl_vars['page']->value == "controversial") {?>class="selected"<?php }?>><a href="./v/controversial" class="choice">具爭議的</a></li>
        <li <?php if ($_smarty_tpl->tpl_vars['page']->value == "top") {?>class="selected"<?php }?>><a href="./v/top" class="choice">頭等</a></li>
        <li <?php if ($_smarty_tpl->tpl_vars['page']->value == "controversial") {?>class="gilded"<?php }?>><a href="./v/gilded" class="choice">精選</a></li>
        <li <?php if ($_smarty_tpl->tpl_vars['page']->value == "controversial") {?>class="wiki"<?php }?>><a href="./v/wiki" class="choice">wiki</a></li>
        <li <?php if ($_smarty_tpl->tpl_vars['page']->value == "controversial") {?>class="ads"<?php }?>><a href="./v/ads" class="choice">宣傳過的</a></li>
    </ul>
</div><?php }
}
