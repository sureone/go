<div class="side">
<!--     <div class="spacer">
        <form action="https://www.reddit.com/r/AMA/search" id="search" role="search">
        	<input type="text" name="q" placeholder="搜尋" tabindex="20">
        	<input type="submit" value="" tabindex="22">

            <div id="searchexpando" class="infobar" style="display: none;">
            	<label><input type="checkbox" name="restrict_sr" tabindex="21">搜尋範圍僅限 r/AMA</label>

                <div id="moresearchinfo" style=""><a href="#" id="search_hidemore">[-]</a>

                    <p>use the following search parameters to narrow your results:</p>
                    <dl>
                        <dt>subreddit:<i>subreddit</i></dt>
                        <dd>find submissions in "subreddit"</dd>
                        <dt>author:<i>username</i></dt>
                        <dd>依「使用者名稱」尋找發文</dd>
                        <dt>site:<i>example.com</i></dt>
                        <dd>find submissions from "example.com"</dd>
                        <dt>url:<i>text</i></dt>
                        <dd>search for "text" in url</dd>
                        <dt>selftext:<i>text</i></dt>
                        <dd>在自行發文的內容中搜尋「文字」</dd>
                        <dt>self:yes (or self:no)</dt>
                        <dd>包含 (或排除) 自己的文章</dd>
                        <dt>nsfw:yes (or nsfw:no)</dt>
                        <dd>納入 (或排除) 標記為不適合公開閱覽的結果</dd>
                    </dl>
                    <p>e.g. <code>subreddit:aww site:imgur.com dog</code></p>

                    <p><a href="https://www.reddit.com/wiki/search">see the search faq for details.</a></p></div>
                <p><a href="https://www.reddit.com/wiki/search" id="search_showmore">進階搜尋：依照作者、版面...</a></p></div>
        </form>
    </div> -->
    {if $page eq "comments"}
        {if isset($things)}
        {foreach $things as $entry}
        <div class="spacer">
            <div class="linkinfo">
                <div class="date"><a style="font-size:medium;padding-right:4px;" href="./v/user/{$entry.author}">{$entry.author_name}</a><span>发表于 </span>
                    <time datetime="{$entry.timeago}">{$entry.timeago}</time>
                </div>
                <div class="score"><span class="number">{$entry.likes-$entry.dislikes}</span> <span class="word">指标</span> (其中{$entry.likes}票赞成)</div>
                <div class="shortlink">本文链接: <input type="text" value="http://boopo.cn/v/a/{$entry.thingid}" readonly="readonly"
                                                         id="shortlink-text"></div>
            </div>
        </div>
        {/foreach} 
        {/if}  
        
    {/if}

    {if $logined eq "true" and isset($pagedir) and $pagedir eq "user"}
    <div class="spacer">
        <div class="sidebox submit submit-text">
            <div class="morelink">
                <a href="./v/message/compose/{$userid}" class="login-required access-required" target="_top">给{$username}发送私信</a>
                <div class="nub"></div>
            </div>
        </div>
    </div>
    {/if}

    <div class="spacer">
        <div class="sidebox submit submit-text">
            <div class="morelink">
            	<a href="./v/submit" class="login-required access-required" target="_top">发表新文章</a>
                <div class="nub"></div>
            </div>
        </div>
    </div>
</div>