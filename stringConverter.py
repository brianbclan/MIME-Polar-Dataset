#@author  Chiung-Lun Hung
#@since   02/14/16
#@coures  USC_CSCI599_Hw1
#@version 1

import sys
import re

inputFile = open("mimetype.txt", "r")
outputFile = open("mimeTypeC.txt", "w")

for i, line in enumerate(inputFile):
	output = "{\"name\":"
	line = line.strip()
	name = line.split(':')[0]
	value = line.split(':')[1].replace('\"', '').replace(',', '')
	output = output + name + ",\"value\":" + str("%.2f" %((float(value)/1741530) * 100)) + "}," 
	outputFile.write(output+"\n")
	#print output
	#print line.split(":")[0]