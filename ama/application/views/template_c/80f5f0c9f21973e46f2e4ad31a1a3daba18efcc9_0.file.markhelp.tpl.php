<?php
/* Smarty version 3.1.30, created on 2017-03-19 14:10:05
  from "D:\go\ama\application\views\common\markhelp.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58ce832d2b7bf5_91731834',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '80f5f0c9f21973e46f2e4ad31a1a3daba18efcc9' => 
    array (
      0 => 'D:\\go\\ama\\application\\views\\common\\markhelp.tpl',
      1 => 1489884924,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58ce832d2b7bf5_91731834 (Smarty_Internal_Template $_smarty_tpl) {
?>
                                <div class="bottom-area"><span class="help-toggle toggle" style=""><a
                                            class="option active " href="#" tabindex="100"
                                            onclick="return toggle(this, helpon, helpoff)">格式說明</a><a class="option "
                                                                                                      href="#">隱藏說明</a></span><a
                                        href="/help/contentpolicy" class="reddiquette" target="_blank" tabindex="100">內容政策</a><span
                                        class="error TOO_LONG field-text" style="display:none"></span><span
                                        class="error RATELIMIT field-ratelimit" style="display:none"></span><span
                                        class="error NO_TEXT field-text" style="display:none"></span><span
                                        class="error TOO_OLD field-parent" style="display:none"></span><span
                                        class="error THREAD_LOCKED field-parent" style="display:none"></span><span
                                        class="error DELETED_COMMENT field-parent" style="display:none"></span><span
                                        class="error USER_BLOCKED field-parent" style="display:none"></span><span
                                        class="error USER_MUTED field-parent" style="display:none"></span><span
                                        class="error MUTED_FROM_SUBREDDIT field-parent" style="display:none"></span>

                                    <div class="usertext-buttons">
                                        <button type="submit" onclick="" class="save" style="display:none">save</button>
                                        <button type="button" onclick="return cancel_usertext(this);" class="cancel"
                                                style="display:none">cancel
                                        </button>
                                    </div>
                                </div>
                                <div class="markhelp" style="display:none"><p></p>

                                    <p>reddit 使用稍微自訂過的 <a href="http://daringfireball.net/projects/markdown/syntax">Markdown</a>
                                        版本，作為文字格式的設定方式。請參閱下方的部分基本格式，或查看<a href="/wiki/commenting">留言維基頁面</a>中的更詳細說明和常見問題解決方式。
                                    </p>

                                    <p></p>
                                    <table class="md">
                                        <tbody>
                                        <tr style="background-color: #ffff99; text-align: center">
                                            <td><em>輸入的文字：</em></td>
                                            <td><em>顯示的文字：</em></td>
                                        </tr>
                                        <tr>
                                            <td>*斜體*</td>
                                            <td><em>斜體</em></td>
                                        </tr>
                                        <tr>
                                            <td>**粗體**</td>
                                            <td><b>粗體</b></td>
                                        </tr>
                                        <tr>
                                            <td>[reddit!](https://reddit.com)</td>
                                            <td><a href="https://reddit.com">reddit!</a></td>
                                        </tr>
                                        <tr>
                                            <td>* 項目 1<br>* 項目 2<br>* 項目 3</td>
                                            <td>
                                                <ul>
                                                    <li>項目 1</li>
                                                    <li>項目 2</li>
                                                    <li>項目 3</li>
                                                </ul>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>&gt; 引用文字</td>
                                            <td>
                                                <blockquote>引用文字</blockquote>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Lines starting with four spaces<br>are treated like code:<br><br><span
                                                    class="spaces">&nbsp;&nbsp;&nbsp;&nbsp;</span>if 1 * 2 &lt;
                                                3:<br><span class="spaces">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>print
                                                "hello, world!"<br></td>
                                            <td>Lines starting with four spaces<br>are treated like code:<br>
                                                <pre>if 1 * 2 &lt; 3:<br>&nbsp;&nbsp;&nbsp;&nbsp;print "hello, world!"</pre>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>~~strikethrough~~</td>
                                            <td><strike>strikethrough</strike></td>
                                        </tr>
                                        <tr>
                                            <td>super^script</td>
                                            <td>super<sup>script</sup></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div><?php }
}
