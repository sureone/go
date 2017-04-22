<?php
/* Smarty version 3.1.30, created on 2017-04-19 02:01:39
  from "D:\go\ama\application\views\common\comment.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58f6a8e377cb81_24105587',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'd03b8025f8ee3e41174be16b12370503e817c2fd' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\comment.tpl',
      1 => 1492331288,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58f6a8e377cb81_24105587 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->ext->_tplFunction->registerTplFunctions($_smarty_tpl, array (
  'renderComments' => 
  array (
    'compiled_filepath' => 'D:\\go\\ama\\application\\views\\template_c\\d03b8025f8ee3e41174be16b12370503e817c2fd_0.file.comment.tpl.cache.php',
    'uid' => 'd03b8025f8ee3e41174be16b12370503e817c2fd',
    'call_name' => 'smarty_template_function_renderComments_1755958f6a8e3601ca5_58282284',
  ),
));
if (!is_callable('smarty_modifier_regex_replace')) require_once 'D:\\go\\ama\\application\\libraries\\libs\\plugins\\modifier.regex_replace.php';
$_smarty_tpl->compiled->nocache_hash = '1755958f6a8e3601ca5_58282284';
}
/* smarty_template_function_renderComments_1755958f6a8e3601ca5_58282284 */
if (!function_exists('smarty_template_function_renderComments_1755958f6a8e3601ca5_58282284')) {
function smarty_template_function_renderComments_1755958f6a8e3601ca5_58282284($_smarty_tpl,$params) {
$params = array_merge(array('level'=>0), $params);
foreach ($params as $key => $value) {
$_smarty_tpl->tpl_vars[$key] = new Smarty_Variable($value, $_smarty_tpl->isRenderingCache);
}?>
    <?php
$_from = $_smarty_tpl->smarty->ext->_foreach->init($_smarty_tpl, $_smarty_tpl->tpl_vars['data']->value, 'entry');
if ($_from !== null) {
foreach ($_from as $_smarty_tpl->tpl_vars['entry']->value) {
?>
    <div class=" thing id-<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
 noncollapsed   comment " id="thing_<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
" onclick="">
        <p class="parent"><a name="df1l1xv"></a></p>
        <div class="midcol unvoted" id="vote-<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
">
            <div class="arrow <?php if (isset($_smarty_tpl->tpl_vars['entry']->value['vote']) && $_smarty_tpl->tpl_vars['entry']->value['vote'] == '1') {?>upmod<?php } else { ?>up<?php }?> login-required access-required" onclick="voteit('./api',this,1,<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
)"></div>
            <div class="arrow <?php if (isset($_smarty_tpl->tpl_vars['entry']->value['vote']) && $_smarty_tpl->tpl_vars['entry']->value['vote'] == '-1') {?>downmod<?php } else { ?>down<?php }?> login-required access-required"  onclick="voteit('./api',this,-1,<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
)"></div>
        </div>
        <div class="entry unvoted">
            <p class="tagline">
                <a href="javascript:void(0)" class="expand" onclick="return togglecomment(this)">[–]</a>
                <a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['entry']->value['author'];?>
" class="author may-blank"><?php echo $_smarty_tpl->tpl_vars['entry']->value['author_name'];?>
</a>
                <span class="userattrs"></span>
                <span class="score dislikes" title="44"><?php echo $_smarty_tpl->tpl_vars['entry']->value['dislikes'];?>
</span>
                <span class="score unvoted" title="45"><?php echo $_smarty_tpl->tpl_vars['entry']->value['likes']-$_smarty_tpl->tpl_vars['entry']->value['dislikes'];?>
指标</span>
                <span class="score likes" title="46"><?php echo $_smarty_tpl->tpl_vars['entry']->value['likes'];?>
指標</span>
                <time class="live-timestamp timeago" datetime="<?php echo $_smarty_tpl->tpl_vars['entry']->value['timeago'];?>
"></time>
                &nbsp;
                <a href="javascript:void(0)" class="numchildren" onclick="return togglecomment(this)">(<?php echo $_smarty_tpl->tpl_vars['entry']->value['replies'];?>
下层留言)</a>

                <span class="reply-button"><a class="access-required" href="javascript:void(0)" data-thingid="<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
"  data-mainid="<?php echo $_smarty_tpl->tpl_vars['entry']->value['main'];?>
" onclick="return reply(this)">回复</a></span>
            </p>
            <form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')"
                  id="form-t1_df1l1xva7c"><input type="hidden" name="thing_id" value="t1_df1l1xv">

                <div class="usertext-body may-blank-within md-container ">
                          <div class="out md"><?php echo '<script'; ?>
>document.write(markdown.toHTML("<?php echo smarty_modifier_regex_replace(smarty_modifier_regex_replace(smarty_modifier_regex_replace($_smarty_tpl->tpl_vars['entry']->value['text'],'/[\r\t\n]/','\\n'),'/[\"]/','\\\"'),'/[\']/','\\\'');?>
"));<?php echo '</script'; ?>
></div>
                </div>
            </form>
            <ul class="flat-list buttons">
               <!--  <li class="first"><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/df1l1xv/"
                     >永久連結</a></li>
                <li><a href="javascript:void(0)"
                                             class="embed-comment">embed</a></li>
                <li class="comment-save-button save-button"><a href="javascript:void(0)">儲存</a></li>
                <li class="report-button"><a href="javascript:void(0)" class="reportbtn access-required"
                                              data-thingid="<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
">檢舉</a></li>
                <li class="give-gold-button"><a href="">贈送金幣</a></li> -->
                
            </ul>
            <div class="reportform report-t1_df1l1xv"></div>
        </div>
        <div class="child" id="child_<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
">
            <div id="siteTable_child_<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
" class="sitetable listing">
                <?php if (isset($_smarty_tpl->tpl_vars['entry']->value['comments'])) {?>
                <?php if (is_array($_smarty_tpl->tpl_vars['entry']->value['comments'])) {?>
                    <?php $_smarty_tpl->ext->_tplFunction->callTemplateFunction($_smarty_tpl, 'renderComments', array('data'=>$_smarty_tpl->tpl_vars['entry']->value['comments'],'level'=>$_smarty_tpl->tpl_vars['level']->value+1), false);?>

                <?php }?>
                <?php }?>
            </div>
        </div>
        <div class="clearleft"></div>
    </div>
    <?php
}
}
$_smarty_tpl->smarty->ext->_foreach->restore($_smarty_tpl);
?>

<?php
}}
/*/ smarty_template_function_renderComments_1755958f6a8e3601ca5_58282284 */
}
