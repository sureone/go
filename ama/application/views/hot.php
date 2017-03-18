<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?><!DOCTYPE html>

<html lang="en">
<head>
<?php include 'common/page-header.php' ?>
</head>
<?php include 'common/thread-tpl.php'?>
<body class="listing-page <?php if(isset($user)){ echo 'loggedin';} ?> hot-page">


<div id="header">
<div id="header-bottom-left">
	<span class="hover pagename"><a href="./">AMA</a></span>
	<ul class="tabmenu "><li class="selected"><a href="./hot" class="choice">熱門</a></li><li><a href="./new" class="choice">最新</a></li><li><a href="./rising" class="choice">好評上升中</a></li><li><a href="./controversial" class="choice">具爭議的</a></li><li><a href="./top" class="choice">頭等</a></li><li><a href="./gilded" class="choice">精選</a></li><li><a href="./wiki" class="choice">wiki</a></li><li><a href="./ads" class="choice">宣傳過的</a></li></ul>
</div>
<?php include 'common/header-bottom-right.php' ?>
</div>
<?php include 'common/side.php' ?>

<div class="content">
	<div class="spacer">
		<div id="siteTable" class="sitetable linklisting">
			
		</div>
	</div>
</div>

<div id="footer"></div>
<?php include 'common/login-modal.php' ?>
</body>


<script type="text/javascript" src="./static/js/hot.js?v=8"></script>
</html>