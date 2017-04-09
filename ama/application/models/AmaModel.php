<?php

//糖尿病

class AmaModel extends CI_Model{
    public function __construct()
    {
        $this->load->database();
        $this->load->library('session');
    }

    public function readHotThings($maxid,$limit){
    		$this->db->select(['author','stype','FROM_UNIXTIME(things.cdate) as timeago','title','content as text','things.cdate','udate','things.id as thingid','ups as likes','downs as dislikes',
                'parent','main','replies']);
    		$this->db->from('things');
    	    $this->db->join('users', 'users.userid = things.author');
    		$this->db->where('main',0);
    		$this->db->where('parent',0);
            $this->db->where('stype','link');
            
            if($maxid!=0)
    		  $this->db->where('things.id<',$maxid);
            else
              $this->db->order_by('things.id','DESC');
    		$this->db->limit($limit);
    		// .get('things');
    		$query = $this->db->get();

    	$rows = $query->result_array();

    	return $rows;
    }


    public function readMessagesByUser($maxid,$limit,$userid,$type='all'){


        $sql1 = "select '' as p_text,'' as p_title,'' as m_author, m.parent,m.main, 
            FROM_UNIXTIME(m.cdate) as timeago,m.readed,
            m.title,m.content as text,
            m.id as thingid,m.ups as likes,m.author,m.downs as dislikes,m.replies,
            m.stype from things m where m.recipients='{$userid}'";
        $sql2 = "select mn.content as p_text,mn.title as p_title,mn.author as m_author, b.parent, mn.id as main,
            FROM_UNIXTIME(b.cdate) as timeago,b.readed,
            b.title,b.content as text,
            b.id as thingid,b.ups as likes,b.author,b.downs as dislikes,b.replies,
            b.stype from things a 
            inner join things b on b.parent = a.id
            inner join things mn on mn.id = b.main 
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
        $this->db->select(['things.author','things.stype','FROM_UNIXTIME(things.cdate) as timeago','things.title',
            'things.content as text','things.cdate','things.udate','things.id as thingid',
            'things.ups as likes','things.parent','things.downs as dislikes',
            'things.parent','things.main','things.replies',
            'a.title as p_title','a.author as p_author','a.content as p_text']);
        $this->db->from('things');
        $this->db->join('users', 'users.userid = things.author');
        $this->db->join('things a','a.id = things.parent','LEFT',TRUE);

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
        else
          $this->db->order_by('things.id','DESC');

        $this->db->where('things.stype','link');
        if($type=="main"){
            $this->db->where('things.author',$userid);
            $this->db->where('things.parent','0');
        }
        if($type=="reply"){
            $this->db->where('things.author',$userid);
            $this->db->where('things.parent<>','0');
        }

        $this->db->limit($limit);
        // .get('things');
        $query = $this->db->get();
        $rows = $query->result_array();

        return $rows;
    }

    public function readThing($thingid){
        $this->db->select(['author','stype','FROM_UNIXTIME(things.cdate) as timeago','title',
            'content as text','things.cdate','udate','things.id as thingid','ups as likes','downs as dislikes',
            'parent','main','replies']);
        $this->db->from('things');
        $this->db->join('users', 'users.userid = things.author');
        $this->db->where('things.id',$thingid);
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
        $this->db->select(['author','stype','FROM_UNIXTIME(things.cdate) as timeago','title',
            'content as text','things.cdate','udate','things.id as thingid','ups as likes','downs as dislikes',
            'parent','main','replies']);
        $this->db->from('things');
        $this->db->join('users', 'users.userid = things.author');
        $this->db->where('main',$main);
        if($parent!=0)
            $this->db->where('parent',$parent);
        $this->db->order_by('things.id','DESC');
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