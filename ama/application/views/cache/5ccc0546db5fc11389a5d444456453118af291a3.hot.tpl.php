<?php
/* Smarty version 3.1.30, created on 2017-05-01 10:01:13
  from "D:\go\ama\application\views\hot.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_5906eb494f47c5_40347724',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '698fa06f07caff7f8c617daf21469f0c8127ca2d' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\hot.tpl',
      1 => 1492558661,
      2 => 'file',
    ),
    '9add0006f06d4c3a3cdd978f02ffd7fb276e3103' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\page-header.tpl',
      1 => 1492561527,
      2 => 'file',
    ),
    '96d0a9d265c5db1f508c80d38d635e22d33f58a6' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\header-bottom-left.tpl',
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
      1 => 1493534878,
      2 => 'file',
    ),
    '167548fb6b600c4daeea8ff69dea4b0028fc6a50' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\side.tpl',
      1 => 1492566806,
      2 => 'file',
    ),
    'b51f04a8c3a4978ff0f489b2c21fbac552e62798' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\thread-simple.tpl',
      1 => 1492918710,
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
function content_5906eb494f47c5_40347724 (Smarty_Internal_Template $_smarty_tpl) {
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
        <style type="text/css">
        .link .score {
            text-align: center;
            color: #ec4a36;
            background: #dbd0b6;
            border: 1px solid #eddeaa;
            font-size: small;
            padding-left: 2px;
            padding-right: 2px;
            font-weight: normal;
            margin-right: 4px;
        }
        .thing{
            display: inline-block;
            margin: 0 0px 0px 0; 
            padding: 2px;
            min-width:240px;
        }

        .listing-page .linklisting .thing {
            position: relative;
            margin: 0 0px 0px 0;
        }
        .link .title {
          
         
            text-overflow: ellipsis;
            overflow: hidden;
            white-space: nowrap;
            max-width: 700px;
        }
    </style>
    
	<style type="text/css">

	</style>
</head>

<body class="listing-page loggedin hot-page">


<div id="header">
<div id="header-bottom-left">
    <span class="hover pagename"><a href="./"><img src="./static/images/logo.gif" alt="后园小亭" height="20"></a></span>
    <ul class="tabmenu ">
        <li class="selected"><a href="./v/hot" class="choice">热门</a></li>
        <li ><a href="./v/news" class="choice">最新</a></li>
<!--         <li ><a href="./v/rising" class="choice">好評上升中</a></li>
        <li ><a href="./v/controversial" class="choice">具爭議的</a></li>
        <li ><a href="./v/top" class="choice">頭等</a></li>
        <li ><a href="./v/gilded" class="choice">精選</a></li>
        <li ><a href="./v/wiki" class="choice">wiki</a></li>
        <li ><a href="./v/ads" class="choice">宣傳過的</a></li> -->
    </ul>
</div>

<div id="header-bottom-right">
<span class="user"><a href="./v/user/sureone/">小网</a>&nbsp;<span class="separator">|</span><a title="沒有新郵件" href="./v/message/inbox/" class="nohavemail" id="mail">信息&nbsp;(2)</a>
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
										
			<div class="thing odd link" id="thing_198" data-thingid=198>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-198">
					<div class="arrow upmod login-required access-required" tabindex="0" onclick="voteit('./api',this,1,198)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">2</div>
					<div class="score likes" title="79">2</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,198)"></div>
				</div> -->
								<div class="entry unvoted">
																	<div class="expando-button collapsed selftext"></div>
																
												 <p class="title"><span class="score unvoted" title="指标 2">2</span><a class="title may-blank loggedin " href="./v/a/198">#满江红</a></p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-04-16 09:49:43"></time>

						 <a href="./v/a/198">1 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


																				<a href="./v/submit/198" data-thingid=198 class="editbtn access-required" data-event-action="edit">编辑</a>


								<a href="javascript:deleteit(198)" data-thingid=198 class="delbtn access-required" data-event-action="delete">删除</a>
												

						 
					</p>
										<div class="expando expando-198" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("怒发冲冠，凭阑处、潇潇雨歇。\n\n抬望眼、仰天长啸，壮怀激烈。\n\n三十功名尘与土，八千里路云和月。\n\n莫等闲，白了少年头，空悲切。\n\n靖康耻，犹未雪；\n\n臣子恨，何时灭。\n\n驾长车，踏破贺兰山缺。\n\n壮志饥餐胡虏肉，笑谈渴饮匈奴血。\n\n待从头、收拾旧山河，朝天阙。"));</script>
 									
								</div>
							</div>
						</form>
					</div>
										
					
					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										
			<div class="thing odd link" id="thing_48" data-thingid=48>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-48">
					<div class="arrow upmod login-required access-required" tabindex="0" onclick="voteit('./api',this,1,48)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">2</div>
					<div class="score likes" title="79">2</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,48)"></div>
				</div> -->
								<div class="entry unvoted">
																	<div class="expando-button collapsed selftext"></div>
																
												 <p class="title"><span class="score unvoted" title="指标 2">2</span><a class="title may-blank loggedin " href="./v/a/48">This is the first thread</a></p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-03-29 20:49:19"></time>

						 <a href="./v/a/48">95 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


																				<a href="./v/submit/48" data-thingid=48 class="editbtn access-required" data-event-action="edit">编辑</a>


								<a href="javascript:deleteit(48)" data-thingid=48 class="delbtn access-required" data-event-action="delete">删除</a>
												

						 
					</p>
										<div class="expando expando-48" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("hello ama!"));</script>
 									
								</div>
							</div>
						</form>
					</div>
										
					
					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										
			<div class="thing odd link" id="thing_224" data-thingid=224>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-224">
					<div class="arrow upmod login-required access-required" tabindex="0" onclick="voteit('./api',this,1,224)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">1</div>
					<div class="score likes" title="79">1</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,224)"></div>
				</div> -->
								<div class="entry unvoted">
																	<div class="expando-button collapsed selftext"></div>
																
												 <p class="title"><span class="score unvoted" title="指标 1">1</span><a class="title may-blank loggedin " href="./v/a/224">beautiy </a></p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-04-23 12:21:31"></time>

						 <a href="./v/a/224">6 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


																				<a href="./v/submit/224" data-thingid=224 class="editbtn access-required" data-event-action="edit">编辑</a>


								<a href="javascript:deleteit(224)" data-thingid=224 class="delbtn access-required" data-event-action="delete">删除</a>
												

						 
					</p>
										<div class="expando expando-224" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("photos"));</script>
 									
								</div>
							</div>
						</form>
					</div>
										
					
					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										
			<div class="thing odd link" id="thing_222" data-thingid=222>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-222">
					<div class="arrow upmod login-required access-required" tabindex="0" onclick="voteit('./api',this,1,222)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">1</div>
					<div class="score likes" title="79">1</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,222)"></div>
				</div> -->
								<div class="entry unvoted">
																	<div class="expando-button collapsed selftext"></div>
																
												 <p class="title"><span class="score unvoted" title="指标 1">1</span><a class="title may-blank loggedin " href="./v/a/222">照片</a></p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-04-23 11:42:34"></time>

						 <a href="./v/a/222">0 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


																				<a href="./v/submit/222" data-thingid=222 class="editbtn access-required" data-event-action="edit">编辑</a>


								<a href="javascript:deleteit(222)" data-thingid=222 class="delbtn access-required" data-event-action="delete">删除</a>
												

						 
					</p>
										<div class="expando expando-222" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("旅游照片"));</script>
 									
								</div>
							</div>
						</form>
					</div>
										
					
					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										
			<div class="thing odd link" id="thing_202" data-thingid=202>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-202">
					<div class="arrow upmod login-required access-required" tabindex="0" onclick="voteit('./api',this,1,202)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">1</div>
					<div class="score likes" title="79">1</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,202)"></div>
				</div> -->
								<div class="entry unvoted">
																					
												 <p class="title"><span class="score unvoted" title="指标 1">1</span><a class="title may-blank loggedin " href="./v/a/202">11111111111111111111111</a></p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-04-16 15:13:28"></time>

						 <a href="./v/a/202">0 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


																				<a href="./v/submit/202" data-thingid=202 class="editbtn access-required" data-event-action="edit">编辑</a>


								<a href="javascript:deleteit(202)" data-thingid=202 class="delbtn access-required" data-event-action="delete">删除</a>
												

						 
					</p>
										<div class="expando expando-202" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML(""));</script>
 									
								</div>
							</div>
						</form>
					</div>
										
					
					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										
			<div class="thing odd link" id="thing_201" data-thingid=201>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-201">
					<div class="arrow upmod login-required access-required" tabindex="0" onclick="voteit('./api',this,1,201)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">1</div>
					<div class="score likes" title="79">1</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,201)"></div>
				</div> -->
								<div class="entry unvoted">
																					
												 <p class="title"><span class="score unvoted" title="指标 1">1</span><a class="title may-blank loggedin " href="./v/a/201">dsfdsfdsfdsfds</a></p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-04-16 15:13:12"></time>

						 <a href="./v/a/201">1 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


																				<a href="./v/submit/201" data-thingid=201 class="editbtn access-required" data-event-action="edit">编辑</a>


								<a href="javascript:deleteit(201)" data-thingid=201 class="delbtn access-required" data-event-action="delete">删除</a>
												

						 
					</p>
										<div class="expando expando-201" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML(""));</script>
 									
								</div>
							</div>
						</form>
					</div>
										
					
					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										
			<div class="thing odd link" id="thing_190" data-thingid=190>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-190">
					<div class="arrow upmod login-required access-required" tabindex="0" onclick="voteit('./api',this,1,190)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">1</div>
					<div class="score likes" title="79">1</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,190)"></div>
				</div> -->
								<div class="entry unvoted">
																	<div class="expando-button collapsed selftext"></div>
																
												 <p class="title"><span class="score unvoted" title="指标 1">1</span><a class="title may-blank loggedin " href="./v/a/190">岳飞 </a></p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-04-16 09:47:45"></time>

						 <a href="./v/a/190">8 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


																				<a href="./v/submit/190" data-thingid=190 class="editbtn access-required" data-event-action="edit">编辑</a>


								<a href="javascript:deleteit(190)" data-thingid=190 class="delbtn access-required" data-event-action="delete">删除</a>
												

						 
					</p>
										<div class="expando expando-190" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML(" 怒发冲冠，凭阑处、潇潇雨歇。抬望眼、仰天长啸，壮怀激烈。三十功名尘与土，八千里路云和月。莫等闲，白了少年头，空悲切。\n\n\n\n  靖康耻，犹未雪；臣子恨，何时灭。驾长车，踏破贺兰山缺。壮志饥餐胡虏肉，笑谈渴饮匈奴血。待从头、收拾旧山河，朝天阙。\n\n"));</script>
 									
								</div>
							</div>
						</form>
					</div>
										
					
					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										
			<div class="thing odd link" id="thing_246" data-thingid=246>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-246">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,246)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,246)"></div>
				</div> -->
								<div class="entry unvoted">
																	<div class="expando-button collapsed selftext"></div>
																
												 <p class="title"><span class="score unvoted" title="指标 0">0</span><a class="title may-blank loggedin " href="./v/a/246">fdsafdsa</a></p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-04-29 19:31:33"></time>

						 <a href="./v/a/246">0 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


																				<a href="./v/submit/246" data-thingid=246 class="editbtn access-required" data-event-action="edit">编辑</a>


								<a href="javascript:deleteit(246)" data-thingid=246 class="delbtn access-required" data-event-action="delete">删除</a>
												

						 
					</p>
										<div class="expando expando-246" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("fdsafdsafdsa"));</script>
 									
								</div>
							</div>
						</form>
					</div>
										
					
					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										
			<div class="thing odd link" id="thing_245" data-thingid=245>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-245">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,245)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,245)"></div>
				</div> -->
								<div class="entry unvoted">
																	<div class="expando-button collapsed selftext"></div>
																
												 <p class="title"><span class="score unvoted" title="指标 0">0</span><a class="title may-blank loggedin " href="./v/a/245">fdsafdsa</a></p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-04-29 19:31:26"></time>

						 <a href="./v/a/245">0 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


																				<a href="./v/submit/245" data-thingid=245 class="editbtn access-required" data-event-action="edit">编辑</a>


								<a href="javascript:deleteit(245)" data-thingid=245 class="delbtn access-required" data-event-action="delete">删除</a>
												

						 
					</p>
										<div class="expando expando-245" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("fdsafdsa"));</script>
 									
								</div>
							</div>
						</form>
					</div>
										
					
					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										
			<div class="thing odd link" id="thing_232" data-thingid=232>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-232">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,232)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,232)"></div>
				</div> -->
								<div class="entry unvoted">
																	<div class="expando-button collapsed selftext"></div>
																
												 <p class="title"><span class="score unvoted" title="指标 0">0</span><a class="title may-blank loggedin " href="./v/a/232">纯纯粹粹纯纯粹粹踩踩踩</a></p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-04-23 13:00:53"></time>

						 <a href="./v/a/232">0 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


																				<a href="./v/submit/232" data-thingid=232 class="editbtn access-required" data-event-action="edit">编辑</a>


								<a href="javascript:deleteit(232)" data-thingid=232 class="delbtn access-required" data-event-action="delete">删除</a>
												

						 
					</p>
										<div class="expando expando-232" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("坎坎坷坷坎坎坷坷坎坎坷坷坎坎坷坷坎坎坷坷"));</script>
 									
								</div>
							</div>
						</form>
					</div>
										
					
					
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


<script type="text/javascript" src="./static/js/hot.js?v=8"></script>
</html><?php }
}
