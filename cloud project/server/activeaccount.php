<?php
	include("connect.php");
 
	$uname = $_GET['uname'];
	$utoken = $_GET['verify'];
	
	$sql = "select utoken_exptime, ustatus from `kuser` where `uname` = '".$uname."' and `utoken` = '".$utoken."'";
	$res = mysql_query($sql);
	$row = mysql_num_rows($res); 
	if(!empty($row)){
		$ustatus = mysql_result($res, 0, "ustatus"); 
		if($ustatus == '1'){
			printf("此用户之前已激活，请不要重复此操作");
		}else{
			$utoken_exptime = mysql_result($res, 0, "utoken_exptime"); 
			if(time() > $utoken_exptime){
				printf('您的激活有效期已过，请登录您的帐号重新发送激活邮件'); 
			}else{
				mysql_free_result($res);
				$sql = "UPDATE `kuser` SET `ustatus` = '1' WHERE `uname` = '".$uname."' and `utoken` = '".$utoken."'";
				$res = mysql_query($sql);
				printf('您的账户现在已激活，请登录'); 
			}
		}

	}else{
		printf('非法操作'); 
	}
			
			