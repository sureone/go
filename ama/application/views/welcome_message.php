<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?><!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title>Ask Me Anything</title>

	<style type="text/css">
	</style>

	<link rel="stylesheet" href="./static/css/common.css" type="text/css" />
</head>
<body>


<div id="header">
<div id="header-bottom-left">
	<span class="hover pagename"><a href="./">AMA</a></span>
	<ul class="tabmenu "><li class="selected"><a href="./hot" class="choice">熱門</a></li><li><a href="./new" class="choice">最新</a></li><li><a href="./rising" class="choice">好評上升中</a></li><li><a href="./controversial" class="choice">具爭議的</a></li><li><a href="./top" class="choice">頭等</a></li><li><a href="./gilded" class="choice">精選</a></li><li><a href="./wiki" class="choice">wiki</a></li><li><a href="./ads" class="choice">宣傳過的</a></li></ul>
	
</div>
<div id="header-bottom-right"><span class="user">想要加入? <a href="./login" class="login-required">登入或註冊</a> 一秒以内.</span><span class="separator">|</span><ul class="flat-list hover"><li><a href="javascript:void(0)" class="pref-lang choice" onclick="return showlang();">中文</a></li></ul></div>
</div>
<div class="side">
	<div class="spacer"><form action="https://www.reddit.com/r/AMA/search" id="search" role="search"><input type="text" name="q" placeholder="搜尋" tabindex="20"><input type="submit" value="" tabindex="22"><div id="searchexpando" class="infobar" style="display: none;"><label><input type="checkbox" name="restrict_sr" tabindex="21">搜尋範圍僅限 r/AMA</label><div id="moresearchinfo" style=""><a href="#" id="search_hidemore">[-]</a><p>use the following search parameters to narrow your results:</p><dl><dt>subreddit:<i>subreddit</i></dt><dd>find submissions in "subreddit"</dd><dt>author:<i>username</i></dt><dd>依「使用者名稱」尋找發文</dd><dt>site:<i>example.com</i></dt><dd>find submissions from "example.com"</dd><dt>url:<i>text</i></dt><dd>search for "text" in url</dd><dt>selftext:<i>text</i></dt><dd>在自行發文的內容中搜尋「文字」</dd><dt>self:yes (or self:no)</dt><dd>包含 (或排除) 自己的文章</dd><dt>nsfw:yes (or nsfw:no)</dt><dd>納入 (或排除) 標記為不適合公開閱覽的結果</dd></dl><p>e.g. <code>subreddit:aww site:imgur.com dog</code></p><p><a href="https://www.reddit.com/wiki/search">see the search faq for details.</a></p></div><p><a href="https://www.reddit.com/wiki/search" id="search_showmore">進階搜尋：依照作者、版面...</a></p></div></form></div>
	<div class="spacer"><div class="sidebox submit submit-text"><div class="morelink"><a href="https://www.reddit.com/r/AMA/submit?selftext=true" data-event-action="submit" data-type="subreddit" data-event-detail="self" class="login-required access-required" target="_top">發表新文章</a><div class="nub"></div></div></div></div>
</div>

<div class="content"></div>

<div id="footer"></div>
</body>
</html>