<?php
	include("connect.php");

	$data = $_POST['value'];
	$obj = json_decode($data);

	$uid = $obj->uid;
	$qid = $obj->qid;
	$cvalue = $obj->cvalue;
	$ctime = date("Y-m-d H:i");

	$sql="INSERT INTO `kcomments`( UID, QID, CVALUE, CTIME) VALUES (".$uid.",
        ".$qid.",'".$cvalue."','".$ctime."');";
    $res = mysql_query($sql);
   	printf("ok");