<html>
<head>
<title>Upload Form</title>
</head>
<body>

<script type="text/javascript">
<?php if(isset($error)):?>
console.log("<?=$error?>");
<?php endif; ?>

<?php if(isset($upload_data)):?>
var upload_response={
<?php foreach ($upload_data as $item => $value):?>
	"<?=$item?>":"<?=str_replace('"','\"',$value);?>",
<?php endforeach; ?>
};

window.parent.handleUploadFileDone(upload_response);
<?php endif; ?>
document.write(upload_response.file_name);
</script>

</body>
</html>