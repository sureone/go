<?php
/* Smarty version 3.1.30, created on 2017-04-01 03:11:25
  from "D:\work\go\ama\application\views\common\message.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58defe3dd4b7b7_22208035',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '03bb522d2b386ac1ed37558bc5ade9715dbc5cb5' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\message.tpl',
      1 => 1491009082,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58defe3dd4b7b7_22208035 (Smarty_Internal_Template $_smarty_tpl) {
?>
<div class=" thing id-t4_7x41ei noncollapsed recipient odd <?php echo $_smarty_tpl->tpl_vars['entry']->value['stype'];?>
 ">
  <p class="parent"></p>
  <p class="subject">

    <span class="subject-text">
    	<?php if ($_smarty_tpl->tpl_vars['entry']->value['stype'] == "message") {?>
    		<?php echo $_smarty_tpl->tpl_vars['entry']->value['title'];?>

    	<?php } else { ?>
    		回帖
    	<?php }?>
    </span>
	<?php if ($_smarty_tpl->tpl_vars['entry']->value['stype'] != "message") {?>
	<a href="./v/comments/<?php echo $_smarty_tpl->tpl_vars['entry']->value['main'];?>
" class="title">
		<?php if (isset($_smarty_tpl->tpl_vars['entry']->value['p_title'])) {?>
			<?php echo $_smarty_tpl->tpl_vars['entry']->value['p_title'];?>

		<?php } else { ?>
			<?php echo $_smarty_tpl->tpl_vars['entry']->value['p_text'];?>

		<?php }?>
	</a>
	<?php }?>
  </p>
  <p>
  </p>
  <div class="entry unvoted">
    <p class="tagline">
      <span class="head">来自
        <span class="sender">
          <a href="./v/user/<?php echo $_smarty_tpl->tpl_vars['entry']->value['author'];?>
" class="author may-blank"><?php echo $_smarty_tpl->tpl_vars['entry']->value['author'];?>
</a>
          <span class="userattrs"></span>
        </span>
        <time class="timeago" datetime="<?php echo $_smarty_tpl->tpl_vars['entry']->value['timeago'];?>
" class=""></time></span>
    </p>
    <div class="md-container">
      <div class="md">
        <p><?php echo $_smarty_tpl->tpl_vars['entry']->value['text'];?>
</p>
      </div>
    </div>
    <ul class="flat-list buttons">
      <li class="first">
        <a href="">永久連結</a></li>
      <li>
        <form class="toggle del_msg-button " action="#" method="get">
          <input type="hidden" name="executed" value="已刪除">
          <span class="option main active">
            <a href="#" class="togglebutton " onclick="return toggle(this)" data-event-action="delete_message">刪除</a></span>
          <span class="option error">你確定嗎？
            <a href="javascript:void(0)" class="yes" onclick="change_state(this, &quot;del_msg&quot;, hide_thing, undefined, null)">是</a>/
            <a href="javascript:void(0)" class="no" onclick="return toggle(this)">否</a></span>
        </form>
      </li>
      <li class="report-button">
        <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">檢舉</a></li>
      <li class="unread">
        <form action="/post/unread" method="post" class="state-button unread-button">
          <input type="hidden" name="executed" value="未讀">
          <span>
            <a href="javascript:void(0)" class=" access-required" data-event-action="mark_unread" onclick="return change_state(this, 'unread_message', unread_thing, true);">標記成未讀取</a></span>
        </form>
      </li>

      <?php if ($_smarty_tpl->tpl_vars['entry']->value['stype'] != "message") {?>
      <li><a class="access-required" href="javascript:void(0)"  data-thingid="<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
" data-mainid="<?php echo $_smarty_tpl->tpl_vars['entry']->value['main'];?>
"  onclick="return reply(this)">回覆</a></li>
      <?php }?>
    </ul>
    <div class="reportform report-t4_7x41ei"></div>
  </div>
  <div class="child" id="child_<?php echo $_smarty_tpl->tpl_vars['entry']->value['thingid'];?>
"></div>
  <div class="clearleft"></div>
</div>
<?php }
}
