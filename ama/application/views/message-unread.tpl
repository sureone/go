
	{include file="common/page-header.tpl"}
</head>

<body class="{if $logined eq "true"}loggedin{/if} {$page}-page">


<div id="header">
<div id="header-bottom-left">
	{include file="common/page-logo.tpl"}
	<ul class="tabmenu ">
		<li><a href="./v/message/compose" class="choice">傳送一個私人訊息</a></li>
		<li class="selected"><a href="./v/message/inbox" class="choice">收件匣</a></li>
		<li><a href="./v/message/sent" class="choice">发件箱</a></li>
</div>
{include file="common/header-bottom-right.tpl"}
</div>
<div class="content">
	<div class="menuarea">
		<div class="spacer">
			<ul class="flat-list hover">
				<li><a href="./v/message/inbox/" class="choice">所有</a></li>
				<li class="selected"><span class="separator">|</span><a href="./v/message/unread/" class="choice">未讀</a></li>
				<li><span class="separator">|</span><a href="./v/message/messages/" class="choice">信息</a></li>
				<li><span class="separator">|</span><a href="./v/message/comments/" class="choice">留言回覆</a></li>
				<li><span class="separator">|</span><a href="./v/message/selfreply/" class="choice">貼文回覆</a></li>
				<li><span class="separator">|</span><a href="./v/message/mentions/" class="choice">用戶名被提及</a></li>
			</ul>
		</div>
	</div>

	<div class="spacer">
		<div id="siteTable" class="sitetable linklisting">

			{foreach $things as $entry}
				{include file="common/message.tpl"}
			{/foreach}

		</div>
	</div>
</div>

<div id="footer"></div>
{include file="common/login-modal.tpl"}

{include file='common/comment-reply-edit.tpl'}
</body>


<script type="text/javascript" src="./static/js/message-inbox.js?v=8"></script>
</html>