<?php
	include("connect.php");

	$value = $_POST['value'];
		  
	$obj=json_decode($value);
		  
	$uname = $obj->uname;
		
	$upassword = $obj->upassword;
	$uemail = $obj->uemail;
	
	$ctime = date("Y-m-d H:i");
		  
	$mselect="select * from `kuser` where uname = '".$uname."'";
	  
	$res = mysql_query($mselect);
	   
	$row   = mysql_num_rows($res); 
	if(empty($row)){
	
		$sql="INSERT INTO `kuser`( uname, upass, utime) VALUES ('".$uname."','".$upassword."','".$ctime."');";
		$res = mysql_query($sql);
	  
		printf("ok");      
	}else{
	
		printf("no");
		  
	}
		  
	die();

