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
		$this->load->library('ci_smarty');
		$this->load->model('amaModel');
		$this->load->helper(array('form', 'url'));
		$this->load->library('wx_crypt');
		$this->load->library('someclass');
		$this->load->library('markdown');
		
	}
	public function index($code="")
	{
		// if($code!="helo"){

		// 	echo "test php";
		// 	return 0;
		// }

		$user_info = $this->session->userdata('user.info');
		$this->showHotView();
		// $this->load->view('hot',array('user'=>$user_info,'page'=>'hot',));
        #$query = $this->db->query("select * from sgfs");
		#$rows = $query->result_array();
		//$this->db->query("insert into threads(content) values('hello')");
		#echo json_encode_utf8($rows);
	}
	public function hot()
	{
		$this->showHotView();
		
	}
	public function questions()
	{
		$this->showQuestionsView();
		
	}
	public function news()
	{
		$this->showNewView();
		
	}
	/**
	* 是否移动端访问访问
	*
	* @return bool
	*/
	function isMobile()
	{
		// 如果有HTTP_X_WAP_PROFILE则一定是移动设备
		if (isset ($_SERVER['HTTP_X_WAP_PROFILE']))
		{
			return true;
		}
		// 如果via信息含有wap则一定是移动设备,部分服务商会屏蔽该信息
		if (isset ($_SERVER['HTTP_VIA']))
		{
			// 找不到为flase,否则为true
			return stristr($_SERVER['HTTP_VIA'], "wap") ? true : false;
		}
		// 脑残法，判断手机发送的客户端标志,兼容性有待提高
		if (isset ($_SERVER['HTTP_USER_AGENT']))
		{
			$clientkeywords = array ('nokia',
			'sony',
			'ericsson',
			'mot',
			'samsung',
			'htc',
			'sgh',
			'lg',
			'sharp',
			'sie-',
			'philips',
			'panasonic',
			'alcatel',
			'lenovo',
			'iphone',
			'ipod',
			'blackberry',
			'meizu',
			'android',
			'netfront',
			'symbian',
			'ucweb',
			'windowsce',
			'palm',
			'operamini',
			'operamobi',
			'openwave',
			'nexusone',
			'cldc',
			'midp',
			'wap',
			'mobile'
			);
			// 从HTTP_USER_AGENT中查找手机浏览器的关键字
			if (preg_match("/(" . implode('|', $clientkeywords) . ")/i", 
				strtolower($_SERVER['HTTP_USER_AGENT'])))
			{
				return true;
			}
		}
		// 协议法，因为有可能不准确，放到最后判断
		if (isset ($_SERVER['HTTP_ACCEPT']))
		{
			// 如果只支持wml并且不支持html那一定是移动设备
			// 如果支持wml和html但是wml在html之前则是移动设备
			if ((strpos($_SERVER['HTTP_ACCEPT'], 'vnd.wap.wml') !== false) 
				&& (strpos($_SERVER['HTTP_ACCEPT'], 'text/html') === false || (strpos($_SERVER['HTTP_ACCEPT'], 'vnd.wap.wml') < strpos($_SERVER['HTTP_ACCEPT'], 'text/html'))))
			{
			return true;
			}
		}
		return false;
	}
	function common(){



		$user_info = $this->session->userdata('user.info');
		$this->ci_smarty->assign("pagetype","list");

		if($this->isMobile()) $this->ci_smarty->assign("ismobile","true");
		if($user_info!=null){
			$this->ci_smarty->assign("user",$user_info);
	        $this->ci_smarty->assign("logined","true");
	        $this->ci_smarty->assign("new_number",$this->amaModel->readMessageAccount($user_info['userid']));
	        return true;
    	}else{
    		$this->ci_smarty->assign("logined","false");
    		return false;
    	}
	}


	function showHotView(){
		$this->common();



		$questions = array(
				array('no'=>1,
					'title'=>'体重指数（BMI）的计算方法是：',
					'options'=>array(
						'A、BMI=体重（kg）/ 身高（m）的平方',   
						'B、BMI=体重（kg）的平方 / 身高（m）',
						'C、BMI=体重（kg）的平方 / 身高（m）的平方',  
						'D、BMI=身高（m）* 体重（kg）',  
						'E、BMI=身高（m）/ 体重(kg)'
					),
					'daan'=>array('1')),
				array('no'=>2,
					'title'=>'下列选项中不是甲亢的临床表现：',
					'options'=>array(
						'A、心悸',  'B、精神亢奋',  'C、月经量过多',  'D、怕热多汗',  'E、大便次数增多' 
					),
					'daan'=>array('3')),
				array('no'=>3,
					'title'=>'IGT是指：',
					'options'=>array(
						'A、线粒体tRNALeu(UUR)基因突变糖尿病',
						'B、青年人中的成年发病型糖尿病', 
						'C、妊娠期糖尿病', 
						'D、糖耐量异常', 
						'E、空腹血糖过高'   

					),
					'daan'=>array('4')),
				array('no'=>4,
					'title'=>'病因学将糖尿病分成的四大类型是：',
					'options'=>array(
						'A、1型，2型，其他特殊类型，GDM',  
						'B、自身免疫，特发性，胰岛素抵抗，胰岛素分泌缺陷',  
						'C、正常葡萄糖耐量，IGT，IFG，糖尿病',  
						'D、正常血糖，IGT，IFG，妊娠糖尿病',   
						'E、1型，2型，IGT，IFG'

					),
					'daan'=>array('1')),
				array('no'=>5,
					'title'=>'胰岛素抵抗是指：',
					'options'=>array(
						'A、机体对胰岛素超常反应',
						'B、机体对胰岛素超常敏',
						'C、机体对胰岛素的生理效应增高',
						'D、机体对胰岛素的生理效应降低',
						'E、机体对胰岛素的需求量减少'

					),
					'daan'=>array('4')),
				array('no'=>6,
					'title'=>'2型糖尿病发病机制的两个基本环节是',
					'options'=>array(

						'A、高胰岛素血症和胰岛素抵抗',
						'B,高胰岛素血症和糖耐量低减',
						'C.胰岛素抵抗和胰岛素分泌缺陷',
						'D.胰岛素抵抗和糖耐量低减',
						'E高胰岛素血症和糖耐量增高'

					),
					'daan'=>array('3')),
				array('no'=>7,
					'title'=>'糖尿病患者失明的主要原因是：',
					'options'=>array(
						'A、脑血管意外',  'B、白内障',  'C、视网膜病变',  'D、青光眼',  'E、虹膜睫状体病变'
					),
					'daan'=>array('3')),
				array('no'=>8,
					'title'=>'糖尿病性神经病变最常见的是：',
					'options'=>array(
						'A、周围神经病变',  'B、颅神经病变',  'C、植物神经病变',  'D、中枢神经病变',  'E、脊髓病变'
					),
					'daan'=>array('1')),
				array('no'=>9,
					'title'=>'糖尿病的诊断标准是:症状+静脉血浆葡萄糖值：',
					'options'=>array(
						'A、随机≥11.1 mmol/L或空腹≥7.0 mmol/L或OGTT中2h≥11.1 mmol/L',
						'B、随机≥7.8 mmol/L或空腹≥7.0 mmol/L',   
						'C、随机≥11.1 mmol/L或空腹≥7.8 mmol/L',   
						'D、随机≥6.1 mmol/L或空腹≥7.8 mmol/L'
   
					),
					'daan'=>array('1')),
				array('no'=>10,
					'title'=>'IGT的2 h血浆葡萄糖值为：',
					'options'=>array(
						'A、7.0～11.1 mmol/L',  'B、6.1～11.1 mmol/L',  'C、6.1～7.0 mmol/L',   
						'D、6.1～7.8 mmol/L',   'E、7.8～11.1 mmol/L'   

					),
					'daan'=>array('5')),
				array('no'=>11,
					'title'=>'糖化血红蛋白测定的临床意义为反映糖尿病患者：',
					'options'=>array(
						'A、近半年内血糖总的水平',  
						'B、近5个月内血糖总的水平',  
						'C、近16周内血糖总的水平',   
						'D、近8～12周内血糖总的水平', 
						'E、近2～3周内血糖总的水平'  

					),
					'daan'=>array('4')),
				array('no'=>12,
					'title'=>'糖尿病的基础治疗包括：',
					'options'=>array(
						'A、饮食治疗和合适的体育锻炼',  
						'B、口服降糖药物治疗',  
						'C、胰岛素治疗',   
						'D、胰腺移植',  
						'E、胰岛细胞移植'   

					),
					'daan'=>array('1')),
				array('no'=>13,
					'title'=>'高胆固醇血症者选用的调节血脂药是：',
					'options'=>array(
						'A、胆酸螯合树脂类',  
						'B、烟酸类',  
						'C、HMG-CoA还原酶抑制剂（他汀类）',   
						'D、氯贝丁酯类',  
						'E、亚油酸及其复方制剂' 

					),
					'daan'=>array('3')),
				array('no'=>14,
					'title'=>'2型糖尿病患者糖化血红蛋白控制目标',
					'options'=>array(
						'A. < 6.5%',
						'B. < 7.0%',
						'C. < 8.0%',
						'D. < 8.5%',
						'E. < 6.8%'

					),
					'daan'=>array('2')),
				array('no'=>15,
					'title'=>'下列不能用于睡前注射的胰岛素类型是',
					'options'=>array(
						'A  中效胰岛素',
						'B  预混胰岛素（30/70）',
						'C  甘精胰岛素',
						'D  长效胰岛素',
						'E  地特胰岛素'

					),
					'daan'=>array('2')),
				array('no'=>16,
					'title'=>'糖尿病患者降压药物首选',
					'options'=>array(
						'A、 ACEI及ARB类药物',
						'B、 钙离子拮抗剂',
						'C、 B受体阻滞剂',
						'D、 利尿剂',
						'E、 a受体阻滞剂'

					),
					'daan'=>array('1')),
				array('no'=>17,
					'title'=>'糖尿病患者低血糖的诊断标准',
					'options'=>array(
						'A．血糖≤ 2.8mmol/L',
						'B .血糖≤ 3.9mmol/L',
						'C . 血糖≤ 6.1mmol/L',
						'D . 血糖≤ 2.0mmol/L',
						'E . 血糖≤ 2.4mmol/L'

					),
					'daan'=>array('2')),
				array('no'=>18,
					'title'=>'国内指南中初发糖尿病患者建议强化治疗的糖化血红蛋白水平',
					'options'=>array(
						'A.  >9.0%',
						'B.  >9.5%',
						'C． >10.0%',
						'D. >11.0%',
						'E. >8.5%'

					),
					'daan'=>array('1')),
				array('no'=>19,
					'title'=>'慢性肾脏病3级eGFR的范围',
					'options'=>array(
						'A． 90-120ml/min',
						'B   60-90ml/min',
						'C  30-60ml/min',
						'D  15-30ml/min',
						'E  < 15ml/min'

					),
					'daan'=>array('3')),
				array('no'=>20,
					'title'=>'成人2型糖尿病患者每周至少运动时间',
					'options'=>array(
						'A  60分钟',
						'B  120分钟',
						'C  180分钟',
						'D  150分钟',
						'E  100分钟'

					),
					'daan'=>array('4')),
				array('no'=>21,
					'title'=>'预防心脑血管疾病，下列说法正确的是',
					'options'=>array(
						'A  控制低密度胆固醇',
						'B  降低甘油三酯',
						'C  降低高密度胆固醇',
						'D  使用贝特类药物',
						'E  低脂饮食'

					),
					'daan'=>array('1')),
				array('no'=>22,
					'title'=>'反复尿路结石的病人应注意排除的内分泌疾病是',
					'options'=>array(
						'A  甲状腺功能亢进症',
						'B  原发性甲状旁腺功能亢进症',
						'C  甲状腺功能减退症',
						'D  糖尿病',
						'E  肢端肥大症'

					),
					'daan'=>array('2')),
				array('no'=>23,
					'title'=>'高甘油三酯血症的主要危害包括',
					'options'=>array(
						'A  急性胰腺炎',
						'B  心脑血管疾病',
						'C  糖尿病',
						'D  高血压',
						'E  痛风发作'

					),
					'daan'=>array('1')),
				array('no'=>24,
					'title'=>'下列甲功结果可能的诊断是 TSH mIU/L(0.27-4.2)>100,FT3pmol/L(3.1-6.8)=5.27,FT4pmol/L(12-22)=11.14',
					'options'=>array(
						'A  甲状腺功能亢进症',
						'B  甲状腺功能减退症',
						'C  亚临床甲亢',
						'D  亚临床甲减',
						'E  低T3 T4综合征'

					),
					'daan'=>array('2')),
				array('no'=>25,
					'title'=>'早期筛查糖尿病并发症的检查不包括',
					'options'=>array(
						'A  眼底照相',
						'B  尿微量白蛋白',
						'C  糖化血红蛋白',
						'D  糖尿病周围神经病变体征筛查',
						'E  足背动脉初诊'

					),
					'daan'=>array('3')),
				array('no'=>26,
					'title'=>'糖尿病综合控制目标的组分不包括',
					'options'=>array(
						'A  血糖',
						'B  血脂',
						'C 血压',
						'D 体重',
						'E 防止感染'

					),
					'daan'=>array('5')),
				array('no'=>27,
					'title'=>'低血糖昏迷的治疗措施是',
					'options'=>array(
						'A． 口服碳水化合物',
						'B． 静脉推注高糖注射液',
						'C   使用糖皮质激素',
						'D  口服巧克力',
						'E  口服高蛋白食物'


					),
					'daan'=>array('2')),
				array('no'=>28,
					'title'=>'2型糖尿病合并肥胖的治疗措施不包括',
					'options'=>array(
						'A 减重代谢手术',
						'B 使用GlP-1受体激动剂',
						'C 饮食控制',
						'D 运动',
						'E  使用胰岛素'


					),
					'daan'=>array('5')),
				array('no'=>29,
					'title'=>'胰岛素使用过程中的注意事项不包括',
					'options'=>array(
						'A 过敏',
						'B 低血糖',
						'C 体重增加',
						'D 下肢水肿',
						'E 肝功能损害'
				

					),
					'daan'=>array('5')),
				array('no'=>30,
					'title'=>'糖尿病足的预防措施不包括',
					'options'=>array(
						'A  良好血糖控制',
						'B  定期足部检查',
						'C  过量运动',
						'D  穿舒适的鞋子',
						'E 防治破溃感染'
												

					),
					'daan'=>array('3')),
				array('no'=>31,
					'title'=>'关于胰岛素治疗说法正确的是',
					'options'=>array(
						'A  若血糖控制不理想，胰岛素剂量可以无限制增加',
						'B  短效胰岛素可以睡前注射',
						'C  胰岛素是最佳的降糖方案',
						'D  胰岛素治疗有低血糖风险',
						'E  使用胰岛素治疗无需关注体重变化'
						

					),
					'daan'=>array('4')),
				array('no'=>32,
					'title'=>'对于糖尿病前期目前建议的干预措施是',
					'options'=>array(
						'A 积极使用降糖药物',
						'B 生活方式干预',
						'C 尽早胰岛素治疗',
						'D 无需特殊处理',
						'E 定期检查并发症'
												

					),
					'daan'=>array('2')),
				array('no'=>33,
					'title'=>'可以适当放宽血糖控制目标的情况不包括',
					'options'=>array(
						'A 高龄或预期寿命有限',
						'B 严重心脑血管疾病',
						'C  认知障碍',
						'D  有糖尿病家族史',
						'E  既往反复低血糖昏迷病史'
						

					),
					'daan'=>array('4')),
				array('no'=>34,
					'title'=>'下列属于糖尿病高危因素的是',
					'options'=>array(
						'A．少动的生活方式',
						'B．超重或肥胖',
						'C．年龄< 40岁',
						'D. 一级亲属有糖尿病家族史',
						'E． 妊娠糖尿病病史',


					),
					'daan'=>array('1','2','4','5')),
				array('no'=>35,
					'title'=>'下列属于口服降糖药物分类的是',
					'options'=>array(
						'A． 双胍类',
						'B． 磺脲类及非磺脲类促泌剂',
						'C． a-糖苷酶抑制剂',
						'D． SGLT-2抑制剂',
						'E． GLP-1受体激动剂剂DPP-4酶抑制剂'


					),
					'daan'=>array('1','2','3','4','5')),
				array('no'=>36,
					'title'=>'国内糖尿病防治指南中推荐可用于一线治疗的口服降糖药物包括',
					'options'=>array(
						'A  双胍类',
						'B  胰岛素促泌剂',
						'C  GLP-1受体激动剂剂DPP-4酶抑制剂',
						'D  SGLT-2抑制剂',
						'E  a-糖苷酶抑制剂'
						

					),
					'daan'=>array('1','2','5')),
				array('no'=>37,
					'title'=>'单独使用一般不出现低血糖反应的降糖药物包括',
					'options'=>array(
						'A 双胍类',
						'B 胰岛素',
						'C DPP－4抑制剂',
						'D 磺脲类促泌剂',
						'E 非磺脲类促泌剂'


					),
					'daan'=>array('1','3')),
				array('no'=>38,
					'title'=>'对于已经合并心脑血管疾病的2型糖尿病患者必须使用的药物包括',
					'options'=>array(
						'A  抗血小板聚集药物',
						'B 他汀类药物',
						'C  抑酸药物',
						'D 营养神经药物',
						'E  抗氧化药物'
											

					),
					'daan'=>array('1','2')),
				array('no'=>39,
					'title'=>'下列属于继发性糖尿病的病因包括',
					'options'=>array(
						'A  使用糖皮质激素',
						'B  甲亢',
						'C  胰腺切除',
						'D  肢端肥大症',
						'E  皮质醇增多症'


					),
					'daan'=>array('1','2','3','4','5')),
				array('no'=>40,
					'title'=>'下列建议筛查继发性高血压的情况包括',
					'options'=>array(
						'A  年轻的高血压患者',
						'B  难治性高血压',
						'C  高血压合并低血钾',
						'D  高血压合并肾上腺占位',
						'E  高血压合并冠心病'


					),
					'daan'=>array('1','2','3','4')),
				array('no'=>41,
					'title'=>'使用他汀类药物须注意',
					'options'=>array(
						'A  肌肉疼痛',
						'B  肝功能损害',
						'C  血小板减少',
						'D  肾功能损害',
						'E  心功能不全'
						

					),
					'daan'=>array('1','2')),
				array('no'=>42,
					'title'=>'对于降糖药物的综合评价包括',
					'options'=>array(
						'A. 有效性',
						'B  低血糖风险',
						'C  体重',
						'D  心血管安全性',
						'E  费用'
						

					),
					'daan'=>array('1','2','3','4','5')),
				array('no'=>43,
					'title'=>'下列属于1型和2型糖尿病鉴别要点的是',
					'options'=>array(
						'A  胰岛功能',
						'B 胰岛自身抗体',
						'C 是否有酮症倾向',
						'D 年龄',
						'E 是否吸烟'


					),
					'daan'=>array('1','2','3','4'))

						
		);
		
		$this->db->select(array('a.name','a.danwei','a.bumen','b.score'));
        $this->db->from('exam_users a');
        $this->db->join('exam_answers b', 'a.id = b.userid');
        $this->db->order_by('a.id','ASC');

        $query = $this->db->get();
        $rows = $query->result_array();


		$this->ci_smarty->assign("page","hot");
		$this->ci_smarty->assign("questions",$questions);

		$this->ci_smarty->assign("exam_users",$rows);
		$this->ci_smarty->display("mobile/mobile.tpl");




	}


	function showHotView2(){
		$this->common();
		
		if($this->isMobile()) {
			if(!$this->ci_smarty->isCached('mobile/mobile.tpl')){ 
				$rows = $this->amaModel->readNewThings(0,200,true);
				$this->ci_smarty->assign("page","hot");
				$this->ci_smarty->assign("things",$rows);
				$this->ci_smarty->display("mobile/mobile.tpl");
			}else{
				$this->ci_smarty->display("mobile/mobile.tpl");
			}
			return;
		}

		if(!$this->ci_smarty->isCached('hot.tpl')){ 
			$rows = $this->amaModel->readHotThings(0,2000);
			$this->ci_smarty->assign("page","hot");
			$this->ci_smarty->assign("things",$rows);
			$this->ci_smarty->display("hot.tpl");
		}else{
			$this->ci_smarty->display("hot.tpl");
		}



	}

	function showQuestionsView(){
		$this->common();
		
		
			$this->ci_smarty->display("questions.tpl");
		


	}

	function showNewView(){
		$this->common();
		if(!$this->ci_smarty->isCached('new.tpl')){ 
			$rows = $this->amaModel->readNewThings(0,2000,false);
			$this->ci_smarty->assign("page","new");
			$this->ci_smarty->assign("things",$rows);
			$this->ci_smarty->display("new.tpl");
		}else{
			$this->ci_smarty->display("new.tpl");
		}


	}
	public function wxtest2(){
		echo $this->someclass->test();
	}
	public function wxtest(){
		$appid = 'wx9ec950ea9d8e2f64';
		$sessionKey = 'tiihtNczf5v6AKRyjwEUhQ==';

		$encryptedData="CiyLU1Aw2KjvrjMdj8YKliAjtP4gsMZM
		                QmRzooG2xrDcvSnxIMXFufNstNGTyaGS
		                9uT5geRa0W4oTOb1WT7fJlAC+oNPdbB+
		                3hVbJSRgv+4lGOETKUQz6OYStslQ142d
		                NCuabNPGBzlooOmB231qMM85d2/fV6Ch
		                evvXvQP8Hkue1poOFtnEtpyxVLW1zAo6
		                /1Xx1COxFvrc2d7UL/lmHInNlxuacJXw
		                u0fjpXfz/YqYzBIBzD6WUfTIF9GRHpOn
		                /Hz7saL8xz+W//FRAUid1OksQaQx4CMs
		                8LOddcQhULW4ucetDf96JcR3g0gfRK4P
		                C7E/r7Z6xNrXd2UIeorGj5Ef7b1pJAYB
		                6Y5anaHqZ9J6nKEBvB4DnNLIVWSgARns
		                /8wR2SiRS7MNACwTyrGvt9ts8p12PKFd
		                lqYTopNHR1Vf7XjfhQlVsAJdNiKdYmYV
		                oKlaRv85IfVunYzO0IKXsyl7JCUjCpoG
		                20f0a04COwfneQAGGwd5oa+T8yO5hzuy
		                Db/XcxxmK01EpqOyuxINew==";

		$iv = 'r7BXXKkLb8qrSNn05n0qiA==';
echo "dddddd";
		$errCode = $this->wx_crypt->decryptData($appid,$sessionKey,$encryptedData, $iv, $data );
echo "dddddd";
		if ($errCode == 0) {
		    echo($data . "\n");
		} else {
		    echo($errCode . "\n");
		}
echo $data;
	}

	public function test(){
		$user_info = $this->session->userdata('user.info');

		// $thing = $this->amaModel->readThing($param)['0'];

		// $thing['comments'] = $this->amaModel->readComments($param,0,100);

		$things = $this->amaModel->readMessagesByUser(0,20,$user_info['userid'],'all');

		echo json_encode_utf8($things);
		
	}


	public function message($page='inbox',$msgto=null){
		if($this->common()==false){
			return;
		}

		$this->ci_smarty->assign("pagedir","message");

		$things= array();
		
		$user_info = $this->session->userdata('user.info');
		$this->ci_smarty->assign("page","message-{$page}");
		$this->ci_smarty->assign("pagetype","archive");
		if($page == "inbox"){
			$things = $this->amaModel->readMessagesByUser(0,20,$user_info['userid'],'all');	
		}

		if($page == "messages"){
			$things = $this->amaModel->readMessagesByUser(0,20,$user_info['userid'],'message');	
		}


		if($page == "comments"){
			$things = $this->amaModel->readMessagesByUser(0,20,$user_info['userid'],'comments');	
		}


		if($page == "selfreply"){
			$things = $this->amaModel->readMessagesByUser(0,20,$user_info['userid'],'selfreply');	
		}

		if($page == "compose"){
			if($msgto!=null){
				$userto = $this->amaModel->readUser($msgto);
				$this->ci_smarty->assign("touserid",$msgto);
				$this->ci_smarty->assign("tousername",$userto['name']);
			}
		}

		$this->ci_smarty->assign("things",$things);
		$this->ci_smarty->display("message-{$page}.tpl");

	}


	public function user($userid,$page='home')
	{
		$this->common();

		$things= array();

		$user = $this->amaModel->readUser($userid);
		$this->ci_smarty->assign("pagedir","user");
		
		$this->ci_smarty->assign("page","user-{$page}");
		$this->ci_smarty->assign("userid",$userid);
		$this->ci_smarty->assign("username",$user['name']);
		$this->ci_smarty->assign("pagetype","archive");
		
		if($page == "home"){
			$things = $this->amaModel->readThingsByUser(0,100,$userid,'home');
		}

		if($page == "replies"){
			$things = $this->amaModel->readThingsByUser(0,100,$userid,"reply");
		}

		if($page == "submitted"){
			$things = $this->amaModel->readThingsByUser(0,100,$userid,"main");
		}

		if($page == "saved"){
			$things = $this->amaModel->readThingsByUser(0,100,$userid,"saved");
		}

		if($page == "upvoted"){
			$things = $this->amaModel->readThingsByUser(0,100,$userid,"ups");
		}

		if($page == "downvoted"){
			$things = $this->amaModel->readThingsByUser(0,100,$userid,"downs");
		}

		$this->ci_smarty->assign("things",$things);
		$this->ci_smarty->display("user-{$page}.tpl",$userid);
	}

	public function logout()
	{
		$this->common();
		$this->load->view('hot');
        #$query = $this->db->query("select * from sgfs");
		#$rows = $query->result_array();
		//$this->db->query("insert into threads(content) values('hello')");
		#echo json_encode_utf8($rows);
	}
	public function submit($thingid=0){

        $this->common();
        $user_info = $this->session->userdata('user.info');
        if($thingid!=0 && $user_info!=null){

        	$rows = $this->amaModel->readThing($thingid);
        	$thing = $rows[0];
        	if($thing['author']==$user_info['userid']){
        		$thing['attaches']=$this->amaModel->readAttaches($thing['thingid']);
        		$this->ci_smarty->assign("thing",$thing);	
        	}
        	
        }

		$this->ci_smarty->assign("page","submit");
		$this->ci_smarty->display("submit.tpl",$thingid);
	}
	public function a($thingid){
		$this->common();
		$this->ci_smarty->assign("page","comments");
		$this->ci_smarty->assign("thingid",$thingid);


		$rows = $this->amaModel->readThing($thingid);
		$thing = $rows[0];

		$comments_result = $this->amaModel->readComments($thingid,0,100);
		$thing['comments']=$comments_result['comments'];
		$thing['comments_count']=$comments_result['comments_count'];
		$thing['attaches']=$this->amaModel->readAttaches($thingid);

		$this->ci_smarty->assign("things",array($thing));
		$this->ci_smarty->assign("page_title",$thing['title']);
		
		$this->ci_smarty->display("comments.tpl",$thingid);
		// $this->load->view('comments',array('user'=>$user_info,'page'=>'comments','thingid'=>$thingid));
	}


	public function uploadform()
    {
            $this->load->view('upload_form', array('error' => ' ' ));
    }


	public function iframe()
    {
            $this->load->view('iframe', array('error' => ' ' ));
    }


    public function do_upload()
    {
		$openid="";
		if(array_key_exists('openId', $_POST)){
			$openid=$_POST['openId'];
			if($this->amaModel->isValidWxUser($openid)==false) 
				return;
		}
            $config['upload_path']          = './uploads/';
            $config['allowed_types']        = 'gif|jpg|png|mp3';
			$config['allowed_types']        = '*';
            $config['max_size']             = 1024*1024*20;
            // $config['max_width']            = 40960;
            // $config['max_height']           = 40960;
            $config['file_ext_tolower']     = TRUE;
			$config['file_ext_tolower']     = TRUE;
			$config['encrypt_name']     = FALSE;
			$config['max_filename_increment']     = 100000000;


            $this->load->library('upload', $config);

            if ( ! $this->upload->do_upload('userfile'))
            {
                    $error = array('error' => $this->upload->display_errors());

                    $this->load->view('upload_success', $error);
            }
            else
            {
            		$result = $this->upload->data();
				$result['openid']=$openid;
            		$result['file_id']=$this->amaModel->addAttach($result);
			if($openid!=''){
				echo $result['file_id'];
			}else{
                    $data = array('upload_data' => $result);
                    $this->load->view('upload_success', $data);
			}

            }
    }

}
