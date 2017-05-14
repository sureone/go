//
//  BoopoTableViewController.swift
//  boopo
//
//  Created by sureone on 2017/5/14.
//  Copyright © 2017年 boopo. All rights reserved.
//

import UIKit

var kSize=UIScreen.main.bounds;

var dataTable:UITableView!
var itemStringArr=["企划部","软件部","咨询部","人事部","后勤部","产品部"]


class BoopoTableViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    /*  创建Post请求 */
    func PostRequest()
    {
        //(1）设置请求路径
        var url:NSURL = NSURL(string:"http://www.boopo.cn:19023/ama/index.php/api")!//不需要传递参数
        
        //(2) 创建请求对象
        var request:NSMutableURLRequest = NSMutableURLRequest(url: url as URL) //默认为get请求
        request.timeoutInterval = 5.0 //设置请求超时为5秒
        request.httpMethod = "POST"  //设置请求方法
        
        //设置请求体
        var param:NSString = NSString(format:"username=%@&pwd=%@",self.userName.text!,self.userPwd.text!)
        //把拼接后的字符串转换为data，设置请求体
        request.HTTPBody = param.dataUsingEncoding(NSUTF8StringEncoding)
        
        //(3) 发送请求
        NSURLConnection.sendAsynchronousRequest(request, queue:NSOperationQueue()) { (res, data, error)in
            //服务器返回：请求方式 = POST，返回数据格式 = JSON，用户名 = 123，密码 = 123
            let  str =NSString(data: data!, encoding:NSUTF8StringEncoding)
            print(str)
            
        }
    }

    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "boopo.cn"
        
    }
    
    //段数
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1;
    }
    
    //行数
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return itemStringArr.count
    }
    
    //行高
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat{
        return 80
    }
    
    /*
     //头部高度
     func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
     return 0.01
     }
     
     //底部高度
     func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
     return 0.01
     }
     */
    
    //cell
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        /*
         let indentifier = "CellA"
         var cell:TableViewCellA! = tableView.dequeueReusableCell(withIdentifier: indentifier) as? TableViewCellA
         if cell == nil {
         cell=TableViewCellA(style: .default, reuseIdentifier: indentifier)
         }
         
         
         return cell!
         */
        
        let identifier="identtifier";
        var cell=tableView.dequeueReusableCell(withIdentifier: identifier)
        if(cell == nil){
            cell=UITableViewCell(style: UITableViewCellStyle.subtitle, reuseIdentifier: identifier);
        }
        
        cell?.textLabel?.text = itemStringArr[indexPath.row];
        cell?.detailTextLabel?.text = "待添加内容";
        cell?.detailTextLabel?.font = UIFont .systemFont(ofSize: CGFloat(13))
        cell?.accessoryType=UITableViewCellAccessoryType.disclosureIndicator
        
        return cell!
    }
    
    //选中cell时触发这个代理
    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath){
        print("indexPath.row = SelectRow第\(indexPath.row)行")
    }
    
    //取消选中cell时，触发这个代理
    public func tableView(_ tableView: UITableView, didDeselectRowAt indexPath: IndexPath){
        print("indexPath.row = DeselectRow第\(indexPath.row)行")
    }
    
    //允许编辑cell
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }
    
    //右滑触发删除按钮
    func tableView(_ tableView: UITableView, editingStyleForRowAt indexPath: IndexPath) -> UITableViewCellEditingStyle {
        return UITableViewCellEditingStyle.init(rawValue: 1)!
    }
    
    //点击删除cell时触发
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        print("indexPath.row = editingStyle第\(indexPath.row)行")
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
}
