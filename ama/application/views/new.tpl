
    {include file="common/page-header.tpl"}
    <style type="text/css">
		.thing{
            display: inline-block;
            margin: 0 0px 0px 0; 
            padding: 2px;
            min-width:300px;
        }

        .listing-page .linklisting .thing {
            position: relative;
            margin: 0 0px 0px 0;
        }


        .link .title {
          
            /*max-width: 400px;*/
           
            /*text-overflow: ellipsis;*/
            /*overflow: hidden;*/
            /*white-space: nowrap;*/
        }
	</style>
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