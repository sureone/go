<?php
/* Smarty version 3.1.30, created on 2017-04-18 08:57:38
  from "D:\work\go\ama\application\views\user-home.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58f5b8e2260482_72537263',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'ff7ef0d0df77a67ff274d34bb0accc8eb64ac9d9' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\user-home.tpl',
      1 => 1492414513,
      2 => 'file',
    ),
    '2cba0286b045df067f392a1b6c88c5e91e74ffac' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\page-header.tpl',
      1 => 1492496511,
      2 => 'file',
    ),
    'f6a422a540377ed7f7be4e15dc86471b0e809685' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\user-header-bottom-left.tpl',
      1 => 1492498592,
      2 => 'file',
    ),
    'f9425a6b5d947a44cc0dd8983944da6b322d1daa' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\page-logo.tpl',
      1 => 1492498622,
      2 => 'file',
    ),
    'f0f6ee9aad64c0aa7300ae99fc743a68b3429bac' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\header-bottom-right.tpl',
      1 => 1492481504,
      2 => 'file',
    ),
    '909714a59f76f675a88c2ed245e2941851f8dac9' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\side.tpl',
      1 => 1492390703,
      2 => 'file',
    ),
    '64fc306606431e9d5e18d170ac85e1f62dcf4f19' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\thread.tpl',
      1 => 1492399935,
      2 => 'file',
    ),
    '4b6dd1202b4b103f1802898db3a09d2c6f39e99e' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\login-modal.tpl',
      1 => 1492390703,
      2 => 'file',
    ),
  ),
  'cache_lifetime' => 10,
),true)) {
function content_58f5b8e2260482_72537263 (Smarty_Internal_Template $_smarty_tpl) {
?>

	<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
	<meta charset="utf-8">
    <base href="http://127.0.0.1/ama/index.php">
        <title>后园小亭</title>
    	<style type="text/css">
	</style>
	<link rel="stylesheet" href="./static/css/common.css?v=2" type="text/css" />
    <script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="http://apps.bdimg.com/libs/handlebars.js/1.3.0/handlebars.min.js"></script>
    <script src="./static/js/form2json.js"></script>
    <script src="./static/js/common.js?v=7"></script>
    <script src="./static/js/jquery.timeago.js?v=1"></script>
    <script src="./static/js/markdown.js?v=3"></script>
    <script type="text/javascript" src="./static/js/ama.js?v=9"></script>

    <script type="text/javascript">
    	var g_logined = true;
    </script>
    
</head>

<body class="listing-page loggedin user-home-page">


<div id="header">
<div id="header-bottom-left">
    <span class="hover pagename"><a href="./">小网</a></span>
    <ul class="tabmenu ">
        <li class="selected"><a href="./v/user/sureone/home" class="choice">总览</a></li>
        <li ><a href="./v/user/sureone/replies" class="choice">留言</a></li>
        <li ><a href="./v/user/sureone/submitted" class="choice">已发表</a></li>
        <li ><a href="./v/user/sureone/saved" class="choice">收藏</a></li>
        <li ><a href="./v/user/sureone/upvoted" class="choice">推</a></li>
        <li ><a href="./v/user/sureone/downvoted" class="choice">嘘</a></li>
    </ul>

    
</div>
<div id="header-bottom-right">
<span class="user"><a href="./v/user/sureone/">小网</a>&nbsp;(<span class="userkarma" title="post karma">1</span>)</span><span class="separator">|</span><a title="沒有新郵件" href="./v/message/inbox/" class="nohavemail" id="mail">信息</a>
<!-- 
<span class="separator">|</span><ul class="flat-list hover">
<li><a href="https://www.reddit.com/prefs/" class="pref-lang choice">偏好設定</a></li>
</ul>
 -->
<span class="separator">|</span><form method="post" id="logout-form" action="./api" class="logout hover"><input type="hidden" name="uh" value="cqaeyi63ta407191a49905d7c0b26e7c2a75cb1b81ddd77995"><input type="hidden" name="top" value="off"><input type="hidden" name="action" value="logout"><a href="javascript:void(0)" onclick="$(this).parent().submit()">登出</a></form>

</div>
</div>
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
        <div class="spacer">
        <div class="sidebox submit submit-text">
            <div class="morelink">
            	<a href="./v/submit" class="login-required access-required" target="_top">发表新文章</a>
                <div class="nub"></div>
            </div>
        </div>
    </div>
</div>

<div class="content">
	<div class="spacer">
		<div id="siteTable" class="sitetable linklisting">
			
										<div class="thing odd link" id="thing_75" data-thingid=75>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-75">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,75)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,75)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/comments/75">121212</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-17 16:45:05"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/75">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,75);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/75" data-thingid=75 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_74" data-thingid=74>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-74">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,74)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,74)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/comments/74">fdsafds</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-17 16:44:57"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/74">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,74);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/74" data-thingid=74 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_73" data-thingid=73>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-73">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,73)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">1</div>
					<div class="score likes" title="79">1</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,73)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/comments/73">dfsafdsa111111111</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-17 16:38:49"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/73">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,73);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/73" data-thingid=73 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_72" data-thingid=72>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-72">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,72)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,72)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/comments/72">gfdsgfdsgfdsg3rertwetre</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-17 16:38:30"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/72">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,72);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/72" data-thingid=72 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_71" data-thingid=71>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-71">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,71)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,71)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/comments/71">dfsafdsafsd</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-17 16:38:03"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/71">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,71);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/71" data-thingid=71 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_70" data-thingid=70>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-70">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,70)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,70)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/comments/70">test</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-17 16:37:21"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/70">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,70);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/70" data-thingid=70 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_69" data-thingid=69>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-69">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,69)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,69)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/comments/69">雨霖铃·寒蝉凄切， 寒蝉凄切， 对长亭晚， 骤雨初歇。都门帐饮无绪， 留恋处， 兰舟催发。念去去， 千里烟波， 暮霭沉沉楚天阔。

多情自古伤离别， 更那堪， 冷落清秋节！</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-17 16:12:31"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/69">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,69);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/69" data-thingid=69 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd comment" id="thing_65" data-thingid=65>
								 
				<p class="parent">
					<span class="subject-text">回帖</span>
					<a name="dffeipd"></a>

					<a href="./v/comments/56" class="title">
													
											</a>
					 by 
					<a href="./v/user/" class="author may-blank id-t2_11v90c"></a>
					<span class="userattrs"></span>
				</p>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-65">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,65)"></div>

										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,65)"></div>
				</div>
								<div class="entry unvoted">
					
												
														<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("dsadfsafsadfsafdsafdsafdsa"));</script></div>
							</div>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-10 17:26:36"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/65">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,65);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/65" data-thingid=65 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd comment" id="thing_64" data-thingid=64>
								 
				<p class="parent">
					<span class="subject-text">回帖</span>
					<a name="dffeipd"></a>

					<a href="./v/comments/56" class="title">
													
											</a>
					 by 
					<a href="./v/user/" class="author may-blank id-t2_11v90c"></a>
					<span class="userattrs"></span>
				</p>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-64">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,64)"></div>

										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,64)"></div>
				</div>
								<div class="entry unvoted">
					
												
														<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("dsadfsafdsafdasfewfewfewfewfewf"));</script></div>
							</div>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-10 17:26:33"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/64">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,64);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/64" data-thingid=64 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd comment" id="thing_63" data-thingid=63>
								 
				<p class="parent">
					<span class="subject-text">回帖</span>
					<a name="dffeipd"></a>

					<a href="./v/comments/56" class="title">
													
											</a>
					 by 
					<a href="./v/user/" class="author may-blank id-t2_11v90c"></a>
					<span class="userattrs"></span>
				</p>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-63">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,63)"></div>

										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,63)"></div>
				</div>
								<div class="entry unvoted">
					
												
														<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("dsafasfewfew"));</script></div>
							</div>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-10 17:26:28"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/63">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,63);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/63" data-thingid=63 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd comment" id="thing_62" data-thingid=62>
								 
				<p class="parent">
					<span class="subject-text">回帖</span>
					<a name="dffeipd"></a>

					<a href="./v/comments/56" class="title">
													
											</a>
					 by 
					<a href="./v/user/" class="author may-blank id-t2_11v90c"></a>
					<span class="userattrs"></span>
				</p>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-62">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,62)"></div>

										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,62)"></div>
				</div>
								<div class="entry unvoted">
					
												
														<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("3333333333333333333333333333333333333333"));</script></div>
							</div>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-01 08:31:36"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/62">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,62);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/62" data-thingid=62 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd comment" id="thing_61" data-thingid=61>
								 
				<p class="parent">
					<span class="subject-text">回帖</span>
					<a name="dffeipd"></a>

					<a href="./v/comments/56" class="title">
													
											</a>
					 by 
					<a href="./v/user/" class="author may-blank id-t2_11v90c"></a>
					<span class="userattrs"></span>
				</p>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-61">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,61)"></div>

										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,61)"></div>
				</div>
								<div class="entry unvoted">
					
												
														<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("2222222222222222222222222"));</script></div>
							</div>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-01 08:31:31"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/61">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,61);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/61" data-thingid=61 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd comment" id="thing_60" data-thingid=60>
								 
				<p class="parent">
					<span class="subject-text">回帖</span>
					<a name="dffeipd"></a>

					<a href="./v/comments/59" class="title">
													fdsafdsafdsafdsa
											</a>
					 by 
					<a href="./v/user/sureone" class="author may-blank id-t2_11v90c">小网</a>
					<span class="userattrs"></span>
				</p>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-60">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,60)"></div>

										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,60)"></div>
				</div>
								<div class="entry unvoted">
					
												
														<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("1111111111111111111"));</script></div>
							</div>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-01 08:31:27"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/60">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,60);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/60" data-thingid=60 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd comment" id="thing_59" data-thingid=59>
								 
				<p class="parent">
					<span class="subject-text">回帖</span>
					<a name="dffeipd"></a>

					<a href="./v/comments/58" class="title">
													fdsafdsafdsafdsafdsafds
											</a>
					 by 
					<a href="./v/user/sureone" class="author may-blank id-t2_11v90c">小网</a>
					<span class="userattrs"></span>
				</p>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-59">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,59)"></div>

										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,59)"></div>
				</div>
								<div class="entry unvoted">
					
												
														<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("fdsafdsafdsafdsa"));</script></div>
							</div>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-01 08:31:23"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/59">1 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,59);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/59" data-thingid=59 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd comment" id="thing_58" data-thingid=58>
								 
				<p class="parent">
					<span class="subject-text">回帖</span>
					<a name="dffeipd"></a>

					<a href="./v/comments/57" class="title">
													hello docker hello docker hello docker
											</a>
					 by 
					<a href="./v/user/sureone" class="author may-blank id-t2_11v90c">小网</a>
					<span class="userattrs"></span>
				</p>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-58">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,58)"></div>

										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,58)"></div>
				</div>
								<div class="entry unvoted">
					
												
														<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("fdsafdsafdsafdsafdsafds"));</script></div>
							</div>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-01 08:31:18"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/58">1 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,58);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/58" data-thingid=58 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd comment" id="thing_57" data-thingid=57>
								 
				<p class="parent">
					<span class="subject-text">回帖</span>
					<a name="dffeipd"></a>

					<a href="./v/comments/56" class="title">
													
											</a>
					 by 
					<a href="./v/user/" class="author may-blank id-t2_11v90c"></a>
					<span class="userattrs"></span>
				</p>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-57">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,57)"></div>

										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,57)"></div>
				</div>
								<div class="entry unvoted">
					
												
														<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("hello docker hello docker hello docker"));</script></div>
							</div>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-03-31 09:21:13"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/57">1 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,57);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/57" data-thingid=57 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd message" id="thing_55" data-thingid=55>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-55">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,55)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,55)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/comments/55">kkkk</a></p>
														
											

										<p class="tagline">
						 						 来自
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						  
						 <time class="live-timestamp timeago" datetime="2017-03-29 14:15:35"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/55">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,55);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/55" data-thingid=55 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd message" id="thing_54" data-thingid=54>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-54">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,54)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,54)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/comments/54">hello are </a></p>
														
											

										<p class="tagline">
						 						 来自
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						  
						 <time class="live-timestamp timeago" datetime="2017-03-29 14:14:55"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/54">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,54);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/54" data-thingid=54 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd message" id="thing_53" data-thingid=53>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-53">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,53)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,53)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/comments/53">hello</a></p>
														
											

										<p class="tagline">
						 						 来自
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						  
						 <time class="live-timestamp timeago" datetime="2017-03-29 14:14:28"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/53">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,53);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/53" data-thingid=53 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd message" id="thing_52" data-thingid=52>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-52">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,52)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,52)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/comments/52">12121</a></p>
														
											

										<p class="tagline">
						 						 来自
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						  
						 <time class="live-timestamp timeago" datetime="2017-03-29 14:12:22"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/52">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,52);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/52" data-thingid=52 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd message" id="thing_51" data-thingid=51>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-51">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,51)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,51)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/comments/51">123</a></p>
														
											

										<p class="tagline">
						 						 来自
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						  
						 <time class="live-timestamp timeago" datetime="2017-03-29 14:11:50"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/51">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,51);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/51" data-thingid=51 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd message" id="thing_50" data-thingid=50>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-50">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,50)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,50)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/comments/50">hi</a></p>
														
											

										<p class="tagline">
						 						 来自
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						  
						 <time class="live-timestamp timeago" datetime="2017-03-29 14:10:57"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/50">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,50);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/50" data-thingid=50 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd message" id="thing_49" data-thingid=49>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-49">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,49)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,49)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/comments/49">hi</a></p>
														
											

										<p class="tagline">
						 						 来自
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						  
						 <time class="live-timestamp timeago" datetime="2017-03-29 14:10:46"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/49">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,49);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/49" data-thingid=49 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd message" id="thing_48" data-thingid=48>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-48">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,48)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,48)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/comments/48">hello sureone</a></p>
														
											

										<p class="tagline">
						 						 来自
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						  
						 <time class="live-timestamp timeago" datetime="2017-03-29 13:58:48"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/comments/48">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,48);return false;">收藏</a></li>
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

																					<li class="edit-button">
								<a href="./v/submit/48" data-thingid=48 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
			

		</div>
	</div>
</div>

<div id="footer"></div>
<div class="modal  fade  login-modal" tabindex="0" aria-hidden="true">
<div class="modal-dialog modal-dialog-lg"><div class="modal-content"><div class="modal-header"><a href="javascript: void 0;" class="c-close c-hide-text" data-dismiss="modal">close this window</a></div><div class="modal-body"><h3 id="cover-msg" class="modal-title" style="display: none;">您必須登入才能操作。</h3><div id="login"><div class="split-panel"><div class="split-panel-section split-panel-divider"><h4 class="modal-title">建立一个新帐号</h4><form id="register-form" method="post" action="./api" class="form-v2"><input type="hidden" name="action" value="reg">

<div class="c-form-group "><label for="user_reg" class="screenreader-only">使用者账号:</label><input value="" name="user" id="user_reg" class="c-form-control" type="text" maxlength="20" tabindex="2" placeholder="选择使用者账号" data-validate-url="/api/check_username.json" data-validate-min="3" autocomplete="off"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div>

<div class="c-form-group "><label for="user_name" class="screenreader-only">使用者名称:</label><input value="" name="user_name" id="user_name" class="c-form-control" type="text" maxlength="20" tabindex="2" placeholder="选择使用者名称" data-validate-url="/api/check_username.json" data-validate-min="3" autocomplete="off"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div>

<div class="c-form-group "><label for="passwd_reg" class="screenreader-only">密码:</label><input id="passwd_reg" class="c-form-control" name="passwd" type="password" tabindex="2" placeholder="密码" data-validate-url="/api/check_password.json" style="padding-right: 5px;"><div class="strength-meter"><div class="strength-meter-fill"></div></div><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div><div class="c-form-group "><label for="passwd2_reg" class="screenreader-only">确认密码:</label><input name="passwd2" id="passwd2_reg" class="c-form-control" type="password" tabindex="2" placeholder="确认密码"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div><div class="c-form-group "><label for="email_reg" class="screenreader-only">电子邮件: &nbsp;<i>(选填)</i></label><input value="" name="email" id="email_reg" class="c-form-control" type="text" tabindex="2" placeholder="电子邮件" data-validate-url="/api/check_email.json" data-validate-on="change blur"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div>



<div class="c-clearfix c-submit-group"><span class="c-form-throbber"></span><button type="submit" class="c-btn c-btn-primary c-pull-right" tabindex="2">注册</button></div><div><div class="c-alert c-alert-danger"></div><span class="error RATELIMIT field-ratelimit" style="display:none"></span><span class="error RATELIMIT field-vdelay" style="display:none"></span></div></form></div>

<div class="split-panel-section"><h4 class="modal-title">登入</h4><form id="login-form" method="post" action="./api" class="form-v2"><input type="hidden" name="action" value="login"><div class="c-form-group "><label for="user_login" class="screenreader-only">使用者名称:</label><input value="" name="user" id="user_login" class="c-form-control" type="text" maxlength="20" tabindex="3" placeholder="使用者名称"></div><div class="c-form-group "><label for="passwd_login" class="screenreader-only">密码:</label><input id="passwd_login" class="c-form-control" name="passwd" type="password" tabindex="3" placeholder="密码"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div><div class="c-checkbox"><input type="checkbox" name="rem" id="rem_login" tabindex="3"><label for="rem_login">记住我</label>

<!-- <a href="/password" class="c-pull-right">重設密碼</a> -->

</div><div class="spacer"><div class="c-form-group g-recaptcha" data-sitekey="6LeTnxkTAAAAAN9QEuDZRpn90WwKk_R1TRW_g-JC"></div><span class="error BAD_CAPTCHA field-captcha" style="display:none"></span></div><div class="c-clearfix c-submit-group"><span class="c-form-throbber"></span><button type="submit" class="c-btn c-btn-primary c-pull-right" tabindex="3">登入</button></div><div><div class="c-alert c-alert-danger"></div></div></form></div></div>

<!-- <p class="login-disclaimer">By signing up, you agree to our <a href="https://www.reddit.com/help/useragreement/">任期</a> and that you have read our <a href="https://www.reddit.com/help/privacypolicy/">隱私權政策</a> and <a href="https://www.reddit.com/help/contentpolicy/">內容政策</a>.</p>
 -->
</div></div></div></div></div>

</body>


<script type="text/javascript" src="./static/js/user-home.js?v=8"></script>
</html><?php }
}
