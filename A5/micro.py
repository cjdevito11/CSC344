import os
import subprocess
import smtplib
import zipfile
import tempfile
import re

strTable = "<html> <center> <font size='2'> <table> <tr> <th> 344 Assignemnts </th> </tr>"
workDir = "/Users/christiandevito/Desktop/programs/CSC344"
#workDir = raw_input("Choose Directory: ")

for root, dir, files in os.walk(workDir):
	aDir = root.replace(workDir + "/", '')
	aHtml = root + "/subDir_" + aDir + ".html"
	if aHtml == "/Users/christiandevito/Desktop/programs/CSC344/subDir_/Users/christiandevito/Desktop/programs/CSC344.html":
		i = 2
	else:
		strRW = "<tr><th><a href=" + aHtml + ">" + aDir + "</a></th></tr>"
		strTable = strTable + strRW
		aTable = "<html><center><table><tr><th>" + aDir + " Page</th></tr>"
		aRw = ""
		for file in files:
			if file.endswith('.DS_Store') or file.endswith('.swp') or file.endswith('.html'):
				i = 1
			else:
				out = subprocess.Popen(['wc', '-l', os.path.join(root, file)])
				strRW = "<tr><th>" + out + "</th></tr>"
				#print file
				if file.endswith('.txt'):
					i = 1
				else:
					data = open(root + "/" + file)
					target = open('../' + aDir + '/' + file + 'variables.txt','w')
					for line in data:
						line = line.rstrip()
						if file.endswith('.c'):
							if re.search('char', line) or re.search('struct', line): # or re.search('int' , line):
								target.write(line + "\n")
						if file.endswith('.clj'):
							if re.search('defn', line):
								target.write(line + "\n")
						if file.endswith('.scala'):
							if re.search('object', line) or re.search('class', line) or re.search('def', line) or re.search('var', line):
								target.write(line + "\n")
						if file.endswith('.pl'):
							if re.search(':-', line):
								target.write(line + "\n")
						if file.endswith('.py'):
							if re.search('def', line):
								target.write(line + "\n")
								
				aRw = "<tr><td><a href=" + root + "/" +  file + ">" + file + "</a></td></tr>"
				aTable = aTable + aRw
		aTable = aTable + "</table><a href='../test.html'><~~~ Back</a></center></html>"
		hs = open(aHtml, 'w')
		hs.write(aTable)

			#strRW = "<tr><td><a href=" + root + ">" + root.replace(workDir + "/", '') + "</a></td><td><a href=" + root + "/" + file + ">" + file + "</a></td></tr>"
			#strTable = strTable + strRW

strTable = strTable + "</center></table></html>"

hs = open("../test.html", 'w')
hs.write(strTable)
#print strTable

#(int.*|char.*)\;