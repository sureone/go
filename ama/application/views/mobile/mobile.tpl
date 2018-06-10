
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>南京市社区糖尿病管培一体化项目</title>
    <link rel="stylesheet" href="./static/css/weui.css"/>
    <link rel="stylesheet" href="./static/css/app.css"/>
<script type="text/javascript">

    var questions = {json_encode_utf8($questions)};
    var questions_num = {count($questions)};
  </script>
    
</head>
<body ontouchstart style="background:url(./static/images/gp.png) top center no-repeat; background-size:cover;">
    <div class="weui-toptips weui-toptips_warn js_tooltips">错误提示</div>

    <div class="container" id="container" >
        <div style="text-align: center;margin-top:50%">
        <h2 style="">南京市<br>社区糖尿病管培一体化项目</h2>
    </div>
    </div>


    <script type="text/html" id="tpl_home">
        <div class="page">
          
            <div class="page__bd " style="">
                <h4 class="weui-media-box__title" style="    text-align: center;margin-top:20%;margin-bottom:20px;">
                社区内分泌专业知识评估<br>请输入姓名和单位信息</h4>
                <div class="weui-cells weui-cells_form">
                    <div class="weui-cell">
                        <div class="weui-cell__hd after-split" style="text-align:center;"><label class="weui-label">姓名</label></div>
                        <div class="weui-cell__bd">
                            <input class="weui-input" type="text" name="name" value="" placeholder="请输入姓名"/>
                        </div>
                    </div>

                    <div class="weui-cell weui-cell_select weui-cell_select-before">
                        
                        <div class="weui-cell__hd">
                            <select class="weui-select" name="bumen">
                                <option value="玄武区">玄武区</option>
                                <option value="秦淮区">秦淮区</option>
                                <option value="鼓楼区">鼓楼区</option>
                                <option value="建邺区">建邺区</option>
                                <option value="栖霞区">栖霞区</option>
                                <option value="雨花台区">雨花台区</option>
                                <option value="浦口区">浦口区</option>
                                <option value="江宁区">江宁区</option>
                                <option value="溧水区">溧水区</option>
                                <option value="高淳区">高淳区</option>
                            </select>
                        </div>
                        <div class="weui-cell__bd">
                            <input class="weui-input" type="text" name="danwei" value="" placeholder="社区卫生中心"/>
                            
                        </div>
                    </div>



                    
                    
                </div>

                <div class="button-sp-area page__bd_spacing" style="margin-top:20px;">
                    <a href="javascript:;" data-id="question" class="weui-btn weui-btn_primary" id="login">开始</a>
                </div>
            </div>
            <div class="page__ft">
                <div class="weui-footer">
                    <p class="weui-footer__links">
                        <a href="javascript:void(0);" class="weui-footer__link">南京市社区糖尿病管培一体化项目</a>
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
          
            <div class="page__bd "  style="margin-top:10px;">
                <div class="icon-box" style="text-align:center;">
                    <i class="weui-icon_msg result-icon"></i>
                   
                </div>
                <h4 class="weui-media-box__title" style="    text-align: center;margin-top:20px;margin-bottom:20px;">答对题数：<span class="correct"></span></h4>
                
                
                
            </div>

            <div class="weui-form-preview">
                <div class="weui-cells">
                
                {foreach $questions as $entry}
                    <a class="weui-cell weui-cell_access jump_question" href="javascript:;" data-id="{$entry.no}"> 
                        <div class="weui-cell__bd">
                            <p>第{$entry.no}题</p>
                        </div>
                        <div class="weui-cell__ft"><i class="result-{$entry@index}"></i></div>
                    </a>

                {/foreach}

                </div>


                <div class="weui-form-preview__ft">
                    <a class="weui-form-preview__btn weui-form-preview__btn_default next_question" href="javascript:" data-id="question">再做一次</a>

                    <button type="submit" class="weui-form-preview__btn weui-form-preview__btn_primary close-window" href="javascript:">退出</button>

                </div>
            </div>

            



            
           
        </div>
    </script>


  
   
   {foreach $questions as $entry}
    <script type="text/html" id="tpl_question_{$entry.no}">
        <div class="page panel" id="question-page-{$entry.no}">
            <div class="page__bd page__bd_spacing">
                <h1 class="page__title" style="text-align:center;margin-top:20px;">第{$entry.no}题{if count($entry.daan) gt 1}（多选）{else}（单选）{/if}</h1>
                <p class="title">{$entry.title}</p>
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
                        <a href="javascript:void(0);" class="weui-footer__link">南京市社区糖尿病管培一体化项目</a>
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
    <script src="./static/js/app.js?v=5"></script>
</body>
</html>
