<?php
/* Smarty version 3.1.30, created on 2017-04-22 06:42:35
  from "D:\go\ama\application\views\comments.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58fadf3bc65f60_41404297',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'a1541aa7f0becfa0b4e92a61fd9fa210baf30ab1' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\comments.tpl',
      1 => 1492836153,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
    'file:common/page-header.tpl' => 1,
    'file:common/page-logo.tpl' => 1,
    'file:common/header-bottom-right.tpl' => 1,
    'file:common/side.tpl' => 1,
    'file:common/thread.tpl' => 1,
    'file:common/thing-attach.tpl' => 1,
    'file:common/markhelp.tpl' => 1,
    'file:common/comment.tpl' => 1,
    'file:common/login-modal.tpl' => 1,
    'file:common/comment-reply-edit.tpl' => 1,
  ),
),false)) {
function content_58fadf3bc65f60_41404297 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->compiled->nocache_hash = '1489158fadf3bbf8943_24041823';
?>

    <?php $_smarty_tpl->_subTemplateRender("file:common/page-header.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</head>
<body class="<?php if ($_smarty_tpl->tpl_vars['logined']->value == "true") {?>loggedin<?php }?> <?php echo $_smarty_tpl->tpl_vars['page']->value;?>
-page">


<div id="header">
    <div id="header-bottom-left">
        <?php $_smarty_tpl->_subTemplateRender("file:common/page-logo.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

        
        <ul class="tabmenu ">
            <li class="selected"><a href="./v/a/<?php echo $_smarty_tpl->tpl_vars['things']->value[0]['thingid'];?>
" class="choice">留言</a></li>
        </ul>
    </div>
    <?php $_smarty_tpl->_subTemplateRender("file:common/header-bottom-right.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</div>
<?php $_smarty_tpl->_subTemplateRender("file:common/side.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>


<div class="content">

    <div id="siteTable" class="sitetable linklisting">
        <?php
$_from = $_smarty_tpl->smarty->ext->_foreach->init($_smarty_tpl, $_smarty_tpl->tpl_vars['things']->value, 'entry');
if ($_from !== null) {
foreach ($_from as $_smarty_tpl->tpl_vars['entry']->value) {
?>
            <?php $_smarty_tpl->_subTemplateRender("file:common/thread.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, true);
?>

        <?php
}
}
$_smarty_tpl->smarty->ext->_foreach->restore($_smarty_tpl);
?>
   

        <?php
$_from = $_smarty_tpl->smarty->ext->_foreach->init($_smarty_tpl, $_smarty_tpl->tpl_vars['things']->value[0]['attaches'], 'entry');
if ($_from !== null) {
foreach ($_from as $_smarty_tpl->tpl_vars['entry']->value) {
?>
                <?php $_smarty_tpl->_subTemplateRender("file:common/thing-attach.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, true);
?>

        <?php
}
}
$_smarty_tpl->smarty->ext->_foreach->restore($_smarty_tpl);
?>
 
    </div>
    <div class="commentarea">
        <div class="panestack-title"><span class="title">头<?php echo $_smarty_tpl->tpl_vars['things']->value[0]['comments_count'];?>
则留言</span><a
                href="/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?limit=500"
                class="title-button ">显示所有<?php echo $_smarty_tpl->tpl_vars['things']->value[0]['replies'];?>
</a></div>
        <div class="menuarea">
            <div class="spacer"><span class="dropdown-title lightdrop">排序依据: </span>

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

    <form action="./api" class="usertext cloneable warn-on-unload" onsubmit="handleFormSubmit(this);return false;" id="form-comment-<?php echo $_smarty_tpl->tpl_vars['thingid']->value;?>
">
        <input type="hidden" name="action" value="submit-new-comment">
        <input type="hidden" name="main" value="<?php echo $_smarty_tpl->tpl_vars['thingid']->value;?>
">
        <input type="hidden" name="parent" value="<?php echo $_smarty_tpl->tpl_vars['thingid']->value;?>
">
        <div class="usertext-edit md-container" style="">
            <div class="md">
                <textarea rows="1" cols="1" name="content" class="" data-event-action="comment" data-type="link"></textarea>
            </div>
            <div class="bottom-area">
                <span class="help-toggle toggle" style="">
                    <a class="option active " href="#" tabindex="100" onclick="return toggle(this, helpon, helpoff)">格式說明</a>
                    <a class="option " href="#">隱藏說明</a>
                </span>
                <a href="/help/contentpolicy" class="reddiquette" target="_blank" tabindex="100">內容政策</a>
                <span class="error TOO_LONG field-text" style="display:none"></span>
                <span
                    class="error RATELIMIT field-ratelimit" style="display:none">
                </span>
                <span class="error NO_TEXT field-text" style="display:none"></span>
                <span class="error TOO_OLD field-parent" style="display:none"></span>
                <span class="error THREAD_LOCKED field-parent" style="display:none"></span>
                <span class="error DELETED_COMMENT field-parent" style="display:none"></span>
                <span class="error USER_BLOCKED field-parent" style="display:none"></span>
                <span class="error USER_MUTED field-parent" style="display:none"></span>
                <span class="error MUTED_FROM_SUBREDDIT field-parent" style="display:none"></span>

                <div class="usertext-buttons">
                    <button type="submit" onclick="" class="save">保存</button>
                    <button type="button" onclick="return cancel_usertext(this);" class="cancel" style="display:none;">取消</button>
                </div>
            </div>
            <?php $_smarty_tpl->_subTemplateRender("file:common/markhelp.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

        </div>
    </form>

    <div id="siteTable_<?php echo $_smarty_tpl->tpl_vars['thingid']->value;?>
" class="sitetable nestedlisting">
        <?php $_smarty_tpl->_subTemplateRender("file:common/comment.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

        <?php $_smarty_tpl->ext->_tplFunction->callTemplateFunction($_smarty_tpl, 'renderComments', array('data'=>$_smarty_tpl->tpl_vars['things']->value[0]['comments']), false);?>

    </div>

</div>

<div id="footer"></div>
<?php $_smarty_tpl->_subTemplateRender("file:common/login-modal.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

<?php $_smarty_tpl->_subTemplateRender("file:common/comment-reply-edit.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</body>


<?php echo '<script'; ?>
 type="text/javascript" src="./static/js/comments.js?v=8"><?php echo '</script'; ?>
>
</html><?php }
}
