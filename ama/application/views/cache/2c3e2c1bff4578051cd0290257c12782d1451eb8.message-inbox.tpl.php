<?php
/* Smarty version 3.1.30, created on 2017-04-18 04:08:44
  from "D:\work\go\ama\application\views\message-inbox.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58f5752cf07208_76359839',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'e74f7ad60ced26fdff5202a6b38ca6ea65c7363c' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\message-inbox.tpl',
      1 => 1492481289,
      2 => 'file',
    ),
    '2cba0286b045df067f392a1b6c88c5e91e74ffac' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\page-header.tpl',
      1 => 1492478170,
      2 => 'file',
    ),
    'f9425a6b5d947a44cc0dd8983944da6b322d1daa' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\page-logo.tpl',
      1 => 1492480911,
      2 => 'file',
    ),
    'f0f6ee9aad64c0aa7300ae99fc743a68b3429bac' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\header-bottom-right.tpl',
      1 => 1492390703,
      2 => 'file',
    ),
    '03bb522d2b386ac1ed37558bc5ade9715dbc5cb5' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\message.tpl',
      1 => 1492390703,
      2 => 'file',
    ),
    '4b6dd1202b4b103f1802898db3a09d2c6f39e99e' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\login-modal.tpl',
      1 => 1492390703,
      2 => 'file',
    ),
    '118fd7b10ea75402fecd4f2e94fb33ab790642da' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\comment-reply-edit.tpl',
      1 => 1492390703,
      2 => 'file',
    ),
    '3562a7a76b8774c8a35323504a6ebee9dd364df8' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\markhelp.tpl',
      1 => 1492390703,
      2 => 'file',
    ),
  ),
  'cache_lifetime' => 10,
),true)) {
function content_58f5752cf07208_76359839 (Smarty_Internal_Template $_smarty_tpl) {
?>

	<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
	<meta charset="utf-8">
    <base href="http://127.0.0.1/ama/index.php">
        <title>后园小亭</title>
    	<style type="text/css">
	</style>
	<link rel="stylesheet" href="./static/css/common.css?v=2" type="text/css" />
    <script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="http://apps.bdimg.com/libs/handlebars.js/1.3.0/handlebars.min.js"></script>
    <script src="./static/js/form2json.js"></script>
    <script src="./static/js/common.js?v=7"></script>
    <script src="./static/js/jquery.timeago.js?v=1"></script>
    <script src="./static/js/markdown.js?v=3"></script>
    <script type="text/javascript" src="./static/js/ama.js?v=8"></script>

    <script type="text/javascript">
    	var g_logined = true;
    </script>
</head>

<body class="loggedin message-inbox-page">


<div id="header">
<div id="header-bottom-left">
	<span class="hover pagename"><a href="./"><img src="./static/images/logo.gif" alt="后园小亭" height="20"></a></span>
	<ul class="tabmenu ">
		<li><a href="./v/message/compose" class="choice">傳送一個私人訊息</a></li>
		<li class="selected"><a href="./v/message/inbox" class="choice">收件匣</a></li>
		<li><a href="./v/message/sent" class="choice">发件箱</a></li>
	</ul>
</div>
<div id="header-bottom-right">
<span class="user"><a href="./v/user/sureone/">sureone</a>&nbsp;(<span class="userkarma" title="post karma">1</span>)</span><span class="separator">|</span><a title="沒有新郵件" href="./v/message/inbox/" class="nohavemail" id="mail">信息</a>
<!-- 
<span class="separator">|</span><ul class="flat-list hover">
<li><a href="https://www.reddit.com/prefs/" class="pref-lang choice">偏好設定</a></li>
</ul>
 -->
<span class="separator">|</span><form method="post" id="logout-form" action="./api" class="logout hover"><input type="hidden" name="uh" value="cqaeyi63ta407191a49905d7c0b26e7c2a75cb1b81ddd77995"><input type="hidden" name="top" value="off"><input type="hidden" name="action" value="logout"><a href="javascript:void(0)" onclick="$(this).parent().submit()">登出</a></form>

</div>
</div>
<div class="content">
	<div class="menuarea">
		<div class="spacer">
			<ul class="flat-list hover">
				<li class="selected"><a href="./v/message/inbox/" class="choice">所有</a></li>
				<li><span class="separator">|</span><a href="./v/message/unread/" class="choice">未讀</a></li>
				<li><span class="separator">|</span><a href="./v/message/messages/" class="choice">信息</a></li>
				<li><span class="separator">|</span><a href="./v/message/comments/" class="choice">留言回覆</a></li>
				<li><span class="separator">|</span><a href="./v/message/selfreply/" class="choice">貼文回覆</a></li>
				<li><span class="separator">|</span><a href="./v/message/mentions/" class="choice">用戶名被提及</a></li>
			</ul>
		</div>
	</div>

	<div class="spacer">
		<div id="siteTable" class="sitetable linklisting">

							<div class=" thing id-t4_7x41ei noncollapsed recipient odd message ">
  <p class="parent"></p>
  <p class="subject">

    <span class="subject-text">
    	    		kkkk
    	    </span>
	  </p>
  <p>
  </p>
  <div class="entry unvoted">
    <p class="tagline">
      <span class="head">来自
        <span class="sender">
          <a href="./v/user/sureone" class="author may-blank">小网</a>
          <span class="userattrs"></span>
        </span>
        <time class="timeago" datetime="2017-03-29 14:15:35" class=""></time></span>
    </p>
    <div class="md-container">
     <div class="out md"><script>document.write(markdown.toHTML("fdsafdsafdsafds"));</script></div>
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

          </ul>
    <div class="reportform report-t4_7x41ei"></div>
  </div>
  <div class="child" id="child_55"></div>
  <div class="clearleft"></div>
</div>

							<div class=" thing id-t4_7x41ei noncollapsed recipient odd message ">
  <p class="parent"></p>
  <p class="subject">

    <span class="subject-text">
    	    		hello are 
    	    </span>
	  </p>
  <p>
  </p>
  <div class="entry unvoted">
    <p class="tagline">
      <span class="head">来自
        <span class="sender">
          <a href="./v/user/sureone" class="author may-blank">小网</a>
          <span class="userattrs"></span>
        </span>
        <time class="timeago" datetime="2017-03-29 14:14:55" class=""></time></span>
    </p>
    <div class="md-container">
     <div class="out md"><script>document.write(markdown.toHTML("333333333333333"));</script></div>
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

          </ul>
    <div class="reportform report-t4_7x41ei"></div>
  </div>
  <div class="child" id="child_54"></div>
  <div class="clearleft"></div>
</div>

							<div class=" thing id-t4_7x41ei noncollapsed recipient odd message ">
  <p class="parent"></p>
  <p class="subject">

    <span class="subject-text">
    	    		hello
    	    </span>
	  </p>
  <p>
  </p>
  <div class="entry unvoted">
    <p class="tagline">
      <span class="head">来自
        <span class="sender">
          <a href="./v/user/sureone" class="author may-blank">小网</a>
          <span class="userattrs"></span>
        </span>
        <time class="timeago" datetime="2017-03-29 14:14:28" class=""></time></span>
    </p>
    <div class="md-container">
     <div class="out md"><script>document.write(markdown.toHTML("are you ok"));</script></div>
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

          </ul>
    <div class="reportform report-t4_7x41ei"></div>
  </div>
  <div class="child" id="child_53"></div>
  <div class="clearleft"></div>
</div>

							<div class=" thing id-t4_7x41ei noncollapsed recipient odd message ">
  <p class="parent"></p>
  <p class="subject">

    <span class="subject-text">
    	    		12121
    	    </span>
	  </p>
  <p>
  </p>
  <div class="entry unvoted">
    <p class="tagline">
      <span class="head">来自
        <span class="sender">
          <a href="./v/user/sureone" class="author may-blank">小网</a>
          <span class="userattrs"></span>
        </span>
        <time class="timeago" datetime="2017-03-29 14:12:22" class=""></time></span>
    </p>
    <div class="md-container">
     <div class="out md"><script>document.write(markdown.toHTML("3333333333"));</script></div>
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

          </ul>
    <div class="reportform report-t4_7x41ei"></div>
  </div>
  <div class="child" id="child_52"></div>
  <div class="clearleft"></div>
</div>

							<div class=" thing id-t4_7x41ei noncollapsed recipient odd message ">
  <p class="parent"></p>
  <p class="subject">

    <span class="subject-text">
    	    		hi
    	    </span>
	  </p>
  <p>
  </p>
  <div class="entry unvoted">
    <p class="tagline">
      <span class="head">来自
        <span class="sender">
          <a href="./v/user/sureone" class="author may-blank">小网</a>
          <span class="userattrs"></span>
        </span>
        <time class="timeago" datetime="2017-03-29 14:10:57" class=""></time></span>
    </p>
    <div class="md-container">
     <div class="out md"><script>document.write(markdown.toHTML("1111"));</script></div>
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

          </ul>
    <div class="reportform report-t4_7x41ei"></div>
  </div>
  <div class="child" id="child_50"></div>
  <div class="clearleft"></div>
</div>

							<div class=" thing id-t4_7x41ei noncollapsed recipient odd message ">
  <p class="parent"></p>
  <p class="subject">

    <span class="subject-text">
    	    		hi
    	    </span>
	  </p>
  <p>
  </p>
  <div class="entry unvoted">
    <p class="tagline">
      <span class="head">来自
        <span class="sender">
          <a href="./v/user/sureone" class="author may-blank">小网</a>
          <span class="userattrs"></span>
        </span>
        <time class="timeago" datetime="2017-03-29 14:10:46" class=""></time></span>
    </p>
    <div class="md-container">
     <div class="out md"><script>document.write(markdown.toHTML("1111"));</script></div>
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

          </ul>
    <div class="reportform report-t4_7x41ei"></div>
  </div>
  <div class="child" id="child_49"></div>
  <div class="clearleft"></div>
</div>

							<div class=" thing id-t4_7x41ei noncollapsed recipient odd message ">
  <p class="parent"></p>
  <p class="subject">

    <span class="subject-text">
    	    		hello sureone
    	    </span>
	  </p>
  <p>
  </p>
  <div class="entry unvoted">
    <p class="tagline">
      <span class="head">来自
        <span class="sender">
          <a href="./v/user/sureone" class="author may-blank">小网</a>
          <span class="userattrs"></span>
        </span>
        <time class="timeago" datetime="2017-03-29 13:58:48" class=""></time></span>
    </p>
    <div class="md-container">
     <div class="out md"><script>document.write(markdown.toHTML("it\'s a message to u"));</script></div>
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

          </ul>
    <div class="reportform report-t4_7x41ei"></div>
  </div>
  <div class="child" id="child_48"></div>
  <div class="clearleft"></div>
</div>

			

		</div>
	</div>
</div>

<div id="footer"></div>
<div class="modal  fade  login-modal" tabindex="0" aria-hidden="true">
<div class="modal-dialog modal-dialog-lg"><div class="modal-content"><div class="modal-header"><a href="javascript: void 0;" class="c-close c-hide-text" data-dismiss="modal">close this window</a></div><div class="modal-body"><h3 id="cover-msg" class="modal-title" style="display: none;">您必須登入才能操作。</h3><div id="login"><div class="split-panel"><div class="split-panel-section split-panel-divider"><h4 class="modal-title">建立一个新帐号</h4><form id="register-form" method="post" action="./api" class="form-v2"><input type="hidden" name="action" value="reg">

<div class="c-form-group "><label for="user_reg" class="screenreader-only">使用者账号:</label><input value="" name="user" id="user_reg" class="c-form-control" type="text" maxlength="20" tabindex="2" placeholder="选择使用者账号" data-validate-url="/api/check_username.json" data-validate-min="3" autocomplete="off"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div>

<div class="c-form-group "><label for="user_name" class="screenreader-only">使用者名称:</label><input value="" name="user_name" id="user_name" class="c-form-control" type="text" maxlength="20" tabindex="2" placeholder="选择使用者名称" data-validate-url="/api/check_username.json" data-validate-min="3" autocomplete="off"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div>

<div class="c-form-group "><label for="passwd_reg" class="screenreader-only">密码:</label><input id="passwd_reg" class="c-form-control" name="passwd" type="password" tabindex="2" placeholder="密码" data-validate-url="/api/check_password.json" style="padding-right: 5px;"><div class="strength-meter"><div class="strength-meter-fill"></div></div><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div><div class="c-form-group "><label for="passwd2_reg" class="screenreader-only">确认密码:</label><input name="passwd2" id="passwd2_reg" class="c-form-control" type="password" tabindex="2" placeholder="确认密码"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div><div class="c-form-group "><label for="email_reg" class="screenreader-only">电子邮件: &nbsp;<i>(选填)</i></label><input value="" name="email" id="email_reg" class="c-form-control" type="text" tabindex="2" placeholder="电子邮件" data-validate-url="/api/check_email.json" data-validate-on="change blur"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div>



<div class="c-clearfix c-submit-group"><span class="c-form-throbber"></span><button type="submit" class="c-btn c-btn-primary c-pull-right" tabindex="2">注册</button></div><div><div class="c-alert c-alert-danger"></div><span class="error RATELIMIT field-ratelimit" style="display:none"></span><span class="error RATELIMIT field-vdelay" style="display:none"></span></div></form></div>

<div class="split-panel-section"><h4 class="modal-title">登入</h4><form id="login-form" method="post" action="./api" class="form-v2"><input type="hidden" name="action" value="login"><div class="c-form-group "><label for="user_login" class="screenreader-only">使用者名称:</label><input value="" name="user" id="user_login" class="c-form-control" type="text" maxlength="20" tabindex="3" placeholder="使用者名称"></div><div class="c-form-group "><label for="passwd_login" class="screenreader-only">密码:</label><input id="passwd_login" class="c-form-control" name="passwd" type="password" tabindex="3" placeholder="密码"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div><div class="c-checkbox"><input type="checkbox" name="rem" id="rem_login" tabindex="3"><label for="rem_login">记住我</label>

<!-- <a href="/password" class="c-pull-right">重設密碼</a> -->

</div><div class="spacer"><div class="c-form-group g-recaptcha" data-sitekey="6LeTnxkTAAAAAN9QEuDZRpn90WwKk_R1TRW_g-JC"></div><span class="error BAD_CAPTCHA field-captcha" style="display:none"></span></div><div class="c-clearfix c-submit-group"><span class="c-form-throbber"></span><button type="submit" class="c-btn c-btn-primary c-pull-right" tabindex="3">登入</button></div><div><div class="c-alert c-alert-danger"></div></div></form></div></div>

<!-- <p class="login-disclaimer">By signing up, you agree to our <a href="https://www.reddit.com/help/useragreement/">任期</a> and that you have read our <a href="https://www.reddit.com/help/privacypolicy/">隱私權政策</a> and <a href="https://www.reddit.com/help/contentpolicy/">內容政策</a>.</p>
 -->
</div></div></div></div></div>



<script id="tpl-comment-edit" type="text/x-handlebars-template">
    
    <form action="./api" class="usertext cloneable warn-on-unload" onsubmit="handleFormSubmit(this);return false;" id="form-comment-{{thingid}}">
    
        <input type="hidden" name="action" value="submit-new-comment">
         
        <input type="hidden" name="main" value="{{mainid}}">
        
        <input type="hidden" name="parent" value="{{thingid}}">
        
        <div class="usertext-edit md-container" style="">
            <div class="md">
                <textarea rows="1" cols="1" name="content" class="" data-event-action="comment" data-type="link"></textarea>
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

                <div class="usertext-buttons">
                    <button type="submit" onclick="" class="save">保存</button>
                    <button type="button" onclick="return cancel_usertext(this);" class="cancel" style="">取消</button>
                </div>
            </div>
            
                                <div class="markhelp" style="display:none"><p></p>

                                    <p>使用稍微自訂過的 <a href="http://daringfireball.net/projects/markdown/syntax">Markdown</a>
                                        版本，作為文字格式的設定方式。請參閱下方的部分基本格式。
                                    </p>

                                    <p></p>
                                    <table class="md">
                                        <tbody>
                                        <tr style="background-color: #ffff99; text-align: center">
                                            <td><em>輸入的文字：</em></td>
                                            <td><em>顯示的文字：</em></td>
                                        </tr>
                                        <tr>
                                            <td>*斜體*</td>
                                            <td><em>斜體</em></td>
                                        </tr>
                                        <tr>
                                            <td>**粗體**</td>
                                            <td><b>粗體</b></td>
                                        </tr>
                                        <tr>
                                            <td>[reddit!](https://reddit.com)</td>
                                            <td><a href="https://reddit.com">reddit!</a></td>
                                        </tr>
                                        <tr>
                                            <td>* 項目 1<br>* 項目 2<br>* 項目 3</td>
                                            <td>
                                                <ul>
                                                    <li>項目 1</li>
                                                    <li>項目 2</li>
                                                    <li>項目 3</li>
                                                </ul>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>&gt; 引用文字</td>
                                            <td>
                                                <blockquote>引用文字</blockquote>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Lines starting with four spaces<br>are treated like code:<br><br><span
                                                    class="spaces">&nbsp;&nbsp;&nbsp;&nbsp;</span>if 1 * 2 &lt;
                                                3:<br><span class="spaces">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>print
                                                "hello, world!"<br></td>
                                            <td>Lines starting with four spaces<br>are treated like code:<br>
                                                <pre>if 1 * 2 &lt; 3:<br>&nbsp;&nbsp;&nbsp;&nbsp;print "hello, world!"</pre>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>~~strikethrough~~</td>
                                            <td><strike>strikethrough</strike></td>
                                        </tr>
                                       
                                        </tbody>
                                    </table>
                                </div>
        </div>
    </form>
</script>
</body>


<script type="text/javascript" src="./static/js/message-inbox.js?v=8"></script>
</html><?php }
}
