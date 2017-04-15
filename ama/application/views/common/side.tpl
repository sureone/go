<div class="side">
    <div class="spacer">
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
    </div>
    {if $page eq "comments"}
        <div class="spacer">
            <div class="linkinfo">
                <div class="date"><span>本文發表於  </span>
                    <time datetime="2017-03-17T07:51:21+00:00">17 Mar 2017</time>
                </div>
                <div class="score"><span class="number">392</span> <span class="word">指標</span> (89% upvoted)</div>
                <div class="shortlink">shortlink: <input type="text" value="https://redd.it/5zwc09" readonly="readonly"
                                                         id="shortlink-text"></div>
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