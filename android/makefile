all:build
debug:
	ant debug
build:
	ant release
	./sign
	adb install -r goapp_publish.apk
sign:goapp_publish.apk
	./sign
install:
	adb install -r goapp_publish.apk
clean:
	find . -name "._*" -exec rm -f {} \;
	find . -name "*.orig" -exec rm -f {} \;
	find . -name "*.swap" -exec rm -f {} \;
	find . -name "*.*~" -exec rm -f {} \;
	rm -rf gen bin 
	

