<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?><!DOCTYPE html>

<html lang="en">
<head>
    {include file='common/page-header.tpl'}
    <style type="text/css">
        {literal}
        .infobar {
            background-color: #FFB6C1;
        }
        {/literal}
    </style>
</head>

<body class="listing-page {if $logined eq "true"}loggedin{/if} {$page}-page">


<div id="header">
    <div id="header-bottom-left">
        <span class="hover pagename"><a href="./">AMA</a></span>
        <ul class="tabmenu ">
            <li class="selected"><a href="./v/submit" class="choice">发表</a></li>
    </div>
    {include file='common/header-bottom-right.tpl'}
</div>
{include file='common/side.tpl'}

<div class="content">
    <form class="submit content warn-on-unload" onsubmit="handleFormSubmit(this); return false;" action="./api" id="newlink" method="post">
        <input name="action" value="submit-new-link" type="hidden">
        
        <div class="formtabs-content">
            <div class="spacer">
                <div id="text-desc" class="infobar">
                    你正要发表以文字为主的文章，请畅所欲言。发文时必须注明标题，但不一定要在文字栏中长篇大论。使用「如果你...请帮我加分」作为主题，是违反银河法规的。
                </div>
            </div>
            <div class="spacer">
                <div class="roundfield " id="title-field">
                    <span class="title required-roundfield">标题</span>
                    <div class="roundfield-content">
                        <textarea name="title" rows="2" required=""></textarea>
                        <div class="error NO_TEXT field-title" style="display:none"></div>
                        <div class="error TOO_LONG field-title" style="display:none"></div>
                    </div>
                </div>
            </div>
            <div class="spacer">
                <div class="roundfield " id="text-field">
                    <span class="title ">文字</span> 
                    <span class="little gray roundfield-description">(非必填项目)</span>
                    <div class="roundfield-content"><input name="kind" value="self" type="hidden">

                        <div class="usertext">
                            <input type="hidden" name="thing_id" value="">
                            <div class="usertext-edit md-container" style="">
                                <div class="md">
                                    <textarea rows="1" cols="1" name="content" class=""></textarea>
                                </div>

                                <div class="bottom-area">
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
                                
                                {include file="common/markhelp.tpl"}
                            </div>
                        </div>
                        <span class="error NO_SELFS field-sr" style="display:none"></span>
                    </div>
                </div>
            </div>

            
            <!-- <div class="spacer">
                <div class="roundfield "><span class="title ">選項</span>

                    <div class="roundfield-content">
                        <input class="nomargin" type="checkbox" checked="checked" name="sendreplies" id="sendreplies" data-send-checked="true">
                        <label for="sendreplies">將回覆寄到我的收件匣</label></div>
                </div>
            </div> -->

       <!--  <div class="roundfield info-notice">please be mindful of reddit's 
            <a href="https://www.reddit.com/help/contentpolicy" target="_blank">內容政策</a>
             and practice 
            <a href="https://www.reddit.com/wiki/reddiquette" target="_blank">良好的 reddit 站規</a>.
        </div> -->
        <!-- <div id="items-required">*required</div> -->
        <input name="resubmit" value="" type="hidden">

        <div class="spacer">
            <button class="btn" name="submit" value="form" type="submit">送出</button>
            <span class="status"></span>
            <span class="error RATELIMIT field-ratelimit" style="display:none"></span>
            <span class="error INVALID_OPTION field-sr" style="display:none"></span>
            <span class="error IN_TIMEOUT field-sr" style="display:none"></span>
        </div>
    </form>
</div>

<div id="footer"></div>
{include 'common/login-modal.tpl'}
</body>


<script type="text/javascript" src="./static/js/submit.js?v=8"></script>
</html>