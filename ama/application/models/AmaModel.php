<?php

//糖尿病

class AmaModel extends CI_Model{
    public function __construct()
    {
        $this->load->database();
        $this->load->library('session');
    }

    public function readHotThings($maxid,$limit){
    		$this->db->select(['author','title','content as text','cdate','udate','things.id as thingid','ups as likes','downs as dislikes']);
    		$this->db->from('things');
    	    $this->db->join('users', 'users.userid = things.author');
    		$this->db->where('main',0);
    		$this->db->where('parent',0);
    		$this->db->where('things.id<',$maxid);
    		$this->db->limit($limit);
    		// .get('things');
    		$query = $this->db->get();

    	$rows = $query->result_array();

    	return $rows;
    }
}