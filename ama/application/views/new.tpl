
    {include file="common/page-header.tpl"}

</head>

<body class="listing-page {if $logined eq "true"}loggedin{/if} {$page}-page">


<div id="header">
{include file="header-bottom-left.tpl"}

{include file="common/header-bottom-right.tpl"}
</div>
{include file="common/side.tpl"}

<div class="content">
    <div class="spacer">
        <div id="siteTable" class="sitetable linklisting">
            {foreach $things as $entry}
                {include file="common/thread-simple.tpl"}
            {/foreach}

        </div>
    </div>
</div>

<div id="footer"></div>
{include file="common/login-modal.tpl"}
</body>


<script type="text/javascript" src="./static/js/hot.js?v=8"></script>
</html>