			<div class="thing odd  link self" id="thing_{$entry.thingid}" data-thingid={$entry.thingid}>
				<p class="parent"></p>
				{if $page neq "comments"}
				<span class="rank"></span>
				{/if}
				<div class="midcol unvoted">
					<div class="arrow up login-required access-required" tabindex="0"  data-thingid="{$entry.thingid}" onclick="voteit(this,1)"></div>
					<div class="score dislikes" title="77">{$entry.dislikes}</div>
					<div class="score unvoted" title="78">{$entry.likes-$entry.dislikes}</div>
					<div class="score likes" title="79">{$entry.likes}</div>
					<div class="arrow down login-required access-required" tabindex="0"  data-thingid="{$entry.thingid}" onclick="voteit(this,-1)"></div>
				</div>
				<div class="entry unvoted">
					<p class="title">
						<a class="title may-blank loggedin " href="./v/comments/{$entry.thingid}">{$entry.title}</a> 
						
					</p>s
					{if $page neq "comments"}
						<div class="expando-button collapsed selftext"></div>
					{/if}
					<p class="tagline">
						 <a href="./v/user/{$entry.author}" class="author may-blank ">{$entry.author}</a>
						 <span class="userattrs"></span>
						 发表于 
						 <time class="live-timestamp timeago" datetime="{$entry.timeago}"></time>
						 
					</p>
					
					<div class="expando expando-{$entry.thingid}" style="display: {if $page eq "comments"}block{else}none{/if};"><form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt"><input type="hidden" name="thing_id" value="t3_609l7s"><div class="usertext-body may-blank-within md-container "><div class="md"><p>{$entry.text}</p>
					</div>
					</div></form></div>
					
					<ul class="flat-list buttons">
						<li class="first"><a href="">{$entry.replies} 留言</a></li>
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

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>