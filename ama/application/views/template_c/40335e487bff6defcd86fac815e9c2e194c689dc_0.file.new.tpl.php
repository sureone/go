<?php
/* Smarty version 3.1.30, created on 2017-04-16 03:51:33
  from "D:\go\ama\application\views\new.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58f2ce25f3d457_30889344',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '40335e487bff6defcd86fac815e9c2e194c689dc' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\new.tpl',
      1 => 1492305523,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
    'file:common/page-header.tpl' => 1,
    'file:header-bottom-left.tpl' => 1,
    'file:common/header-bottom-right.tpl' => 1,
    'file:common/side.tpl' => 1,
    'file:common/thread-simple.tpl' => 1,
    'file:common/login-modal.tpl' => 1,
  ),
),false)) {
function content_58f2ce25f3d457_30889344 (Smarty_Internal_Template $_smarty_tpl) {
?>
<html lang="en">
<head>
    <?php $_smarty_tpl->_subTemplateRender("file:common/page-header.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</head>

<body class="listing-page <?php if ($_smarty_tpl->tpl_vars['logined']->value == "true") {?>loggedin<?php }?> <?php echo $_smarty_tpl->tpl_vars['page']->value;?>
-page">


<div id="header">
<?php $_smarty_tpl->_subTemplateRender("file:header-bottom-left.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>


<?php $_smarty_tpl->_subTemplateRender("file:common/header-bottom-right.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</div>
<?php $_smarty_tpl->_subTemplateRender("file:common/side.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>


<div class="content">
    <div class="spacer">
        <div id="siteTable" class="sitetable linklisting">
            <?php
$_from = $_smarty_tpl->smarty->ext->_foreach->init($_smarty_tpl, $_smarty_tpl->tpl_vars['things']->value, 'entry');
if ($_from !== null) {
foreach ($_from as $_smarty_tpl->tpl_vars['entry']->value) {
?>
                <?php $_smarty_tpl->_subTemplateRender("file:common/thread-simple.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, true);
?>

            <?php
}
}
$_smarty_tpl->smarty->ext->_foreach->restore($_smarty_tpl);
?>


        </div>
    </div>
</div>

<div id="footer"></div>
<?php $_smarty_tpl->_subTemplateRender("file:common/login-modal.tpl", $_smarty_tpl->cache_id, $_smarty_tpl->compile_id, 0, $_smarty_tpl->cache_lifetime, array(), 0, false);
?>

</body>


<?php echo '<script'; ?>
 type="text/javascript" src="./static/js/hot.js?v=8"><?php echo '</script'; ?>
>
</html><?php }
}
