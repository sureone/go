//logs.js
var util = require('../../utils/util.js');
var app = getApp();
Page({
  data: {
    imgs: [],
    openId:null,
    attaches:[],
    progress:0
  },
  submitThing:function(e){

    var data={
      action:'submit-new-link',
     
      content:e.detail.value.content,
      title:'',
      openId:app.globalData.openId
    }

    if(this.data.attaches.length>0){
      data['attaches']=this.data.attaches;
       
    }
    wx.request({
                    method:'POST',
                    url: 'https://www.askmeany.cn/api',
                    data: data,
                    success:function(res){
                      console.log(res.data);
                      wx.navigateBack({delta:1});
                    }
                  })



  },
  onLoad: function () {
    var that = this;
    
    wx.chooseImage({
      success: function(res) {
        var tempFilePaths = res.tempFilePaths
        var cnt=0;
        var uploadDone=function(data){
          cnt++;
          console.log(data);
          that.data.attaches.push({
            file_id:data,file_comment:''
          });

          that.setData({
            progress:cnt/tempFilePaths.length*100
          });
          if(cnt==tempFilePaths.length){
            that.setData({
              imgs:tempFilePaths,
              openId:app.globalData.openId
            }); 
          }
        }
        var i=0;
        for(i=0;i<tempFilePaths.length;i++){
          wx.uploadFile({
            url: 'https://www.askmeany.cn/v/do_upload',
            filePath: tempFilePaths[i],
            name: 'userfile',
            formData:{
              'openId':app.globalData.openId
            },
            success: function(res){
              var data = res.data;
              //return file id
              uploadDone(data)
            },
            fail:function(res){
              console.log(res);
            }
          })

        }
       
     
           
      }
    })

   
  }
})
