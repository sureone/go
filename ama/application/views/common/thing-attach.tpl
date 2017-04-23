<div>
	
	{if $attach.image_width neq '0'}
	
	<a href="./uploads/{$attach.file_name}">
		<img src="./uploads/{$attach.file_name}" style="max-width:700px;max-height:240px;"></a>
	<br/>
	{/if}
	<span class="attachment_order">附件{$attach.file_no}:</span>&nbsp;<a href="./uploads/{$attach.file_name}">{if $attach.file_comment neq ''}{$attach.file_comment}{else}{$attach.file_name}{/if}</a>
</div>