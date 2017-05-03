<?php defined('BASEPATH') OR exit('No direct script access allowed');

class Someclass {

    protected $CI;
    public function __construct()
    {
         $this->CI =& get_instance();
        // Do something with $params

    }

    public  function    test(){
        return "hello ci";
    }
}