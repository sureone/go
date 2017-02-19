import os
import re
import shutil

def batch_rename(appendName):
	for dirname, dirnames, filenames in os.walk('.'):
		#for subdirname in dirnames:
		#    print os.path.join(dirname, subdirname)
		for filename in filenames:
			print os.path.join(dirname, filename)
			src_file = os.path.join(dirname, filename)
			dest_file = os.path.join(dirname, appendName + filename)
			shutil.copyfile(src_file, dest_file)

batch_rename('yunzi2_')					