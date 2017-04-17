
    {include file="common/page-header.tpl"}
</head>
<body class="{if $logined eq "true"}loggedin{/if} {$page}-page">


<div id="header">
    <div id="header-bottom-left">
        <span class="hover pagename"><a href="./">&nbsp;AMA</a></span>
        <ul class="tabmenu ">
            <li class="selected"><a href="./v/comments/{$things[0].thingid}" class="choice">留言</a></li>
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

    <form action="./api" class="usertext cloneable warn-on-unload" onsubmit="handleFormSubmit(this);return false;" id="form-comment-{$thingid}">
        <input type="hidden" name="action" value="submit-new-comment">
        <input type="hidden" name="main" value="{$thingid}">
        <input type="hidden" name="parent" value="{$thingid}">
        <div class="usertext-edit md-container" style="">
            <div class="md">
                <textarea rows="1" cols="1" name="content" class="" data-event-action="comment" data-type="link"></textarea>
            </div>
            <div class="bottom-area">
                <span class="help-toggle toggle" style="">
                    <a class="option active " href="#" tabindex="100" onclick="return toggle(this, helpon, helpoff)">格式說明</a>
                    <a class="option " href="#">隱藏說明</a>
                </span>
                <a href="/help/contentpolicy" class="reddiquette" target="_blank" tabindex="100">內容政策</a>
                <span class="error TOO_LONG field-text" style="display:none"></span>
                <span
                    class="error RATELIMIT field-ratelimit" style="display:none">
                </span>
                <span class="error NO_TEXT field-text" style="display:none"></span>
                <span class="error TOO_OLD field-parent" style="display:none"></span>
                <span class="error THREAD_LOCKED field-parent" style="display:none"></span>
                <span class="error DELETED_COMMENT field-parent" style="display:none"></span>
                <span class="error USER_BLOCKED field-parent" style="display:none"></span>
                <span class="error USER_MUTED field-parent" style="display:none"></span>
                <span class="error MUTED_FROM_SUBREDDIT field-parent" style="display:none"></span>

                <div class="usertext-buttons">
                    <button type="submit" onclick="" class="save">保存</button>
                    <button type="button" onclick="return cancel_usertext(this);" class="cancel" style="display:none;">取消</button>
                </div>
            </div>
            {include file="common/markhelp.tpl"}
        </div>
    </form>

    <div id="siteTable_{$thingid}" class="sitetable nestedlisting">
        {include file="common/comment.tpl"}
        {call name=renderComments data=$things[0].comments}
    </div>

</div>

<div id="footer"></div>
{include file='common/login-modal.tpl'}
{include file='common/comment-reply-edit.tpl'}
</body>


<script type="text/javascript" src="./static/js/comments.js?v=8"></script>
</html>