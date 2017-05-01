<?php
/* Smarty version 3.1.30, created on 2017-05-01 08:21:30
  from "D:\go\ama\application\views\comments.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_5906d3ea8daf41_06499552',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    'a1541aa7f0becfa0b4e92a61fd9fa210baf30ab1' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\comments.tpl',
      1 => 1492931852,
      2 => 'file',
    ),
    '9add0006f06d4c3a3cdd978f02ffd7fb276e3103' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\page-header.tpl',
      1 => 1492561527,
      2 => 'file',
    ),
    '8e352f3f10d8bdc0f289664303560737af49f0b6' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\comment-reply-edit.tpl',
      1 => 1492931870,
      2 => 'file',
    ),
    '80f5f0c9f21973e46f2e4ad31a1a3daba18efcc9' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\markhelp.tpl',
      1 => 1492563924,
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
    'e8a712b4ffc2ccb7cb81043427e3b9e31f39dc94' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\thread.tpl',
      1 => 1492918783,
      2 => 'file',
    ),
    'd03b8025f8ee3e41174be16b12370503e817c2fd' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\comment.tpl',
      1 => 1492932668,
      2 => 'file',
    ),
    'b1ed80734fcd156dc543efa5cb2c0efdf8a86984' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\login-modal.tpl',
      1 => 1492238906,
      2 => 'file',
    ),
    '92480ae619bca4be4858d7e757663330f94640b0' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\file-attach.tpl',
      1 => 1492931325,
      2 => 'file',
    ),
  ),
  'cache_lifetime' => 10,
),true)) {
function content_5906d3ea8daf41_06499552 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->ext->_tplFunction->registerTplFunctions($_smarty_tpl, array (
  'renderComments' => 
  array (
    'compiled_filepath' => 'D:\\go\\ama\\application\\views\\template_c\\d03b8025f8ee3e41174be16b12370503e817c2fd_0.file.comment.tpl.cache.php',
    'uid' => 'd03b8025f8ee3e41174be16b12370503e817c2fd',
    'call_name' => 'smarty_template_function_renderComments_1922158fc583f98b5d2_91583026',
  ),
));
?>

    <!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
	<meta charset="utf-8">
    <base href="http://127.0.0.1/ama/index.php">
    	<title>岳飞 </title>
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
    	var g_logined = false;
    </script>
    
</head>
<body class=" comments-page">

<script id="tpl-comment-edit" type="text/x-handlebars-template">
    
    <form action="./api" class="usertext cloneable warn-on-unload" onsubmit="handleFormSubmit(this);return false;" id="form-comment-{{thingid}}">
    
        <input type="hidden" name="action" value="submit-new-comment">
         
        <input type="hidden" name="main" value="{{mainid}}">
        
        <input type="hidden" name="parent" value="{{thingid}}">
        
        <div class="usertext-edit md-container" style="">
            <div class="md">
                <textarea rows="1" cols="1" name="content" class="" data-event-action="comment" data-type="link"></textarea>
            </div>
            <div class="bottom-area">
                <span class="attach-toggle toggle" style="">
                    <a class="option active " href="#" tabindex="100" onclick="return toggle(this, attachon, attachoff)">添加附件</a>
                    <a class="option " href="#">不添加附件</a>
                </span>
                <span class="help-toggle toggle" style="">
                    <a class="option active " href="#" tabindex="100" onclick="return toggle(this, helpon, helpoff)">格式說明</a>
                    <a class="option " href="#">隱藏說明</a>
                </span>
                <a class="reddiquette" target="_blank" tabindex="100">內容政策</a>
                <span class="error TOO_LONG field-text" style="display:none"></span>
                <span
                    class="error RATELIMIT field-ratelimit" style="display:none">
                </span>
                <span class="error NO_TEXT field-text" style="display:none"></span>
                <span class="error TOO_OLD field-parent" style="display:none"></span>
                <span class="error THREAD_LOCKED field-parent" style="display:none"></span>
                <span class="error DELETED_COMMENT field-parent" style="display:none"></span>
                <span class="error USER_BLOCKED field-parent" style="display:none"></span>
                <span class="error USER_MUTED field-parent" style="display:none"></span>
                <span class="error MUTED_FROM_SUBREDDIT field-parent" style="display:none"></span>

                <div class="usertext-buttons">
                    <button type="submit" onclick="" class="save">保存</button>
                    <button type="button" onclick="return cancel_usertext(this);" class="cancel" style="">取消</button>
                </div>
            </div>
            
                                <div class="markhelp" style="display:none"><p></p>

                                    <p>使用稍微自訂過的 <a href="http://daringfireball.net/projects/markdown/syntax">Markdown</a>
                                        版本，作為文字格式的設定方式。請參閱下方的部分基本格式。
                                    </p>

                                    <p></p>
                                    <table class="md">
                                        <tbody>
                                        <tr style="background-color: #ffff99; text-align: center">
                                            <td><em>輸入的文字：</em></td>
                                            <td><em>顯示的文字：</em></td>
                                        </tr>
                                        <tr>
                                            <td>*斜體*</td>
                                            <td><em>斜體</em></td>
                                        </tr>
                                        <tr>
                                            <td>**粗體**</td>
                                            <td><b>粗體</b></td>
                                        </tr>
                                        <tr>
                                            <td>[后园小亭](http://boopo.cn)</td>
                                            <td><a href="http://boopo.cn">后园小亭</a></td>
                                        </tr>
                                        <tr>
                                            <td>* 項目 1<br>* 項目 2<br>* 項目 3</td>
                                            <td>
                                                <ul>
                                                    <li>項目 1</li>
                                                    <li>項目 2</li>
                                                    <li>項目 3</li>
                                                </ul>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>&gt; 引用文字</td>
                                            <td>
                                                <blockquote>引用文字</blockquote>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Lines starting with four spaces<br>are treated like code:<br><br><span
                                                    class="spaces">&nbsp;&nbsp;&nbsp;&nbsp;</span>if 1 * 2 &lt;
                                                3:<br><span class="spaces">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>print
                                                "hello, world!"<br></td>
                                            <td>Lines starting with four spaces<br>are treated like code:<br>
                                                <pre>if 1 * 2 &lt; 3:<br>&nbsp;&nbsp;&nbsp;&nbsp;print "hello, world!"</pre>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>~~strikethrough~~</td>
                                            <td><strike>strikethrough</strike></td>
                                        </tr>
                                       
                                        </tbody>
                                    </table>
                                </div>
        </div>
    </form>
</script>

<div id="header">
    <div id="header-bottom-left">
        <span class="hover pagename"><a href="./"><img src="./static/images/logo.gif" alt="后园小亭" height="20"></a></span>
        
        <ul class="tabmenu ">
            <li class="selected"><a href="./v/a/190" class="choice">留言</a></li>
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
            <div class="linkinfo">
                <div class="date"><a style="font-size:medium;padding-right:4px;" href="./v/user/sureone">小网</a><span>发表于 </span>
                    <time datetime="2017-04-16 09:47:45">2017-04-16 09:47:45</time>
                </div>
                <div class="score"><span class="number">1</span> <span class="word">指标</span> (其中1票赞成)</div>
                <div class="shortlink">本文链接: <input type="text" value="http://boopo.cn/v/a/190" readonly="readonly"
                                                         id="shortlink-text"></div>
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

    <div id="siteTable" class="sitetable linklisting">
                    			<div class="thing odd link" id="thing_190" data-thingid=190>
				
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
							<li class="first"><a href="./v/a/190">8 留言</a></li>
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

													</ul>
						 
					</p>
										<div class="expando expando-190" style="display: block;">
						<form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')" id="form-t3_609l7sfwt">
							<input type="hidden" name="thing_id" value="t3_609l7s">
							<div class="usertext-body may-blank-within md-container ">
								<div class="out md"><script>document.write(markdown.toHTML(" 怒发冲冠，凭阑处、潇潇雨歇。抬望眼、仰天长啸，壮怀激烈。三十功名尘与土，八千里路云和月。莫等闲，白了少年头，空悲切。\n\n\n\n  靖康耻，犹未雪；臣子恨，何时灭。驾长车，踏破贺兰山缺。壮志饥餐胡虏肉，笑谈渴饮匈奴血。待从头、收拾旧山河，朝天阙。\n\n"));</script>
	

							        <div class="thing-attaches">
									     
							    	</div>
								</div>

							
							</div>
						</form>
					</div>
										
					
					<div class="reportform"></div>

					
				</div>
				<div class="child"></div>
				<div class="clearleft"></div>
			</div>
           


    </div>
    <div class="commentarea">
        <div class="panestack-title"><span class="title">头8则留言</span><a
                href="/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?limit=500"
                class="title-button ">显示所有8</a></div>
        <div class="menuarea">
            <div class="spacer"><span class="dropdown-title lightdrop">排序依据: </span>

                <div class="dropdown lightdrop" onclick="open_menu(this)"><span class="selected">最佳</span></div>
                <div class="drop-choices lightdrop"><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=top"
                        class="choice">頭等</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=new"
                        class="choice">最新</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=controversial"
                        class="choice">具爭議的</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=old"
                        class="choice">最舊</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=random"
                        class="hidden choice">隨機</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=qa"
                        class="choice">問與答</a><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?sort=live"
                        class="hidden choice">live (beta)</a></div>
            </div>
            <div class="spacer"></div>
        </div>
    </div>

    <script>
      var thingid = 190;
        var mainid =   190;       
       
        var tpl = Handlebars.compile($("#tpl-comment-edit").html());
        h = (tpl({thingid:thingid,mainid:mainid}));

        document.write(h);
        
    </script>

    <div id="siteTable_190" class="sitetable nestedlisting">
        
                <div class=" thing id-208 noncollapsed   comment " id="thing_208" onclick="">
        <p class="parent"><a name="df1l1xv"></a></p>
        <div class="midcol unvoted" id="vote-208">
            <div class="arrow up login-required access-required" onclick="voteit('./api',this,1,208)"></div>
            <div class="arrow down login-required access-required"  onclick="voteit('./api',this,-1,208)"></div>
        </div>
        <div class="entry unvoted">
            <p class="tagline">
                <a href="javascript:void(0)" class="expand" onclick="return togglecomment(this)">[–]</a>
                <a href="./v/user/sureone" class="author may-blank">小网</a>
                <span class="userattrs"></span>
                <span class="score dislikes" title="44">0</span>
                <span class="score unvoted" title="45">0指标</span>
                <span class="score likes" title="46">0指標</span>
                <time class="live-timestamp timeago" datetime="2017-04-22 09:22:58"></time>
                &nbsp;
                <a href="javascript:void(0)" class="numchildren" onclick="return togglecomment(this)">(0下层留言)</a>

                <span class="reply-button"><a class="access-required" href="javascript:void(0)" data-thingid="208"  data-mainid="190" onclick="return reply(this)">回复</a></span>
            </p>
            <form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')"
                  id="form-t1_df1l1xva7c"><input type="hidden" name="thing_id" value="t1_df1l1xv">

                <div class="usertext-body may-blank-within md-container ">
                    <div class="out md"><script>document.write(markdown.toHTML("怒发冲冠，凭阑处、潇潇雨歇。\n\n\n\n抬望眼、仰天长啸，壮怀激烈。\n\n\n\n三十功名尘与土，八千里路云和月。\n\n\n\n莫等闲，白了少年头，空悲切。\n\n\n\n靖康耻，犹未雪；\n\n> 臣子恨，何时灭。\n\n> 驾长车，踏破贺兰山缺。\n\n> 壮志饥餐胡虏肉，笑谈渴饮匈奴血。\n\n> 待从头、收拾旧山河，朝天阙。"));</script>

                         <div class="thing-attaches">
                             
                        </div>
                      </div>
                </div>
            </form>
            <ul class="flat-list buttons">
               <!--  <li class="first"><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/df1l1xv/"
                     >永久連結</a></li>
                <li><a href="javascript:void(0)"
                                             class="embed-comment">embed</a></li>
                <li class="comment-save-button save-button"><a href="javascript:void(0)">儲存</a></li>
                <li class="report-button"><a href="javascript:void(0)" class="reportbtn access-required"
                                              data-thingid="208">檢舉</a></li>
                <li class="give-gold-button"><a href="">贈送金幣</a></li> -->
                
            </ul>
            <div class="reportform report-t1_df1l1xv"></div>
        </div>
        <div class="child" id="child_208">
            <div id="siteTable_child_208" class="sitetable listing">
                                                        

                                            </div>
        </div>
        <div class="clearleft"></div>
    </div>
        <div class=" thing id-197 noncollapsed   comment " id="thing_197" onclick="">
        <p class="parent"><a name="df1l1xv"></a></p>
        <div class="midcol unvoted" id="vote-197">
            <div class="arrow up login-required access-required" onclick="voteit('./api',this,1,197)"></div>
            <div class="arrow down login-required access-required"  onclick="voteit('./api',this,-1,197)"></div>
        </div>
        <div class="entry unvoted">
            <p class="tagline">
                <a href="javascript:void(0)" class="expand" onclick="return togglecomment(this)">[–]</a>
                <a href="./v/user/sureone" class="author may-blank">小网</a>
                <span class="userattrs"></span>
                <span class="score dislikes" title="44">0</span>
                <span class="score unvoted" title="45">0指标</span>
                <span class="score likes" title="46">0指標</span>
                <time class="live-timestamp timeago" datetime="2017-04-16 09:49:23"></time>
                &nbsp;
                <a href="javascript:void(0)" class="numchildren" onclick="return togglecomment(this)">(0下层留言)</a>

                <span class="reply-button"><a class="access-required" href="javascript:void(0)" data-thingid="197"  data-mainid="190" onclick="return reply(this)">回复</a></span>
            </p>
            <form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')"
                  id="form-t1_df1l1xva7c"><input type="hidden" name="thing_id" value="t1_df1l1xv">

                <div class="usertext-body may-blank-within md-container ">
                    <div class="out md"><script>document.write(markdown.toHTML("怒发冲冠，凭阑处、潇潇雨歇。\n\n抬望眼、仰天长啸，壮怀激烈。\n\n三十功名尘与土，八千里路云和月。\n\n莫等闲，白了少年头，空悲切。\n\n靖康耻，犹未雪；\n\n臣子恨，何时灭。\n\n驾长车，踏破贺兰山缺。\n\n壮志饥餐胡虏肉，笑谈渴饮匈奴血。\n\n待从头、收拾旧山河，朝天阙。"));</script>

                         <div class="thing-attaches">
                             
                        </div>
                      </div>
                </div>
            </form>
            <ul class="flat-list buttons">
               <!--  <li class="first"><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/df1l1xv/"
                     >永久連結</a></li>
                <li><a href="javascript:void(0)"
                                             class="embed-comment">embed</a></li>
                <li class="comment-save-button save-button"><a href="javascript:void(0)">儲存</a></li>
                <li class="report-button"><a href="javascript:void(0)" class="reportbtn access-required"
                                              data-thingid="197">檢舉</a></li>
                <li class="give-gold-button"><a href="">贈送金幣</a></li> -->
                
            </ul>
            <div class="reportform report-t1_df1l1xv"></div>
        </div>
        <div class="child" id="child_197">
            <div id="siteTable_child_197" class="sitetable listing">
                                                        

                                            </div>
        </div>
        <div class="clearleft"></div>
    </div>
        <div class=" thing id-196 noncollapsed   comment " id="thing_196" onclick="">
        <p class="parent"><a name="df1l1xv"></a></p>
        <div class="midcol unvoted" id="vote-196">
            <div class="arrow up login-required access-required" onclick="voteit('./api',this,1,196)"></div>
            <div class="arrow down login-required access-required"  onclick="voteit('./api',this,-1,196)"></div>
        </div>
        <div class="entry unvoted">
            <p class="tagline">
                <a href="javascript:void(0)" class="expand" onclick="return togglecomment(this)">[–]</a>
                <a href="./v/user/sureone" class="author may-blank">小网</a>
                <span class="userattrs"></span>
                <span class="score dislikes" title="44">0</span>
                <span class="score unvoted" title="45">0指标</span>
                <span class="score likes" title="46">0指標</span>
                <time class="live-timestamp timeago" datetime="2017-04-16 09:49:03"></time>
                &nbsp;
                <a href="javascript:void(0)" class="numchildren" onclick="return togglecomment(this)">(0下层留言)</a>

                <span class="reply-button"><a class="access-required" href="javascript:void(0)" data-thingid="196"  data-mainid="190" onclick="return reply(this)">回复</a></span>
            </p>
            <form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')"
                  id="form-t1_df1l1xva7c"><input type="hidden" name="thing_id" value="t1_df1l1xv">

                <div class="usertext-body may-blank-within md-container ">
                    <div class="out md"><script>document.write(markdown.toHTML("怒发冲冠，凭阑处、潇潇雨歇。抬望眼、仰天长啸，壮怀激烈。三十功名尘与土，八千里路云和月。莫等闲，白了少年头，空悲切。\n\n\n\n靖康耻，犹未雪；臣子恨，何时灭。驾长车，踏破贺兰山缺。壮志饥餐胡虏肉，笑谈渴饮匈奴血。待从头、收拾旧山河，朝天阙。"));</script>

                         <div class="thing-attaches">
                             
                        </div>
                      </div>
                </div>
            </form>
            <ul class="flat-list buttons">
               <!--  <li class="first"><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/df1l1xv/"
                     >永久連結</a></li>
                <li><a href="javascript:void(0)"
                                             class="embed-comment">embed</a></li>
                <li class="comment-save-button save-button"><a href="javascript:void(0)">儲存</a></li>
                <li class="report-button"><a href="javascript:void(0)" class="reportbtn access-required"
                                              data-thingid="196">檢舉</a></li>
                <li class="give-gold-button"><a href="">贈送金幣</a></li> -->
                
            </ul>
            <div class="reportform report-t1_df1l1xv"></div>
        </div>
        <div class="child" id="child_196">
            <div id="siteTable_child_196" class="sitetable listing">
                                                        

                                            </div>
        </div>
        <div class="clearleft"></div>
    </div>
        <div class=" thing id-195 noncollapsed   comment " id="thing_195" onclick="">
        <p class="parent"><a name="df1l1xv"></a></p>
        <div class="midcol unvoted" id="vote-195">
            <div class="arrow up login-required access-required" onclick="voteit('./api',this,1,195)"></div>
            <div class="arrow down login-required access-required"  onclick="voteit('./api',this,-1,195)"></div>
        </div>
        <div class="entry unvoted">
            <p class="tagline">
                <a href="javascript:void(0)" class="expand" onclick="return togglecomment(this)">[–]</a>
                <a href="./v/user/sureone" class="author may-blank">小网</a>
                <span class="userattrs"></span>
                <span class="score dislikes" title="44">0</span>
                <span class="score unvoted" title="45">0指标</span>
                <span class="score likes" title="46">0指標</span>
                <time class="live-timestamp timeago" datetime="2017-04-16 09:48:58"></time>
                &nbsp;
                <a href="javascript:void(0)" class="numchildren" onclick="return togglecomment(this)">(0下层留言)</a>

                <span class="reply-button"><a class="access-required" href="javascript:void(0)" data-thingid="195"  data-mainid="190" onclick="return reply(this)">回复</a></span>
            </p>
            <form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')"
                  id="form-t1_df1l1xva7c"><input type="hidden" name="thing_id" value="t1_df1l1xv">

                <div class="usertext-body may-blank-within md-container ">
                    <div class="out md"><script>document.write(markdown.toHTML("怒发冲冠，凭阑处、潇潇雨歇。抬望眼、仰天长啸，壮怀激烈。三十功名尘与土，八千里路云和月。莫等闲，白了少年头，空悲切。\n\n\n\nfdsaf\n\n\n\n靖康耻，犹未雪；臣子恨，何时灭。驾长车，踏破贺兰山缺。壮志饥餐胡虏肉，笑谈渴饮匈奴血。待从头、收拾旧山河，朝天阙。"));</script>

                         <div class="thing-attaches">
                             
                        </div>
                      </div>
                </div>
            </form>
            <ul class="flat-list buttons">
               <!--  <li class="first"><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/df1l1xv/"
                     >永久連結</a></li>
                <li><a href="javascript:void(0)"
                                             class="embed-comment">embed</a></li>
                <li class="comment-save-button save-button"><a href="javascript:void(0)">儲存</a></li>
                <li class="report-button"><a href="javascript:void(0)" class="reportbtn access-required"
                                              data-thingid="195">檢舉</a></li>
                <li class="give-gold-button"><a href="">贈送金幣</a></li> -->
                
            </ul>
            <div class="reportform report-t1_df1l1xv"></div>
        </div>
        <div class="child" id="child_195">
            <div id="siteTable_child_195" class="sitetable listing">
                                                        

                                            </div>
        </div>
        <div class="clearleft"></div>
    </div>
        <div class=" thing id-194 noncollapsed   comment " id="thing_194" onclick="">
        <p class="parent"><a name="df1l1xv"></a></p>
        <div class="midcol unvoted" id="vote-194">
            <div class="arrow up login-required access-required" onclick="voteit('./api',this,1,194)"></div>
            <div class="arrow down login-required access-required"  onclick="voteit('./api',this,-1,194)"></div>
        </div>
        <div class="entry unvoted">
            <p class="tagline">
                <a href="javascript:void(0)" class="expand" onclick="return togglecomment(this)">[–]</a>
                <a href="./v/user/sureone" class="author may-blank">小网</a>
                <span class="userattrs"></span>
                <span class="score dislikes" title="44">0</span>
                <span class="score unvoted" title="45">0指标</span>
                <span class="score likes" title="46">0指標</span>
                <time class="live-timestamp timeago" datetime="2017-04-16 09:48:40"></time>
                &nbsp;
                <a href="javascript:void(0)" class="numchildren" onclick="return togglecomment(this)">(0下层留言)</a>

                <span class="reply-button"><a class="access-required" href="javascript:void(0)" data-thingid="194"  data-mainid="190" onclick="return reply(this)">回复</a></span>
            </p>
            <form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')"
                  id="form-t1_df1l1xva7c"><input type="hidden" name="thing_id" value="t1_df1l1xv">

                <div class="usertext-body may-blank-within md-container ">
                    <div class="out md"><script>document.write(markdown.toHTML("怒发冲冠，凭阑处、潇潇雨歇。\n\n抬望眼、仰天长啸，壮怀激烈。"));</script>

                         <div class="thing-attaches">
                             
                        </div>
                      </div>
                </div>
            </form>
            <ul class="flat-list buttons">
               <!--  <li class="first"><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/df1l1xv/"
                     >永久連結</a></li>
                <li><a href="javascript:void(0)"
                                             class="embed-comment">embed</a></li>
                <li class="comment-save-button save-button"><a href="javascript:void(0)">儲存</a></li>
                <li class="report-button"><a href="javascript:void(0)" class="reportbtn access-required"
                                              data-thingid="194">檢舉</a></li>
                <li class="give-gold-button"><a href="">贈送金幣</a></li> -->
                
            </ul>
            <div class="reportform report-t1_df1l1xv"></div>
        </div>
        <div class="child" id="child_194">
            <div id="siteTable_child_194" class="sitetable listing">
                                                        

                                            </div>
        </div>
        <div class="clearleft"></div>
    </div>
        <div class=" thing id-193 noncollapsed   comment " id="thing_193" onclick="">
        <p class="parent"><a name="df1l1xv"></a></p>
        <div class="midcol unvoted" id="vote-193">
            <div class="arrow up login-required access-required" onclick="voteit('./api',this,1,193)"></div>
            <div class="arrow down login-required access-required"  onclick="voteit('./api',this,-1,193)"></div>
        </div>
        <div class="entry unvoted">
            <p class="tagline">
                <a href="javascript:void(0)" class="expand" onclick="return togglecomment(this)">[–]</a>
                <a href="./v/user/sureone" class="author may-blank">小网</a>
                <span class="userattrs"></span>
                <span class="score dislikes" title="44">0</span>
                <span class="score unvoted" title="45">0指标</span>
                <span class="score likes" title="46">0指標</span>
                <time class="live-timestamp timeago" datetime="2017-04-16 09:48:22"></time>
                &nbsp;
                <a href="javascript:void(0)" class="numchildren" onclick="return togglecomment(this)">(0下层留言)</a>

                <span class="reply-button"><a class="access-required" href="javascript:void(0)" data-thingid="193"  data-mainid="190" onclick="return reply(this)">回复</a></span>
            </p>
            <form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')"
                  id="form-t1_df1l1xva7c"><input type="hidden" name="thing_id" value="t1_df1l1xv">

                <div class="usertext-body may-blank-within md-container ">
                    <div class="out md"><script>document.write(markdown.toHTML("sdafdsafdsa\n\nfdsafdsa\n\nfdsafdsa\n\nfdsafds"));</script>

                         <div class="thing-attaches">
                             
                        </div>
                      </div>
                </div>
            </form>
            <ul class="flat-list buttons">
               <!--  <li class="first"><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/df1l1xv/"
                     >永久連結</a></li>
                <li><a href="javascript:void(0)"
                                             class="embed-comment">embed</a></li>
                <li class="comment-save-button save-button"><a href="javascript:void(0)">儲存</a></li>
                <li class="report-button"><a href="javascript:void(0)" class="reportbtn access-required"
                                              data-thingid="193">檢舉</a></li>
                <li class="give-gold-button"><a href="">贈送金幣</a></li> -->
                
            </ul>
            <div class="reportform report-t1_df1l1xv"></div>
        </div>
        <div class="child" id="child_193">
            <div id="siteTable_child_193" class="sitetable listing">
                                                        

                                            </div>
        </div>
        <div class="clearleft"></div>
    </div>
        <div class=" thing id-192 noncollapsed   comment " id="thing_192" onclick="">
        <p class="parent"><a name="df1l1xv"></a></p>
        <div class="midcol unvoted" id="vote-192">
            <div class="arrow up login-required access-required" onclick="voteit('./api',this,1,192)"></div>
            <div class="arrow down login-required access-required"  onclick="voteit('./api',this,-1,192)"></div>
        </div>
        <div class="entry unvoted">
            <p class="tagline">
                <a href="javascript:void(0)" class="expand" onclick="return togglecomment(this)">[–]</a>
                <a href="./v/user/sureone" class="author may-blank">小网</a>
                <span class="userattrs"></span>
                <span class="score dislikes" title="44">0</span>
                <span class="score unvoted" title="45">0指标</span>
                <span class="score likes" title="46">0指標</span>
                <time class="live-timestamp timeago" datetime="2017-04-16 09:48:14"></time>
                &nbsp;
                <a href="javascript:void(0)" class="numchildren" onclick="return togglecomment(this)">(0下层留言)</a>

                <span class="reply-button"><a class="access-required" href="javascript:void(0)" data-thingid="192"  data-mainid="190" onclick="return reply(this)">回复</a></span>
            </p>
            <form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')"
                  id="form-t1_df1l1xva7c"><input type="hidden" name="thing_id" value="t1_df1l1xv">

                <div class="usertext-body may-blank-within md-container ">
                    <div class="out md"><script>document.write(markdown.toHTML("**粗體**"));</script>

                         <div class="thing-attaches">
                             
                        </div>
                      </div>
                </div>
            </form>
            <ul class="flat-list buttons">
               <!--  <li class="first"><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/df1l1xv/"
                     >永久連結</a></li>
                <li><a href="javascript:void(0)"
                                             class="embed-comment">embed</a></li>
                <li class="comment-save-button save-button"><a href="javascript:void(0)">儲存</a></li>
                <li class="report-button"><a href="javascript:void(0)" class="reportbtn access-required"
                                              data-thingid="192">檢舉</a></li>
                <li class="give-gold-button"><a href="">贈送金幣</a></li> -->
                
            </ul>
            <div class="reportform report-t1_df1l1xv"></div>
        </div>
        <div class="child" id="child_192">
            <div id="siteTable_child_192" class="sitetable listing">
                                                        

                                            </div>
        </div>
        <div class="clearleft"></div>
    </div>
        <div class=" thing id-191 noncollapsed   comment " id="thing_191" onclick="">
        <p class="parent"><a name="df1l1xv"></a></p>
        <div class="midcol unvoted" id="vote-191">
            <div class="arrow up login-required access-required" onclick="voteit('./api',this,1,191)"></div>
            <div class="arrow down login-required access-required"  onclick="voteit('./api',this,-1,191)"></div>
        </div>
        <div class="entry unvoted">
            <p class="tagline">
                <a href="javascript:void(0)" class="expand" onclick="return togglecomment(this)">[–]</a>
                <a href="./v/user/sureone" class="author may-blank">小网</a>
                <span class="userattrs"></span>
                <span class="score dislikes" title="44">0</span>
                <span class="score unvoted" title="45">0指标</span>
                <span class="score likes" title="46">0指標</span>
                <time class="live-timestamp timeago" datetime="2017-04-16 09:48:02"></time>
                &nbsp;
                <a href="javascript:void(0)" class="numchildren" onclick="return togglecomment(this)">(0下层留言)</a>

                <span class="reply-button"><a class="access-required" href="javascript:void(0)" data-thingid="191"  data-mainid="190" onclick="return reply(this)">回复</a></span>
            </p>
            <form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')"
                  id="form-t1_df1l1xva7c"><input type="hidden" name="thing_id" value="t1_df1l1xv">

                <div class="usertext-body may-blank-within md-container ">
                    <div class="out md"><script>document.write(markdown.toHTML("怒发冲冠，凭阑处、潇潇雨歇。抬望眼、仰天长啸，壮怀激烈。三十功名尘与土，八千里路云和月。莫等闲，白了少年头，空悲切。\n\n  靖康耻，犹未雪；臣子恨，何时灭。驾长车，踏破贺兰山缺。壮志饥餐胡虏肉，笑谈渴饮匈奴血。待从头、收拾旧山河，朝天阙。"));</script>

                         <div class="thing-attaches">
                             
                        </div>
                      </div>
                </div>
            </form>
            <ul class="flat-list buttons">
               <!--  <li class="first"><a
                        href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/df1l1xv/"
                     >永久連結</a></li>
                <li><a href="javascript:void(0)"
                                             class="embed-comment">embed</a></li>
                <li class="comment-save-button save-button"><a href="javascript:void(0)">儲存</a></li>
                <li class="report-button"><a href="javascript:void(0)" class="reportbtn access-required"
                                              data-thingid="191">檢舉</a></li>
                <li class="give-gold-button"><a href="">贈送金幣</a></li> -->
                
            </ul>
            <div class="reportform report-t1_df1l1xv"></div>
        </div>
        <div class="child" id="child_191">
            <div id="siteTable_child_191" class="sitetable listing">
                                                        

                                            </div>
        </div>
        <div class="clearleft"></div>
    </div>
    

    </div>

</div>
<iframe src="" style="display:none;" id="iframe_upload" name="iframe_upload"></iframe>
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
<script id="tpl-attach-tool" type="text/x-handlebars-template">
<div class="attach-tool" style="border:1px dotted gray;">
    <span class="title required-roundfield">附件</span>
    <ul id="attaches">   
    </ul>    
    <form action="http://127.0.0.1/ama/index.php/v/do_upload" enctype="multipart/form-data" method="post" accept-charset="utf-8" target="iframe_upload">
        <input type="file" name="userfile" size="20" />
        <input type="submit" value="upload" />
    </form>
</div>
</script>

<script id="tpl-file-attach" type="text/x-handlebars-template">
    
    <li class="attach-file new" file_id="{{file_id}}">
        {{#if _image_file}}
        	<a href="./uploads/{{file_name}}"><img width="160" src="./uploads/{{file_name}}"></a>
        {{/if}}
        <a href="javascript:removeNewAttach({{file_id}})">删除附件</a>
        <a href="javascript:changeAttachOrder({{file_id}},-1)">向上</a>
        <a href="javascript:changeAttachOrder({{file_id}},1)">向下</a>
        
        <input type="text" name="attach-comment-{{file_id}}" value="" placeholder="附件说明({{file_name}})">
    </li>
    
</script>
<script type="text/javascript" src="./static/js/comments.js?v=8"></script>
</html><?php }
}
