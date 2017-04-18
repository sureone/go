<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class V extends CI_Controller {

	/**
	 * Index Page for this controller.
	 *
	 * Maps to the following URL
	 * 		http://example.com/index.php/welcome
	 *	- or -
	 * 		http://example.com/index.php/welcome/index
	 *	- or -
	 * Since this controller is set as the default controller in
	 * config/routes.php, it's displayed at http://example.com/
	 *
	 * So any other public methods not prefixed with an underscore will
	 * map to /index.php/welcome/<method_name>
	 * @see https://codeigniter.com/user_guide/general/urls.html
	 */
	function __construct()
	{		
		// Call the Model constructor		
		parent::__construct();
		$this->load->helper('json');
		$this->load->library('session');
		$this->load->database();
		$this->load->helper('file');
		$this->load->library('ci_smarty');
		$this->load->model('amaModel');
	}
	public function index()
	{
		$user_info = $this->session->userdata('user.info');
		$this->showHotView();
		// $this->load->view('hot',array('user'=>$user_info,'page'=>'hot',));
        #$query = $this->db->query("select * from sgfs");
		#$rows = $query->result_array();
		//$this->db->query("insert into threads(content) values('hello')");
		#echo json_encode_utf8($rows);
	}
	public function hot()
	{
		$this->showHotView();
		
	}
	public function news()
	{
		$this->showNewView();
		
	}

	function common(){
		$user_info = $this->session->userdata('user.info');
		$this->ci_smarty->assign("pagetype","list");
		if($user_info!=null){
			$this->ci_smarty->assign("user",$user_info);
	        $this->ci_smarty->assign("logined","true");
	        return true;
    	}else{
    		$this->ci_smarty->assign("logined","false");
    		return false;
    	}
	}

	function showHotView(){
		$this->common();

		if(!$this->ci_smarty->isCached('hot.tpl')){ 
			$rows = $this->amaModel->readHotThings(0,10);
			$this->ci_smarty->assign("page","hot");
			$this->ci_smarty->assign("things",$rows);
			$this->ci_smarty->display("hot.tpl");
		}else{
			$this->ci_smarty->display("hot.tpl");
		}



	}


	function showNewView(){
		$this->common();
		if(!$this->ci_smarty->isCached('new.tpl')){ 
			$rows = $this->amaModel->readNewThings(0,10);
			$this->ci_smarty->assign("page","new");
			$this->ci_smarty->assign("things",$rows);
			$this->ci_smarty->display("new.tpl");
		}else{
			$this->ci_smarty->display("new.tpl");
		}


	}

	public function test(){
		$user_info = $this->session->userdata('user.info');

		// $thing = $this->amaModel->readThing($param)['0'];

		// $thing['comments'] = $this->amaModel->readComments($param,0,100);

		$things = $this->amaModel->readMessagesByUser(0,20,$user_info['userid'],'all');

		echo json_encode_utf8($things);
		
	}


	public function message($page='inbox'){
		if($this->common()==false){
			return;
		}

		$things= array();
		
		$user_info = $this->session->userdata('user.info');
		$this->ci_smarty->assign("page","message-{$page}");
		$this->ci_smarty->assign("pagetype","archive");
		if($page == "inbox"){
			$things = $this->amaModel->readMessagesByUser(0,20,$user_info['userid'],'all');	
		}

		if($page == "messages"){
			$things = $this->amaModel->readMessagesByUser(0,20,$user_info['userid'],'message');	
		}


		if($page == "comments"){
			$things = $this->amaModel->readMessagesByUser(0,20,$user_info['userid'],'comments');	
		}


		if($page == "selfreply"){
			$things = $this->amaModel->readMessagesByUser(0,20,$user_info['userid'],'selfreply');	
		}

		$this->ci_smarty->assign("things",$things);
		$this->ci_smarty->display("message-{$page}.tpl");

	}


	public function user($userid,$page='home')
	{
		$this->common();

		$user = $this->amaModel->readUser($userid);
		$things= array();
		
		$this->ci_smarty->assign("page","user-{$page}");
		$this->ci_smarty->assign("userid",$userid);
		$this->ci_smarty->assign("user_name",$user['name']);
		$this->ci_smarty->assign("pagetype","archive");
		
		if($page == "home"){
			$things = $this->amaModel->readThingsByUser(0,100,$userid,'home');
		}

		if($page == "replies"){
			$things = $this->amaModel->readThingsByUser(0,100,$userid,"reply");
		}

		if($page == "submitted"){
			$things = $this->amaModel->readThingsByUser(0,100,$userid,"main");
		}

		if($page == "saved"){
			$things = $this->amaModel->readThingsByUser(0,100,$userid,"saved");
		}

		if($page == "upvoted"){
			$things = $this->amaModel->readThingsByUser(0,100,$userid,"ups");
		}

		if($page == "downvoted"){
			$things = $this->amaModel->readThingsByUser(0,100,$userid,"downs");
		}

		$this->ci_smarty->assign("things",$things);
		$this->ci_smarty->display("user-{$page}.tpl");
	}

	public function logout()
	{
		$this->common();
		$this->load->view('hot');
        #$query = $this->db->query("select * from sgfs");
		#$rows = $query->result_array();
		//$this->db->query("insert into threads(content) values('hello')");
		#echo json_encode_utf8($rows);
	}
	public function submit($thingid=0){

        $this->common();
        $user_info = $this->session->userdata('user.info');
        if($thingid!=0 && $user_info!=null){

        	$thing = $this->amaModel->readThing($thingid)['0'];
        	if($thing['author']==$user_info['userid']){
        		$this->ci_smarty->assign("thing",$thing);	
        	}
        	
        }

		$this->ci_smarty->assign("page","submit");
		$this->ci_smarty->display("submit.tpl");
	}
	public function comments($thingid){
		$this->common();
		$this->ci_smarty->assign("page","comments");
		$this->ci_smarty->assign("thingid",$thingid);


		$thing = $this->amaModel->readThing($thingid)['0'];

		$comments_result = $this->amaModel->readComments($thingid,0,100);
		$thing['comments']=$comments_result['comments'];
		$thing['comments_count']=$comments_result['comments_count'];

		$this->ci_smarty->assign("things",array($thing));
		$this->ci_smarty->assign("page_title",$thing['title']);
		
		$this->ci_smarty->display("comments.tpl");
		// $this->load->view('comments',array('user'=>$user_info,'page'=>'comments','thingid'=>$thingid));
	}


}
