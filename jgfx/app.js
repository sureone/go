//app.js
App({
  onLaunch: function () {
    //调用API从本地缓存中获取数据
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    var that = this;

    wx.login({
      success: function(res) {
        if (res.code) {

          var loginCode = res.code;

          wx.getUserInfo({
                withCredentials:true,
                success: function (res) {
                  that.globalData.userInfo = res.userInfo
                   //发起网络请求
                  wx.request({
                    method:'POST',
                    url: 'https://www.askmeany.cn/ama/api/wxlogin/'+loginCode,
                    data: res,
                    success:function(res){
                      console.log(res.data);
                    }
                  })

                }
          })

         
        } else {
          console.log('获取用户登录态失败！' + res.errMsg)
        }
      }
    });
  },
  getUserInfo:function(cb){
    var that = this
    if(this.globalData.userInfo){
      typeof cb == "function" && cb(this.globalData.userInfo)
    }else{
      //调用登录接口
      wx.login({
        success: function () {
          wx.getUserInfo({
            withCredentials:true,
            success: function (res) {
              that.globalData.userInfo = res.userInfo
              typeof cb == "function" && cb(that.globalData.userInfo)
            }
          })
        }
      })
    }
  },
  globalData:{
    userInfo:null,
  }
})