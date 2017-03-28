			<div class="thing odd {if $entry.parent neq 0}comment{else}link self{/if}" id="thing_{$entry.thingid}" data-thingid={$entry.thingid}>
				{if $entry.parent neq 0}
				<p class="parent">
					<a name="dffeipd"></a>
					<a href="./v/comments/{$entry.p_id}" class="title">
						{if isset($entry.p_title) and $entry.p_title neq '' }
							{$entry.p_title}
						{else}
							{$entry.p_text}
						{/if}
					</a>
					 by 
					<a href="./v/user/{$entry.p_author}" class="author may-blank id-t2_11v90c">{$entry.p_author}</a>
					<span class="userattrs"></span>
				</p>
				{/if}

				{if $page neq "comments"}
				<span class="rank"></span>
				{/if}
				<div class="midcol unvoted">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,{$entry.thingid})"></div>

					{if $entry.parent eq 0}
					<div class="score dislikes" title="77">{$entry.dislikes}</div>
					<div class="score unvoted" title="78">{$entry.likes-$entry.dislikes}</div>
					<div class="score likes" title="79">{$entry.likes}</div>
					{/if}
					<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,{$entry.thingid})"></div>
				</div>
				<div class="entry unvoted">
					
						{if $pagetype eq "list"}
						 <p class="title"><a class="title may-blank loggedin " href="./v/comments/{$entry.thingid}">{$entry.title}</a></p>
						{else}
						
							{if isset($entry.title) and $entry.title neq '' }
								 <p class="title"><a class="title may-blank loggedin " href="./v/comments/{$entry.thingid}">{$entry.title}</a></p>
							{else}
							<div class="usertext-body may-blank-within md-container ">
								<div class="md">
								{$entry.text}
								</div>
							</div>
							{/if}
							
						{/if}
					

					{if $page eq "hot"}
						<div class="expando-button collapsed selftext"></div>
					{/if}
					<p class="tagline">
						 <a href="./v/user/{$entry.author}" class="author may-blank ">{$entry.author}</a>
						 <span class="userattrs"></span>
						 发表于 
						 <time class="live-timestamp timeago" datetime="{$entry.timeago}"></time>
						 
					</p>
					{if $pagetype eq "list"}
					<div class="expando expando-{$entry.thingid}" style="display: {if $page eq "comments"}block{else}none{/if};">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="md">
									<p>{$entry.text}</p>
								</div>
							</div>
						</form>
					</div>
					{/if}
					
					<ul class="flat-list buttons">
						<li class="first"><a href="./v/comments/{$entry.thingid}">{$entry.replies} 留言</a></li>
						<li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li>
						<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,{$entry.thingid});return false;">儲存</a></li>
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