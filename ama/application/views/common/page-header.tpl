<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
	<meta charset="utf-8">
    <base href="http://127.0.0.1/ama/index.php">
    {if isset($page_title)}
	<title>{$page_title}</title>
    {else}
    <title>问吧 Ask Me Anything </title>
    {/if}
	<style type="text/css">
	</style>
	<link rel="stylesheet" href="./static/css/common.css?v=2" type="text/css" />
    <script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="http://apps.bdimg.com/libs/handlebars.js/1.3.0/handlebars.min.js"></script>
    <script src="./static/js/form2json.js"></script>
    <script src="./static/js/common.js?v=7"></script>
    <script src="./static/js/jquery.timeago.js?v=1"></script>
    <script src="./static/js/markdown.min.js?v=1"></script>
    <script type="text/javascript" src="./static/js/ama.js?v=8"></script>

    <script type="text/javascript">
    	var g_logined = {$logined};
    </script>