<div id="header-bottom-left">
    {include file="common/page-logo.tpl"}
    <ul class="tabmenu ">
        <li {if $page eq "user-home"}class="selected"{/if}><a href="./v/user/{$userid}/home" class="choice">总览</a></li>
        <li {if $page eq "user-replies"}class="selected"{/if}><a href="./v/user/{$userid}/replies" class="choice">留言</a></li>
        <li {if $page eq "user-submitted"}class="selected"{/if}><a href="./v/user/{$userid}/submitted" class="choice">已发表</a></li>
        <li {if $page eq "user-saved"}class="selected"{/if}><a href="./v/user/{$userid}/saved" class="choice">收藏</a></li>
        <li {if $page eq "user-upvoted"}class="selected"{/if}><a href="./v/user/{$userid}/upvoted" class="choice">推</a></li>
        <li {if $page eq "user-downvoted"}class="selected"{/if}><a href="./v/user/{$userid}/downvoted" class="choice">嘘</a></li>
    </ul>
</div>