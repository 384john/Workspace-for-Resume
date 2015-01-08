<?php 
	include("connect.php");

	$mqid = $_GET['qid'];
	$mstart = $_GET['start'];
	$mend = $_GET['end'];

	$sql = "select `kcomments`.* , `kuser`.`UNAME` , `kuser`.`UHEAD` from `kcomments` , `kuser` where `kcomments`.`uid` = `kuser`.`USERID` and `kcomments`.`qid` = ".$mqid." order by `kcomments`.`cid` desc LIMIT ".$mstart.",5";
	//echo $sql;
	$res = mysql_query($sql);
	$arr = array();
	while($row = mysql_fetch_assoc($res)){
		$arr[] = $row;
	}
	die(json_encode($arr));


