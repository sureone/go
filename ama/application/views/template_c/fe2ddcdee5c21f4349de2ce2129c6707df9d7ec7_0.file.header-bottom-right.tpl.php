<?php
/* Smarty version 3.1.30, created on 2017-03-28 14:55:25
  from "D:\go\ama\application\views\common\header-bottom-right.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58da5d3d236786_39325098',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'fe2ddcdee5c21f4349de2ce2129c6707df9d7ec7' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\header-bottom-right.tpl',
      1 => 1490705717,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58da5d3d236786_39325098 (Smarty_Internal_Template $_smarty_tpl) {
?>
<div id="header-bottom-right">
<?php if ($_smarty_tpl->tpl_vars['logined']->value == "true") {?>
<span class="user"><a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['user']->value['userid'];?>
/"><?php echo $_smarty_tpl->tpl_vars['user']->value['userid'];?>
</a>&nbsp;(<span class="userkarma" title="post karma">1</span>)</span><span class="separator">|</span><a title="沒有新郵件" href="./v/message/inbox/" class="nohavemail" id="mail">信息</a><span class="separator">|</span><ul class="flat-list hover"><li><a href="https://www.reddit.com/prefs/" class="pref-lang choice">偏好設定</a></li></ul><span class="separator">|</span><form method="post" id="logout-form" action="./api" class="logout hover"><input type="hidden" name="uh" value="cqaeyi63ta407191a49905d7c0b26e7c2a75cb1b81ddd77995"><input type="hidden" name="top" value="off"><input type="hidden" name="action" value="logout"><a href="javascript:void(0)" onclick="$(this).parent().submit()">登出</a></form>
<?php } else { ?>
<span class="user">想要加入? <a href="" class="login-required">登入或註冊</a> 一秒以内.</span><span class="separator">|</span><ul class="flat-list hover"><li><a href="javascript:void(0)" class="pref-lang choice" onclick="return showlang();">中文</a></li></ul>
<?php }?>
</div><?php }
}
