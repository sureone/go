/**
 * Created by jf on 2015/9/11.
 * Modified by bear on 2016/9/7.
 */



$(function () {

    var cur_question_no = 0;

    var cur_page = 0;

    var login_data = null;

    var answers = null;

    var results = [];
    var pageManager = {
        $container: $('#container'),
        _pageStack: [],
        _configs: [],
        _pageAppend: function(){},
        _defaultPage: null,
        _pageIndex: 1,
        setDefault: function (defaultPage) {
            this._defaultPage = this._find('name', defaultPage);
            return this;
        },
        setPageAppend: function (pageAppend) {
            this._pageAppend = pageAppend;
            return this;
        },
        init: function () {
            var self = this;

            $(window).on('hashchange', function () {
                var state = history.state || {};
                var url = location.hash.indexOf('#') === 0 ? location.hash : '#';
                var page = self._find('url', url) || self._defaultPage;
                if (state._pageIndex <= self._pageIndex || self._findInStack(url)) {
                    self._back(page);
                } else {
                    self._go(page);
                }
            });

            if (history.state && history.state._pageIndex) {
                this._pageIndex = history.state._pageIndex;
            }

            this._pageIndex--;

            var url = location.hash.indexOf('#') === 0 ? location.hash : '#';
            var page = self._find('url', url) || self._defaultPage;
            setTimeout(function(){
                self._go(page);
            },1500)
            // this._go(page);
            return this;
        },
        push: function (config) {
            this._configs.push(config);
            return this;
        },
        go: function (to) {
            var config = this._find('name', to);
            if (!config) {
                return;
            }
            location.hash = config.url;




            if(config.url.indexOf("#question_")==0){
                cur_question_no = parseInt(config.url.slice("#question_".length));
            }
        },
        _go: function (config) {
            this._pageIndex ++;

            history.replaceState && history.replaceState({_pageIndex: this._pageIndex}, '', location.href);

            var html = $(config.template).html();
            var $html = $(html).addClass('slideIn').addClass(config.name);
            $html.on('animationend webkitAnimationEnd', function(){
                $html.removeClass('slideIn').addClass('js_show');
            });
            this.$container.append($html);
            this._pageAppend.call(this, $html);
            this._pageStack.push({
                config: config,
                dom: $html
            });

            if (!config.isBind) {
                this._bind(config);
            }


            return this;
        },
        back: function () {
            history.back();
        },
        _back: function (config) {
            


            var url = location.hash.indexOf('#') === 0 ? location.hash : '#';
            console.log("back "+url);

            var stack = null;
            




            while(true){
                this._pageIndex --;

                stack = this._pageStack.pop();

                if(stack == null || stack.config.url==url){
                    break;
                }

                stack.dom.remove();


            }

            if(stack.config.url==url){
                this._pageStack.push(stack);
            }

            if (!stack) {
                return;
            }

            if(url == '#'){
                cur_question_no = 0;
            }
            if(url.indexOf("#question_")==0){
                cur_question_no = parseInt(url.slice("#question_".length));
            }
            var found = this._findInStack(url);
            if (!found) {
                var html = $(config.template).html();
                var $html = $(html).addClass('js_show').addClass(config.name);
                $html.insertBefore(stack.dom);

                if (!config.isBind) {
                    this._bind(config);
                }

                this._pageStack.push({
                    config: config,
                    dom: $html
                });
            }

            // stack.dom.addClass('slideOut').on('animationend webkitAnimationEnd', function () {
            //     stack.dom.remove();
            // });

            return this;
        },
        _findInStack: function (url) {
            var found = null;
            for(var i = 0, len = this._pageStack.length; i < len; i++){
                var stack = this._pageStack[i];
                if (stack.config.url === url) {
                    found = stack;
                    break;
                }
            }
            return found;
        },
        _find: function (key, value) {
            var page = null;
            for (var i = 0, len = this._configs.length; i < len; i++) {
                if (this._configs[i][key] === value) {
                    page = this._configs[i];
                    break;
                }
            }
            return page;
        },
        _bind: function (page) {
            var events = page.events || {};
            for (var t in events) {
                for (var type in events[t]) {
                    this.$container.on(type, t, events[t][type]);
                }
            }
            page.isBind = true;
        }
    };

    function fastClick(){
        var supportTouch = function(){
            try {
                document.createEvent("TouchEvent");
                return true;
            } catch (e) {
                return false;
            }
        }();
        var _old$On = $.fn.on;

        $.fn.on = function(){
            if(/click/.test(arguments[0]) && typeof arguments[1] == 'function' && supportTouch){ // 只扩展支持touch的当前元素的click事件
                var touchStartY, callback = arguments[1];
                _old$On.apply(this, ['touchstart', function(e){
                    touchStartY = e.changedTouches[0].clientY;
                }]);
                _old$On.apply(this, ['touchend', function(e){
                    if (Math.abs(e.changedTouches[0].clientY - touchStartY) > 10) return;

                    e.preventDefault();
                    callback.apply(this, [e]);
                }]);
            }else{
                _old$On.apply(this, arguments);
            }
            return this;
        };
    }
    function preload(){
        $(window).on("load", function(){
            // var imgList = [
            //     "./images/layers/content.png",
            //     "./images/layers/navigation.png",
            //     "./images/layers/popout.png",
            //     "./images/layers/transparent.gif"
            // ];
            // for (var i = 0, len = imgList.length; i < len; ++i) {
            //     new Image().src = imgList[i];
            // }
        });
    }
    function androidInputBugFix(){
        // .container 设置了 overflow 属性, 导致 Android 手机下输入框获取焦点时, 输入法挡住输入框的 bug
        // 相关 issue: https://github.com/weui/weui/issues/15
        // 解决方法:
        // 0. .container 去掉 overflow 属性, 但此 demo 下会引发别的问题
        // 1. 参考 http://stackoverflow.com/questions/23757345/android-does-not-correctly-scroll-on-input-focus-if-not-body-element
        //    Android 手机下, input 或 textarea 元素聚焦时, 主动滚一把
        if (/Android/gi.test(navigator.userAgent)) {
            window.addEventListener('resize', function () {
                if (document.activeElement.tagName == 'INPUT' || document.activeElement.tagName == 'TEXTAREA') {
                    window.setTimeout(function () {
                        document.activeElement.scrollIntoViewIfNeeded();
                    }, 0);
                }
            })
        }
    }
    function setJSAPI(){
       

        
    }
    function setPageManager(){
        var pages = {}, tpls = $('script[type="text/html"]');
        var winH = $(window).height();

        for (var i = 0, len = tpls.length; i < len; ++i) {
            var tpl = tpls[i], name = tpl.id.replace(/tpl_/, '');
            pages[name] = {
                name: name,
                url: '#' + name,
                template: '#' + tpl.id
            };
        }
        pages.home.url = '#';

        for (var page in pages) {
            pageManager.push(pages[page]);
        }
        pageManager
            .setPageAppend(function($html){
                var $foot = $html.find('.page__ft');
                if($foot.length < 1) return;

                if($foot.position().top + $foot.height() < winH){
                    $foot.addClass('j_bottom');
                }else{
                    $foot.removeClass('j_bottom');
                }
            })
            .setDefault('home')
            .init();
    }

    function init(){
        preload();
        fastClick();
        androidInputBugFix();
        setJSAPI();
        setPageManager();

        window.pageManager = pageManager;
        window.home = function(){
            location.hash = '';
        };
    }
    init();


    $(document).on('click','.last_question', function(){
        if((cur_question_no-1)<=0) return;
        saveAnswer(cur_question_no);
        history.back();
    });


    $(document).on('click','#login', function(){

        $.ajax({
          type: 'POST',
          url: './api/exam_login',
          // data to be added to query string:
          data: JSON.stringify({'name':$("input[name=name]").val(),'bumen':'','danwei':$("input[name=danwei]").val()}),
          // type of data we are expecting in return:
          dataType: 'json',
          timeout: 300,
          context: $('body'),
          success: function(data){
            console.log(data.data);
            login_data = data.data;

            answers = JSON.parse(data.data.answers);

            if(login_data.name=='lipingatgl'){
                pageManager.go("admin");
                
            }else{
                enterQuestion(1);
            }

          },
          error: function(xhr, type){
            // alert('Ajax error!')
          }
        })
    });

    function enterQuestion (question) {

        if(question>0 && question<=questions_num){



            window.pageManager.go("question_"+question);
            setTimeout(function(){
                var options = questions[question-1].options;
                for (var i=0;i<options.length;i++){
                    var optid = 'o-'+question+"-"+i;

                    var v = answers[optid] || 0;

                    if(v==1){
                        $("#"+optid).prop("checked",true);
                    }else{
                        $("#"+optid).prop("checked",false);
                        
                    }
                }

                if(question == 1){
                    $("#question-page-1 .last_question").hide();
                }

                if(question == questions_num){
                    $("#question-page-"+question+" .next_question").html("完成");



                }
            },100);
        }


        // body...
    }

    function saveAnswer(question,score=0){
        if(question>0 && question<=questions_num){
            var options = questions[question-1].options;
            for (var i=0;i<options.length;i++){
                var optid = 'o-'+question+"-"+i;
                var v = $("#"+optid).prop('checked')?1:0;
                answers[optid]=v;
            }
            console.log(answers);


            
        }
        $.ajax({
          type: 'POST',
          url: './api/save_answer/'+login_data['userid'],
          // data to be added to query string:
          data: JSON.stringify({'answers':JSON.stringify(answers),'score':score}),
          // type of data we are expecting in return:
          dataType: 'json',
          timeout: 300,
          context: $('body'),
          success: function(data){
            console.log(data.data);

          },
          error: function(xhr, type){
            // alert('Ajax error!')
          }
        })
    }

    function completeExam(){
        
        window.pageManager.go("question_end");
        wrongs = 0;
        corrects=questions_num;
        results = [];
        for(var i=0;i<questions_num;i++){
            var question = questions[i];
            var daan = {};
            for(j=0;j<question.options.length;j++){
                daan['o-'+(i+1)+'-'+(j)]=0;
            }
            for(j=0;j<question.daan.length;j++){
                daan['o-'+(i+1)+'-'+(question.daan[j]-1)]=1;
            }

            results.push(1);

            for(j=0;j<question.options.length;j++){
                id = 'o-'+(i+1)+'-'+(j)
                if(daan[id]!=answers[id]){
                    wrongs ++;
                    corrects --;
                    results[i]=(0);


                    break;
                }
            }
        }

        setTimeout(function(){
            if(questions_num == corrects){
                $("#page-question-end .result-icon").addClass("weui-icon-success");
            }else{

                $("#page-question-end .result-icon").addClass("weui-icon-warn");
            }

            $("#page-question-end .correct").html(corrects+"／"+questions_num);  

            for(var i=0;i<questions_num;i++){
                if(results[i]==0){
                    $("#page-question-end .result-"+i).addClass("weui-icon-warn");
                }else{
                    $("#page-question-end .result-"+i).addClass("weui-icon-success");
                }
            }      
        },100);

        cur_question_no = 0;


        saveAnswer(questions_num,corrects);

    }

    $(document).on('click','.next_question', function(){

        saveAnswer(cur_question_no);
        if(cur_question_no==questions_num){
            //end of exam
            completeExam();
            return;
        }
        if((cur_question_no+1)>questions_num) return;
        enterQuestion(cur_question_no+1);


    });

    $(document).on('click','.jump_question', function(){

        
        var id = $(this).data('id');
        enterQuestion(id);

    });

    $(document).on('click','.close-window', function(){
        WeixinJSBridge.invoke('closeWindow',{},function(res){

        });

    });

    
    $('.js_category').on('click', function(){
        var winH = $(window).height();
        var categorySpace = 10;
        var $this = $(this),
            $inner = $this.next('.js_categoryInner'),
            $page = $this.parents('.page'),
            $parent = $(this).parent('li');
        var innerH = $inner.data('height');
        bear = $page;

        if(!innerH){
            $inner.css('height', 'auto');
            innerH = $inner.height();
            $inner.removeAttr('style');
            $inner.data('height', innerH);
        }

        if($parent.hasClass('js_show')){
            $parent.removeClass('js_show');
        }else{
            $parent.siblings().removeClass('js_show');

            $parent.addClass('js_show');
            if(this.offsetTop + this.offsetHeight + innerH > $page.scrollTop() + winH){
                var scrollTop = this.offsetTop + this.offsetHeight + innerH - winH + categorySpace;

                if(scrollTop > this.offsetTop){
                    scrollTop = this.offsetTop - categorySpace;
                }

                $page.scrollTop(scrollTop);
            }
        }
    });

});
