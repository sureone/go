<?php

//糖尿病

class AmaModel extends CI_Model{
    public function __construct()
    {
        $this->load->database();
        $this->load->library('session');
    }

    public function readUser($userid){

        $query = $this->db->get_where('users', array('userid' => $userid), 1, 0);  
        $rows = $query->result_array();

        if(count($rows)==1) return $rows[0];

        return null;
    }

    public function readHotThings($maxid,$limit){

        $user_info = $this->session->userdata('user.info');

        $fields = ['a.author','users.name as author_name','a.stype',
                'FROM_UNIXTIME(a.cdate) as timeago','a.title','a.content as text',
                'a.cdate','a.udate','a.id as thingid','a.ups as likes',
                'a.downs as dislikes',
                'a.parent','a.main','a.replies'];
        if($user_info!=null){
            array_push($fields,'ut.idata as vote');
        }
    		$this->db->select($fields);
    		$this->db->from('things a');
    	    $this->db->join('users', 'users.userid = a.author');
            if($user_info!=null){
                $this->db->join('user_thing_map ut',
                    // "ut.thingid=a.id and ut.maptype='vote' and ut.userid='" . $user_info['userid'] . "'","LEFT",TRUE);
                    "ut.id=CONCAT('" . $user_info['userid'] . "-',a.id,'-vote')","LEFT",TRUE);
    		}
            $this->db->where('main',0);
    		$this->db->where('parent',0);
            $this->db->where('stype','link');
            

            $this->db->order_by('a.score','DESC');
            
            $this->db->order_by('a.id','DESC');


    		$this->db->limit($limit);
    		// .get('things');
    		$query = $this->db->get();

    	$rows = $query->result_array();

    	return $rows;
    }

     public function readNewThings($maxid,$limit){

        $user_info = $this->session->userdata('user.info');

        $fields = ['a.author','users.name as author_name','a.stype',
                'FROM_UNIXTIME(a.cdate) as timeago','a.title','a.content as text',
                'a.cdate','a.udate','a.id as thingid','a.ups as likes',
                'a.downs as dislikes',
                'a.parent','a.main','a.replies'];
        if($user_info!=null){
            array_push($fields,'ut.idata as vote');
        }
            $this->db->select($fields);
            $this->db->from('things a');
            $this->db->join('users', 'users.userid = a.author');
            if($user_info!=null){
                $this->db->join('user_thing_map ut',
                    // "ut.thingid=a.id and ut.maptype='vote' and ut.userid='" . $user_info['userid'] . "'","LEFT",TRUE);
                    "ut.id=CONCAT('" . $user_info['userid'] . "-',a.id,'-vote')","LEFT",TRUE);
            }
            $this->db->where('main',0);
            $this->db->where('parent',0);
            $this->db->where('stype','link');
            
            if($maxid!=0)
              $this->db->where('a.id<',$maxid);
            else
              $this->db->order_by('a.id','DESC');
            $this->db->limit($limit);
            // .get('things');
            $query = $this->db->get();

        $rows = $query->result_array();

        return $rows;
    }



    public function readMessagesByUser($maxid,$limit,$userid,$type='all'){


        $sql1 = "select '' as p_text,'' as p_title,'' as m_author,
            '' as m_author_name,m.parent,m.main, 
            FROM_UNIXTIME(m.cdate) as timeago,m.readed,
            m.title,m.content as text,
            m.id as thingid,m.ups as likes,m.author,bu.name as author_name,
            m.downs as dislikes,m.replies,
            m.stype from things m 
            left join users bu on bu.userid=m.author
            where m.recipients='{$userid}'";
        $sql2 = "select mn.content as p_text,mn.title as p_title,mn.author as m_author,
            mnu.name as m_author_name,b.parent, mn.id as main,
            FROM_UNIXTIME(b.cdate) as timeago,b.readed,
            b.title,b.content as text,
            b.id as thingid,b.ups as likes,b.author,bu.name as author_name,b.downs as dislikes,b.replies,
            b.stype from things a 
            inner join things b on b.parent = a.id
            left join users bu on bu.userid=b.author
            inner join things mn on mn.id = b.main 
            left join users mnu on mnu.userid=mn.author
            where a.author='{$userid}'";
        $sql = "{$sql1} order by m.id DESC"; 
        if($type=='all'){
            $sql = "select u.* from (({$sql1}) union ({$sql2})) u order by u.thingid DESC";
            // $sql = "({$sql2})";
        }

        if($type=='unread'){
            $sql = "select u.* from (({$sql1} and m.readed='0') union ({$sql2} and b.readed='0')) u order by u.thingid DESC";
            // $sql = "({$sql2})";
        }

        if($type=='comments'){
            $sql = "{$sql2} and mn.author <> '{$userid}' order by b.id DESC";
        }


        if($type=='selfreply'){
            $sql = "{$sql2} and mn.author = '{$userid}' order by b.id DESC";
        }


        $query = $this->db->query($sql);
        $rows = $query->result_array();


        //set the thing as read
        $this->db->set('readed','1',FALSE);
        foreach ($rows as $row) {
            $this->db->or_where('ID',$row['thingid']);
        }
        $this->db->update('things');
        
        return $rows;
    }



    public function readThingsByUser($maxid,$limit,$userid,$type='all'){
        $this->db->select(['things.author','users.name as author_name','things.stype',
            'FROM_UNIXTIME(things.cdate) as timeago','things.title',
            'things.content as text','things.cdate','things.udate','things.id as thingid',
            'things.ups as likes','things.parent','things.downs as dislikes',
            'things.parent','things.main','things.replies',
            'a.title as p_title','a.author as p_author',
            'au.name  as p_author_name','a.content as p_text']);
        $this->db->from('things');
        $this->db->join('users', 'users.userid = things.author');
        $this->db->join('things a','a.id = things.parent','LEFT',TRUE);
        $this->db->join('users au', 'au.userid = a.author','LEFT',TRUE);
        

        if($type=='saved'){
             $this->db->join('user_thing_map b', "b.userid='" . $userid . "' and b.maptype='save' and b.thingid=things.id",'INNER',TRUE);
        }

        if($type=='ups'){
             $this->db->join('user_thing_map b', "b.userid='" . $userid . "' and b.maptype='vote' and b.idata='1' and b.thingid=things.id",'INNER',TRUE);
        }

        if($type=='downs'){
             $this->db->join('user_thing_map b', "b.userid='" . $userid . "' and b.maptype='vote' and b.idata='-1' and b.thingid=things.id",'INNER',TRUE);
        }
        
        if($maxid!=0)   
          $this->db->where('things.id<',$maxid);
          

       
        if($type=="main"){
            $this->db->where('things.author',$userid);
            $this->db->where('things.parent','0');
           
        }
        if($type=="reply"){
            $this->db->where('things.author',$userid);
            $this->db->where('things.parent >',0);
        }

        if($type=='home'){
            $this->db->where('things.author',$userid);

        }

         $this->db->where('things.stype','link');

        $this->db->order_by('things.id','DESC');

        $this->db->limit($limit);
        // .get('things');
        $query = $this->db->get();
        $rows = $query->result_array();

        return $rows;
    }

    function isAdmin($userid){
        return false;
    }

    public function deleteThing($thingid,$userid){
        $thing = $this->readThing($thingid);
        if($this->isAdmin($userid) || (  count($thing)==1 && $thing[0]['thingid'] == $thingid && $thing[0]['author']==$userid)){
            $this->db->where('id', $thingid);
            $this->db->delete('things');
            return true;
        }

        return false;


    }
    public function readThing($thingid){

        $user_info = $this->session->userdata('user.info');

        $fields = ['a.author','users.name as author_name','a.stype',
                'FROM_UNIXTIME(a.cdate) as timeago','a.title','a.content as text',
                'a.cdate','a.udate','a.id as thingid','a.ups as likes',
                'a.downs as dislikes',
                'a.parent','a.main','a.replies'];
        if($user_info!=null){
            array_push($fields,'ut.idata as vote');
        }
            $this->db->select($fields);
            $this->db->from('things a');
            $this->db->join('users', 'users.userid = a.author');
            if($user_info!=null){
                $this->db->join('user_thing_map ut',
                   "ut.id=CONCAT('" . $user_info['userid'] . "-',a.id,'-vote')","LEFT",TRUE);
            }
              $this->db->where('a.id',$thingid);
            $query = $this->db->get();

        $rows = $query->result_array();

        return $rows;
    }


    // 一定要是引用传递
    function genThingsTree($rows,&$cur){


        $cur['comments']=array();
        // 一定要是引用
        foreach($rows as &$child){
            if($child['parent']==$cur['thingid']){
                //修改数组一定要在push之前，否则修改无效
                $this->genThingsTree($rows,$child);
                array_push($cur['comments'],$child);
            }
        }



    }
    

    public function readComments($main,$parent,$limit){

         $user_info = $this->session->userdata('user.info');
        $fields = ['a.author','users.name as author_name','a.stype',
        'FROM_UNIXTIME(a.cdate) as timeago','a.title',
            'a.content as text','a.cdate','a.udate','a.id as thingid',
            'a.ups as likes','a.downs as dislikes',
            'a.parent','a.main','a.replies'];

    if($user_info!=null){
            array_push($fields,'ut.idata as vote');
        }

        $this->db->select($fields);
        $this->db->from('things a');
        $this->db->join('users', 'users.userid = a.author');

        if($user_info!=null){
                $this->db->join('user_thing_map ut',
                    "ut.id=CONCAT('" . $user_info['userid'] . "-',a.id,'-vote')","LEFT",TRUE);
            }
        $this->db->where('a.main',$main);
        if($parent!=0)
            $this->db->where('a.parent',$parent);
        $this->db->order_by('a.id','DESC');
        $this->db->limit($limit);
        // .get('things');
        $query = $this->db->get();
        $rows = $query->result_array();

        $result = array();

        foreach ($rows as &$row){
            if($row['parent']==$main){
                $this->genThingsTree($rows,$row);
                array_push($result, $row);
                
            }
        }

        return array('comments'=>$result,'comments_count'=>count($rows));
    }
}