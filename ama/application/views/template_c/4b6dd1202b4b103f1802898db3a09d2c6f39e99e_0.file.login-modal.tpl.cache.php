<?php
/* Smarty version 3.1.30, created on 2017-04-17 10:31:34
  from "D:\work\go\ama\application\views\common\login-modal.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58f47d6698cbf6_95652166',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '4b6dd1202b4b103f1802898db3a09d2c6f39e99e' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\login-modal.tpl',
      1 => 1492390703,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58f47d6698cbf6_95652166 (Smarty_Internal_Template $_smarty_tpl) {
$_smarty_tpl->compiled->nocache_hash = '2004858f47d66981061_82090050';
?>
<div class="modal  fade  login-modal" tabindex="0" aria-hidden="true">
<div class="modal-dialog modal-dialog-lg"><div class="modal-content"><div class="modal-header"><a href="javascript: void 0;" class="c-close c-hide-text" data-dismiss="modal">close this window</a></div><div class="modal-body"><h3 id="cover-msg" class="modal-title" style="display: none;">您必須登入才能操作。</h3><div id="login"><div class="split-panel"><div class="split-panel-section split-panel-divider"><h4 class="modal-title">建立一个新帐号</h4><form id="register-form" method="post" action="./api" class="form-v2"><input type="hidden" name="action" value="reg">

<div class="c-form-group "><label for="user_reg" class="screenreader-only">使用者账号:</label><input value="" name="user" id="user_reg" class="c-form-control" type="text" maxlength="20" tabindex="2" placeholder="选择使用者账号" data-validate-url="/api/check_username.json" data-validate-min="3" autocomplete="off"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div>

<div class="c-form-group "><label for="user_name" class="screenreader-only">使用者名称:</label><input value="" name="user_name" id="user_name" class="c-form-control" type="text" maxlength="20" tabindex="2" placeholder="选择使用者名称" data-validate-url="/api/check_username.json" data-validate-min="3" autocomplete="off"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div>

<div class="c-form-group "><label for="passwd_reg" class="screenreader-only">密码:</label><input id="passwd_reg" class="c-form-control" name="passwd" type="password" tabindex="2" placeholder="密码" data-validate-url="/api/check_password.json" style="padding-right: 5px;"><div class="strength-meter"><div class="strength-meter-fill"></div></div><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div><div class="c-form-group "><label for="passwd2_reg" class="screenreader-only">确认密码:</label><input name="passwd2" id="passwd2_reg" class="c-form-control" type="password" tabindex="2" placeholder="确认密码"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div><div class="c-form-group "><label for="email_reg" class="screenreader-only">电子邮件: &nbsp;<i>(选填)</i></label><input value="" name="email" id="email_reg" class="c-form-control" type="text" tabindex="2" placeholder="电子邮件" data-validate-url="/api/check_email.json" data-validate-on="change blur"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div>



<div class="c-clearfix c-submit-group"><span class="c-form-throbber"></span><button type="submit" class="c-btn c-btn-primary c-pull-right" tabindex="2">注册</button></div><div><div class="c-alert c-alert-danger"></div><span class="error RATELIMIT field-ratelimit" style="display:none"></span><span class="error RATELIMIT field-vdelay" style="display:none"></span></div></form></div>

<div class="split-panel-section"><h4 class="modal-title">登入</h4><form id="login-form" method="post" action="./api" class="form-v2"><input type="hidden" name="action" value="login"><div class="c-form-group "><label for="user_login" class="screenreader-only">使用者名称:</label><input value="" name="user" id="user_login" class="c-form-control" type="text" maxlength="20" tabindex="3" placeholder="使用者名称"></div><div class="c-form-group "><label for="passwd_login" class="screenreader-only">密码:</label><input id="passwd_login" class="c-form-control" name="passwd" type="password" tabindex="3" placeholder="密码"><div class="c-form-control-feedback-wrapper "><span class="c-form-control-feedback c-form-control-feedback-throbber"></span><span class="c-form-control-feedback c-form-control-feedback-error" title=""></span><span class="c-form-control-feedback c-form-control-feedback-success"></span></div></div><div class="c-checkbox"><input type="checkbox" name="rem" id="rem_login" tabindex="3"><label for="rem_login">记住我</label>

<!-- <a href="/password" class="c-pull-right">重設密碼</a> -->

</div><div class="spacer"><div class="c-form-group g-recaptcha" data-sitekey="6LeTnxkTAAAAAN9QEuDZRpn90WwKk_R1TRW_g-JC"></div><span class="error BAD_CAPTCHA field-captcha" style="display:none"></span></div><div class="c-clearfix c-submit-group"><span class="c-form-throbber"></span><button type="submit" class="c-btn c-btn-primary c-pull-right" tabindex="3">登入</button></div><div><div class="c-alert c-alert-danger"></div></div></form></div></div>

<!-- <p class="login-disclaimer">By signing up, you agree to our <a href="https://www.reddit.com/help/useragreement/">任期</a> and that you have read our <a href="https://www.reddit.com/help/privacypolicy/">隱私權政策</a> and <a href="https://www.reddit.com/help/contentpolicy/">內容政策</a>.</p>
 -->
</div></div></div></div></div>
<?php }
}
