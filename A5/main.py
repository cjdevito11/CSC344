import os
import os.path
import subprocess
import smtplib
import zipfile
import tempfile
import re
from email import encoders
from email.message import Message
from email.mime.base import MIMEBase
from email.mime.multipart import MIMEMultipart  


# Remove duplicate elements 
def Remove(duplicate): 
    final_list = [] 
    for num in duplicate: 
        if num not in final_list: 
            final_list.append(num) 
    return final_list 


def getVariables(workDir):
	finalresult = ""
	cCommentRegex = re.compile("//.*")
	cljCommentRegex = re.compile(";.*")
	cStringRegex = re.compile('\".*\"|{|}|\(|\)|^\s*|=|<|>|\[|\]|^|%|@|$|#|!|&&|\#|\:|,|[0-9]|\*|\.')
	#regex = re.compile("[a-z.*A-Z.*]")
	
	
	#workDir = "/Users/christiandevito/Desktop/programs/CSC344"
	#workDir = raw_input("Choose Directory: ")
	for root, dir, files in os.walk(workDir):
		aDir = root.replace(workDir + "/", '')
		for file in files:
			if file.endswith('.DS_Store') or file.endswith('.swp') or file.endswith('.html') or file.endswith('.txt')or file.endswith('.zip'):
					i = 1
			else:
				data = open(root + "/" + file)
				target = open('../' + aDir + '/' + file + '_Variables.txt','w')
				finalresult = ""
				result = ""
				for line in data:
					line = line.rstrip()
					if file.endswith('.c'):
						result = cCommentRegex.sub(' ',line);
						result = cStringRegex.sub(' ',result);
						finalresult = finalresult + result

					if file.endswith('.clj'):
					
						result = cljCommentRegex.sub(' ',line);
						result = cStringRegex.sub(' ',result);
						finalresult = finalresult + result
						#if re.search('defn', line):
							#target.write(line + "\n")
					if file.endswith('.scala'):
						result = cCommentRegex.sub(' ',line);
						result = cStringRegex.sub(' ',result);
						finalresult = finalresult + result
					
					if file.endswith('.pl'):
						result = cCommentRegex.sub(' ',line);
						result = cStringRegex.sub(' ',result);
						finalresult = finalresult + result
						
					if file.endswith('.py'):
						result = cCommentRegex.sub(' ',line);
						result = cStringRegex.sub(' ',result);
						finalresult = finalresult + result
						
					
				
				finalresult = re.split(' ', str(finalresult))
				#print(finalresult)
				finalresult = Remove(finalresult)
				target.write(str(finalresult) + "\n")

def writeHTML():
	strTable = "<html> <center> <font size='2'> <table> <tr> <th> 344 Assignemnts </th> </tr>"
	absFilePath = os.path.abspath(__file__)
	#print(absFilePath)
	fileDir = os.path.dirname(os.path.abspath(__file__))
	#print(fileDir)
	parentDir = os.path.dirname(fileDir)

	for root, dir, files in os.walk(parentDir):
		
		aDir = root.replace(parentDir + "/", '')
		#print("root : " + root)
		#print "aDir : " + aDir
		if root == aDir:
			i = 2
		#if aHtml == "/home/cdevito/Users/christiandevito/Desktop/programs/CSC344/subDir_/home/cdevito/Users/christiandevito/Desktop/programs/CSC344.html":
		else:
			strHtml = "subDir_" + aDir + ".html"
			aHtml = "../" + aDir + "/" + strHtml
			print strHtml
			strRW = "<tr><th><a href=" + root + "/" + strHtml + ">" + aDir + "</a></th></tr>"
			strTable = strTable + strRW
			aTable = "<html><center><table><tr><th>" + aDir + " Page</th></tr>"
			aRw = ""
			for file in files:
				if file.endswith('.DS_Store') or file.endswith('.swp') or file.endswith('.html'):
					i = 1
				else:
					out = subprocess.Popen(['wc', '-l', os.path.join(root, file)])
					
					aRw = "<tr><td><a href=" + root + "/" +  file + ">" + file + "</a></td></tr>"
					aTable = aTable + aRw
			aTable = aTable + "</table><a href='../test.html'><~~~ Back</a></center></html>"
			hs = open(aHtml, 'w')
			hs.write(aTable)
	strTable = strTable + "</center></table></html>"
	hs = open("../test.html", 'w')
	hs.write(strTable)

def sendMail(workDir):
	print("here")
	sender = "cdevito@oswego.edu"
	emailname = ""
	emailloc = ""
	emailfull = ""
	emailname = raw_input("Enter email before @: ")    
	emailloc = raw_input("Enter email server: ")
	emailend = raw_input("Enter end: ")
	emailfull = (emailname + "@" + emailloc + "." + emailend)
	
	zfile = 'csc344.zip'
	
	zf = tempfile.TemporaryFile(prefix='mail', suffix='.zip')
	with zipfile.ZipFile(zfile, 'w') as myzip:
		#workDir = "/Users/christiandevito/Desktop/programs/CSC344"
		#workDir = raw_input("Choose Directory: ")
		for root, dir, files in os.walk(workDir):
			for file in files:
				if file.endswith('.c') or file.endswith('.clj') or file.endswith('.py') or file.endswith('.pl') or file.endswith('.scala') or file.endswith('.txt') or file.endswith('.html'):
					myzip.write(os.path.join(root, file))
					zf = myzip
		myzip.close()
		zf = open(zfile,'rb')
	print("zipped")
	themsg = MIMEMultipart()
	themsg['Subject'] = 'File %s' % zfile
	themsg['To'] = ', '.join(emailfull)
	themsg['From'] = sender
	themsg.preamble = 'I am not using a MIME-aware mail reader.\n'
	msg = MIMEBase('application', 'zip')
	msg.set_payload(zf.read())
	encoders.encode_base64(msg)
	msg.add_header('Content-Disposition', 'attachment', filename=zfile)
	themsg.attach(msg)
	themsg = themsg.as_string()
	# send the message
	print("Sending Email")
	smtp = smtplib.SMTP()
	smtp.connect()
	smtp.sendmail(sender, emailfull, themsg)
	smtp.close()
	print("Email Sent")




#newPath = os.path.join(parentDir, 'StringFunctions')
#print(newPath)


workDir = raw_input("Choose Directory: ")
getVariables(workDir)
writeHTML()
sendMail(workDir)

