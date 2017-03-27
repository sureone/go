<?php
/* Smarty version 3.1.30, created on 2017-03-27 09:51:56
  from "D:\work\go\ama\application\views\common\thread.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58d8c49c375ca8_57841822',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '64fc306606431e9d5e18d170ac85e1f62dcf4f19' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\thread.tpl',
      1 => 1490601106,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58d8c49c375ca8_57841822 (Smarty_Internal_Template $_smarty_tpl) {
?>
			<div class="thing odd  link self" id="thing_<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
" data-thingid=<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
>
				<p class="parent"></p>
				<?php if ($_smarty_tpl->tpl_vars['page']->value != "comments") {?>
				<span class="rank"></span>
				<?php }?>
				<div class="midcol unvoted">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
)"></div>
					<div class="score dislikes" title="77"><?php echo $_smarty_tpl->tpl_vars['entry']->value['dislikes'];?>
</div>
					<div class="score unvoted" title="78"><?php echo $_smarty_tpl->tpl_vars['entry']->value['likes']-$_smarty_tpl->tpl_vars['entry']->value['dislikes'];?>
</div>
					<div class="score likes" title="79"><?php echo $_smarty_tpl->tpl_vars['entry']->value['likes'];?>
</div>
					<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
)"></div>
				</div>
				<div class="entry unvoted">
					<p class="title">
						<a class="title may-blank loggedin " href="./v/comments/<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
"><?php echo $_smarty_tpl->tpl_vars['entry']->value['title'];?>
</a> 
						
					</p>
					<?php if ($_smarty_tpl->tpl_vars['page']->value != "comments") {?>
						<div class="expando-button collapsed selftext"></div>
					<?php }?>
					<p class="tagline">
						 <a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['entry']->value['author'];?>
" class="author may-blank "><?php echo $_smarty_tpl->tpl_vars['entry']->value['author'];?>
</a>
						 <span class="userattrs"></span>
						 发表于 
						 <time class="live-timestamp timeago" datetime="<?php echo $_smarty_tpl->tpl_vars['entry']->value['timeago'];?>
"></time>
						 
					</p>
					
					<div class="expando expando-<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
" style="display: <?php if ($_smarty_tpl->tpl_vars['page']->value == "comments") {?>block<?php } else { ?>none<?php }?>;"><form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt"><input type="hidden" name="thing_id" value="t3_609l7s"><div class="usertext-body may-blank-within md-container "><div class="md"><p><?php echo $_smarty_tpl->tpl_vars['entry']->value['text'];?>
</p>
					</div>
					</div></form></div>
					
					<ul class="flat-list buttons">
						<li class="first"><a href="./v/comments/<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
"><?php echo $_smarty_tpl->tpl_vars['entry']->value['replies'];?>
 留言</a></li>
						<li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li>
						<li class="link-save-button save-button"><a href="#">儲存</a></li>
						<li>
							<form action="/post/hide" method="post" class="state-button hide-button">
								<input type="hidden" name="executed" value="隱藏">
								<span>
									<a href="javascript:void(0)" class=" " onclick="change_state(this, 'hide', hide_thing);">隱藏</a>
								</span>
							</form>
						</li>
						<li class="report-button">
							<a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">檢舉</a>
						</li>
					</ul>
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div><?php }
}
