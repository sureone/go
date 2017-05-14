//
//  BoopoTableViewController.swift
//  boopo
//
//  Created by sureone on 2017/5/14.
//  Copyright © 2017年 boopo. All rights reserved.
//

import UIKit

var kSize=UIScreen.main.bounds;


class BoopoTableViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {

    @IBOutlet var listTableView : UITableView!
    var dataSource:NSArray = []
    /*  创建Post请求 */
    func postRequestDeprecated()
    {
        //(1）设置请求路径
        let url:NSURL = NSURL(string:"https://www.boopo.cn:19023/ama/index.php/api")!//不需要传递参数
        
        //(2) 创建请求对象
        let request:NSMutableURLRequest = NSMutableURLRequest(url: url as URL) //默认为get请求
        request.timeoutInterval = 5.0 //设置请求超时为5秒
        request.httpMethod = "POST"  //设置请求方法
        
        //设置请求体
        let param:NSString = NSString(format:"{\"action\":\"read-hot-things\"}")
        //把拼接后的字符串转换为data，设置请求体
        request.httpBody = param.data(using: String.Encoding.utf8.rawValue)
        
        
        
        
        //(3) 发送请求
        NSURLConnection.sendAsynchronousRequest(request as URLRequest, queue:OperationQueue()) { (res, data, error)in
            //服务器返回：请求方式 = POST，返回数据格式 = JSON，用户名 = 123，密码 = 123
            let  str = NSString(data: data!, encoding:String.Encoding.utf8.rawValue)
            print(str!)
            
        }
    }
    
    func onDataReady(data:Any){
        
        self.dataSource = data as!NSArray
        for item in self.dataSource{
            print(item)
        }
        
        listTableView.reloadData()
        
        
    }
    
    
    func postRequest(){
        
        
        var request = URLRequest(url: URL(string: "https://www.boopo.cn:19023/ama/index.php/api")!)
        request.httpMethod = "POST"
        
        let params = ["action":"read-hot-things"] as Dictionary<String, String>
        
        request.httpBody = try? JSONSerialization.data(withJSONObject: params, options: [])
        
        let session = URLSession.shared
        
        session.dataTask(with: request) {data, response, err in
            //let  str = NSString(data: data!, encoding:String.Encoding.utf8.rawValue)
            //print(str!)
            let jsonData = try? JSONSerialization.jsonObject(with: data!)
            self.onDataReady(data: jsonData ?? [])
            
            }.resume()
        
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.title = "boopo.cn"
        postRequest()
    }
    
    //段数
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1;
    }
    
    //行数
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dataSource.count
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
        let item = self.dataSource[indexPath.row] as! Dictionary<String,Any>
        cell?.textLabel?.text = item["title"] as? String;
        cell?.detailTextLabel?.text = item["timeago"] as? String;
        cell?.detailTextLabel?.font = UIFont .systemFont(ofSize: CGFloat(13))
        cell?.accessoryType=UITableViewCellAccessoryType.disclosureIndicator
        
        return cell!
    }
    
    //选中cell时触发这个代理
    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath){
        print("indexPath.row = SelectRow第\(indexPath.row)行")
        self.performSegue(withIdentifier: "show-detail", sender: self)
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
