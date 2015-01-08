<?php
	include("connect.php");

	$value = $_POST['value'];
		  
	$obj=json_decode($value);
		  
	$uname = $obj->uname;
		
	$upassword = $obj->upassword;
	$upassword = md5($upassword);
	
	$mselect="select * from `kuser` where uname = '".$uname."'";
	  
	$res = mysql_query($mselect);
	$row = mysql_num_rows($res); 
	
	if(!empty($res)){
		$ustatus = mysql_result($res, 0, "ustatus"); 
		mysql_free_result($res);
	
		$mselect="select * from `kuser` where uname = '".$uname."' and upass = '".$upassword."'";
		  
		$res = mysql_query($mselect);
	   
		$row = mysql_num_rows($res); 
		if(!empty($row)){
			if($ustatus == '0'){
				printf("noactive");
			}else{
				$arr = array();
				while($row = mysql_fetch_assoc($res)){
					$arr[] = $row;
			 	}
			 	die(json_encode($arr));
			}
			
		}else{
			printf("nopass");
		}
	}else{
	
		printf("nouser");
		  
	}

