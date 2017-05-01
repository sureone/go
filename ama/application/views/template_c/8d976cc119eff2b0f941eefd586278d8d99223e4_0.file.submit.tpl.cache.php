<?php
/* Smarty version 3.1.30, created on 2017-04-23 07:47:07
  from "D:\go\ama\application\views\submit.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58fc3fdbcc3190_17079592',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '8d976cc119eff2b0f941eefd586278d8d99223e4' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\submit.tpl',
      1 => 1492926293,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
    'file:common/page-header.tpl' => 1,
    'file:common/page-logo.tpl' => 1,
    'file:common/header-bottom-right.tpl' => 1,
    'file:common/side.tpl' => 1,
    'file:common/markhelp.tpl' => 1,
    'file:common/login-modal.tpl' => 1,
    'file:common/file-attach.tpl' => 1,
  ),
),false)) {
function content_58fc3fdbcc3190_17079592 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->compiled->nocache_hash = '1731558fc3fdbc3a5f7_24425891';
?>

    <?php $_smarty_tpl->_subTemplateRender("file:common/page-header.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>



    <?php echo '<script'; ?>
 type="text/javascript" src="./static/js/submit.js?v=8"><?php echo '</script'; ?>
>

</head>

<body class="listing-page <?php if ($_smarty_tpl->tpl_vars['logined']->value == "true") {?>loggedin<?php }?> <?php echo $_smarty_tpl->tpl_vars['page']->value;?>
-page">


<div id="header">
    <div id="header-bottom-left">
        <?php $_smarty_tpl->_subTemplateRender("file:common/page-logo.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

        <ul class="tabmenu ">
            <li class="selected"><a href="./v/submit" class="choice"><?php if (isset($_smarty_tpl->tpl_vars['thing']->value)) {?>编辑<?php } else { ?>发表<?php }?></a></li>
    </div>
    <?php $_smarty_tpl->_subTemplateRender("file:common/header-bottom-right.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</div>
<?php $_smarty_tpl->_subTemplateRender("file:common/side.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>


<div class="content">

        
        <div class="formtabs-content">
        <form class="submit content warn-on-unload" action="./api" id="newlink" method="post">
            <input name="action" value="submit-new-link" type="hidden">
            <div class="spacer">
                <div id="text-desc" class="infobar">
                    你正要发表以文字为主的文章，请畅所欲言。发文时必须注明标题，但不一定要在文字栏中长篇大论。使用「如果你...请帮我加分」作为主题，是违反后园法规的。
                </div>
            </div>
            <div class="spacer">
                <div class="roundfield " id="title-field">
                    <span class="title required-roundfield">标题</span>
                    <div class="roundfield-content">
                        <textarea class="thing-title key-monitor" name="title" rows="1" required=""><?php if (isset($_smarty_tpl->tpl_vars['thing']->value)) {
echo $_smarty_tpl->tpl_vars['thing']->value['title'];
}?></textarea>
                        <div class="error NO_TEXT field-title" style="display:none"></div>
                        <div class="error TOO_LONG field-title" style="display:none"></div>
                    </div>
                </div>
            </div>
            <div class="spacer">
                <div class="roundfield " id="text-field">
                    <span class="title ">文字</span> 
                    <span class="little gray roundfield-description">(非必填项目)</span>
                    <div class="roundfield-content"><input name="kind" value="self" type="hidden">

                        <div class="usertext">
                            <input type="hidden" name="thing_id" value="">
                            <div class="usertext-edit md-container" style="">
                                <div class="md">
                                    <textarea rows="1" cols="1" name="content" style="height:120px;" class="thing-title key-monitor" ><?php if (isset($_smarty_tpl->tpl_vars['thing']->value)) {
echo $_smarty_tpl->tpl_vars['thing']->value['text'];
}?></textarea>
                                </div>

                                <div class="bottom-area">

                                    <span class="help-toggle toggle" style="">
                                        <a class="option active " href="#" tabindex="100" onclick="return toggle(this, helpon, helpoff)">格式說明</a>
                                        <a class="option " href="#">隱藏說明</a>
                                    </span>
                                    <a class="reddiquette" target="_blank" tabindex="100">內容政策</a>
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
                                </div>
                                
                                <?php $_smarty_tpl->_subTemplateRender("file:common/markhelp.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

                            </div>
                        </div>
                        <span class="error NO_SELFS field-sr" style="display:none"></span>
                    </div>
                </div>
            </div>

            
            <!-- <div class="spacer">
                <div class="roundfield "><span class="title ">選項</span>

                    <div class="roundfield-content">
                        <input class="nomargin" type="checkbox" checked="checked" name="sendreplies" id="sendreplies" data-send-checked="true">
                        <label for="sendreplies">將回覆寄到我的收件匣</label></div>
                </div>
            </div> -->

       <!--  <div class="roundfield info-notice">please be mindful of reddit's 
            <a href="https://www.reddit.com/help/contentpolicy" target="_blank">內容政策</a>
             and practice 
            <a href="https://www.reddit.com/wiki/reddiquette" target="_blank">良好的 reddit 站規</a>.
        </div> -->
        <!-- <div id="items-required">*required</div> -->
        <input name="resubmit" value="" type="hidden">
        <input name="thingid" value="<?php if (isset($_smarty_tpl->tpl_vars['thing']->value)) {
echo $_smarty_tpl->tpl_vars['thing']->value['thingid'];
} else { ?>0<?php }?>" type="hidden">
    </form>
    
       
    
    <div class="spacer">
        <div class="roundfield " id="title-field">
            <span class="title required-roundfield">附件</span>

            <iframe src="" style="display:none;" id="iframe_upload" name="iframe_upload"></iframe>
            <ul id="attaches">
                <?php if (isset($_smarty_tpl->tpl_vars['thing']->value)) {?>
                <?php
$_from = $_smarty_tpl->smarty->ext->_foreach->init($_smarty_tpl, $_smarty_tpl->tpl_vars['thing']->value['attaches'], 'attach');
if ($_from !== null) {
foreach ($_from as $_smarty_tpl->tpl_vars['attach']->value) {
?>
                 <li class="attach-file old" file_id="<?php echo $_smarty_tpl->tpl_vars['attach']->value['id'];?>
">
                    <?php if ($_smarty_tpl->tpl_vars['attach']->value['image_width'] != 0) {?>
                        <a href="./uploads/<?php echo $_smarty_tpl->tpl_vars['attach']->value['file_name'];?>
"><img width="160" src="./uploads/<?php echo $_smarty_tpl->tpl_vars['attach']->value['file_name'];?>
"></a>
                    <?php }?>
                    <a href="javascript:removeOldAttach(<?php echo $_smarty_tpl->tpl_vars['thing']->value['thingid'];?>
,<?php echo $_smarty_tpl->tpl_vars['attach']->value['id'];?>
)">删除附件</a>
                    <a href="javascript:changeAttachOrder(<?php echo $_smarty_tpl->tpl_vars['attach']->value['id'];?>
,-1)">向上</a>
                    <a href="javascript:changeAttachOrder(<?php echo $_smarty_tpl->tpl_vars['attach']->value['id'];?>
,1)">向下</a>
                    <input type="text" name="attach-comment-<?php echo $_smarty_tpl->tpl_vars['attach']->value['id'];?>
" value="<?php echo $_smarty_tpl->tpl_vars['attach']->value['file_comment'];?>
" placeholder="附件说明(<?php echo $_smarty_tpl->tpl_vars['attach']->value['file_name'];?>
)">
                </li>
                <?php
}
}
$_smarty_tpl->smarty->ext->_foreach->restore($_smarty_tpl);
?>

                <?php }?>

            </ul>    

            <form action="http://127.0.0.1/ama/index.php/v/do_upload" enctype="multipart/form-data" method="post" accept-charset="utf-8" target="iframe_upload">
                <input type="file" name="userfile" size="20" />
                <input type="submit" value="upload" />
            </form>
           
        </div>
    </div>    
    <div class="spacer">
        <button class="btn" name="submit" value="" type="button"
         onclick="javascript:handleFormSubmit(document.getElementById('newlink'));">送出</button>
        <span class="status"></span>
        <span class="error RATELIMIT field-ratelimit" style="display:none"></span>
        <span class="error INVALID_OPTION field-sr" style="display:none"></span>
        <span class="error IN_TIMEOUT field-sr" style="display:none"></span>
    </div>

</div>

<div id="footer"></div>
<?php $_smarty_tpl->_subTemplateRender("file:common/login-modal.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

<?php $_smarty_tpl->_subTemplateRender("file:common/file-attach.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 9999, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</body>




</html><?php }
}
