@Rem androidǩ������//ע��ָ��

@Rem echo����ʾָ�� ��ʽ��echo [{on|off}] [message]
@echo **********************************************************
@echo androidǩ������
@echo **********************************************************

@Rem �ļ��Ƿ���������ʽ��if exist ·��+�ļ��� ���� 
@if exist android.keystore goto sign

@echo ����ǩ���ļ�android.keystore

@Rem keytool�����ʽ��-genkey����ǩ�� -alias���� -keyalg�����㷨 -validity��Ч���� -keystore����ǩ���ļ�����
keytool -genkey -alias android.keystore -keyalg RSA -validity 40000 -keystore android.keystore
@echo ��ʼǩ����

@Rem jarsigner�����ʽ��-verbose�����ϸ��Ϣ -keystore��Կ��λ�� -signedjarҪ���ɵ��ļ� Ҫǩ�����ļ� ��Կ���ļ�
jarsigner -verbose -keystore android.keystore -signedjar goapp_signed.apk bin\goapp-unsigned.apk sureone
@goto over

:sign
@echo ��ʼǩ����
jarsigner -verbose -keystore android.keystore -signedjar goapp_signed.apk bin\goapp-unsigned.apk sureone

:over
@echo ********************android.apk ǩ����ɣ�************************
pause

