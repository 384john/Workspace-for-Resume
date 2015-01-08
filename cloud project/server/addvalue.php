<?php

	include("connect.php");

    $data = $_POST['value'];
    // $inputfile = $_POST['img'];
    $obj = json_decode($data);
    $uid = $obj->uid;
	$tid = $obj->tid;
	$title = $obj->title;
	$startdate = $obj->startdate;
	$starttime = $obj->starttime;
	$enddate = $obj->enddate;
	$endtime = $obj->endtime;
	$spot = $obj->spot;
	$maxnum = $obj->maxnum;
	$detail = $obj->detail;
	// $qimg = $obj->qimg;
	// $qvalue = $obj->qvalue;
	// $qlike = $obj->qlike;
	// $qunlike = $obj->qunlike;
	// if(!empty($qimg)){
 // $file = fopen("./Valueimg/".$qimg , "w");  	  
 //         $fwflag = fwrite( $file, base64_decode( $inputfile ) );  
 //         fclose($file);  
	// }

	$sql = "INSERT INTO `kvalue`
			(tid, uid, title, startdate, starttime, enddate, endtime, spot, maxnum, detail, ischeckde) 
			VALUES 
			(".$tid.",".$uid.",'".$title."','".$startdate."','".$starttime."','".$enddate."','".$endtime."','".$spot."','".$maxnum."','".$detail."','1');";
    $res = mysql_query($sql);
    printf("ok");