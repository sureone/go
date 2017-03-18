$(document).ready(function () {
// <div class="modal-backdrop fade in"></div>

    var g_thread=null;
    for(var i=0;i<threads.length;i++){
        var t = threads[i];

        if(t.thingid==g_thingid) g_thread=t;
    }
    var tpl = Handlebars.compile($("#tpl-comment").html());
    function renderComments(containerid,g_thread){
        var container = $("#"+containerid);
        container.html('');
    
        for(var i=0;i<g_thread.child.length;i++){
            var t = g_thread.child[i];
            var h = tpl(t);
            container.append(h);
            renderComments("siteTable_child_"+t.thingid,t);
        }

    }

    renderComments("siteTable_"+g_thingid,g_thread);

});