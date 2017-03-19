<script type="text/x-handlebars" id="tpl-thread-item-new">
<div class=" thing linkflair linkflair-music odd  link self" id="thing_{{thingid}}" data-thingid="{{thingid}}">
	<p class="parent"></p>
	<span class="rank">{{no}}</span>
	<div class="midcol unvoted">
		<div class="arrow up login-required access-required"></div>
		<div class="score dislikes">{{dislikes}}</div>
		<div class="score unvoted">{{score}}</div>
		<div class="score likes">{{like}}</div>
		<div class="arrow down login-required access-required"></div>
	</div>
	<a class="thumbnail self may-blank loggedin " href="></a>
	<div class="entry unvoted">
		<p class="title">
			<span class="linkflairlabel" title="Music">Music</span>
			<a class="title may-blank loggedin ">{{title}}</a>
			
		</p>
		<div class="expando-button collapsed selftext"></div>
		<p class="tagline">
			submitted <time>18小時前</time> 
			<time class="edited-timestamp">*</time> by <a href="" class="author may-blank ">{{author}}</a>
			<span class="userattrs"></span>
		</p>
		<ul class="flat-list buttons">
			<li class="first"><a href="">393 留言</a></li>
			<li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li>
			<li class="link-save-button save-button"><a href="#">儲存</a></li>
			<li>
				<form action="/post/hide" method="post" class="state-button hide-button">
					<input type="hidden" name="executed" value="隱藏">
					<span><a href="javascript:void(0)" class=" ">隱藏</a></span>
				</form>
			</li>
			<li class="report-button"><a href="javascript:void(0)" class="reportbtn access-required">檢舉</a></li>
		</ul>
		<div class="reportform"></div>
		<div class="expando expando-uninitialized" style="display: none">
			<span class="error">loading...</span>
		</div>
	</div>
	<div class="child"></div>
	<div class="clearleft"></div>
</div>

</script>
<script type="text/x-handlebars" id="tpl-thread-text">
<form action="#" class="usertext warn-on-unload" id="form-t3_6023n78wi">
	<input type="hidden" name="thing_id" value="t3_6023n7">
	<div class="usertext-body may-blank-within md-container ">
	<div class="md">
	{{text}}
	</div>
	</div>
</form>
</script>
<script type="text/x-handlebars" id="tpl-thread-item">
	<div class="thing odd  link self" id="thing_{{idx}}" data-idx={{idx}}>
		<p class="parent"></p>
		<span class="rank">{{no}}</span>
		<div class="midcol unvoted">
			<div class="arrow up login-required access-required" tabindex="0"  data-thingid="{{thingid}}" onclick="voteit(this,1)"></div>
			<div class="score dislikes" title="77">{{dislikes}}</div>
			<div class="score unvoted" title="78">{{score}}</div>
			<div class="score likes" title="79">{{likes}}</div>
			<div class="arrow down login-required access-required" tabindex="0"  data-thingid="{{thingid}}" onclick="voteit(this,-1)"></div>
		</div>
		<div class="entry unvoted">
			<p class="title">
				<a class="title may-blank loggedin " href="./v/comments/{{thingid}}">{{title}}</a> 
				
			</p>
			<div class="expando-button collapsed selftext"></div>
			<p class="tagline">发表 <time class="live-timestamp">{{timeago}}</time>by
				 <a href="" class="author may-blank ">{{author}}</a>
				 <span class="userattrs"></span>
			</p>
			<ul class="flat-list buttons">
				<li class="first"><a href="">139 留言</a></li>
				<li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li>
				<li class="link-save-button save-button"><a href="#">儲存</a></li>
				<li>
					<form action="/post/hide" method="post" class="state-button hide-button">
						<input type="hidden" name="executed" value="隱藏">
						<span>
							<a href="javascript:void(0)" class=" " onclick="change_state(this, 'hide', hide_thing);">隱藏</a>
						</span>
					</form>
				</li>
				<li class="report-button">
					<a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">檢舉</a>
				</li>
			</ul>
			<div class="reportform"></div>
			<div class="expando expando-uninitialized expando-{{idx}}" style="display: none">
				<span class="error">loading...</span>
			</div>
		</div>
		<div class="child"></div>
		<div class="clearleft"></div>
	</div>


</script>
<script type="text/x-handlebars" id="tpl-comment">
    <div class=" thing id-{{thingid}} noncollapsed   comment " id="thing_{{thingid}}" onclick="">
        <p class="parent"><a name="df1l1xv"></a></p>
        <div class="midcol unvoted">
            <div class="arrow up login-required access-required" data-thingid="{{thingid}}" onclick="voteit(this,1)"></div>
            <div class="arrow down login-required access-required"  data-thingid="{{thingid}}" onclick="voteit(this,-1)"></div>
        </div>
        <div class="entry unvoted">
            <p class="tagline">
                <a href="javascript:void(0)" class="expand" onclick="return togglecomment(this)">[–]</a>
                <a href="./v/user/{{userid}}" class="author may-blank">{{author}}</a>
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
                                              data-thingid="{{thingid}}">檢舉</a></li>
                <li class="give-gold-button"><a href="/gold?goldtype=gift&amp;months=1&amp;thing=t1_df1l1xv"
                                                title="give reddit gold in appreciation of this post."
                                                class="give-gold login-required access-required"
                                                data-event-action="gild">贈送金幣</a></li>
                <li class="reply-button"><a class="access-required" href="javascript:void(0)" data-thingid="{{thingid}}"  onclick="return reply(this)">回覆</a></li>
            </ul>
            <div class="reportform report-t1_df1l1xv"></div>
        </div>
        <div class="child" id="child_{{thingid}}">
            <div id="siteTable_child_{{thingid}}" class="sitetable listing">

            </div>
        </div>
        <div class="clearleft"></div>
    </div>
</script>

<script type="text/x-handlebars" id="tpl-comment-edit">
<form action="#" class="usertext cloneable warn-on-unload" onsubmit="return post_form(this, 'comment')" id="form-comment-{{thingid}}">
	<input type="hidden" name="thingid" value="{{thingid}}">
	<div class="usertext-edit md-container" style="">
		<div class="md">
			<textarea rows="1" cols="1" name="text" class="" data-event-action="comment" data-type="link"></textarea>
		</div>
		<?php include "markhelp.php" ?>
	</div>
</form>

</script>