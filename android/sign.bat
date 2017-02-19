@Rem android签名程序//注释指令

@Rem echo是显示指令 格式：echo [{on|off}] [message]
@echo **********************************************************
@echo android签名工具
@echo **********************************************************

@Rem 文件是否存在命令格式：if exist 路径+文件名 命令 
@if exist android.keystore goto sign

@echo 创建签名文件android.keystore

@Rem keytool命令格式：-genkey产生签名 -alias别名 -keyalg加密算法 -validity有效天数 -keystore生产签名文件名称
keytool -genkey -alias android.keystore -keyalg RSA -validity 40000 -keystore android.keystore
@echo 开始签名：

@Rem jarsigner命令格式：-verbose输出详细信息 -keystore密钥库位置 -signedjar要生成的文件 要签名的文件 密钥库文件
jarsigner -verbose -keystore android.keystore -signedjar goapp_signed.apk bin\goapp-unsigned.apk sureone
@goto over

:sign
@echo 开始签名：
jarsigner -verbose -keystore android.keystore -signedjar goapp_signed.apk bin\goapp-unsigned.apk sureone

:over
@echo ********************android.apk 签名完成！************************
pause

