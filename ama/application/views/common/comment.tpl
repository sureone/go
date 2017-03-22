{function name=renderComments level=0}
    {foreach $data as $entry}
    <div class=" thing id-{$entry.thingid} noncollapsed   comment " id="thing_{$entry.thingid}" onclick="">
        <p class="parent"><a name="df1l1xv"></a></p>
        <div class="midcol unvoted">
            <div class="arrow up login-required access-required" data-thingid="{$entry.thingid}" onclick="voteit(this,1)"></div>
            <div class="arrow down login-required access-required"  data-thingid="{$entry.thingid}" onclick="voteit(this,-1)"></div>
        </div>
        <div class="entry unvoted">
            <p class="tagline">
                <a href="javascript:void(0)" class="expand" onclick="return togglecomment(this)">[–]</a>
                <a href="./v/user/{$entry.userid}" class="author may-blank">{$entry.author}</a>
                <span class="userattrs"></span>
                <span class="score dislikes" title="44">44 指標</span>
                <span class="score unvoted" title="45">45 指標</span>
                <span class="score likes" title="46">46 指標</span>
                <time class="live-timestamp"> 22小時前 </time>
                &nbsp;
                <a href="javascript:void(0)" class="numchildren" onclick="return togglecomment(this)">(39下層留言)</a></p>
            <form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')"
                  id="form-t1_df1l1xva7c"><input type="hidden" name="thing_id" value="t1_df1l1xv">

                <div class="usertext-body may-blank-within md-container ">
                    <div class="md"><p>What was your daily routine ?</p>

                        <p>What did they give you uppon arrival (other than your razor) ?</p>

                        <p>How did you cope with boredom ?</p>
                    </div>
                </div>
            </form>
            <ul class="flat-list buttons">
                <li class="first"><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/df1l1xv/"
                     >永久連結</a></li>
                <li><a href="javascript:void(0)"
                                             class="embed-comment">embed</a></li>
                <li class="comment-save-button save-button"><a href="javascript:void(0)">儲存</a></li>
                <li class="report-button"><a href="javascript:void(0)" class="reportbtn access-required"
                                              data-thingid="{$entry.thingid}">檢舉</a></li>
                <li class="give-gold-button"><a href="">贈送金幣</a></li>
                <li class="reply-button"><a class="access-required" href="javascript:void(0)" data-thingid="{$entry.thingid}"  onclick="return reply(this)">回覆</a></li>
            </ul>
            <div class="reportform report-t1_df1l1xv"></div>
        </div>
        <div class="child" id="child_{$entry.thingid}">
            <div id="siteTable_child_{$entry.thingid}" class="sitetable listing">
                {if is_array($entry.comments)}
                    {renderComments data=$entry.comments level=$level+1}
                {/if}
            </div>
        </div>
        <div class="clearleft"></div>
    </div>
    {/foreach}
{/function}