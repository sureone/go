<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Welcome extends CI_Controller {

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
		$this->load->view('welcome_message');
		
		//$rows = $query->result_array();
		//$this->db->query("insert into threads(content) values('hello')");
		//echo json_encode_utf8($this->doLoadThreads(""));
	}
	
	public function api(){
		$idata = file_get_contents('php://input');
		$jobj = json_decode($idata);
		$result = "";
		switch ($jobj->{'ACTION'}){
			case 'postThread':
				$result = $this->doPostThread($jobj);
			break;
			case 'loadThreadsTo':
			case 'loadThreadsFrom':
				$result = $this->doLoadThreads($jobj);
			break;
			default:
			break;
		}
		echo json_encode_utf8($result);
		
	}

	function doPostThread($json){
		$date = new DateTime();
		$data = array(
			'uid'=>$json->{'uid'},
			'content'=>$json->{'content'},
			'rcount'=>0,
			'sn'=>$json->{'sn'},
			'uname'=>$json->{'uname'},
			'cdate'=>$date->format('Y-m-d H:i:s')
		);
		$this->db->insert('threads',$data);
		return 'OK';
		
	}
	
	function doLoadThreads($json){
		$query = $this->db->query("select * from threads");
		$rows = $query->result_array();
		$result = array(
			'ACTION'=>$json->{'ACTION'},
			'threads'=>$rows
		);
		return $result;
	}

}
