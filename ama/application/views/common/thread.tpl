			<div class="thing odd {$entry.stype}" id="thing_{$entry.thingid}" data-thingid={$entry.thingid}>
				{if $entry.parent neq 0}
				 
				<p class="parent">
					<span class="subject-text">回帖</span>
					<a name="dffeipd"></a>

					<a href="./v/a/{$entry.parent}" class="title">
						{if isset($entry.p_title) and $entry.p_title neq '' }
							{$entry.p_title}
						{else}
							{$entry.p_text}
						{/if}
					</a>
					 by 
					<a href="./v/user/{$entry.p_author}" class="author may-blank id-t2_11v90c">{$entry.p_author_name}</a>
					<span class="userattrs"></span>
				</p>
				{/if}

				{if $page neq "comments"}
				<span class="rank"></span>
				{/if}
				{if $page neq "message-inbox"}
				<div class="midcol unvoted" id="vote-{$entry.thingid}">
					<div class="arrow {if isset($entry.vote) and $entry.vote eq '1' }upmod{else}up{/if} login-required access-required" tabindex="0" onclick="voteit('./api',this,1,{$entry.thingid})"></div>

					{if $entry.parent eq 0}
					<div class="score dislikes" title="77">{$entry.dislikes}</div>
					<div class="score unvoted" title="78">{$entry.likes-$entry.dislikes}</div>
					<div class="score likes" title="79">{$entry.likes}</div>
					{/if}
					<div class="arrow {if isset($entry.vote) and $entry.vote eq '-1' }downmod{else}down{/if} login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,{$entry.thingid})"></div>
				</div>
				{/if}
				<div class="entry unvoted">
					
						{if $pagetype eq "list"}
						 <p class="title"><a class="title may-blank loggedin " href="./v/a/{$entry.thingid}">{$entry.title}</a></p>
						{else}
						
							{if isset($entry.title) and $entry.title neq '' }
								 <p class="title"><a class="title may-blank loggedin " href="./v/a/{$entry.thingid}">{$entry.title}</a></p>
							{else}
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("{$entry.text|regex_replace:'/[\r\t\n]/':'\\n'|regex_replace:'/[\"]/':'\\\"'|regex_replace:'/[\']/':'\\\''}"));</script></div>
							</div>
							{/if}
							
						{/if}
					

					{if $page eq "hot"}
						<div class="expando-button collapsed selftext"></div>
					{/if}
					<p class="tagline">
						 {if $entry.stype eq 'message'}
						 来自
						 {/if}
						 <a href="./v/user/{$entry.author}" class="author may-blank ">{$entry.author_name}</a>
						 
						 <span class="userattrs"></span>

						 {if $entry.stype neq 'message'}
						 发表于
						 {/if} 
						 <time class="live-timestamp timeago" datetime="{$entry.timeago}"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/a/{$entry.thingid}">{$entry.replies} 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,{$entry.thingid});return false;">收藏</a></li>
							<!-- <li>
								<form action="/post/hide" method="post" class="state-button hide-button">
									<input type="hidden" name="executed" value="隱藏">
									<span>
										<a href="javascript:void(0)" class=" " onclick="change_state(this, 'hide', hide_thing);">隱藏</a>
									</span>
								</form>
							</li> -->
							<li class="report-button">
								<a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>
							</li>

							{if $logined eq "true"}
							{if $user.userid eq $entry.author}
							<li class="edit-button">
								<a href="./v/submit/{$entry.thingid}" data-thingid={$entry.thingid} class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
							{/if}
							{/if}
						</ul>
						 
					</p>
					{if $pagetype eq "list"}
					<div class="expando expando-{$entry.thingid}" style="display: {if $page eq "comments"}block{else}none{/if};">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("{$entry.text|regex_replace:'/[\r\t\n]/':'\\n'|regex_replace:'/[\"]/':'\\\"'|regex_replace:'/[\']/':'\\\''}"));</script></div>
							</div>
						</form>
					</div>
					{/if}
					
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>