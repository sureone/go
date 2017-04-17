
	{include file="common/page-header.tpl"}
</head>

<body class="listing-page {if $logined eq "true"}loggedin{/if} {$page}-page">


<div id="header">
<div id="header-bottom-left">
	<span class="hover pagename"><a href="./">信息</a></span>
	<ul class="tabmenu ">
		<li><a href="./v/message/compose" class="choice">傳送一個私人訊息</a></li>
		<li><a href="./v/message/inbox" class="choice">收件匣</a></li>
		<li class="selected"><a href="./v/message/sent" class="choice">发件箱</a></li>
</div>
{include file="common/header-bottom-right.tpl"}
</div>
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