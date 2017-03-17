<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Api extends CI_Controller {

    /**
     * Index Page for this controller.
     *
     * Maps to the following URL
     *      http://example.com/index.php/welcome
     *  - or -
     *      http://example.com/index.php/welcome/index
     *  - or -
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
        $idata = file_get_contents('php://input');
        $jobj = json_decode($idata);
        $result = "";
        switch ($jobj->{'action'}){
            case 'login':
                $result = $this->doLogin($jobj);
            break;
            case 'reg':
                $result = $this->doReg($jobj);
            break;
            case 'logout':
                $result = $this->doLogout($jobj);
            default:
            break;
        }
        echo json_encode_utf8($result);
    }
    function doLogout($json){
        $this->session->unset_userdata('user.info');
        return array('code'=>200);
    }

    function doLogin($json){

        $id = $json->{'user'};
        $passwd = $json->{'passwd'};

        $query = $this->db->select(['userid','name','email','status'])
                          ->where('userid',$id)
                          ->where('passwd',md5($passwd))
                          ->get('users');
        $rows = $query->result_array();
        if(count($rows)==1){
            $this->session->set_userdata('user.info',$rows[0]);
            $this->session->set_userdata('logged',true);
        }
        return array('code'=>200,'rows'=>$rows);
    }
    //{"action":"reg","user":"fdsa","user_name":"fdsaf","passwd":"fdsafsd","passwd2":"fdsafd","email":"fdsafds","digest_subscribe":"true"}
    function doReg($json){

        $id = $json->{'user'};
        $passwd = $json->{'passwd'};
        $name = $json->{'user_name'};
        $email = $json->{'email'};

        $query = $this->db->select('name')
                          ->where('userid',$id)
                          ->get('users');
        $rows = $query->result_array();

        $code = 400;
        if(count($rows)==0){
            $this->db->insert('users',array('userid'=>$id,'passwd'=>md5($passwd),'name'=>$name,'email'=>$email));
            $code = 200;
            return $this->doLogin($json);
        }

        return array('code'=>$code);
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
