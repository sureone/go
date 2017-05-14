//
//  DetailViewController.swift
//  boopo
//
//  Created by sureone on 2017/5/14.
//  Copyright © 2017年 boopo. All rights reserved.
//

import UIKit

class DetailViewController: UIViewController {

    @IBOutlet var webView : UIWebView!
    override func viewDidLoad() {
        super.viewDidLoad()
//        self.title = "boopo.cn"
        if let url = URL(string: "https://www.boopo.cn:19023/ama/") {
            let request = URLRequest(url: url)
            webView.loadRequest(request)
        }
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
