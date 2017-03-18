<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?><!DOCTYPE html>

<html lang="en">
<head>
    <?php include 'common/page-header.php' ?>
</head>
<?php include 'common/thread-tpl.php' ?>
<body class="<?php if (isset($user)) {
    echo 'loggedin';
} ?> comments-page">


<div id="header">
    <div id="header-bottom-left">
        <span class="hover pagename"><a href="./">AMA</a></span>
        <ul class="tabmenu ">
            <li class="selected"><a href="./v/comments/<?= $thingid ?>" class="choice">留言</a></li>
        </ul>
    </div>
    <?php include 'common/header-bottom-right.php' ?>
</div>
<?php include 'common/side.php' ?>

<div class="content">

    <div id="siteTable" class="sitetable linklisting">
        <div class=" thing id-t3_5zwc09 odd  link self" id="thing_t3_5zwc09" onclick="click_thing(this)"
             data-fullname="t3_5zwc09" data-type="link" data-cid="" data-author="its710somewhere"
             data-author-fullname="t2_t22bo" data-subreddit="AMA" data-subreddit-fullname="t5_2r4eo"
             data-timestamp="1489737081000"
             data-url="/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/" data-domain="self.AMA"
             data-rank="" data-context="comments"><p class="parent"></p><span class="rank"></span>

            <div class="midcol unvoted">
                <div class="arrow up login-required access-required" data-event-action="upvote" role="button"
                     aria-label="推" tabindex="0"></div>
                <div class="score dislikes" title="418">418</div>
                <div class="score unvoted" title="419">419</div>
                <div class="score likes" title="420">420</div>
                <div class="arrow down login-required access-required" data-event-action="downvote" role="button"
                     aria-label="噓" tabindex="0"></div>
            </div>
            <div class="entry unvoted"><p class="title"><a class="title may-blank loggedin " data-event-action="title"
                                                           href="/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/"
                                                           tabindex="1"
                                                           data-href-url="/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/"
                                                           data-inbound-url="/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?utm_content=title&amp;utm_medium=front&amp;utm_source=reddit&amp;utm_name=AMA"
                                                           rel="">I have been to prison in NY and NJ several times. AMA
                        about those state prisons and the differences between them.</a> <span class="domain">(<a
                            href="/r/AMA/">self.AMA</a>)</span></p>

                <p class="tagline">submitted
                    <time title="Fri Mar 17 07:51:21 2017 UTC" datetime="2017-03-17T07:51:21+00:00"
                          class="live-timestamp">21小時前
                    </time>
                    by <a href="https://www.reddit.com/user/its710somewhere" class="author may-blank id-t2_t22bo">its710somewhere</a><span
                        class="userattrs"></span></p>
                <div class="expando">
                    <form action="#" class="usertext warn-on-unload" onsubmit="return post_form(this, 'editusertext')"
                          id="form-t3_5zwc09ma0"><input type="hidden" name="thing_id" value="t3_5zwc09">

                        <div class="usertext-body may-blank-within md-container ">
                            <div class="md"><p>I made an offhand comment in a /mildlyinteresting post about having been
                                    to prison. 6 people have asked me to do an AMA in response to that comment. I think
                                    this type of AMA is posted far too often, but these people obviously disagree. So
                                    here it is. Ask me anything. I will answer any question that does not risk revealing
                                    my IRL identity. </p>
                            </div>
                        </div>
                    </form>
                </div>
                <ul class="flat-list buttons">
                    <li class="first"><a
                            href="https://www.reddit.com/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/"
                            data-inbound-url="/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?utm_content=comments&amp;utm_medium=front&amp;utm_source=reddit&amp;utm_name=AMA"
                            data-href-url="/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/"
                            data-event-action="comments" class="bylink comments may-blank" rel="nofollow">387 留言</a>
                    </li>
                    <li class="share"><a class="post-sharing-button" href="javascript: void 0;">分享</a></li>
                    <li class="link-save-button save-button"><a href="#">儲存</a></li>
                    <li>
                        <form action="/post/hide" method="post" class="state-button hide-button"><input type="hidden"
                                                                                                        name="executed"
                                                                                                        value="隱藏"><span><a
                                    href="javascript:void(0)" class=" " data-event-action="hide"
                                    onclick="change_state(this, 'hide', hide_thing);">隱藏</a></span></form>
                    </li>
                    <li class="give-gold-button"><a href="/gold?goldtype=gift&amp;months=1&amp;thing=t3_5zwc09"
                                                    title="give reddit gold in appreciation of this post."
                                                    class="give-gold login-required access-required"
                                                    data-event-action="gild">贈送金幣</a></li>
                    <li class="report-button"><a href="javascript:void(0)" class="reportbtn access-required"
                                                 data-event-action="report">檢舉</a></li>
                </ul>
                <div class="reportform report-t3_5zwc09"></div>
            </div>
            <div class="child"></div>
            <div class="clearleft"></div>
        </div>
        <div class="clearleft"></div>

    </div>
    <div class="commentarea">
        <div class="panestack-title"><span class="title">頭 200 則留言</span><a
                href="/r/AMA/comments/5zwc09/i_have_been_to_prison_in_ny_and_nj_several_times/?limit=500"
                class="title-button ">顯示所有387</a></div>
        <div class="menuarea">
            <div class="spacer"><span class="dropdown-title lightdrop">排序依據: </span>

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

    <script type="text/javascript">
        (function () {
            var tpl = Handlebars.compile($("#tpl-comment-edit").html());
            document.write(tpl({thingid:<?=$thingid?>}))
        })();

        <?php echo "var g_thingid={$thingid};"; ?>
    </script>

    <div id="siteTable_<?=$thingid?>" class="sitetable nestedlisting">
        <script>
        (function () {
            var tpl = Handlebars.compile($("#tpl-comment").html());
            document.write(tpl({thingid:<?=$thingid?>}))
        })();
        </script>
    </div>

</div>

<div id="footer"></div>
<?php include 'common/login-modal.php' ?>
</body>


<script type="text/javascript" src="./static/js/comments.js?v=8"></script>
</html>