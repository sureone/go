<?php
/* Smarty version 3.1.30, created on 2017-04-17 04:22:03
  from "D:\work\go\ama\application\views\common\markhelp.tpl" */

/* @var Smarty_Internal_Template $_smarty_tpl */
if ($_smarty_tpl->_decodeProperties($_smarty_tpl, array (
  'version' => '3.1.30',
  'unifunc' => 'content_58f426cb28fbe0_12001847',
  'has_nocache_code' => false,
  'file_dependency' => 
  array (
    '3562a7a76b8774c8a35323504a6ebee9dd364df8' => 
    array (
      0 => 'D:\\work\\go\\ama\\application\\views\\common\\markhelp.tpl',
      1 => 1492390703,
      2 => 'file',
    ),
  ),
  'includes' => 
  array (
  ),
),false)) {
function content_58f426cb28fbe0_12001847 (Smarty_Internal_Template $_smarty_tpl) {
?>

                                <div class="markhelp" style="display:none"><p></p>

                                    <p>使用稍微自訂過的 <a href="http://daringfireball.net/projects/markdown/syntax">Markdown</a>
                                        版本，作為文字格式的設定方式。請參閱下方的部分基本格式。
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
                                       
                                        </tbody>
                                    </table>
                                </div><?php }
}
