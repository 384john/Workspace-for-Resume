<?php 
	include("connect.php");
	
	$sql = "SELECT * FROM `kuser`";
	$res = mysql_query($sql);
	$arr = array();
	while($row = mysql_fetch_assoc($res)){
		$arr[] = $row;
	}
	die(json_encode($arr));


