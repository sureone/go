<?php
/* Smarty version 3.1.30, created on 2017-04-02 09:13:40
  from "D:\go\ama\application\views\user-header-bottom-left.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58e0a4a4198fc1_45739064',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '217cbffcf18965f83bef902ef96780591ab3a14f' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\user-header-bottom-left.tpl',
      1 => 1491117155,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58e0a4a4198fc1_45739064 (Smarty_Internal_Template $_smarty_tpl) {
?>
<div id="header-bottom-left">
    <span class="hover pagename"><a href="./">AMA</a></span>
    <ul class="tabmenu ">
        <li <?php if ($_smarty_tpl->tpl_vars['page']->value == "user-home") {?>class="selected"<?php }?>><a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['userid']->value;?>
/home" class="choice">总览</a></li>
        <li <?php if ($_smarty_tpl->tpl_vars['page']->value == "user-replies") {?>class="selected"<?php }?>><a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['userid']->value;?>
/replies" class="choice">留言</a></li>
        <li <?php if ($_smarty_tpl->tpl_vars['page']->value == "user-submitted") {?>class="selected"<?php }?>><a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['userid']->value;?>
/submitted" class="choice">已发表</a></li>
        <li <?php if ($_smarty_tpl->tpl_vars['page']->value == "user-saved") {?>class="selected"<?php }?>><a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['userid']->value;?>
/saved" class="choice">收藏</a></li>
        <li <?php if ($_smarty_tpl->tpl_vars['page']->value == "user-upvoted") {?>class="selected"<?php }?>><a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['userid']->value;?>
/upvoted" class="choice">推</a></li>
        <li <?php if ($_smarty_tpl->tpl_vars['page']->value == "user-downvoted") {?>class="selected"<?php }?>><a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['userid']->value;?>
/downvoted" class="choice">嘘</a></li>
    </ul>
</div><?php }
}
