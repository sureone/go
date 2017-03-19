<html lang="en">
<head>
	{include file="common/page-header.tpl"}
</head>

<body class="listing-page {if $logined eq "true"}loggedin{/if} hot-page">


<div id="header">
<div id="header-bottom-left">
	<span class="hover pagename"><a href="./">AMA</a></span>
	<ul class="tabmenu "><li class="selected"><a href="./hot" class="choice">熱門</a></li><li><a href="./new" class="choice">最新</a></li><li><a href="./rising" class="choice">好評上升中</a></li><li><a href="./controversial" class="choice">具爭議的</a></li><li><a href="./top" class="choice">頭等</a></li><li><a href="./gilded" class="choice">精選</a></li><li><a href="./wiki" class="choice">wiki</a></li><li><a href="./ads" class="choice">宣傳過的</a></li></ul>
</div>
{include file="common/header-bottom-right.tpl"}
</div>
{include file="common/side.tpl"}

<div class="content">
	<div class="spacer">
		<div id="siteTable" class="sitetable linklisting">
			{section loop=$things name=a}
			<div class="thing odd  link self" id="thing_{$things[a].thingid}" data-thingid={$things[a].thingid}>
				<p class="parent"></p>
				<span class="rank">{$things[a].no}</span>
				<div class="midcol unvoted">
					<div class="arrow up login-required access-required" tabindex="0"  data-thingid="{$things[a].thingid}" onclick="voteit(this,1)"></div>
					<div class="score dislikes" title="77">{$things[a].dislikes}</div>
					<div class="score unvoted" title="78">{$things[a].score}</div>
					<div class="score likes" title="79">{$things[a].likes}</div>
					<div class="arrow down login-required access-required" tabindex="0"  data-thingid="{$things[a].thingid}" onclick="voteit(this,-1)"></div>
				</div>
				<div class="entry unvoted">
					<p class="title">
						<a class="title may-blank loggedin " href="./v/comments/{$things[a].thingid}">{$things[a].title}</a> 
						
					</p>
					<div class="expando-button collapsed selftext"></div>
					<p class="tagline">发表 <time class="live-timestamp">{$things[a].timeago}</time>by
						 <a href="" class="author may-blank ">{$things[a].author}</a>
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
					<div class="expando expando-uninitialized expando-{$things[a].thingid}" style="display: none">
						<span class="error">loading...</span>
					</div>
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
			{/section}

		</div>
	</div>
</div>

<div id="footer"></div>
{include file="common/login-modal.tpl"}
</body>


<script type="text/javascript" src="./static/js/hot.js?v=8"></script>
</html>