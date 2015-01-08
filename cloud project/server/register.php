<?php
	include("connect.php");

    $data = $_POST['value'];
    $obj = json_decode($data);
    $command = $obj->command;

    if($command == "checkUser"){
		$uname = $obj->uname;
		$sql = "select ustatus from `kuser` where uname = '".$uname."'";
		$res = mysql_query($sql);
		$row = mysql_num_rows($res); 
		if(!empty($row)){
			$ustatus = mysql_result($res, 0, "ustatus"); 
			printf("existUserAndStatusIs".$ustatus);

		}else{
			mysql_free_result($res);
			$uemail = $obj->uemail;
			$sql = "SELECT uemail FROM  `kuser` WHERE `uemail` = '".$uemail."'";
			$res = mysql_query($sql);
			$row = mysql_num_rows($res); 
			if(!empty($row)){
				printf("existEmail");
			}else{
				printf("ok");
			}
		}
			
	}else if($command == "addUser"){
    	$uname = $obj->uname;
		$upassword = $obj->upassword;
		$uemail = $obj->uemail;

		$upassword = md5($upassword);
		$utime = date("Y-m-d H:i");

		$nowtime = time();
		$utoken = md5($uname.$upassword.$nowtime); //创建用于激活识别码 
		$utoken_exptime = $nowtime+60*60*24;//过期时间为24小时后 

    	$sql="INSERT INTO `kuser`( uname, upass, uemail, utime, utoken, utoken_exptime, ustatus) VALUES ('".$uname."','".$upassword."','".$uemail."','".$utime."','".$utoken."','".$utoken_exptime."','0');";
		$res = mysql_query($sql);

		include("gmail.php");

		//send the message, check for errors
		if (!($mail->send())) {
		    printf("Mailer Error: " . $mail->ErrorInfo);
		} else {
		    printf("Message sent");
		}
		//printf("ok");  

    }else if($command == "repeatSendEmail"){
			$uname = $obj->uname;
			$upassword = $obj->upassword;
			$upassword = md5($upassword);

			$sql = "select uemail, ustatus from `kuser` where uname = '".$uname."' and upass = '".$upassword."'";
			$res = mysql_query($sql);
			$row = mysql_num_rows($res); 
			if(!empty($row)){
				$ustatus = mysql_result($res, 0, "ustatus"); 
				$uemail = mysql_result($res, 0, "uemail"); 
				if($ustatus == '1'){
					printf('actived'); 
				}else{
					mysql_free_result($res);
					$nowtime = time();
					$utoken = md5($uname.$upassword.$nowtime); //创建用于激活识别码 
					$utoken_exptime = $nowtime+60*60*24;//过期时间为24小时后 
					
					include("gmail.php");
					//send the message, check for errors

					if (!($mail->send())) {
					    printf("Mailer Error: " . $mail->ErrorInfo);
					} else {
						$sql = "UPDATE `kuser` SET `utoken` = '".$utoken."', `utoken_exptime` = '".$utoken_exptime."' WHERE `uname` = '".$uname."'";
						$res = mysql_query($sql);

					    printf("Message sent");
					}
				}

			}else{
				printf('username and password are error'); 
			}
		
	}
?>