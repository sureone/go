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
            case 'submit-new-link':
                $result = $this->doSubmitNewLink($jobj);
            break;
            case 'submit-new-message':
                $result = $this->doSubmitNewMessage($jobj);
            break;
            case 'vote-link':
                $result = $this->doVoteLink($jobj);
            break;
            case 'save-link':
                $result = $this->doSaveLink($jobj);
            break;
            case 'submit-new-comment':
                $result = $this->doSubmitNewComment($jobj);
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

    function doSaveLink($json){
        $user = $this->session->userdata('user.info');
        $result = array('code'=>404);
        if(isset($_SESSION['user.info'])){
            $id = $user['userid'] . '-' . $json->{'thingid'} . '-save';
            $query = $this->db->select(['idata'])
                              ->where('id',$id)
                              ->get('user_thing_map');
            $rows = $query->result_array();
            if(count($rows)==0){
                 $this->db->insert('user_thing_map',
                    array('id'=>$id,
                        'userid'=>$user['userid'],
                        'thingid'=>$json->{'thingid'},
                        'maptype'=>'save',
                        'cdate'=>$this->curTime(),
                        'udate'=>$this->curTime()));

            }
            $result = array('code'=>200,'mapid'=>$id);
        }
        return $result;
    }

    function doVoteLink($json){
        $user = $this->session->userdata('user.info');
        $result = array('code'=>404);
        if(isset($_SESSION['user.info'])){
            $id = $user['userid'] . '-' . $json->{'thingid'} . '-vote';
            $query = $this->db->select(['idata'])
                              ->where('id',$id)
                              ->get('user_thing_map');
            $rows = $query->result_array();

            $idata_new = $json->{'idata'};
            $this->db->trans_start();
            if(count($rows)==1){

                $idata_old = $rows[0]['idata'];
                if($idata_old == $idata_new){
                    $this->db->where('id',$id);
                    $this->db->delete('user_thing_map');

                    if($idata_new==1){
                        $this->db->set('ups','ups-1',FALSE);
                    }else{
                        $this->db->set('downs','downs-1',FALSE);
                    }
                    $this->db->where('ID',$json->{'thingid'});
                    $this->db->update('things');

                }else{
                    $this->db->set('idata',$json->{'idata'});
                    $this->db->where('id',$id);
                    $this->db->update('user_thing_map');


                    if($idata_new==1){
                        $this->db->set('ups','ups+1',FALSE);
                        $this->db->set('downs','downs-1',FALSE);
                    }else if($idata_new==-1){
                        $this->db->set('ups','ups-1',FALSE);
                        $this->db->set('downs','downs+1',FALSE);
                    }
                    $this->db->where('ID',$json->{'thingid'});
                    $this->db->update('things');
                }

            }else{
                $this->db->insert('user_thing_map',
                    array('id'=>$id,
                        'idata'=>$json->{'idata'},
                        'userid'=>$user['userid'],
                        'thingid'=>$json->{'thingid'},
                        'maptype'=>'vote',
                        'cdate'=>$this->curTime(),
                        'udate'=>$this->curTime()));
                if($idata_new==1){
                    $this->db->set('ups','ups+1',FALSE);
                }else{
                    $this->db->set('downs','downs+1',FALSE);
                }
                $this->db->where('ID',$json->{'thingid'});
                $this->db->update('things');
            }
            $result = array('code'=>200,'mapid'=>$id);
            $this->db->trans_complete();

        }
        return $result;

        
    }

    function doSubmitNewLink($json){
        $user = $this->session->userdata('user.info');
        if(isset($_SESSION['user.info'])){
        
            $this->db->insert('things',
                array('author'=>$user['userid'],
                    'title'=>$json->{'title'},
                    'stype'=>'link',
                    'content'=>$json->{'content'},
                    'cdate'=>$this->curTime(),
                    'udate'=>$this->curTime()));
                return array('code'=>200,'thingid'=>$this->db->insert_id());

        }else{
            return array('code'=>404);
        }
    }

    function doSubmitNewMessage($json){
        $user = $this->session->userdata('user.info');
        if(isset($_SESSION['user.info'])){
        
            $this->db->insert('things',
                array('author'=>$user['userid'],
                    'title'=>$json->{'title'},
                    'recipients'=>$json->{'recipients'},
                    'stype'=>'message',
                    'content'=>$json->{'content'},
                    'cdate'=>$this->curTime(),
                    'udate'=>$this->curTime()));
                return array('code'=>200,'thingid'=>$this->db->insert_id());

        }else{
            return array('code'=>404);

        }
        
    }

    function doSubmitNewComment($json){
        $user = $this->session->userdata('user.info');
        if(isset($_SESSION['user.info'])){

            $this->db->trans_start();
        
            $this->db->insert('things',
                array('author'=>$user['userid'],
                    'title'=>'',
                    'content'=>$json->{'content'},
                    'stype'=>'comment',
                    'main'=>$json->{'main'},
                    'parent'=>$json->{'parent'},
                    'cdate'=>$this->curTime(),
                    'udate'=>$this->curTime()));

            $commentid = $this->db->insert_id();
            
            $this->db->set('replies','replies+1',FALSE);
            $this->db->where('ID',$json->{'main'});
            $this->db->or_where('ID',$json->{'parent'});
            $this->db->update('things');



            $this->db->trans_complete();
            return array('code'=>200,'commentid'=>$commentid);

        }else{
            return array('code'=>404);

        }

        
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
    

    function curTime(){
        $ms = time();
        return $ms;
    }
 

}
