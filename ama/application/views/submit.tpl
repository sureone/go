
    {include file='common/page-header.tpl'}


    <script type="text/javascript" src="./static/js/submit.js?v=8"></script>

</head>

<body class="listing-page {if $logined eq "true"}loggedin{/if} {$page}-page">


<div id="header">
    <div id="header-bottom-left">
        {include file="common/page-logo.tpl"}
        <ul class="tabmenu ">
            <li class="selected"><a href="./v/submit" class="choice">{if isset($thing)}编辑{else}发表{/if}</a></li>
    </div>
    {include file='common/header-bottom-right.tpl'}
</div>
{include file='common/side.tpl'}

<div class="content">

        
        <div class="formtabs-content">
        <form class="submit content warn-on-unload" action="./api" id="newlink" method="post">
            <input name="action" value="submit-new-link" type="hidden">
            <div class="spacer">
                <div id="text-desc" class="infobar">
                    你正要发表以文字为主的文章，请畅所欲言。发文时必须注明标题，但不一定要在文字栏中长篇大论。使用「如果你...请帮我加分」作为主题，是违反后园法规的。
                </div>
            </div>
            <div class="spacer">
                <div class="roundfield " id="title-field">
                    <span class="title required-roundfield">标题</span>
                    <div class="roundfield-content">
                        <textarea class="thing-title key-monitor" name="title" rows="1" required="">{if isset($thing)}{$thing.title}{/if}</textarea>
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
                                    <textarea rows="1" cols="1" name="content" style="height:120px;" class="thing-title key-monitor" >{if isset($thing)}{$thing.text}{/if}</textarea>
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
        <input name="thingid" value="{if isset($thing)}{$thing.thingid}{else}0{/if}" type="hidden">
    </form>
    
       
    
    <div class="spacer">
        <div class="roundfield " id="title-field">
            <span class="title required-roundfield">附件</span>

            <iframe src="" style="display:none;" id="iframe_upload" name="iframe_upload"></iframe>
            <ul id="attaches">
                {if isset($thing)}
                {foreach $thing.attaches as $attach}
                 <li class="attach-file old" file_id="{$attach.id}">
                    {if $attach.image_width neq 0}
                        <a href="./uploads/{$attach.file_name}"><img width="160" src="./uploads/{$attach.file_name}"></a>
                    {/if}
                    <a href="javascript:removeOldAttach({$thing.thingid},{$attach.id})">删除附件</a>
                    <a href="javascript:changeAttachOrder({$attach.id},-1)">向上</a>
                    <a href="javascript:changeAttachOrder({$attach.id},1)">向下</a>
                    <input type="text" name="attach-comment-{$attach.id}" value="{$attach.file_comment}" placeholder="附件说明({$attach.file_name})">
                </li>
                {/foreach}
                {/if}

            </ul>    

            <form action="./v/do_upload" enctype="multipart/form-data" method="post" accept-charset="utf-8" target="iframe_upload">
                <input type="file" name="userfile" size="20" />
                <input type="submit" value="upload" />
            </form>
           
        </div>
    </div>    
    <div class="spacer">
        <button class="btn" name="submit" value="" type="button"
         onclick="javascript:handleFormSubmit(document.getElementById('newlink'));">送出</button>
        <span class="status"></span>
        <span class="error RATELIMIT field-ratelimit" style="display:none"></span>
        <span class="error INVALID_OPTION field-sr" style="display:none"></span>
        <span class="error IN_TIMEOUT field-sr" style="display:none"></span>
    </div>

</div>

<div id="footer"></div>
{include 'common/login-modal.tpl'}
{include 'common/file-attach.tpl'}
</body>




</html>