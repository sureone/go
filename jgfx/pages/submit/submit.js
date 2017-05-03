//logs.js
var util = require('../../utils/util.js')
Page({
  data: {
    imgs: []
  },
  onLoad: function () {
    var that = this;
    wx.chooseImage({
      success: function(res) {
        var tempFilePaths = res.tempFilePaths
        that.setData({
          imgs:tempFilePaths
        });
        wx.uploadFile({
          url: 'https://www.askmeany.cn/ama/v/do_upload', //仅为示例，非真实的接口地址
          filePath: tempFilePaths[0],
          name: 'userfile',
          formData:{
            'user': 'test'
          },
          success: function(res){
            var data = res.data
            //do something
            console.log(data);
          },
          fail:function(res){
            console.log(res);
          }
        })
      }
    })

   
  }
})
