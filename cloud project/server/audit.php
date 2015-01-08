<?php 
	include("connect.php");

	$mstart = $_GET['start'];
	$mend = $_GET['end'];
	
	$sql = "SELECT `kvalue`.*,`kuser`.`USERID`,`kuser`.`UNAME`,`kuser`.`UHEAD` FROM `kvalue` ,`kuser` WHERE `kvalue`.`UID`=`kuser`.`USERID` AND `kvalue`.`ISCHECKDE` = 0 order by `kvalue`.`QID` desc limit ".$mstart.",5";
	 
	$res = mysql_query($sql);
	$arr = array();
	while($row = mysql_fetch_assoc($res)){
		$arr[] = $row;
	}
	die(json_encode($arr));


