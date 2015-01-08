<?php
/**
 * This example shows settings to use when sending via Google's Gmail servers.
 */

//SMTP needs accurate times, and the PHP time zone MUST be set
//This should be done in your php.ini, but this is how to do it if you don't have access to that
//date_default_timezone_set('Etc/UTC');

require 'PHPMailer/PHPMailerAutoload.php';

//Create a new PHPMailer instance
$mail = new PHPMailer;

//Tell PHPMailer to use SMTP
$mail->isSMTP();

//Enable SMTP debugging
// 0 = off (for production use)
// 1 = client messages
// 2 = client and server messages
$mail->SMTPDebug = 0;

//Ask for HTML-friendly debug output
// $mail->Debugoutput = 'html';

//Set the hostname of the mail server
$mail->Host = 'smtp.gmail.com';

//Set the SMTP port number - 587 for authenticated TLS, a.k.a. RFC4409 SMTP submission
$mail->Port = 587;

//Set the encryption system to use - ssl (deprecated) or tls
$mail->SMTPSecure = 'tls';

//Whether to use SMTP authentication
$mail->SMTPAuth = true;

//Username to use for SMTP authentication - use full email address for gmail
$mail->Username = "nyulinksystem@gmail.com";


//Password to use for SMTP authentication
$mail->Password = "QWERTYUIOP123456";


//Set who the message is to be sent from
$mail->setFrom('hs1942@nyu.edu', 'NYULinkSystem');

//Set an alternative reply-to address
// $mail->addReplyTo('replyto@example.com', 'First Last');

//Set who the message is to be sent to
$mail->addAddress($uemail, '');

//Set the subject line
$mail->Subject = 'NYULink Account Activie Mail';
$activeUrl = "http://172.16.14.6/NYULink/activeaccount.php?uname=".$uname."&verify=".$utoken;

//Read an HTML message body from an external file, convert referenced images to embedded,
//convert HTML into a basic plain-text alternative body
//****$mail->msgHTML(file_get_contents('contents.html'), dirname(__FILE__));

//Replace the plain text body with one created manually
$mail->AltBody = "text/html";
$mail->Body =  "
<html><head>
<meta http-equiv=\"Content-Language\" content=\"zh-cn\">
<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GB2312\">
</head>
<body>
        NYULink account active link：<a href=\"".$activeUrl."\">".$activeUrl."</a> <br /><br />
Thinks！<br /><br />
</body>
</html>
";                
//"pleast enter : <a>http://192.168.1.8:80/NYULink/activeaccount?uid=".$uid."&verify=".$utoken."</a>";
//Attach an image file
// $mail->addAttachment('images/phpmailer_mini.png');


