<div>
<a href="./uploads/{$entry.file_name}">{if $entry.file_comment neq ''}{$entry.file_comment}{else}{$entry.file_name}{/if}</a>
{if $entry.image_width neq '0'}
<img src="./uploads/{$entry.file_name}">
{/if}
</div>