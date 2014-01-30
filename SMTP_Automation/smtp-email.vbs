dim EmailFrom: EmailFrom = WScript.Arguments.UnNamed(0)
dim EmailFromName: EmailFromName = WScript.Arguments.UnNamed(1)
dim EmailTo: EmailTo = WScript.Arguments.UnNamed(2)
dim SMTPServer: SMTPServer = WScript.Arguments.UnNamed(3)
dim SMTPLogon: SMTPLogon = WScript.Arguments.UnNamed(4)
dim EmailSubject: EmailSubject = WScript.Arguments.UnNamed(5)
dim EmailBody: EmailBody = WScript.Arguments.UnNamed(6)
dim LogLocation: LogLocation = WScript.Arguments.UnNamed(7)

Const cdoSendUsingPickup = 1 	'Send message using local SMTP service pickup directory.
Const cdoSendUsingPort = 2 	'Send the message using SMTP over TCP/IP networking.

Const cdoAnonymous = 0 	' No authentication
Const cdoBasic = 1 	' BASIC clear text authentication
Const cdoNTLM = 2 	' NTLM, Microsoft proprietary authentication

' First, create the message

Set objMessage = CreateObject("CDO.Message")
objMessage.Subject = EmailSubject
objMessage.From = """" & EmailFromName & """ <" & EmailFrom & ">"
objMessage.To = EmailTo
objMessage.TextBody = EmailBody
objMessage.AddAttachment LogLocation

' Second, configure the server

objMessage.Configuration.Fields.Item _
("http://schemas.microsoft.com/cdo/configuration/sendusing") = 2

objMessage.Configuration.Fields.Item _
("http://schemas.microsoft.com/cdo/configuration/smtpserver") = SMTPServer

objMessage.Configuration.Fields.Item _
("http://schemas.microsoft.com/cdo/configuration/smtpauthenticate") = cdoBasic

objMessage.Configuration.Fields.Item _
("http://schemas.microsoft.com/cdo/configuration/sendusername") = SMTPLogon

objMessage.Configuration.Fields.Item _
("http://schemas.microsoft.com/cdo/configuration/smtpconnectiontimeout") = 15

objMessage.Configuration.Fields.Update

' Lastly, send the message

objMessage.Send
