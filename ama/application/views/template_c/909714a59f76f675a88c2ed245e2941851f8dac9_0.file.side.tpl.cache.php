<?php
/* Smarty version 3.1.30, created on 2017-05-03 06:34:15
  from "D:\work\go\ama\application\views\common\side.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_59095dc749d931_70931160',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '909714a59f76f675a88c2ed245e2941851f8dac9' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\side.tpl',
      1 => 1493708019,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_59095dc749d931_70931160 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->compiled->nocache_hash = '2318359095dc746eb39_34485585';
?>
<div class="side">
<!--     <div class="spacer">
        <form action="https://www.reddit.com/r/AMA/search" id="search" role="search">
        	<input type="text" name="q" placeholder="搜尋" tabindex="20">
        	<input type="submit" value="" tabindex="22">

            <div id="searchexpando" class="infobar" style="display: none;">
            	<label><input type="checkbox" name="restrict_sr" tabindex="21">搜尋範圍僅限 r/AMA</label>

                <div id="moresearchinfo" style=""><a href="#" id="search_hidemore">[-]</a>

                    <p>use the following search parameters to narrow your results:</p>
                    <dl>
                        <dt>subreddit:<i>subreddit</i></dt>
                        <dd>find submissions in "subreddit"</dd>
                        <dt>author:<i>username</i></dt>
                        <dd>依「使用者名稱」尋找發文</dd>
                        <dt>site:<i>example.com</i></dt>
                        <dd>find submissions from "example.com"</dd>
                        <dt>url:<i>text</i></dt>
                        <dd>search for "text" in url</dd>
                        <dt>selftext:<i>text</i></dt>
                        <dd>在自行發文的內容中搜尋「文字」</dd>
                        <dt>self:yes (or self:no)</dt>
                        <dd>包含 (或排除) 自己的文章</dd>
                        <dt>nsfw:yes (or nsfw:no)</dt>
                        <dd>納入 (或排除) 標記為不適合公開閱覽的結果</dd>
                    </dl>
                    <p>e.g. <code>subreddit:aww site:imgur.com dog</code></p>

                    <p><a href="https://www.reddit.com/wiki/search">see the search faq for details.</a></p></div>
                <p><a href="https://www.reddit.com/wiki/search" id="search_showmore">進階搜尋：依照作者、版面...</a></p></div>
        </form>
    </div> -->
    <?php if ($_smarty_tpl->tpl_vars['page']->value == "comments") {?>
        <?php if (isset($_smarty_tpl->tpl_vars['things']->value)) {?>
        <?php
$_from = $_smarty_tpl->smarty->ext->_foreach->init($_smarty_tpl, $_smarty_tpl->tpl_vars['things']->value, 'entry');
if ($_from !== null) {
foreach ($_from as $_smarty_tpl->tpl_vars['entry']->value) {
?>
        <div class="spacer">
            <div class="linkinfo">
                <div class="date"><a style="font-size:medium;padding-right:4px;" href="./v/user/<?php echo $_smarty_tpl->tpl_vars['entry']->value['author'];?>
"><?php echo $_smarty_tpl->tpl_vars['entry']->value['author_name'];?>
</a><span>发表于 </span>
                    <time datetime="<?php echo $_smarty_tpl->tpl_vars['entry']->value['timeago'];?>
"><?php echo $_smarty_tpl->tpl_vars['entry']->value['timeago'];?>
</time>
                </div>
                <div class="score"><span class="number"><?php echo $_smarty_tpl->tpl_vars['entry']->value['likes']-$_smarty_tpl->tpl_vars['entry']->value['dislikes'];?>
</span> <span class="word">指标</span> (其中<?php echo $_smarty_tpl->tpl_vars['entry']->value['likes'];?>
票赞成)</div>
                <div class="shortlink">本文链接: <input type="text" value="http://boopo.cn/v/a/<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
" readonly="readonly"
                                                         id="shortlink-text"></div>
            </div>
        </div>
        <?php
}
}
$_smarty_tpl->smarty->ext->_foreach->restore($_smarty_tpl);
?>
 
        <?php }?>  
        
    <?php }?>

    <?php if ($_smarty_tpl->tpl_vars['logined']->value == "true" && isset($_smarty_tpl->tpl_vars['pagedir']->value) && $_smarty_tpl->tpl_vars['pagedir']->value == "user") {?>
    <div class="spacer">
        <div class="sidebox submit submit-text">
            <div class="morelink">
                <a href="./v/message/compose/<?php echo $_smarty_tpl->tpl_vars['userid']->value;?>
" class="login-required access-required" target="_top">给<?php echo $_smarty_tpl->tpl_vars['username']->value;?>
发送私信</a>
                <div class="nub"></div>
            </div>
        </div>
    </div>
    <?php }?>

    <div class="spacer">
        <div class="sidebox submit submit-text">
            <div class="morelink">
            	<a href="./v/submit" class="login-required access-required" target="_top">发表新文章</a>
                <div class="nub"></div>
            </div>
        </div>
    </div>
</div><?php }
}
