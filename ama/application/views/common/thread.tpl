			<div class="thing odd  link self" id="thing_{$things[a].thingid}" data-thingid={$things[a].thingid}>
				<p class="parent"></p>
				{if $page neq "comments"}
				<span class="rank">{$things[a].no}</span>
				{/if}
				<div class="midcol unvoted">
					<div class="arrow up login-required access-required" tabindex="0"  data-thingid="{$things[a].thingid}" onclick="voteit(this,1)"></div>
					<div class="score dislikes" title="77">{$things[a].dislikes}</div>
					<div class="score unvoted" title="78">{$things[a].score}</div>
					<div class="score likes" title="79">{$things[a].likes}</div>
					<div class="arrow down login-required access-required" tabindex="0"  data-thingid="{$things[a].thingid}" onclick="voteit(this,-1)"></div>
				</div>
				<div class="entry unvoted">
					<p class="title">
						<a class="title may-blank loggedin " href="./v/comments/{$things[a].thingid}">{$things[a].title}</a> 
						
					</p>
					{if $page neq "comments"}
						<div class="expando-button collapsed selftext"></div>
					{/if}
					<p class="tagline">发表 <time class="live-timestamp">{$things[a].timeago}</time>by
						 <a href="" class="author may-blank ">{$things[a].author}</a>
						 <span class="userattrs"></span>
					</p>
					{if $page eq "comments"}
					<div class="expando"><form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt"><input type="hidden" name="thing_id" value="t3_609l7s"><div class="usertext-body may-blank-within md-container "><div class="md"><p>I'm  a young programmer who wants to create a few apps for the African environment and beyond. As well as help promote African hip hop on the internet.</p>
					</div>
					</div></form></div>
					{/if}
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

					<div class="expando expando-uninitialized expando-{$things[a].thingid}" style="display: none">
						<span class="error">loading...</span>
					</div>
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>