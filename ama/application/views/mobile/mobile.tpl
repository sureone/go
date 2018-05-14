
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>南京市管培一体化考试</title>
    <link rel="stylesheet" href="./static/css/weui.css"/>
    <link rel="stylesheet" href="./static/css/app.css"/>
<script type="text/javascript">

    var questions = {json_encode_utf8($questions)};
    var questions_num = {count($questions)};
  </script>
    
</head>
<body ontouchstart>
    <div class="weui-toptips weui-toptips_warn js_tooltips">错误提示</div>

    <div class="container" id="container"></div>

    <script type="text/html" id="tpl_home">
        <div class="page">
          
            <div class="page__bd ">
                <h4 class="weui-media-box__title" style="    text-align: center;margin-top:20px;margin-bottom:20px;">请输入姓名和单位信息开始考试</h4>
                <div class="weui-cells weui-cells_form">
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">姓名</label></div>
                        <div class="weui-cell__bd">
                            <input class="weui-input" type="text" name="name" value="" placeholder="请输入姓名"/>
                        </div>
                    </div>

                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">单位</label></div>
                        <div class="weui-cell__bd">
                            <input class="weui-input" type="text" name="danwei" value="" placeholder="请输入单位"/>
                        </div>
                    </div>


                    
                    
                </div>

                <div class="button-sp-area page__bd_spacing" style="margin-top:20px;">
                    <a href="javascript:;" data-id="question" class="weui-btn weui-btn_plain-default" id="login">开始</a>
                </div>
            </div>
            <div class="page__ft">
                <div class="weui-footer">
                    <p class="weui-footer__links">
                        <a href="javascript:void(0);" class="weui-footer__link">一体化管培项目</a>
                    </p>
                    <p class="weui-footer__text">Copyright © 2018 鼓楼医院 内分泌</p>
                </div>
            </div>
        </div>
    </script>


    <script type="text/html" id="tpl_admin">
        <div class="page badge">
            <div class="page__hd" style="text-align:center;">
                <h1 class="">统计</h1>
                <p class="">共{count($exam_users)}人</p>
            </div>
            <div class="page__bd">
                
                <div class="weui-cells">
                {foreach $exam_users as $entry}
                
                    <div class="weui-cell">
                        
                        <div class="weui-cell__bd">
                            <p>{$entry.name}</p>
                            <p style="font-size: 13px;color: #888888;">{$entry.danwei}</p>
                        </div>

                        <div class="weui-cell__ft" style="font-size:25px;">{$entry.score}／{count($questions)}</div>
                    </div>
                    
                
                {/foreach}
                </div>
            </div>
            
        </div>
    </script>


     <script type="text/html" id="tpl_question_end">
        <div class="page" id="page-question-end">
          
            <div class="page__bd"  style="margin-top:100px;">
                <div class="icon-box" style="text-align:center;">
                    <i class="weui-icon_msg result-icon"></i>
                   
                </div>
                <h4 class="weui-media-box__title" style="    text-align: center;margin-top:20px;margin-bottom:20px;">答对题数：<span class="correct"></span></h4>
                
                
                
            </div>
            <div class="page__bd page__bd_spacing">
                <div class="button-sp-area">
                    <a href="javascript:;" class="weui-btn weui-btn_plain-primary next_question" data-id="question" >再做一次</a>
                </div>
            </div>

            <div class="page__bd page__bd_spacing" style="margin-top:20px;">
                <div class="button-sp-area">
                    <a href="javascript:;" class="weui-btn weui-btn_plain-default close-window"  >退出</a>
                </div>
            </div>



            
           
        </div>
    </script>


  
   
   {foreach $questions as $entry}
    <script type="text/html" id="tpl_question_{$entry.no}">
        <div class="page panel" id="question-page-{$entry.no}">
            <div class="page__bd page__bd_spacing">
                <h1 class="page__title" style="text-align:center;margin-top:20px;">第{$entry.no}题</h1>
                <p class="page__desc">{$entry.title}</p>
            </div>
            <div class="page__bd">
          
                <div class="weui-form-preview">
                    <div class="weui-cells weui-cells_checkbox">
                    {foreach $entry.options as $option}



                       
                            <label class="weui-cell weui-check__label" for="o-{$entry.no}-{$option@index}">
                                <div class="weui-cell__hd">
                                    <input type="checkbox" class="weui-check" name="o-{$entry.no}-{$option@index}" id="o-{$entry.no}-{$option@index}">
                                    <i class="weui-icon-checked"></i>
                                </div>
                                <div class="weui-cell__bd">
                                    <p>{$option}</p>
                                </div>
                            </label>
                        
                    {/foreach}

                    </div>
                    <div class="weui-form-preview__ft">
                        <a class="weui-form-preview__btn weui-form-preview__btn_default last_question" href="javascript:" data-id="question">上一题</a>

                        <button type="submit" class="weui-form-preview__btn weui-form-preview__btn_primary next_question" href="javascript:"  data-id="question">下一题</button>

                    </div>
                </div>
                
            </div>
            <div class="page__ft">
                <div class="weui-footer">
                    <p class="weui-footer__links">
                        <a href="javascript:void(0);" class="weui-footer__link">一体化管培项目</a>
                    </p>
                    <p class="weui-footer__text">Copyright © 2018 鼓楼医院 内分泌</p>
                </div>
            </div>
            
        </div>
    </script>
    {/foreach}

    <script src="./static/js/zepto.min.js"></script>
    <script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script src="https://res.wx.qq.com/open/libs/weuijs/1.0.0/weui.min.js"></script>
    <script src="./static/js/app.js?v=4"></script>
</body>
</html>
