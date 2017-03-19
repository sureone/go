<?php
/* Smarty version 3.1.30, created on 2017-03-19 13:48:40
  from "D:\go\ama\application\views\hot.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58ce7e282e4ad4_26075529',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '698fa06f07caff7f8c617daf21469f0c8127ca2d' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\hot.tpl',
      1 => 1489927506,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
    'file:common/page-header.tpl' => 1,
    'file:common/header-bottom-right.tpl' => 1,
    'file:common/side.tpl' => 1,
    'file:common/login-modal.tpl' => 1,
  ),
),false)) {
function content_58ce7e282e4ad4_26075529 (Smarty_Internal_Template $_smarty_tpl) {
?>
<html lang="en">
<head>
	<?php $_smarty_tpl->_subTemplateRender("file:common/page-header.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</head>

<body class="listing-page <?php if ($_smarty_tpl->tpl_vars['logined']->value == "true") {?>loggedin<?php }?> hot-page">


<div id="header">
<div id="header-bottom-left">
	<span class="hover pagename"><a href="./">AMA</a></span>
	<ul class="tabmenu "><li class="selected"><a href="./hot" class="choice">熱門</a></li><li><a href="./new" class="choice">最新</a></li><li><a href="./rising" class="choice">好評上升中</a></li><li><a href="./controversial" class="choice">具爭議的</a></li><li><a href="./top" class="choice">頭等</a></li><li><a href="./gilded" class="choice">精選</a></li><li><a href="./wiki" class="choice">wiki</a></li><li><a href="./ads" class="choice">宣傳過的</a></li></ul>
</div>
<?php $_smarty_tpl->_subTemplateRender("file:common/header-bottom-right.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</div>
<?php $_smarty_tpl->_subTemplateRender("file:common/side.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>


<div class="content">
	<div class="spacer">
		<div id="siteTable" class="sitetable linklisting">
			<?php
$__section_a_0_saved = isset($_smarty_tpl->tpl_vars['__smarty_section_a']) ? $_smarty_tpl->tpl_vars['__smarty_section_a'] : false;
$__section_a_0_loop = (is_array(@$_loop=$_smarty_tpl->tpl_vars['things']->value) ? count($_loop) : max(0, (int) $_loop));
$__section_a_0_total = $__section_a_0_loop;
$_smarty_tpl->tpl_vars['__smarty_section_a'] = new Smarty_Variable(array());
if ($__section_a_0_total != 0) {
for ($__section_a_0_iteration = 1, $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] = 0; $__section_a_0_iteration <= $__section_a_0_total; $__section_a_0_iteration++, $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']++){
?>
			<div class="thing odd  link self" id="thing_<?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['thingid'];?>
" data-thingid=<?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['thingid'];?>
>
				<p class="parent"></p>
				<span class="rank"><?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['no'];?>
</span>
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
					<div class="expando-button collapsed selftext"></div>
					<p class="tagline">发表 <time class="live-timestamp"><?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['timeago'];?>
</time>by
						 <a href="" class="author may-blank "><?php echo $_smarty_tpl->tpl_vars['things']->value[(isset($_smarty_tpl->tpl_vars['__smarty_section_a']->value['index']) ? $_smarty_tpl->tpl_vars['__smarty_section_a']->value['index'] : null)]['author'];?>
</a>
						 <span class="userattrs"></span>
					</p>
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
			</div>
			<?php
}
}
if ($__section_a_0_saved) {
$_smarty_tpl->tpl_vars['__smarty_section_a'] = $__section_a_0_saved;
}
?>

		</div>
	</div>
</div>

<div id="footer"></div>
<?php $_smarty_tpl->_subTemplateRender("file:common/login-modal.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</body>


<?php echo '<script'; ?>
 type="text/javascript" src="./static/js/hot.js?v=8"><?php echo '</script'; ?>
>
</html><?php }
}
