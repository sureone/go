$(document).ready(function () {
// <div class="modal-backdrop fade in"></div>

    

    function renderThreads(){

        var tpl = Handlebars.compile($("#tpl-thread-item").html());
        var container = $("#siteTable");
        container.html('');
        for(var i=0;i<threads.length;i++){
            var t = threads[i];
            t['idx']=i;
            t['no']=i+1;
            var html = tpl(t);
            container.append(html);

        }

    }

    renderThreads();

});