<?php

//糖尿病

class AmaModel extends CI_Model{
    public function __construct()
    {
        $this->load->database();
        $this->load->library('session');
    }

    public function readHotThings($maxid,$limit){
    		$this->db->select(['author','FROM_UNIXTIME(cdate) as timeago','title','content as text','cdate','udate','things.id as thingid','ups as likes','downs as dislikes',
                'parent','main','replies']);
    		$this->db->from('things');
    	    $this->db->join('users', 'users.userid = things.author');
    		$this->db->where('main',0);
    		$this->db->where('parent',0);
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





    public function readThingsByUser($maxid,$limit,$userid,$type='all'){
        $this->db->select(['things.author','FROM_UNIXTIME(things.cdate) as timeago','things.title',
            'things.content as text','things.cdate','things.udate','things.id as thingid',
            'things.ups as likes','things.downs as dislikes',
            'things.parent','things.main','things.replies',
            'a.title as p_title','a.ID as p_id','a.author as p_author','a.content as p_text']);
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
        $this->db->select(['author','FROM_UNIXTIME(cdate) as timeago','title',
            'content as text','cdate','udate','things.id as thingid','ups as likes','downs as dislikes',
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
        $this->db->select(['author','FROM_UNIXTIME(cdate) as timeago','title',
            'content as text','cdate','udate','things.id as thingid','ups as likes','downs as dislikes',
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