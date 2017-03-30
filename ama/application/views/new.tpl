<html lang="en">
<head>
    {include file="common/page-header.tpl"}
</head>

<body class="listing-page {if $logined eq "true"}loggedin{/if} {$page}-page">


<div id="header">
<div id="header-bottom-left">
    <span class="hover pagename"><a href="./">AMA</a></span>
    <ul class="tabmenu ">
        <li><a href="./v/hot" class="choice">熱門</a></li>
        <li class="selected"><a href="./v/new" class="choice">最新</a></li>
        <li><a href="./v/rising" class="choice">好評上升中</a></li>
        <li><a href="./v/controversial" class="choice">具爭議的</a></li>
        <li><a href="./v/top" class="choice">頭等</a></li>
        <li><a href="./v/gilded" class="choice">精選</a></li>
        <li><a href="./v/wiki" class="choice">wiki</a></li>
        <li><a href="./v/ads" class="choice">宣傳過的</a></li>
    </ul>
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


<script type="text/javascript" src="./static/js/hot.js?v=8"></script>
</html>