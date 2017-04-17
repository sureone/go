<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Mm extends CI_Controller {

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
        $this->load->library('ci_smarty');
        $this->load->model('amaModel');
    }
    public function index()
    {   
        if(ENVIRONMENT == 'maintain')
            $this->doMaintainJobs();
    }

    function doMaintainJobs(){
        echo 'hello admin';
    }


}
