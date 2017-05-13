<div>
	{if $attach.file_type eq 'audio/mpeg'}
		<audio controls="true"><source src="./uploads/{$attach.file_name}"></audio>
		<br/>
		<strong>{if $attach.file_comment neq ''}{$attach.file_comment}{else}{$attach.file_name}{/if}</strong>

	{else}
	
	<a href="./uploads/{$attach.file_name}">
		<img src="./uploads/{$attach.file_name}" style="max-width:700px;max-height:240px;"></a>
		<br/>
		<span class="attachment_order">附件{$attach.file_no}:</span>&nbsp;<a href="./uploads/{$attach.file_name}">{if $attach.file_comment neq ''}{$attach.file_comment}{else}{$attach.file_name}{/if}</a>

	{/if}
	
</div>