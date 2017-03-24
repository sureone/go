<html lang="en">
<head>
	{include file="common/page-header.tpl"}
</head>

<body class="listing-page {if $logined eq "true"}loggedin{/if} {$page}-page">


<div id="header">
<div id="header-bottom-left">
	<span class="hover pagename"><a href="./">AMA</a></span>
	<ul class="tabmenu "><li class="selected"><a href="./user/{$user.userid}/home" class="choice">总览</a></li><li><a href="./user/{$user.userid}/message" class="choice">留言</a></li><li><a href="./user/{$user.userid}/submitted" class="choice">已发表</a></li><li><a href="./user/{$user.userid}/saved" class="choice">收藏</a></li><li><a href="./user/{$user.userid}/upvoted" class="choice">推</a></li><li><a href="./user/{$user.userid}/downvoted" class="choice">嘘</a></li></ul>
</div>
{include file="common/header-bottom-right.tpl"}
</div>
{include file="common/side.tpl"}

<div class="content">
	<div class="spacer">
		<div id="siteTable" class="sitetable linklisting">

			{foreach $things as $entry}
				{include file="common/thread.tpl"}
			{/foreach}

		</div>
	</div>
</div>

<div id="footer"></div>
{include file="common/login-modal.tpl"}
</body>


<script type="text/javascript" src="./static/js/user-home.js?v=8"></script>
</html>