<?php
	include("connect.php");

	$value = $_POST['value'];
		  
	$obj=json_decode($value);

	$uid = $obj->uid;		  
	$gender = $obj->gender;
	$age = $obj->age;
	$address = $obj->address;
	$status = $obj->status;
	$email = $obj->email;
	$facebook = $obj->facebook;
	$latitude = (float) $obj->latitude;
	$longitude =(float) $obj->longitude;

	$kimg = $obj->kimg;
	if(!empty($kimg)){
		$inputfile = $_POST['img'];
 		$file = fopen("./Userimg/".$kimg , "w");  	  
        $fwflag = fwrite( $file, base64_decode( $inputfile ) );  
        fclose($file);  
	}

	$sql="update `kuser` set usex = '".$gender."',uhead = '".$kimg."',uage = '".$age."',uplace = '".$address."',ulat = '".$latitude."',ulng = '".$longitude."',uexplain = '".$status."',uemail = '".$email."',ufacebook = '".$facebook."'
	 where userid = '".$uid."'";
	$res = mysql_query($sql);
	
	$mselect="select * from `kuser` where userid = '".$uid."'";
	$res = mysql_query($mselect);
   
	$row   = mysql_num_rows($res); 
	if(!empty($row)){
		$arr = array();
		 while($row = mysql_fetch_assoc($res)){
			$arr[] = $row;
		 }
		 die(json_encode($arr));
	}
  

