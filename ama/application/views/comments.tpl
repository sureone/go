
    {include file="common/page-header.tpl"}
</head>
<body class="{if $logined eq "true"}loggedin{/if} {$page}-page">
{include file='common/comment-reply-edit.tpl'}

<div id="header">
    <div id="header-bottom-left">
        {include file="common/page-logo.tpl"}
        
        <ul class="tabmenu ">
            <li class="selected"><a href="./v/a/{$things[0].thingid}" class="choice">留言</a></li>
        </ul>
    </div>
    {include file="common/header-bottom-right.tpl"}
</div>
{include file='common/side.tpl'}

<div class="content">

    <div id="siteTable" class="sitetable linklisting">
        {foreach $things as $entry}
            {include file="common/thread.tpl"}
        {/foreach}   


    </div>
    <div class="commentarea">
        <div class="panestack-title"><span class="title">头{$things[0].comments_count}则留言</span><a
                href="/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?limit=500"
                class="title-button ">显示所有{$things[0].replies}</a></div>
        <div class="menuarea">
            <div class="spacer"><span class="dropdown-title lightdrop">排序依据: </span>

                <div class="dropdown lightdrop" onclick="open_menu(this)"><span class="selected">最佳</span></div>
                <div class="drop-choices lightdrop"><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=top"
                        class="choice">頭等</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=new"
                        class="choice">最新</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=controversial"
                        class="choice">具爭議的</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=old"
                        class="choice">最舊</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=random"
                        class="hidden choice">隨機</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=qa"
                        class="choice">問與答</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=live"
                        class="hidden choice">live (beta)</a></div>
            </div>
            <div class="spacer"></div>
        </div>
    </div>

    <script>
      var thingid = {$things[0].thingid};
        var mainid =   {$things[0].thingid};       
       {literal}
        var tpl = Handlebars.compile($("#tpl-comment-edit").html());
        h = (tpl({thingid:thingid,mainid:mainid}));

        document.write(h);
        {/literal}
    </script>

    <div id="siteTable_{$thingid}" class="sitetable nestedlisting">
        {include file="common/comment.tpl"}
        {call name=renderComments data=$things[0].comments}
    </div>

</div>
<iframe src="" style="display:none;" id="iframe_upload" name="iframe_upload"></iframe>
<div id="footer"></div>
{include file='common/login-modal.tpl'}

</body>
<script id="tpl-attach-tool" type="text/x-handlebars-template">
<div class="attach-tool" style="border:1px dotted gray;">
    <span class="title required-roundfield">附件</span>
    <ul id="attaches">   
    </ul>    
    <form action="./v/do_upload" enctype="multipart/form-data" method="post" accept-charset="utf-8" target="iframe_upload">
        <input type="file" name="userfile" size="20" />
        <input type="submit" value="upload" />
    </form>
</div>
</script>
{include 'common/file-attach.tpl'}
<script type="text/javascript" src="./static/js/comments.js?v=8"></script>
</html>