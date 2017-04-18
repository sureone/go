<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
    <meta charset="utf-8">
    <base href="http://www.askmeany.cn/index.php">
    {if isset($page_title)}
	<title>{$page_title}</title>
    {else}
    <title>后园小亭</title>
    {/if}
	<style type="text/css">
	</style>
	<link rel="stylesheet" href="./static/css/common.css?v=2" type="text/css" />
    <script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="http://apps.bdimg.com/libs/handlebars.js/1.3.0/handlebars.min.js"></script>
    <script src="./static/js/form2json.js"></script>
    <script src="./static/js/common.js?v=7"></script>
    <script src="./static/js/jquery.timeago.js?v=1"></script>
    <script src="./static/js/markdown.js?v=3"></script>
    <script type="text/javascript" src="./static/js/ama.js?v=9"></script>

    <script type="text/javascript">
    	var g_logined = {$logined};
    </script>
    {if $page eq "hot" or $page eq "new"}
    <style type="text/css">
        .link .score {
            text-align: center;
            color: #ec4a36;
            background: #dbd0b6;
            border: 1px solid #eddeaa;
            font-size: small;
            padding-left: 2px;
            padding-right: 2px;
            font-weight: normal;
            margin-right: 4px;
        }
        .thing{
            display: inline-block;
            margin: 0 0px 0px 0; 
            padding: 2px;
            min-width:240px;
        }

        .listing-page .linklisting .thing {
            position: relative;
            margin: 0 0px 0px 0;
        }
        .link .title {
          
         
            text-overflow: ellipsis;
            overflow: hidden;
            white-space: nowrap;
            max-width: 700px;
        }
    </style>
    {/if}
