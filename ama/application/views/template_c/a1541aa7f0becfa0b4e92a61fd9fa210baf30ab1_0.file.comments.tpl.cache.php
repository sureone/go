<?php
/* Smarty version 3.1.30, created on 2017-04-23 09:17:52
  from "D:\go\ama\application\views\comments.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58fc552060df44_04633198',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'a1541aa7f0becfa0b4e92a61fd9fa210baf30ab1' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\comments.tpl',
      1 => 1492931852,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
    'file:common/page-header.tpl' => 1,
    'file:common/comment-reply-edit.tpl' => 1,
    'file:common/page-logo.tpl' => 1,
    'file:common/header-bottom-right.tpl' => 1,
    'file:common/side.tpl' => 1,
    'file:common/thread.tpl' => 1,
    'file:common/comment.tpl' => 1,
    'file:common/login-modal.tpl' => 1,
    'file:common/file-attach.tpl' => 1,
  ),
),false)) {
function content_58fc552060df44_04633198 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->compiled->nocache_hash = '3046358fc55205b0335_09806205';
?>

    <?php $_smarty_tpl->_subTemplateRender("file:common/page-header.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</head>
<body class="<?php if ($_smarty_tpl->tpl_vars['logined']->value == "true") {?>loggedin<?php }?> <?php echo $_smarty_tpl->tpl_vars['page']->value;?>
-page">
<?php $_smarty_tpl->_subTemplateRender("file:common/comment-reply-edit.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>


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

    <?php echo '<script'; ?>
>
      var thingid = <?php echo $_smarty_tpl->tpl_vars['things']->value[0]['thingid'];?>
;
        var mainid =   <?php echo $_smarty_tpl->tpl_vars['things']->value[0]['thingid'];?>
;       
       
        var tpl = Handlebars.compile($("#tpl-comment-edit").html());
        h = (tpl({thingid:thingid,mainid:mainid}));

        document.write(h);
        
    <?php echo '</script'; ?>
>

    <div id="siteTable_<?php echo $_smarty_tpl->tpl_vars['thingid']->value;?>
" class="sitetable nestedlisting">
        <?php $_smarty_tpl->_subTemplateRender("file:common/comment.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

        <?php $_smarty_tpl->ext->_tplFunction->callTemplateFunction($_smarty_tpl, 'renderComments', array('data'=>$_smarty_tpl->tpl_vars['things']->value[0]['comments']), false);?>

    </div>

</div>
<iframe src="" style="display:none;" id="iframe_upload" name="iframe_upload"></iframe>
<div id="footer"></div>
<?php $_smarty_tpl->_subTemplateRender("file:common/login-modal.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>


</body>
<?php echo '<script'; ?>
 id="tpl-attach-tool" type="text/x-handlebars-template">
<div class="attach-tool" style="border:1px dotted gray;">
    <span class="title required-roundfield">附件</span>
    <ul id="attaches">   
    </ul>    
    <form action="http://127.0.0.1/ama/index.php/v/do_upload" enctype="multipart/form-data" method="post" accept-charset="utf-8" target="iframe_upload">
        <input type="file" name="userfile" size="20" />
        <input type="submit" value="upload" />
    </form>
</div>
<?php echo '</script'; ?>
>
<?php $_smarty_tpl->_subTemplateRender("file:common/file-attach.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

<?php echo '<script'; ?>
 type="text/javascript" src="./static/js/comments.js?v=8"><?php echo '</script'; ?>
>
</html><?php }
}
