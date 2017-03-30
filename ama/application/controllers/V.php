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
		$this->load->model('amamodel');
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

		$rows = $this->amamodel->readHotThings(0,10);
		$this->ci_smarty->assign("page","hot");
		$this->ci_smarty->assign("things",$rows);
		$this->ci_smarty->display("hot.tpl");


	}

	public function test(){
		$user_info = $this->session->userdata('user.info');

		// $thing = $this->amamodel->readThing($param)['0'];

		// $thing['comments'] = $this->amamodel->readComments($param,0,100);

		$things = $this->amamodel->readMessagesAndRepliesByUser(0,20,$user_info['userid']);

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
			$things = $this->amamodel->readMessagesAndRepliesByUser(0,20,$user_info['userid']);	
		}

		$this->ci_smarty->assign("things",$things);
		$this->ci_smarty->display("message-{$page}.tpl");

	}


	public function user($userid,$page='home')
	{
		$this->common();

		$things= array();
		
		$this->ci_smarty->assign("page","user-{$page}");
		$this->ci_smarty->assign("userid",$userid);
		$this->ci_smarty->assign("pagetype","archive");
		
		if($page == "home"){
			$things = $this->amamodel->readThingsByUser(0,100,$userid);
		}

		if($page == "replies"){
			$things = $this->amamodel->readThingsByUser(0,100,$userid,"reply");
		}

		if($page == "submitted"){
			$things = $this->amamodel->readThingsByUser(0,100,$userid,"main");
		}

		if($page == "saved"){
			$things = $this->amamodel->readThingsByUser(0,100,$userid,"saved");
		}

		if($page == "upvoted"){
			$things = $this->amamodel->readThingsByUser(0,100,$userid,"ups");
		}

		if($page == "downvoted"){
			$things = $this->amamodel->readThingsByUser(0,100,$userid,"downs");
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
	public function submit(){

        $this->common();
		$this->ci_smarty->assign("page","comments");
		$this->ci_smarty->display("submit.tpl");
	}
	public function comments($thingid){
		$this->common();
		$this->ci_smarty->assign("page","comments");
		$this->ci_smarty->assign("thingid",$thingid);


		$thing = $this->amamodel->readThing($thingid)['0'];

		$comments_result = $this->amamodel->readComments($thingid,0,100);
		$thing['comments']=$comments_result['comments'];
		$thing['comments_count']=$comments_result['comments_count'];

		$this->ci_smarty->assign("things",array($thing));
		
		$this->ci_smarty->display("comments.tpl");
		// $this->load->view('comments',array('user'=>$user_info,'page'=>'comments','thingid'=>$thingid));
	}


}