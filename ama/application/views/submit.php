<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?><!DOCTYPE html>

<html lang="en">
<head>
    <?php include 'common/page-header.php' ?>
    <style type="text/css">
        .infobar {
            background-color: #FFB6C1;
        }
    </style>
</head>
<?php include 'common/thread-tpl.php' ?>
<body class="listing-page <?php if (isset($user)) {
    echo 'loggedin';
} ?> hot-page">


<div id="header">
    <div id="header-bottom-left">
        <span class="hover pagename"><a href="./">AMA</a></span>
        <ul class="tabmenu ">
            <li class="selected"><a href="./v/submit" class="choice">发表</a></li>
    </div>
    <?php include 'common/header-bottom-right.php' ?>
</div>
<?php include 'common/side.php' ?>

<div class="content">
    <form class="submit content warn-on-unload" onsubmit="handleFormSubmit(this); return false;" action="" id="newlink"
          method="post"><input type="hidden" name="uh" value="30rij8g6va0ca3449b2951f463f6f4146a35b9c0135e54bd03">

        <div class="formtabs-content">
            <div class="spacer">
                <div id="text-desc" class="infobar">
                    你正要發表以文字為主的文章，請暢所欲言。發文時必須註明標題，但不一定要在文字欄位中長篇大論。使用「如果你...請幫我加分」作為標題，是違反銀河法規的。
                </div>
            </div>
            <div class="spacer">
                <div class="roundfield " id="title-field"><span class="title required-roundfield">標題</span>

                    <div class="roundfield-content"><textarea name="title" rows="2" required=""></textarea>

                        <div class="error NO_TEXT field-title" style="display:none"></div>
                        <div class="error TOO_LONG field-title" style="display:none"></div>
                    </div>
                </div>
            </div>
            <div class="spacer">
                <div class="roundfield " id="text-field"><span class="title ">文字</span> <span
                        class="little gray roundfield-description">(非必填項目)</span>

                    <div class="roundfield-content"><input name="kind" value="self" type="hidden">

                        <div class="usertext"><input type="hidden" name="thing_id" value="">

                            <div class="usertext-edit md-container" style="">
                                <div class="md"><textarea rows="1" cols="1" name="text" class=""></textarea></div>
                                
                                <?php include "common/markhelp.php" ?>
                            </div>
                        </div>
                        <span class="error NO_SELFS field-sr" style="display:none"></span></div>
                </div>
            </div>

            
            <div class="spacer">
                <div class="roundfield "><span class="title ">選項</span>

                    <div class="roundfield-content"><input class="nomargin" type="checkbox" checked="checked"
                                                           name="sendreplies" id="sendreplies" data-send-checked="true"><label
                            for="sendreplies">將回覆寄到我的收件匣</label></div>
                </div>
            </div>

        <div class="roundfield info-notice">please be mindful of reddit's <a
                href="https://www.reddit.com/help/contentpolicy" target="_blank">內容政策</a> and practice <a
                href="https://www.reddit.com/wiki/reddiquette" target="_blank">良好的 reddit 站規</a>.
        </div>
        <div id="items-required">*required</div>
        <input name="resubmit" value="" type="hidden">

        <div class="spacer">
            <button class="btn" name="submit" value="form" type="submit">送出</button>
            <span class="status"></span><span class="error RATELIMIT field-ratelimit" style="display:none"></span><span
                class="error INVALID_OPTION field-sr" style="display:none"></span><span
                class="error IN_TIMEOUT field-sr" style="display:none"></span></div>
    </form>
</div>

<div id="footer"></div>
<?php include 'common/login-modal.php' ?>
</body>


<script type="text/javascript" src="./static/js/submit.js?v=8"></script>
</html>