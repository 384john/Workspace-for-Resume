<?php
	include("connect.php");

	$value = $_POST['value'];
		  
	$obj=json_decode($value);
		  
	$latitude = (float)$obj->latitude;
		
	$longitude = (float)$obj->longitude;

	$radius = (float)$obj->radius;

	$mselect="SELECT * FROM `kuser` WHERE GETDISTANCE(".$latitude.",".$longitude.",ULAT,ULNG) < ".$radius;
	
	$res = mysql_query($mselect);
	   
	$row   = mysql_num_rows($res); 
	if(!empty($row)){
		$arr = array();
		while($row = mysql_fetch_assoc($res)){
			$arr[] = $row;
		}
		die(json_encode($arr));
	}else{
			printf("nopass");
	}

	

