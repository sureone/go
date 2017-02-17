#!/usr/bin/env python
from distutils.core import setup,Extension
setup(name='goapi',
	ext_modules=[Extension('_goapi',sources=['goapi_wrap.c','goapi.cc','logicgo.cc','buffer.c'])],
	py_modules=["goapi"],
	version='0.2',
	author='sureone@gmail.com',
	description="""go logic wrapper""",)

