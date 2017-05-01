<div id="header-bottom-right">
{if $logined eq "true"}
<span class="user"><a href="./v/user/{$user.userid}/">{$user.name}</a>&nbsp;<span class="separator">|</span><a title="沒有新郵件" href="./v/message/inbox/" class="nohavemail" id="mail">信息&nbsp;({$new_number})</a>
<!-- 
<span class="separator">|</span><ul class="flat-list hover">
<li><a href="https://www.reddit.com/prefs/" class="pref-lang choice">偏好設定</a></li>
</ul>
 -->
<span class="separator">|</span><form method="post" id="logout-form" action="./api" class="logout hover"><input type="hidden" name="uh" value="cqaeyi63ta407191a49905d7c0b26e7c2a75cb1b81ddd77995"><input type="hidden" name="top" value="off"><input type="hidden" name="action" value="logout"><a href="javascript:void(0)" onclick="$(this).parent().submit()">登出</a></form>
{else}
<span class="user">想要加入?  一秒<a href="" class="login-required">登入或註冊</a></span>

<!-- <span class="separator">|</span>
<ul class="flat-list hover">
	<li><a href="javascript:void(0)" class="pref-lang choice" onclick="return showlang();">中文</a></li>
</ul>
 -->
{/if}

</div>