<?php
/* Smarty version 3.1.30, created on 2017-04-16 04:07:35
  from "D:\go\ama\application\views\common\thread-simple.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58f2d1e779c204_47266642',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'b51f04a8c3a4978ff0f489b2c21fbac552e62798' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\thread-simple.tpl',
      1 => 1492308437,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58f2d1e779c204_47266642 (Smarty_Internal_Template $_smarty_tpl) {
if (!is_callable('smarty_modifier_regex_replace')) require_once 'D:\\go\\ama\\application\\libraries\\libs\\plugins\\modifier.regex_replace.php';
?>
			
			<div class="thing odd <?php echo $_smarty_tpl->tpl_vars['entry']->value['stype'];?>
" id="thing_<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
" data-thingid=<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
>
				<?php if ($_smarty_tpl->tpl_vars['entry']->value['parent'] != 0) {?>
				 
				<p class="parent">
					<span class="subject-text">回帖</span>
					<a name="dffeipd"></a>

					<a href="./v/comments/<?php echo $_smarty_tpl->tpl_vars['entry']->value['parent'];?>
" class="title">
						<?php if (isset($_smarty_tpl->tpl_vars['entry']->value['p_title']) && $_smarty_tpl->tpl_vars['entry']->value['p_title'] != '') {?>
							<?php echo $_smarty_tpl->tpl_vars['entry']->value['p_title'];?>

						<?php } else { ?>
							<?php echo $_smarty_tpl->tpl_vars['entry']->value['p_text'];?>

						<?php }?>
					</a>
					 by 
					<a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['entry']->value['p_author'];?>
" class="author may-blank id-t2_11v90c"><?php echo $_smarty_tpl->tpl_vars['entry']->value['p_author_name'];?>
</a>
					<span class="userattrs"></span>
				</p>
				<?php }?>

				<?php if ($_smarty_tpl->tpl_vars['page']->value != "comments") {?>
				<span class="rank"></span>
				<?php }?>
				<?php if ($_smarty_tpl->tpl_vars['page']->value != "message-inbox") {?>
				<!-- <div class="midcol unvoted" id="vote-<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
">
					<div class="arrow <?php if (isset($_smarty_tpl->tpl_vars['entry']->value['vote']) && $_smarty_tpl->tpl_vars['entry']->value['vote'] == '1') {?>upmod<?php } else { ?>up<?php }?> login-required access-required" tabindex="0" onclick="voteit('./api',this,1,<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
)"></div>

					<?php if ($_smarty_tpl->tpl_vars['entry']->value['parent'] == 0) {?>
					<div class="score dislikes" title="77"><?php echo $_smarty_tpl->tpl_vars['entry']->value['dislikes'];?>
</div>
					<div class="score unvoted" title="78"><?php echo $_smarty_tpl->tpl_vars['entry']->value['likes']-$_smarty_tpl->tpl_vars['entry']->value['dislikes'];?>
</div>
					<div class="score likes" title="79"><?php echo $_smarty_tpl->tpl_vars['entry']->value['likes'];?>
</div>
					<?php }?>
					<div class="arrow <?php if (isset($_smarty_tpl->tpl_vars['entry']->value['vote']) && $_smarty_tpl->tpl_vars['entry']->value['vote'] == '-1') {?>downmod<?php } else { ?>down<?php }?> login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
)"></div>
				</div> -->
				<?php }?>
				<div class="entry unvoted">
					
						<?php if ($_smarty_tpl->tpl_vars['pagetype']->value == "list") {?>
						 <p class="title"><a class="title may-blank loggedin " href="./v/comments/<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
"><?php echo $_smarty_tpl->tpl_vars['entry']->value['title'];?>
</a></p>
						<?php } else { ?>
						
							<?php if (isset($_smarty_tpl->tpl_vars['entry']->value['title']) && $_smarty_tpl->tpl_vars['entry']->value['title'] != '') {?>
								 <p class="title"><a class="title may-blank loggedin " href="./v/comments/<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
"><?php echo $_smarty_tpl->tpl_vars['entry']->value['title'];?>
</a></p>
							<?php } else { ?>
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><?php echo '<script'; ?>
>document.write(markdown.toHTML("<?php echo smarty_modifier_regex_replace(smarty_modifier_regex_replace(smarty_modifier_regex_replace($_smarty_tpl->tpl_vars['entry']->value['text'],'/[\r\t\n]/','\\n'),'/[\"]/','\\\"'),'/[\']/','\\\'');?>
"));<?php echo '</script'; ?>
></div>
							</div>
							<?php }?>
							
						<?php }?>
					

					<?php if ($_smarty_tpl->tpl_vars['page']->value == "hot") {?>
						<div class="expando-button collapsed selftext"></div>
					<?php }?>
					<p class="tagline">
						 <?php if ($_smarty_tpl->tpl_vars['entry']->value['stype'] == 'message') {?>
						 来自
						 <?php }?>
						 <a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['entry']->value['author'];?>
" class="author may-blank "><?php echo $_smarty_tpl->tpl_vars['entry']->value['author_name'];?>
</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="<?php echo $_smarty_tpl->tpl_vars['entry']->value['timeago'];?>
"></time>

						 <a href="./v/comments/<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
"><?php echo $_smarty_tpl->tpl_vars['entry']->value['replies'];?>
 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">檢舉</a>


						 
					</p>
					<?php if ($_smarty_tpl->tpl_vars['pagetype']->value == "list") {?>
					<div class="expando expando-<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
" style="display: <?php if ($_smarty_tpl->tpl_vars['page']->value == "comments") {?>block<?php } else { ?>none<?php }?>;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><?php echo '<script'; ?>
>document.write(markdown.toHTML("<?php echo smarty_modifier_regex_replace(smarty_modifier_regex_replace(smarty_modifier_regex_replace($_smarty_tpl->tpl_vars['entry']->value['text'],'/[\r\t\n]/','\\n'),'/[\"]/','\\\"'),'/[\']/','\\\'');?>
"));<?php echo '</script'; ?>
></div>
							</div>
						</form>
					</div>
					<?php }?>
					
					<ul class="flat-list buttons">
						<li class="first"></li>
						
						<li class="report-button">
							
						</li>
					</ul>
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div><?php }
}
