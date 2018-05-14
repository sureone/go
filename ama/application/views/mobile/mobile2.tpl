<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
    <meta charset="utf-8">
    <base href="/ama/index.php">
  <link rel="apple-touch-icon-precomposed" sizes="57x57" href="./static/images/newicon57.png">  
  <link rel="apple-touch-icon-precomposed" sizes="72x72" href="./static/images/newicon72.png">  
  <link rel="apple-touch-icon-precomposed" sizes="114x114" href="./static/images/newicon114.png">  
  <link rel="apple-touch-icon-precomposed" sizes="144x144" href="./static/images/newicon144.png">  
  
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no, minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <title>boopo</title>
    <link href="./static/f7/css/framework7.ios.min.css" rel="stylesheet">
    <link href="./static/f7/css/framework7.ios.colors.min.css" rel="stylesheet">
	<!-- <script src="./static/f7/js/framework7.min.js"></script> -->
	
	<script type="text/javascript" src="https://apps.bdimg.com/libs/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://apps.bdimg.com/libs/handlebars.js/1.3.0/handlebars.min.js"></script>
    <script src="./static/js/form2json.js"></script>
    <script src="./static/js/common.js?v=7"></script>
    <script src="./static/js/jquery.timeago.js?v=1"></script>
    <script src="./static/js/markdown.js?v=3"></script>
	<script type="text/javascript" src="./static/js/ama.js?v=9"></script>


</head>
  <body>
    <!-- Status bar overlay for fullscreen mode-->
    <div class="statusbar-overlay"></div>
    <!-- Panels overlay-->
    <div class="panel-overlay"></div>
    <!-- Left panel with reveal effect-->
    <div class="panel panel-left panel-reveal">
      <div class="content-block">
        <p>Left panel content goes here</p>
      </div>
    </div>
    <!-- Right panel with cover effect-->
    <div class="panel panel-right panel-cover">
      <div class="content-block">
        <p>Right panel content goes here</p>
      </div>
    </div>
    <!-- Views-->
    <div class="views">
      <!-- Your main view, should have "view-main" class-->
      <div class="view view-main">
        <!-- Top Navbar-->
        <div class="navbar">
          <!-- Navbar inner for Index page-->
          <div data-page="index" class="navbar-inner">
            <!-- We have home navbar without left link-->
            <div class="center sliding"><img src="./static/images/boopo.png"  height="20"></div>
            <div class="right">
              <!-- Right link contains only icon - additional "icon-only" class-->
			  <!-- <a href="#" class="link icon-only open-panel"> <i class="icon icon-bars"></i></a> -->
            </div>
          </div>
          <!-- Navbar inner for About page-->
          <div data-page="about" class="navbar-inner cached">
            <div class="left sliding"><a href="#" class="back link"> <i class="icon icon-back"></i><span>Back</span></a></div>
            <div class="center sliding">About Us</div>
          </div>
          <!-- Navbar inner for Services page-->
          <div data-page="services" class="navbar-inner cached">
            <div class="left sliding"><a href="#" class="back link"> <i class="icon icon-back"></i><span>Back</span></a></div>
            <div class="center sliding">Services</div>
          </div>
          <!-- Navbar inner for Form page-->
          <div data-page="form" class="navbar-inner cached">
            <div class="left sliding"><a href="#" class="back link"> <i class="icon icon-back"></i><span>Back</span></a></div>
            <div class="center sliding">Form</div>
          </div>
        </div>
        <!-- Pages, because we need fixed-through navbar and toolbar, it has additional appropriate classes-->
        <div class="pages navbar-through toolbar-through">
          <!-- Index Page-->
          <div data-page="index" class="page">
            <!-- Scrollable page content-->
            <div class="page-content">
			
				{foreach $things as $entry}
					<div class="card facebook-card">
					  <div class="card-header">
						<div class="facebook-avatar"><img src="./static/images/newicon57.png" width="34" height="34"></div>
						<div class="facebook-name">{$entry.author_name}</div>
						<div class="facebook-date"><time class="live-timestamp timeago" datetime="{$entry.timeago}"></time></div>
					  </div>
					  <div class="card-content">
						<div class="card-content-inner">
						  <p>{$entry.title}</p>
						  
						    {foreach $entry.attaches as $attach}
							
							{if $attach.file_type eq 'audio/mpeg'}
								<a href="./uploads/{$attach.file_name}">{if $attach.file_comment neq ''}{$attach.file_comment}{else}{$attach.file_name}{/if}</a>
							{else}
                                <img src="./uploads/{$attach.file_name}" width="100%">
							{/if}
							
                            {/foreach} 
							
						  <p><script>document.write(markdown.toHTML("{$entry.text|regex_replace:'/[\r\t\n]/':'\\n'|regex_replace:'/[\"]/':'\\\"'|regex_replace:'/[\']/':'\\\''}"));</script></p>
                          
						 
						</div>
					  </div>
					  <div class="card-footer">
						<a href="#" class="link">喜欢({$entry.likes-$entry.dislikes})</a>
						<a href="#" class="link">评论({$entry.replies})</a>
						<a href="#" class="link">分享</a>
					  </div>
					</div>   
				{/foreach}
				
				
            </div>
          </div>
          <!-- About Page-->
          <div data-page="about" class="page cached">
            <div class="page-content">
              <div class="content-block">
               
              </div>
            </div>
          </div>
          <!-- Services Page-->
          <div data-page="services" class="page cached">
            <div class="page-content">
              <div class="content-block">
                
              </div>
            </div>
          </div>
          <!-- Form Page-->
          <div data-page="form" class="page cached">
            <div class="page-content">
              <div class="content-block-title">Form Example</div>
              <div class="list-block">
                <ul>
                  <li>
                    <div class="item-content">
                      <div class="item-media"><i class="icon icon-form-name"></i></div>
                      <div class="item-inner"> 
                        <div class="item-title label">Name</div>
                        <div class="item-input">
                          <input type="text" placeholder="Your name">
                        </div>
                      </div>
                    </div>
                  </li>
                  <li>
                    <div class="item-content">
                      <div class="item-media"><i class="icon icon-form-email"></i></div>
                      <div class="item-inner"> 
                        <div class="item-title label">E-mail</div>
                        <div class="item-input">
                          <input type="email" placeholder="E-mail">
                        </div>
                      </div>
                    </div>
                  </li>
                  <li>
                    <div class="item-content">
                      <div class="item-media"><i class="icon icon-form-url"></i></div>
                      <div class="item-inner"> 
                        <div class="item-title label">URL</div>
                        <div class="item-input">
                          <input type="url" placeholder="URL">
                        </div>
                      </div>
                    </div>
                  </li>
                  <li>
                    <div class="item-content">
                      <div class="item-media"><i class="icon icon-form-password"></i></div>
                      <div class="item-inner"> 
                        <div class="item-title label">Password</div>
                        <div class="item-input">
                          <input type="password" placeholder="Password">
                        </div>
                      </div>
                    </div>
                  </li>
                  <li>
                    <div class="item-content">
                      <div class="item-media"><i class="icon icon-form-tel"></i></div>
                      <div class="item-inner"> 
                        <div class="item-title label">Phone</div>
                        <div class="item-input">
                          <input type="tel" placeholder="Phone">
                        </div>
                      </div>
                    </div>
                  </li>
                  <li>
                    <div class="item-content">
                      <div class="item-media"><i class="icon icon-form-gender"></i></div>
                      <div class="item-inner"> 
                        <div class="item-title label">Gender</div>
                        <div class="item-input">
                          <select>
                            <option>Male</option>
                            <option>Female</option>
                          </select>
                        </div>
                      </div>
                    </div>
                  </li>
                  <li>
                    <div class="item-content">
                      <div class="item-media"><i class="icon icon-form-calendar"></i></div>
                      <div class="item-inner"> 
                        <div class="item-title label">Birth date</div>
                        <div class="item-input">
                          <input type="date" placeholder="Birth day" value="2014-04-30">
                        </div>
                      </div>
                    </div>
                  </li>
                  <li>
                    <div class="item-content">
                      <div class="item-media"><i class="icon icon-form-toggle"></i></div>
                      <div class="item-inner"> 
                        <div class="item-title label">Switch</div>
                        <div class="item-input">
                          <label class="label-switch">
                            <input type="checkbox">
                            <div class="checkbox"></div>
                          </label>
                        </div>
                      </div>
                    </div>
                  </li>
                  <li>
                    <div class="item-content">
                      <div class="item-media"><i class="icon icon-form-settings"></i></div>
                      <div class="item-inner">
                        <div class="item-title label">Slider</div>
                        <div class="item-input">
                          <div class="range-slider">
                            <input type="range" min="0" max="100" value="50" step="0.1">
                          </div>
                        </div>
                      </div>
                    </div>
                  </li>
                  <li class="align-top">
                    <div class="item-content">
                      <div class="item-media"><i class="icon icon-form-comment"></i></div>
                      <div class="item-inner"> 
                        <div class="item-title label">Textarea</div>
                        <div class="item-input">
                          <textarea></textarea>
                        </div>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
              <div class="content-block">
                <div class="row">
                  <div class="col-50"><a href="#" class="button button-big button-fill color-red">Cancel</a></div>
                  <div class="col-50">
                    <input type="submit" value="Submit" class="button button-big button-fill color-green">
                  </div>
                </div>
              </div>
              <div class="content-block-title">Checkbox group</div>
              <div class="list-block">
                <ul>
                  <li>
                    <label class="label-checkbox item-content">
                      <input type="checkbox" name="ks-checkbox" value="Books" checked>
                      <div class="item-media"><i class="icon icon-form-checkbox"></i></div>
                      <div class="item-inner">
                        <div class="item-title">Books</div>
                      </div>
                    </label>
                  </li>
                  <li>
                    <label class="label-checkbox item-content">
                      <input type="checkbox" name="ks-checkbox" value="Movies">
                      <div class="item-media"><i class="icon icon-form-checkbox"></i></div>
                      <div class="item-inner">
                        <div class="item-title">Movies</div>
                      </div>
                    </label>
                  </li>
                  <li>
                    <label class="label-checkbox item-content">
                      <input type="checkbox" name="ks-checkbox" value="Food">
                      <div class="item-media"><i class="icon icon-form-checkbox"></i></div>
                      <div class="item-inner">
                        <div class="item-title">Food</div>
                      </div>
                    </label>
                  </li>
                  <li>
                    <label class="label-checkbox item-content">
                      <input type="checkbox" name="ks-checkbox" value="Drinks">
                      <div class="item-media"><i class="icon icon-form-checkbox"></i></div>
                      <div class="item-inner">
                        <div class="item-title">Drinks</div>
                      </div>
                    </label>
                  </li>
                </ul>
              </div>
              <div class="content-block-title">Radio buttons group</div>
              <div class="list-block">
                <ul>
                  <li>
                    <label class="label-radio item-content">
                      <input type="radio" name="ks-radio" value="Books" checked>
                      <div class="item-inner">
                        <div class="item-title">Books</div>
                      </div>
                    </label>
                  </li>
                  <li>
                    <label class="label-radio item-content">
                      <input type="radio" name="ks-radio" value="Movies">
                      <div class="item-inner">
                        <div class="item-title">Movies</div>
                      </div>
                    </label>
                  </li>
                  <li>
                    <label class="label-radio item-content">
                      <input type="radio" name="ks-radio" value="Food">
                      <div class="item-inner">
                        <div class="item-title">Food</div>
                      </div>
                    </label>
                  </li>
                  <li>
                    <label class="label-radio item-content">
                      <input type="radio" name="ks-radio" value="Drinks">
                      <div class="item-inner">
                        <div class="item-title">Drinks</div>
                      </div>
                    </label>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
        <!-- Bottom Toolbar-->
<!--         <div class="toolbar">
          <div class="toolbar-inner"><a href="#" class="link">Link 1</a><a href="#" class="link">Link 2</a></div>
        </div> -->
      </div>
    </div>
    <!-- Path to your app js-->
    <script type="text/javascript" src="js/my-app.js"></script>
  </body>
</html>