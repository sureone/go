<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?><!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title>Ask Me Anything</title>

	<style type="text/css">
	</style>

	<link rel="stylesheet" href="./static/css/common.css?v=2" type="text/css" />
    <script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.js"></script>
    <script type="text/javascript" src="http://apps.bdimg.com/libs/handlebars.js/1.3.0/handlebars.min.js"></script>
    <script src="./static/js/form2json.js"></script>
    <script src="./static/js/common.js?v=6"></script>
    <?php $url_prefix='./index.php' ?>
</head>
<body>


<div id="header">
<div id="header-bottom-left">
	<span class="hover pagename"><a href="./">AMA</a></span>
	<ul class="tabmenu "><li class="selected"><a href="./hot" class="choice">熱門</a></li><li><a href="./new" class="choice">最新</a></li><li><a href="./rising" class="choice">好評上升中</a></li><li><a href="./controversial" class="choice">具爭議的</a></li><li><a href="./top" class="choice">頭等</a></li><li><a href="./gilded" class="choice">精選</a></li><li><a href="./wiki" class="choice">wiki</a></li><li><a href="./ads" class="choice">宣傳過的</a></li></ul>
</div>
<div id="header-bottom-right">
<?php if(isset($user)){ ?>
<span class="user"><a href="https://www.reddit.com/user/yangxiaowang/">yangxiaowang</a>&nbsp;(<span class="userkarma" title="post karma">1</span>)</span><span class="separator">|</span><a title="沒有新郵件" href="https://www.reddit.com/message/inbox/" class="nohavemail" id="mail">信息</a><span class="separator">|</span><ul class="flat-list hover"><li><a href="https://www.reddit.com/prefs/" class="pref-lang choice">偏好設定</a></li></ul><span class="separator">|</span><form method="post" id="logout-form" action="<?=$url_prefix?>/api" class="logout hover"><input type="hidden" name="uh" value="cqaeyi63ta407191a49905d7c0b26e7c2a75cb1b81ddd77995"><input type="hidden" name="top" value="off"><input type="hidden" name="action" value="logout"><a href="javascript:void(0)" onclick="$(this).parent().submit()">登出</a></form>
<?php }else{ ?>
<span class="user">想要加入? <a href="./login" class="login-required">登入或註冊</a> 一秒以内.</span><span class="separator">|</span><ul class="flat-list hover"><li><a href="javascript:void(0)" class="pref-lang choice" onclick="return showlang();">中文</a></li></ul>
<?php } ?>
</div>
</div>
<div class="side">
	<div class="spacer"><form action="https://www.reddit.com/r/AMA/search" id="search" role="search"><input type="text" name="q" placeholder="搜尋" tabindex="20"><input type="submit" value="" tabindex="22"><div id="searchexpando" class="infobar" style="display: none;"><label><input type="checkbox" name="restrict_sr" tabindex="21">搜尋範圍僅限 r/AMA</label><div id="moresearchinfo" style=""><a href="#" id="search_hidemore">[-]</a><p>use the following search parameters to narrow your results:</p><dl><dt>subreddit:<i>subreddit</i></dt><dd>find submissions in "subreddit"</dd><dt>author:<i>username</i></dt><dd>依「使用者名稱」尋找發文</dd><dt>site:<i>example.com</i></dt><dd>find submissions from "example.com"</dd><dt>url:<i>text</i></dt><dd>search for "text" in url</dd><dt>selftext:<i>text</i></dt><dd>在自行發文的內容中搜尋「文字」</dd><dt>self:yes (or self:no)</dt><dd>包含 (或排除) 自己的文章</dd><dt>nsfw:yes (or nsfw:no)</dt><dd>納入 (或排除) 標記為不適合公開閱覽的結果</dd></dl><p>e.g. <code>subreddit:aww site:imgur.com dog</code></p><p><a href="https://www.reddit.com/wiki/search">see the search faq for details.</a></p></div><p><a href="https://www.reddit.com/wiki/search" id="search_showmore">進階搜尋：依照作者、版面...</a></p></div></form></div>
	<div class="spacer"><div class="sidebox submit submit-text"><div class="morelink"><a href="https://www.reddit.com/r/AMA/submit?selftext=true" data-event-action="submit" data-type="subreddit" data-event-detail="self" class="login-required access-required" target="_top">發表新文章</a><div class="nub"></div></div></div></div>
</div>

<div class="content">
	<div class="spacer">
		<div id="siteTable" class="sitetable linklisting">
			
		</div>
	</div>
</div>

<div id="footer"></div>

<div class="modal  fade  login-modal" tabindex="0" aria-hidden="true">
<div class="modal-dialog modal-dialog-lg"><div class="modal-content"><div class="modal-header"><a href="javascript: void 0;" class="c-close c-hide-text" data-dismiss="modal">close this window</a></div><div class="modal-body"><h3 id="cover-msg" class="modal-title" style="display: none;">您必須登入才能操作。</h3><div id="login"><div class="split-panel"><div class="split-panel-section split-panel-divider"><h4 class="modal-title">建立一個新帳號</h4><form id="register-form" method="post" action="<?=$url_prefix?>/api" class="form-v2"><input type="hidden" name="action" value="reg">

<div class="c-form-group "><label for="user_reg" class="screenreader-only">使用者账号:</label><input value="" name="user" id="user_reg" class="c-form-control" type="text" maxlength="20" tabindex="2" placeholder="選擇使用者账号" data-validate-url="/api/check_username.json" data-validate-min="3" autocomplete="off"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div>

<div class="c-form-group "><label for="user_name" class="screenreader-only">使用者名稱:</label><input value="" name="user_name" id="user_name" class="c-form-control" type="text" maxlength="20" tabindex="2" placeholder="選擇使用者名稱" data-validate-url="/api/check_username.json" data-validate-min="3" autocomplete="off"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div>

<div class="c-form-group "><label for="passwd_reg" class="screenreader-only">密碼:</label><input id="passwd_reg" class="c-form-control" name="passwd" type="password" tabindex="2" placeholder="密碼" data-validate-url="/api/check_password.json" style="padding-right: 5px;"><div class="strength-meter"><div class="strength-meter-fill"></div></div><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div><div class="c-form-group "><label for="passwd2_reg" class="screenreader-only">確認密碼:</label><input name="passwd2" id="passwd2_reg" class="c-form-control" type="password" tabindex="2" placeholder="確認密碼"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div><div class="c-form-group "><label for="email_reg" class="screenreader-only">電子郵件: &nbsp;<i>(選填)</i></label><input value="" name="email" id="email_reg" class="c-form-control" type="text" tabindex="2" placeholder="電子郵件" data-validate-url="/api/check_email.json" data-validate-on="change blur"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div><div class="c-checkbox"><input type="checkbox" name="rem" id="rem_reg" tabindex="2"><label for="rem_reg">記住我</label></div><input type="hidden" name="digest_subscribe" id="digest_subscribe" value="true"><div class="spacer"><div class="c-form-group g-recaptcha" data-sitekey="6LeTnxkTAAAAAN9QEuDZRpn90WwKk_R1TRW_g-JC"></div><span class="error BAD_CAPTCHA field-captcha" style="display:none"></span></div><div class="c-clearfix c-submit-group"><span class="c-form-throbber"></span><button type="submit" class="c-btn c-btn-primary c-pull-right" tabindex="2">註冊</button></div><div><div class="c-alert c-alert-danger"></div><span class="error RATELIMIT field-ratelimit" style="display:none"></span><span class="error RATELIMIT field-vdelay" style="display:none"></span></div></form></div>

<div class="split-panel-section"><h4 class="modal-title">登入</h4><form id="login-form" method="post" action="<?=$url_prefix?>/api" class="form-v2"><input type="hidden" name="action" value="login"><div class="c-form-group "><label for="user_login" class="screenreader-only">使用者名稱:</label><input value="" name="user" id="user_login" class="c-form-control" type="text" maxlength="20" tabindex="3" placeholder="使用者名稱"></div><div class="c-form-group "><label for="passwd_login" class="screenreader-only">密碼:</label><input id="passwd_login" class="c-form-control" name="passwd" type="password" tabindex="3" placeholder="密碼"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div><div class="c-checkbox"><input type="checkbox" name="rem" id="rem_login" tabindex="3"><label for="rem_login">記住我</label><a href="/password" class="c-pull-right">重設密碼</a></div><div class="spacer"><div class="c-form-group g-recaptcha" data-sitekey="6LeTnxkTAAAAAN9QEuDZRpn90WwKk_R1TRW_g-JC"></div><span class="error BAD_CAPTCHA field-captcha" style="display:none"></span></div><div class="c-clearfix c-submit-group"><span class="c-form-throbber"></span><button type="submit" class="c-btn c-btn-primary c-pull-right" tabindex="3">登入</button></div><div><div class="c-alert c-alert-danger"></div></div></form></div></div><p class="login-disclaimer">By signing up, you agree to our <a href="https://www.reddit.com/help/useragreement/">任期</a> and that you have read our <a href="https://www.reddit.com/help/privacypolicy/">隱私權政策</a> and <a href="https://www.reddit.com/help/contentpolicy/">內容政策</a>.</p></div></div></div></div></div>

</body>
<script type="text/x-handlebars" id="tpl-thread-item">
	<div class="thing odd  link self" id="thing_{{thingid}}" data-thingid={{thingid}}>
		<p class="parent"></p>
		<span class="rank">{{no}}</span>
		<div class="midcol unvoted">
			<div class="arrow up login-required access-required" tabindex="0"></div>
			<div class="score dislikes" title="77">{{dislikes}}</div>
			<div class="score unvoted" title="78">{{score}}</div>
			<div class="score likes" title="79">{{likes}}</div>
			<div class="arrow down login-required access-required" tabindex="0"></div>
		</div>
		<div class="entry unvoted">
			<p class="title">
				<a class="title may-blank loggedin " href="./coments/{{thingid}}">{{title}}</a> 
				<span class="domain">(<a href="/r/AMA/">self.AMA</a>)</span>
			</p>
			<div class="expando-button collapsed selftext"></div>
			<p class="tagline">发表 <time class="live-timestamp">{{timeago}}</time>by
				 <a href="" class="author may-blank ">{{author}}</a>
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
			<div class="expando expando-uninitialized" style="display: none">
				<span class="error">loading...</span>
			</div>
		</div>
		<div class="child"></div>
		<div class="clearleft"></div>
	</div>

</script>

<script type="text/javascript" src="./static/js/ama.js?v=8"></script>
</html>