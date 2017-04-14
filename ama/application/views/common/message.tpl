<div class=" thing id-t4_7x41ei noncollapsed recipient odd {$entry.stype} ">
  <p class="parent"></p>
  <p class="subject">

    <span class="subject-text">
    	{if $entry.stype eq "message"}
    		{$entry.title}
    	{else}
    		回帖
    	{/if}
    </span>
	{if $entry.stype neq "message"}
	<a href="./v/comments/{$entry.main}" class="title">
		{if isset($entry.p_title)}
			{$entry.p_title}
		{else}
			{$entry.p_text}
		{/if}
	</a>
	{/if}
  </p>
  <p>
  </p>
  <div class="entry unvoted">
    <p class="tagline">
      <span class="head">来自
        <span class="sender">
          <a href="./v/user/{$entry.author}" class="author may-blank">{$entry.author}</a>
          <span class="userattrs"></span>
        </span>
        <time class="timeago" datetime="{$entry.timeago}" class=""></time></span>
    </p>
    <div class="md-container">
     <div class="out md"><script>document.write(markdown.toHTML("{$entry.text|regex_replace:'/[\r\t\n]/':'\\n'|regex_replace:'/[\"]/':'\\\"'|regex_replace:'/[\']/':'\\\''}"));</script></div>
    </div>
    <ul class="flat-list buttons">
      <li class="first">
        <a href="">永久連結</a></li>
      <li>
        <form class="toggle del_msg-button " action="#" method="get">
          <input type="hidden" name="executed" value="已刪除">
          <span class="option main active">
            <a href="#" class="togglebutton " onclick="return toggle(this)" data-event-action="delete_message">刪除</a></span>
          <span class="option error">你確定嗎？
            <a href="javascript:void(0)" class="yes" onclick="change_state(this, &quot;del_msg&quot;, hide_thing, undefined, null)">是</a>/
            <a href="javascript:void(0)" class="no" onclick="return toggle(this)">否</a></span>
        </form>
      </li>
      <li class="report-button">
        <a href="javascript:void(0)" class="reportbtn access-required" data-event-action="report">檢舉</a></li>
      <li class="unread">
        <form action="/post/unread" method="post" class="state-button unread-button">
          <input type="hidden" name="executed" value="未讀">
          <span>
            <a href="javascript:void(0)" class=" access-required" data-event-action="mark_unread" onclick="return change_state(this, 'unread_message', unread_thing, true);">標記成未讀取</a></span>
        </form>
      </li>

      {if $entry.stype neq "message"}
      <li><a class="access-required" href="javascript:void(0)"  data-thingid="{$entry.thingid}" data-mainid="{$entry.main}"  onclick="return reply(this)">回覆</a></li>
      {/if}
    </ul>
    <div class="reportform report-t4_7x41ei"></div>
  </div>
  <div class="child" id="child_{$entry.thingid}"></div>
  <div class="clearleft"></div>
</div>
