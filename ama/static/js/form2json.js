/**
 * 将一个表单的数据返回成JSON对象去除名称包含'_txt_val'
 * 依赖jQuery类库
 * @return {}
 */
$.fn.serializeForm = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
                // 不包含'_txt_val'
                if(this.name.indexOf('_txt_val') < 0){
                    if (o[this.name]) {
                        if (!o[this.name].push) {
                            o[this.name] = [o[this.name]];
                        }
                        o[this.name].push(this.value || '');
                    } else {
                        o[this.name] = this.value || '';
                    }
                }
            });
    return o;
};

/**
 * 将一个表单的数据返回成JSON对象
 * 依赖jQuery类库
 * @return {}
 */
$.fn.serializeFormStandard = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
                if (o[this.name]) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }
            });
    return o;
};