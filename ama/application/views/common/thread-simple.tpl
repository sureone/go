			
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
<!-- 				<div class="midcol unvoted" id="vote-{$entry.thingid}">
					<div class="arrow {if isset($entry.vote) and $entry.vote eq '1' }upmod{else}up{/if} login-required access-required" tabindex="0" onclick="voteit('./api',this,1,{$entry.thingid})"></div>

					{if $entry.parent eq 0}
					<div class="score dislikes" title="77">{$entry.dislikes}</div>
					<div class="score unvoted" title="78">{$entry.likes-$entry.dislikes}</div>
					<div class="score likes" title="79">{$entry.likes}</div>
					{/if}
					<div class="arrow {if isset($entry.vote) and $entry.vote eq '-1' }downmod{else}down{/if} login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,{$entry.thingid})"></div>
				</div> -->
				{/if}
				<div class="entry unvoted">
					{if $page eq "hot" or $page eq "new" }
						{if isset($entry.text) and $entry.text neq ''}
						<div class="expando-button collapsed selftext"></div>
						{/if}
					{/if}
					
						{if $pagetype eq "list"}
						 <p class="title"><span class="score unvoted" title="指标 {$entry.likes-$entry.dislikes}">{$entry.likes-$entry.dislikes}</span><a class="title may-blank loggedin " href="./v/a/{$entry.thingid}">
						 	{if isset($entry.title) and $entry.title neq '' }
								{$entry.title}
							{else}
								{$entry.text}
							{/if}

						 </a>
						</p>
						{else}
						
							{if isset($entry.title) and $entry.title neq '' }
								 <p class="title"><a class="title may-blank loggedin " href="./v/a/{$entry.thingid}">{$entry.title}</a></p>
							{else}
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("{$entry.text|regex_replace:'/[\r\t\n]/':'\\n'|regex_replace:'/[\"]/':'\\\"'|regex_replace:'/[\']/':'\\\''}"));</script>
 									
								</div>
							</div>
							{/if}
							
						{/if}
					

					
					<p class="tagline">
						 {if $entry.stype eq 'message'}
						 来自
						 {/if}
						 <a href="./v/user/{$entry.author}" class="author may-blank ">{$entry.author_name}</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="{$entry.timeago}"></time>

						 <a href="./v/a/{$entry.thingid}">{$entry.replies} 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


						{if $logined eq "true"}
						{if $user.userid eq $entry.author}
								<a href="./v/submit/{$entry.thingid}" data-thingid={$entry.thingid} class="editbtn access-required" data-event-action="edit">编辑</a>


								<a href="javascript:deleteit({$entry.thingid})" data-thingid={$entry.thingid} class="delbtn access-required" data-event-action="delete">删除</a>
						{/if}
						{/if}


						 
					</p>
					{if $pagetype eq "list"}
					<div class="expando expando-{$entry.thingid}" style="display: {if $page eq "comments"}block{else}none{/if};">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("{$entry.text|regex_replace:'/[\r\t\n]/':'\\n'|regex_replace:'/[\"]/':'\\\"'|regex_replace:'/[\']/':'\\\''}"));</script>
 									
								</div>
							</div>
						</form>
					</div>
					{/if}
					
					
					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>