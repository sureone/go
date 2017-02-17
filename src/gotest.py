import goapi
go=goapi.logicgo_init();
print go
goapi.setSize(go,19);
goapi.startGamePy(go);
buf=goapi.buffer();
goapi.dumpToHex(go,buf);
print buf.size;
print buf.ptr;
