<?php
/* Smarty version 3.1.30, created on 2017-04-19 04:15:11
  from "D:\go\ama\application\views\user-submitted.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58f6c82f8ecf18_78246068',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '6b5732f2df205d386e69acd1ad1f198460632586' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\user-submitted.tpl',
      1 => 1492438741,
      2 => 'file',
    ),
    '9add0006f06d4c3a3cdd978f02ffd7fb276e3103' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\page-header.tpl',
      1 => 1492561527,
      2 => 'file',
    ),
    '217cbffcf18965f83bef902ef96780591ab3a14f' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\user-header-bottom-left.tpl',
      1 => 1492558661,
      2 => 'file',
    ),
    'ba02905916e36335648b0f6c193dea096839e9bb' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\page-logo.tpl',
      1 => 1492558661,
      2 => 'file',
    ),
    'fe2ddcdee5c21f4349de2ce2129c6707df9d7ec7' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\header-bottom-right.tpl',
      1 => 1492558661,
      2 => 'file',
    ),
    '167548fb6b600c4daeea8ff69dea4b0028fc6a50' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\side.tpl',
      1 => 1492566806,
      2 => 'file',
    ),
    'e8a712b4ffc2ccb7cb81043427e3b9e31f39dc94' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\thread.tpl',
      1 => 1492563366,
      2 => 'file',
    ),
    'b1ed80734fcd156dc543efa5cb2c0efdf8a86984' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\login-modal.tpl',
      1 => 1492238906,
      2 => 'file',
    ),
  ),
  'cache_lifetime' => 10,
),true)) {
function content_58f6c82f8ecf18_78246068 (Smarty_Internal_Template $_smarty_tpl) {
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

<body class="listing-page loggedin user-submitted-page">


<div id="header">
<div id="header-bottom-left">
    <span class="hover pagename"><a href="./"><img src="./static/images/logo.gif" alt="后园小亭" height="20"></a></span>
    <ul class="tabmenu ">
        <li ><a href="./v/user/sureone/home" class="choice">总览</a></li>
        <li ><a href="./v/user/sureone/replies" class="choice">留言</a></li>
        <li class="selected"><a href="./v/user/sureone/submitted" class="choice">已发表</a></li>
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
                <a href="./v/message/compose/sureone" class="login-required access-required" target="_top">给小网发送私信</a>
                <div class="nub"></div>
            </div>
        </div>
    </div>
    
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

										<div class="thing odd link" id="thing_204" data-thingid=204>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-204">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,204)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,204)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/a/204">dfsafdsae32rewqrewq</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-16 15:58:17"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/a/204">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,204);return false;">收藏</a></li>
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
								<a href="./v/submit/204" data-thingid=204 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_203" data-thingid=203>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-203">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,203)"></div>

										<div class="score dislikes" title="77">1</div>
					<div class="score unvoted" title="78">-1</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,203)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/a/203">bbbhblgukg</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-16 15:18:31"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/a/203">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,203);return false;">收藏</a></li>
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
								<a href="./v/submit/203" data-thingid=203 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_202" data-thingid=202>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-202">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,202)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">1</div>
					<div class="score likes" title="79">1</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,202)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/a/202">11111111111111111111111</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-16 15:13:28"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/a/202">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,202);return false;">收藏</a></li>
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
								<a href="./v/submit/202" data-thingid=202 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_201" data-thingid=201>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-201">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,201)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">1</div>
					<div class="score likes" title="79">1</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,201)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/a/201">dsfdsfdsfdsfds</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-16 15:13:12"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/a/201">1 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,201);return false;">收藏</a></li>
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
								<a href="./v/submit/201" data-thingid=201 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_198" data-thingid=198>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-198">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,198)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">2</div>
					<div class="score likes" title="79">2</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,198)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/a/198">#满江红</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-16 09:49:43"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/a/198">1 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,198);return false;">收藏</a></li>
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
								<a href="./v/submit/198" data-thingid=198 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_190" data-thingid=190>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-190">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,190)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">1</div>
					<div class="score likes" title="79">1</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,190)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/a/190">岳飞 </a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-16 09:47:45"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/a/190">7 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,190);return false;">收藏</a></li>
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
								<a href="./v/submit/190" data-thingid=190 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_189" data-thingid=189>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-189">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,189)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,189)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/a/189">满江红</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-16 09:46:42"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/a/189">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,189);return false;">收藏</a></li>
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
								<a href="./v/submit/189" data-thingid=189 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_182" data-thingid=182>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-182">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,182)"></div>

										<div class="score dislikes" title="77">1</div>
					<div class="score unvoted" title="78">-1</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,182)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/a/182">dsafdsafds</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-15 13:58:08"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/a/182">2 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,182);return false;">收藏</a></li>
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
								<a href="./v/submit/182" data-thingid=182 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_159" data-thingid=159>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-159">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,159)"></div>

										<div class="score dislikes" title="77">1</div>
					<div class="score unvoted" title="78">-1</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,159)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/a/159">fdsafds</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-14 18:17:23"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/a/159">12 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,159);return false;">收藏</a></li>
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
								<a href="./v/submit/159" data-thingid=159 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_82" data-thingid=82>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-82">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,82)"></div>

										<div class="score dislikes" title="77">1</div>
					<div class="score unvoted" title="78">-1</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,82)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/a/82">*x*</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-14 15:50:28"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/a/82">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,82);return false;">收藏</a></li>
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
								<a href="./v/submit/82" data-thingid=82 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_81" data-thingid=81>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-81">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,81)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,81)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/a/81">eeeee</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-14 15:49:17"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/a/81">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,81);return false;">收藏</a></li>
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
								<a href="./v/submit/81" data-thingid=81 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_80" data-thingid=80>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-80">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,80)"></div>

										<div class="score dislikes" title="77">1</div>
					<div class="score unvoted" title="78">-1</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,80)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/a/80">eee</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-04-14 15:48:58"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/a/80">0 留言</a></li>
							<!-- <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li> -->
							<li class="link-save-button save-button"><a href="" onclick="saveit('./api',this,80);return false;">收藏</a></li>
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
								<a href="./v/submit/80" data-thingid=80 class="editbtn access-required" data-event-action="edit">编辑</a>
							</li>
																				</ul>
						 
					</p>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										<div class="thing odd link" id="thing_52" data-thingid=52>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-52">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,52)"></div>

										<div class="score dislikes" title="77">1</div>
					<div class="score unvoted" title="78">-1</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,52)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/a/52">this is my second test thread</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-03-30 21:03:35"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/a/52">1 留言</a></li>
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
										<div class="thing odd link" id="thing_48" data-thingid=48>
				
								<span class="rank"></span>
												<div class="midcol unvoted" id="vote-48">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,48)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">2</div>
					<div class="score likes" title="79">2</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,48)"></div>
				</div>
								<div class="entry unvoted">
					
												
															 <p class="title"><a class="title may-blank loggedin " href="./v/a/48">This is the first thread</a></p>
														
											

										<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 						 发表于
						  
						 <time class="live-timestamp timeago" datetime="2017-03-29 20:49:19"></time>

						 <ul class="flat-list buttons" style="display: inline-block;">
							<li class="first"><a href="./v/a/48">95 留言</a></li>
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
