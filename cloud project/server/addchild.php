<?php

	include("connect.php");

    $data = $_POST['value'];
    $obj = json_decode($data);

    $uid = $obj->uid;
	$gender = $obj->gender;
	$name = $obj->name;
	$age = $obj->age;
	$hobbies = $obj->hobbies;
	$detail = $obj->detail;

	$kimg = $obj->kimg;

	if(!empty($kimg)){
		$inputfile = $_POST['img'];
 		$file = fopen("./Userimg/".$kimg , "w");  	  
        $fwflag = fwrite( $file, base64_decode( $inputfile ) );  
        fclose($file);  
	}

	$sql = "INSERT INTO `kchild`
			(userid, cname, chead, cage, chobbies, cdetail, csex) 
			VALUES 
			(".$uid.",'".$name."','".$kimg."','".$age."','".$hobbies."','".$detail."','".$gender."');";
    $res = mysql_query($sql);

    printf("ok");