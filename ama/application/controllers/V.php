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
		$this->load->helper(array('form', 'url'));
		$this->load->library('wXBizDataCrypt');
		
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
	        $this->ci_smarty->assign("new_number",$this->amaModel->readMessageAccount($user_info['userid']));
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


	public function message($page='inbox',$msgto=null){
		if($this->common()==false){
			return;
		}

		$this->ci_smarty->assign("pagedir","message");

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

		if($page == "compose"){
			if($msgto!=null){
				$userto = $this->amaModel->readUser($msgto);
				$this->ci_smarty->assign("touserid",$msgto);
				$this->ci_smarty->assign("tousername",$userto['name']);
			}
		}

		$this->ci_smarty->assign("things",$things);
		$this->ci_smarty->display("message-{$page}.tpl");

	}


	public function user($userid,$page='home')
	{
		$this->common();

		$things= array();

		$user = $this->amaModel->readUser($userid);
		$this->ci_smarty->assign("pagedir","user");
		
		$this->ci_smarty->assign("page","user-{$page}");
		$this->ci_smarty->assign("userid",$userid);
		$this->ci_smarty->assign("username",$user['name']);
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
		$this->ci_smarty->display("user-{$page}.tpl",$userid);
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
        		$thing['attaches']=$this->amaModel->readAttaches($thing['thingid']);
        		$this->ci_smarty->assign("thing",$thing);	
        	}
        	
        }

		$this->ci_smarty->assign("page","submit");
		$this->ci_smarty->display("submit.tpl",$thingid);
	}
	public function a($thingid){
		$this->common();
		$this->ci_smarty->assign("page","comments");
		$this->ci_smarty->assign("thingid",$thingid);


		$thing = $this->amaModel->readThing($thingid)['0'];

		$comments_result = $this->amaModel->readComments($thingid,0,100);
		$thing['comments']=$comments_result['comments'];
		$thing['comments_count']=$comments_result['comments_count'];
		$thing['attaches']=$this->amaModel->readAttaches($thingid);

		$this->ci_smarty->assign("things",array($thing));
		$this->ci_smarty->assign("page_title",$thing['title']);
		
		$this->ci_smarty->display("comments.tpl",$thingid);
		// $this->load->view('comments',array('user'=>$user_info,'page'=>'comments','thingid'=>$thingid));
	}


	public function uploadform()
    {
            $this->load->view('upload_form', array('error' => ' ' ));
    }


	public function iframe()
    {
            $this->load->view('iframe', array('error' => ' ' ));
    }


    public function do_upload()
    {
            $config['upload_path']          = './uploads/';
            $config['allowed_types']        = 'gif|jpg|png';
            $config['max_size']             = 1024*1024*20;
            $config['max_width']            = 4096;
            $config['max_height']           = 4096;
            $config['file_ext_tolower']     = TRUE;
			$config['file_ext_tolower']     = TRUE;
			$config['encrypt_name']     = FALSE;
			$config['max_filename_increment']     = 100000000;


            $this->load->library('upload', $config);

            if ( ! $this->upload->do_upload('userfile'))
            {
                    $error = array('error' => $this->upload->display_errors());

                    $this->load->view('upload_success', $error);
            }
            else
            {
            		$result = $this->upload->data();
            		$result['file_id']=$this->amaModel->addAttach($result);
                    $data = array('upload_data' => $result);
                    $this->load->view('upload_success', $data);

            }
    }

}
