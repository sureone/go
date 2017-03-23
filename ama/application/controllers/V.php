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
	const lv2=array(array(
	        'thingid'=>1,
	        'title'=>'18 year old female, fed via NG tube AMA',
	        'text'=>'i am an 18 year old girl, fed via an NG tube (and have been for over 2 years) due to illness. normally people have a lot of questions about my tube, and currently i am in hospital with no entertainment so i thought i would answer any questions people have!',
	        'author'=>'chloegbih',
	        'userid'=>'chloegbih',
	        'likes'=>200,
	        'timeago'=>'',
	        'no'=>1,
	        'dislikes'=>160,
	        'score'=>78,
	        'comments'=>array()),
	        array(
	        'thingid'=>2,
	        'title'=>'18 year old female, fed via NG tube AMA',
	        'text'=>'i am an 18 year old girl, fed via an NG tube (and have been for over 2 years) due to illness. normally people have a lot of questions about my tube, and currently i am in hospital with no entertainment so i thought i would answer any questions people have!',
	        'author'=>'chloegbih',
	        'userid'=>'chloegbih',
	        'likes'=>200,
	        'timeago'=>'',
	        'no'=>2,
	        'dislikes'=>160,
	        'score'=>78,
	        'comments'=>array()),
	        array(
	        'thingid'=>3,
	        'title'=>'18 year old female, fed via NG tube AMA',
	        'text'=>'i am an 18 year old girl, fed via an NG tube (and have been for over 2 years) due to illness. normally people have a lot of questions about my tube, and currently i am in hospital with no entertainment so i thought i would answer any questions people have!',
	        'author'=>'chloegbih',
	        'userid'=>'chloegbih',
	        'likes'=>200,
	        'timeago'=>'',
	        'no'=>3,
	        'dislikes'=>160,
	        'score'=>78,
	        'comments'=>array())
	        );

	const lv1=array(array(
	        'thingid'=>1,
	        'title'=>'18 year old female, fed via NG tube AMA',
	        'text'=>'i am an 18 year old girl, fed via an NG tube (and have been for over 2 years) due to illness. normally people have a lot of questions about my tube, and currently i am in hospital with no entertainment so i thought i would answer any questions people have!',
	        'author'=>'chloegbih',
	        'userid'=>'chloegbih',
	        'likes'=>200,
	        'timeago'=>'',
	        'no'=>1,
	        'dislikes'=>160,
	        'score'=>78,
	        'comments'=>self::lv2),
	        array(
	        'thingid'=>2,
	        'title'=>'18 year old female, fed via NG tube AMA',
	        'text'=>'i am an 18 year old girl, fed via an NG tube (and have been for over 2 years) due to illness. normally people have a lot of questions about my tube, and currently i am in hospital with no entertainment so i thought i would answer any questions people have!',
	        'author'=>'chloegbih',
	        'userid'=>'chloegbih',
	        'likes'=>200,
	        'timeago'=>'',
	        'no'=>2,
	        'dislikes'=>160,
	        'score'=>78,
	        'comments'=>array()),
	        array(
	        'thingid'=>3,
	        'title'=>'18 year old female, fed via NG tube AMA',
	        'text'=>'i am an 18 year old girl, fed via an NG tube (and have been for over 2 years) due to illness. normally people have a lot of questions about my tube, and currently i am in hospital with no entertainment so i thought i would answer any questions people have!',
	        'author'=>'chloegbih',
	        'userid'=>'chloegbih',
	        'likes'=>200,
	        'timeago'=>'',
	        'no'=>3,
	        'dislikes'=>160,
	        'score'=>78,
	        'comments'=>array())
	        );

	protected $things=array(
			array(
	        'thingid'=>1,
	        'title'=>'18 year old female, fed via NG tube AMA',
	        'text'=>'i am an 18 year old girl, fed via an NG tube (and have been for over 2 years) due to illness. normally people have a lot of questions about my tube, and currently i am in hospital with no entertainment so i thought i would answer any questions people have!',
	        'author'=>'chloegbih',
	        'userid'=>'chloegbih',
	        'likes'=>200,
	        'timeago'=>'',
	        'no'=>1,
	        'dislikes'=>160,
	        'score'=>78,
	        'comments'=>self::lv1),
	        array(
	        'thingid'=>2,
	        'title'=>'18 year old female, fed via NG tube AMA',
	        'text'=>'i am an 18 year old girl, fed via an NG tube (and have been for over 2 years) due to illness. normally people have a lot of questions about my tube, and currently i am in hospital with no entertainment so i thought i would answer any questions people have!',
	        'author'=>'chloegbih',
	        'userid'=>'chloegbih',
	        'likes'=>200,
	        'timeago'=>'',
	        'no'=>2,
	        'dislikes'=>160,
	        'score'=>78,
	        'comments'=>array()),
	        array(
	        'thingid'=>3,
	        'title'=>'18 year old female, fed via NG tube AMA',
	        'text'=>'i am an 18 year old girl, fed via an NG tube (and have been for over 2 years) due to illness. normally people have a lot of questions about my tube, and currently i am in hospital with no entertainment so i thought i would answer any questions people have!',
	        'author'=>'chloegbih',
	        'userid'=>'chloegbih',
	        'likes'=>200,
	        'timeago'=>'',
	        'no'=>3,
	        'dislikes'=>160,
	        'score'=>78,
	        'comments'=>array())
	        );


	function common(){
		$user_info = $this->session->userdata('user.info');
		if($user_info!=null){
			$this->ci_smarty->assign("user",$user_info);
	        $this->ci_smarty->assign("logined","true");
    	}else{
    		$this->ci_smarty->assign("logined","false");
    	}
	}
	function showHotView(){
		$this->common();

		$this->ci_smarty->assign("page","hot");
		$this->ci_smarty->assign("things",$this->things);
		$this->ci_smarty->display("hot.tpl");


	}

	public function test(){
		$this->ci_smarty->assign("testary", array(array('name'=>'jerry',"old"=>2),array('name'=>'jack','old'=>3)));
		$this->ci_smarty->display('test.tpl');
	}


	public function user($userid,$page='home')
	{
		$this->common();
		
		$this->ci_smarty->assign("page","user-{$page}");
		$this->ci_smarty->assign("things",array());
		if($page == "home"){

		}
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
		$this->ci_smarty->assign("things",array($this->things[0]));
		
		$this->ci_smarty->display("comments.tpl");
		// $this->load->view('comments',array('user'=>$user_info,'page'=>'comments','thingid'=>$thingid));
	}

	
	function doPostThread($json){
		$date = new DateTime();
		$data = array(
			'uid'=>$json->{'uid'},
			'content'=>$json->{'content'},
			'sn'=>$json->{'sn'},
			'rid'=>0,
			'rcount'=>0,
			'uname'=>$json->{'uname'},
			'cdate'=>$date->format('Y-m-d H:i:s')
		);
		if(array_key_exists('rid', $json)){
			$data['rid']= $json->{'rid'};
		}
		$this->db->insert('threads',$data);
		if(array_key_exists('rid', $json) && $json->{'rid'}>0){
			$this->db->set('rcount','rcount+1',FALSE);
			$this->db->where('id',$json->{'rid'});
			$this->db->update('threads');
		}
		return 'OK';
		
	}
	
	function doLoadThreads($json){
		$action = $json->{'ACTION'};
		$id = $json->{'id'};
		$sql = "select * from threads";
		$sql = $sql . " where rid='" . $json->{'rid'} . "'";
		if($id!='now'){
			if($action=='loadThreadsTo'){
				$sql = $sql . " and id < '{$id}'";
			}else{
				$sql = $sql . " and id > '{$id}'";
			}
		}
		$sql = $sql . " order by id desc limit " . $json->{'max'};
		
		$query = $this->db->query($sql);
		$rows = $query->result_array();
		$result = array(
			'ACTION'=>$json->{'ACTION'},
			'threads'=>$rows
		);
		return $result;
	}
	
	function doLoadSgfs($json){
		$action = $json->{'ACTION'};
		$id = $json->{'id'};
		$sql = "select * from sgfs where 1=1";
         
		if($id!='now'){
			if($action=='loadSgfsTo'){
				$sql = $sql . " and id < '{$id}'";
			}else{
				$sql = $sql . " and id > '{$id}'";
			}
		}

		if(array_key_exists('skey', $json)){
            $skey = $json->{'skey'};
            $sql = $sql . " and (black like '%{$skey}' or white like '%{$skey}' or name like '%{$skey}')";
        }
        
		$sql = $sql . " order by id desc limit " . $json->{'max'};
		
		$query = $this->db->query($sql);
		$rows = $query->result_array();
		$result = array(
			'ACTION'=>$json->{'ACTION'},
			'rows'=>$rows
		);
		return $result;
	}

}
