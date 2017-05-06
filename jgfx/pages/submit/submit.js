//logs.js
var util = require('../../utils/util.js');
var app = getApp();
Page({
  data: {
    imgs: [],
    openId:null
  },
  submitThing:function(e){

    var data={
      action:'submit-new-link',
      attaches:[
        {file_comment:'',file_id:e.detail.value.file_id}
      ],
      content:e.detail.value.content,
      title:'',
      openId:e.detail.value.openId
    }
    wx.request({
                    method:'POST',
                    url: 'https://www.askmeany.cn/ama/api',
                    data: data,
                    success:function(res){
                      console.log(res.data);
                    }
                  })



  },
  onLoad: function () {
    var that = this;
    wx.chooseImage({
      success: function(res) {
        var tempFilePaths = res.tempFilePaths
       
        wx.uploadFile({
          url: 'https://www.askmeany.cn/ama/v/do_upload',
          filePath: tempFilePaths[0],
          name: 'userfile',
          formData:{
            'openId':app.globalData.openId
          },
          success: function(res){
            var data = res.data

            //return file id

             that.setData({
          imgs:tempFilePaths,
          openId:app.globalData.openId
        });

            //do something
            console.log(data);
            that.setData({file_id:res.data});
          },
          fail:function(res){
            console.log(res);
          }
        })
      }
    })

   
  }
})
