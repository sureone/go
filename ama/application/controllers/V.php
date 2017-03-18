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
	}
	public function index()
	{
		$user_info = $this->session->userdata('user.info');
		$this->load->view('hot',array('user'=>$user_info));
        #$query = $this->db->query("select * from sgfs");
		#$rows = $query->result_array();
		//$this->db->query("insert into threads(content) values('hello')");
		#echo json_encode_utf8($rows);
	}
	public function logout()
	{
		$this->session->unset_userdata('user.info');
		$this->load->view('hot');
        #$query = $this->db->query("select * from sgfs");
		#$rows = $query->result_array();
		//$this->db->query("insert into threads(content) values('hello')");
		#echo json_encode_utf8($rows);
	}

	public function comments($thingid){
		$user_info = $this->session->userdata('user.info');
		$this->load->view('comments',array('user'=>$user_info));
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
