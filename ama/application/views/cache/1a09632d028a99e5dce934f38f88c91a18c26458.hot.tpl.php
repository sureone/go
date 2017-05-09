<?php
/* Smarty version 3.1.30, created on 2017-05-08 11:46:12
  from "D:\work\go\ama\application\views\hot.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_59103e64130b81_16261267',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'd8aab19a792690ccfbdb9016c5f963b339aca521' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\hot.tpl',
      1 => 1492496316,
      2 => 'file',
    ),
    '2cba0286b045df067f392a1b6c88c5e91e74ffac' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\page-header.tpl',
      1 => 1494233935,
      2 => 'file',
    ),
    '60fa3e11c1f4314b315f8259b36371c39276f334' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\header-bottom-left.tpl',
      1 => 1492480921,
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
      1 => 1493708019,
      2 => 'file',
    ),
    '909714a59f76f675a88c2ed245e2941851f8dac9' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\side.tpl',
      1 => 1493708019,
      2 => 'file',
    ),
    'a6511d30798b0d5592df6709fdb08d9628d23d2a' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\thread-simple.tpl',
      1 => 1494233935,
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
function content_59103e64130b81_16261267 (Smarty_Internal_Template $_smarty_tpl) {
?>
	<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
	<meta charset="utf-8">
    <base href="http://127.0.0.1/ama/index.php">
        <title>波普网络</title>
        <link rel="stylesheet" href="./static/css/common.css?v=2" type="text/css" />
    <script type="text/javascript" src="https://apps.bdimg.com/libs/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://apps.bdimg.com/libs/handlebars.js/1.3.0/handlebars.min.js"></script>
    <script src="./static/js/form2json.js"></script>
    <script src="./static/js/common.js?v=7"></script>
    <script src="./static/js/jquery.timeago.js?v=1"></script>
    <script src="./static/js/markdown.js?v=3"></script>
    <script type="text/javascript" src="./static/js/ama.js?v=9"></script>

    <script type="text/javascript">
    	var g_logined = false;
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

                .side{
            display: none;
        }
            </style>
          
	<style>
	          #header,.side{
            display: none;
        }
		.formtabs-content {
			width: 100%;
			border-top: 4px solid #5f99cf;
			padding-top: 10px;
		}
        	</style>

	<style type="text/css">

	</style>
</head>

<body class="listing-page  hot-page">


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
<span class="user">想要加入?  一秒<a href="" class="login-required">登入或註冊</a></span>

<!-- <span class="separator">|</span>
<ul class="flat-list hover">
	<li><a href="javascript:void(0)" class="pref-lang choice" onclick="return showlang();">中文</a></li>
</ul>
 -->

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
										
			<div class="thing odd link" id="thing_73" data-thingid=73>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-73">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,73)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">1</div>
					<div class="score likes" title="79">1</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,73)"></div>
				</div> -->
								<div class="entry unvoted">
																	<div class="expando-button collapsed selftext"></div>
																
												 <p class="title"><span class="score unvoted" title="指标 1">1</span><a class="title may-blank loggedin " href="./v/a/73">
						 									dfsafdsa111111111
							
						 </a>
						</p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-04-17 16:38:49"></time>

						 <a href="./v/a/73">0 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


						

						 
					</p>
										<div class="expando expando-73" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("fdsafdsafdsa11122222222222222"));</script>
 									
								</div>
							</div>
						</form>
					</div>
										
					
					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										
			<div class="thing odd link" id="thing_75" data-thingid=75>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-75">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,75)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,75)"></div>
				</div> -->
								<div class="entry unvoted">
																	<div class="expando-button collapsed selftext"></div>
																
												 <p class="title"><span class="score unvoted" title="指标 0">0</span><a class="title may-blank loggedin " href="./v/a/75">
						 									121212
							
						 </a>
						</p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-04-17 16:45:05"></time>

						 <a href="./v/a/75">0 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


						

						 
					</p>
										<div class="expando expando-75" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("3232424"));</script>
 									
								</div>
							</div>
						</form>
					</div>
										
					
					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										
			<div class="thing odd link" id="thing_74" data-thingid=74>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-74">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,74)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,74)"></div>
				</div> -->
								<div class="entry unvoted">
																	<div class="expando-button collapsed selftext"></div>
																
												 <p class="title"><span class="score unvoted" title="指标 0">0</span><a class="title may-blank loggedin " href="./v/a/74">
						 									fdsafds
							
						 </a>
						</p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-04-17 16:44:57"></time>

						 <a href="./v/a/74">0 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


						

						 
					</p>
										<div class="expando expando-74" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("fdsafds"));</script>
 									
								</div>
							</div>
						</form>
					</div>
										
					
					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										
			<div class="thing odd link" id="thing_72" data-thingid=72>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-72">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,72)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,72)"></div>
				</div> -->
								<div class="entry unvoted">
																	<div class="expando-button collapsed selftext"></div>
																
												 <p class="title"><span class="score unvoted" title="指标 0">0</span><a class="title may-blank loggedin " href="./v/a/72">
						 									gfdsgfdsgfdsg3rertwetre
							
						 </a>
						</p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-04-17 16:38:30"></time>

						 <a href="./v/a/72">0 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


						

						 
					</p>
										<div class="expando expando-72" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("dsfafdsafsafsd\n\n*fdsafsdafd*fdsafdsa*fdsafdsa*"));</script>
 									
								</div>
							</div>
						</form>
					</div>
										
					
					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										
			<div class="thing odd link" id="thing_71" data-thingid=71>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-71">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,71)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,71)"></div>
				</div> -->
								<div class="entry unvoted">
																	<div class="expando-button collapsed selftext"></div>
																
												 <p class="title"><span class="score unvoted" title="指标 0">0</span><a class="title may-blank loggedin " href="./v/a/71">
						 									dfsafdsafsd
							
						 </a>
						</p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-04-17 16:38:03"></time>

						 <a href="./v/a/71">0 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


						

						 
					</p>
										<div class="expando expando-71" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("fdsafdsfdsfd"));</script>
 									
								</div>
							</div>
						</form>
					</div>
										
					
					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										
			<div class="thing odd link" id="thing_70" data-thingid=70>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-70">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,70)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,70)"></div>
				</div> -->
								<div class="entry unvoted">
																	<div class="expando-button collapsed selftext"></div>
																
												 <p class="title"><span class="score unvoted" title="指标 0">0</span><a class="title may-blank loggedin " href="./v/a/70">
						 									test
							
						 </a>
						</p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-04-17 16:37:21"></time>

						 <a href="./v/a/70">0 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


						

						 
					</p>
										<div class="expando expando-70" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML("fdsafds"));</script>
 									
								</div>
							</div>
						</form>
					</div>
										
					
					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
										
			<div class="thing odd link" id="thing_69" data-thingid=69>
				
								<span class="rank"></span>
								<!-- 				<div class="midcol unvoted" id="vote-69">
					<div class="arrow up login-required access-required" tabindex="0" onclick="voteit('./api',this,1,69)"></div>

										<div class="score dislikes" title="77">0</div>
					<div class="score unvoted" title="78">0</div>
					<div class="score likes" title="79">0</div>
										<div class="arrow down login-required access-required" tabindex="0" onclick="voteit('./api',this,-1,69)"></div>
				</div> -->
								<div class="entry unvoted">
																	<div class="expando-button collapsed selftext"></div>
																
												 <p class="title"><span class="score unvoted" title="指标 0">0</span><a class="title may-blank loggedin " href="./v/a/69">
						 									雨霖铃·寒蝉凄切， 寒蝉凄切， 对长亭晚， 骤雨初歇。都门帐饮无绪， 留恋处， 兰舟催发。念去去， 千里烟波， 暮霭沉沉楚天阔。

多情自古伤离别， 更那堪， 冷落清秋节！
							
						 </a>
						</p>
											

					
					<p class="tagline">
						 						 <a href="./v/user/sureone" class="author may-blank ">小网</a>
						 
						 <span class="userattrs"></span>

						 <time class="live-timestamp timeago" datetime="2017-04-17 16:12:31"></time>

						 <a href="./v/a/69">0 留言</a>
						 <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">检举</a>


						

						 
					</p>
										<div class="expando expando-69" style="display: none;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML(" 寒蝉凄切， 对长亭晚， 骤雨初歇。\n\n\n\n都门帐饮无绪， 留恋处， 兰舟催发。\n\n\n\n执手相看泪眼， 竟无语凝噎。\n\n\n\n念去去， 千里烟波， 暮霭沉沉楚天阔。\n\n\n\n多情自古伤离别， 更那堪， 冷落清秋节！\n\n\n\n今宵酒醒何处？ 杨柳岸， 晓风残月。\n\n\n\n此去经年， 应是良辰好景虚设。\n\n\n\n便纵有千种风情， 更与何人说？ "));</script>
 									
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
