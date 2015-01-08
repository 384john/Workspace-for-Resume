<?php

	 include("connect.php");

	 $uid = $_GET['uid'];
	 $mstart = $_GET['start'];
	 $mend = $_GET['end'];
	 
     $sql="select * from `kchild` where userid = '".$uid."' order by `childid` asc limit ".$mstart.",5";
	 $res = mysql_query($sql);
	 $arr = array();
	 while($row = mysql_fetch_assoc($res)){
			$arr[] = $row;
	 }
	 die(json_encode($arr));
