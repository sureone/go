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
			<span class="domain">(<a href="/r/IAmA/">self.IAmA</a>)</span>
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
	<div class="usertext-body may-blank-within md-container ">
	<div class="md">
	{{text}}
	</div>
	</div>
</script>
<script type="text/x-handlebars" id="tpl-thread-item">
	<div class="thing odd  link self" id="thing_{{idx}}" data-idx={{idx}}>
		<p class="parent"></p>
		<span class="rank">{{no}}</span>
		<div class="midcol unvoted">
			<div class="arrow up login-required access-required" tabindex="0"></div>
			<div class="score dislikes" title="77">{{dislikes}}</div>
			<div class="score unvoted" title="78">{{score}}</div>
			<div class="score likes" title="79">{{likes}}</div>
			<div class="arrow down login-required access-required" tabindex="0"></div>
		</div>
		<div class="entry unvoted">
			<p class="title">
				<a class="title may-blank loggedin " href="./v/comments/{{thingid}}">{{title}}</a> 
				<span class="domain">(<a href="/r/AMA/">self.AMA</a>)</span>
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