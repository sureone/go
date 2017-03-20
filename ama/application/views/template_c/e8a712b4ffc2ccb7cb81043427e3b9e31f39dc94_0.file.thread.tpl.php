<?php
/* Smarty version 3.1.30, created on 2017-03-19 14:37:48
  from "D:\go\ama\application\views\common\thread.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58ce89ac177e01_28404062',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'e8a712b4ffc2ccb7cb81043427e3b9e31f39dc94' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\thread.tpl',
      1 => 1489930595,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58ce89ac177e01_28404062 (Smarty_Internal_Template $_smarty_tpl) {
?>
			<div class="thing odd  link self" id="thing_<?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['thingid'];?>
" data-thingid=<?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['thingid'];?>
>
				<p class="parent"></p>
				<?php if ($_smarty_tpl->tpl_vars['page']->value != "comments") {?>
				<span class="rank"><?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['no'];?>
</span>
				<?php }?>
				<div class="midcol unvoted">
					<div class="arrow up login-required access-required" tabindex="0"  data-thingid="<?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['thingid'];?>
" onclick="voteit(this,1)"></div>
					<div class="score dislikes" title="77"><?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['dislikes'];?>
</div>
					<div class="score unvoted" title="78"><?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['score'];?>
</div>
					<div class="score likes" title="79"><?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['likes'];?>
</div>
					<div class="arrow down login-required access-required" tabindex="0"  data-thingid="<?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['thingid'];?>
" onclick="voteit(this,-1)"></div>
				</div>
				<div class="entry unvoted">
					<p class="title">
						<a class="title may-blank loggedin " href="./v/comments/<?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['thingid'];?>
"><?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['title'];?>
</a> 
						
					</p>
					<?php if ($_smarty_tpl->tpl_vars['page']->value != "comments") {?>
						<div class="expando-button collapsed selftext"></div>
					<?php }?>
					<p class="tagline">发表 <time class="live-timestamp"><?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['timeago'];?>
</time>by
						 <a href="" class="author may-blank "><?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['author'];?>
</a>
						 <span class="userattrs"></span>
					</p>
					<?php if ($_smarty_tpl->tpl_vars['page']->value == "comments") {?>
					<div class="expando"><form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt"><input type="hidden" name="thing_id" value="t3_609l7s"><div class="usertext-body may-blank-within md-container "><div class="md"><p>I'm  a young programmer who wants to create a few apps for the African environment and beyond. As well as help promote African hip hop on the internet.</p>
					</div>
					</div></form></div>
					<?php }?>
					<ul class="flat-list buttons">
						<li class="first"><a href="">139 留言</a></li>
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

					<div class="expando expando-uninitialized expando-<?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['thingid'];?>
" style="display: none">
						<span class="error">loading...</span>
					</div>
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div><?php }
}
