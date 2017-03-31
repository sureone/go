
try {

    function toggle(e, t, n) {
        
        var i = $(e).parent().addBack().filter(".option"), s = i.removeClass("active").siblings().addClass("active").get(0);
        return n && !s.onclick && (s.onclick = function () {
            return toggle(s, n, t)
        }), t && t(e), !1
    }

    function helpon(e) {
        $(e).parents(".usertext-edit:first").children(".markhelp:first").show()
    }

    function helpoff(e) {
        $(e).parents(".usertext-edit:first").children(".markhelp:first").hide()
    }

   
    function reply(e){


        var thingid = $(e).attr('data-thingid');
        var mainid =   $(e).attr('data-mainid');       
        if($("#form-comment-"+thingid).length>0) return;
        var tpl = Handlebars.compile($("#tpl-comment-edit").html());
        h = (tpl({thingid:thingid,mainid:mainid}));

        $('#child_'+thingid).prepend(h);

    }

    function cancel_usertext(e){
        $(e).parent().closest("form.usertext").remove();
    }

    function voteit(e,d){

        if(d==1){
            console.log("vote up");
        }else{
            console.log("vote down");
        }

    }
} catch (err) {
    console.log(err.toString())
};
var $$ = (function () {
    Date.prototype.format = function(format) {
        var o = {
            "M+" : this.getMonth() + 1, // month
            "d+" : this.getDate(), // day
            "h+" : this.getHours(), // hour
            "m+" : this.getMinutes(), // minute
            "s+" : this.getSeconds(), // second
            "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
            "S" : this.getMilliseconds()
            // millisecond
        }

        if (/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (this.getFullYear() + "")
                .substr(4 - RegExp.$1.length));
        }

        for ( var k in o) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                    : ("00" + o[k]).substr(("" + o[k]).length));
            }
        }
        return format;
    }
    // 给日期类对象添加日期差方法，返回日期与diff参数日期的时间差，单位为天
    Date.prototype.diff = function(date){
        return (this.getTime() - date.getTime())/(24 * 60 * 60 * 1000);
    };

    return {
        ajaxPost: function (url, param, callback,view) {
            var postData = JSON.stringify(param);
            "undefined" != typeof NProgress &&(NProgress.start());
            $.ajax({
                dataType: 'json',
                type: 'POST',
                url: url,
                data: postData,
                success: function (data) {
                    "undefined" != typeof NProgress &&(NProgress.done());
                    if (callback) {
                        callback(true, data,view);
                    }
                },
                error: function () {
                    if (callback) {
                        callback(false,view);
                    }
                    "undefined" != typeof NProgress &&(NProgress.done());
                }
            });
        },
        ajaxSyncPost: function (url, param, callback,view) {
            var postData = param;

            $.ajax({
                dataType: 'json',
                type: 'POST',
                url: url,
                async:false,
                data: postData,
                success: function (data) {
                    if (callback) {
                        callback(true, data,view);
                    }
                },
                error: function () {
                    if (callback) {
                        callback(false,view);
                    }
                }
            });
        },
        ajaxPostForm: function (url, formSelector, param, callback) {
            var formData = $(formSelector).serializeFormStandard();
            if (param) {
                for (var i in param) {
                    formData[i] = param[i];
                }
            }

            $$.ajaxPost(url, formData, callback);
        },
    }
})();


