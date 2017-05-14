//
//  ThingModel.swift
//  boopo
//
//  Created by sureone on 2017/5/14.
//  Copyright © 2017年 boopo. All rights reserved.
//

import Foundation

class ThingModel: NSObject {
    
    var thingdata: [String : String]
    
    // designated initializer
    init(thingdata: [String : String]) {
        self.thingdata = thingdata
        super.init()
    }
    
    
    func title()->String {
        return self.thingdata["title"]!
    }
    
    
    
    func text()->String {
        return self.thingdata["text"]!
    }
}
