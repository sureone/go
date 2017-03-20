<?php
/* Smarty version 3.1.30, created on 2017-03-19 14:37:50
  from "D:\go\ama\application\views\comments.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58ce89ae871953_29016644',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'a1541aa7f0becfa0b4e92a61fd9fa210baf30ab1' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\comments.tpl',
      1 => 1489930662,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
    'file:common/page-header.tpl' => 1,
    'file:common/header-bottom-right.tpl' => 1,
    'file:common/side.tpl' => 1,
    'file:common/thread.tpl' => 1,
    'file:common/markhelp.tpl' => 1,
    'file:common/login-modal.tpl' => 1,
  ),
),false)) {
function content_58ce89ae871953_29016644 (Smarty_Internal_Template $_smarty_tpl) {
?>

<html lang="en">
<head>
    <?php $_smarty_tpl->_subTemplateRender("file:common/page-header.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</head>
<body class="<?php if ($_smarty_tpl->tpl_vars['logined']->value == "true") {?>loggedin<?php }?> comments-page">


<div id="header">
    <div id="header-bottom-left">
        <span class="hover pagename"><a href="./">AMA</a></span>
        <ul class="tabmenu ">
            <li class="selected"><a href="./v/comments/<?php echo '<?=';?> $thingid <?php echo '?>';?>" class="choice">留言</a></li>
        </ul>
    </div>
    <?php $_smarty_tpl->_subTemplateRender("file:common/header-bottom-right.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</div>
<?php $_smarty_tpl->_subTemplateRender("file:common/side.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>


<div class="content">

    <div id="siteTable" class="sitetable linklisting">
        <?php
$__section_a_0_saved = isset($_smarty_tpl->tpl_vars['__smarty_section_a']) ? $_smarty_tpl->tpl_vars['__smarty_section_a'] : false;
$__section_a_0_loop = (is_array(@$_loop=$_smarty_tpl->tpl_vars['things']->value) ? count($_loop) : max(0, (int) $_loop));
$__section_a_0_total = $__section_a_0_loop;
$_smarty_tpl->tpl_vars['__smarty_section_a'] = new Smarty_Variable(array());
if ($__section_a_0_total != 0) {
for ($__section_a_0_iteration = 1, $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] = 0; $__section_a_0_iteration <= $__section_a_0_total; $__section_a_0_iteration++, $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']++){
?>
            <?php $_smarty_tpl->_subTemplateRender("file:common/thread.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, true);
?>

        <?php
}
}
if ($__section_a_0_saved) {
$_smarty_tpl->tpl_vars['__smarty_section_a'] = $__section_a_0_saved;
}
?>    
    </div>
    <div class="commentarea">
        <div class="panestack-title"><span class="title">頭 200 則留言</span><a
                href="/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?limit=500"
                class="title-button ">顯示所有387</a></div>
        <div class="menuarea">
            <div class="spacer"><span class="dropdown-title lightdrop">排序依據: </span>

                <div class="dropdown lightdrop" onclick="open_menu(this)"><span class="selected">最佳</span></div>
                <div class="drop-choices lightdrop"><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=top"
                        class="choice">頭等</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=new"
                        class="choice">最新</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=controversial"
                        class="choice">具爭議的</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=old"
                        class="choice">最舊</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=random"
                        class="hidden choice">隨機</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=qa"
                        class="choice">問與答</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=live"
                        class="hidden choice">live (beta)</a></div>
            </div>
            <div class="spacer"></div>
        </div>
    </div>

    <form action="#" class="usertext cloneable warn-on-unload" onsubmit="return post_form(this, 'comment')" id="form-comment-<?php echo $_smarty_tpl->tpl_vars['thingid']->value;?>
">
        <input type="hidden" name="thingid" value="<?php echo $_smarty_tpl->tpl_vars['thingid']->value;?>
">
        <div class="usertext-edit md-container" style="">
            <div class="md">
                <textarea rows="1" cols="1" name="text" class="" data-event-action="comment" data-type="link"></textarea>
            </div>
            <?php $_smarty_tpl->_subTemplateRender("file:common/markhelp.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

        </div>
    </form>

    <div id="siteTable_<?php echo $_smarty_tpl->tpl_vars['thingid']->value;?>
" class="sitetable nestedlisting">
        
    </div>

</div>

<div id="footer"></div>
<?php $_smarty_tpl->_subTemplateRender("file:common/login-modal.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</body>


<?php echo '<script'; ?>
 type="text/javascript" src="./static/js/comments.js?v=8"><?php echo '</script'; ?>
>
</html><?php }
}
