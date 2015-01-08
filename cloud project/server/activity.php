<?php 
	 include("connect.php");

	 $uid = $_GET['uid'];
	 $mstart = $_GET['start'];
	 $mend = $_GET['end'];

     $sql = "SELECT `kvalue`.*,`kuser`.`USERID`,`kuser`.`UNAME`,`kuser`.`UHEAD`,IFNULL(`T`.`COMMENTCOUNT`,0) AS `COMMENTCOUNT` ,IFNULL(`T2`.`JOINCOUNT`,0) AS `JOINCOUNT` 
FROM `kvalue` inner join `kuser` on `kvalue`.`UID`=`kuser`.`USERID` 
left join (SELECT `kvalue`.QID,COUNT(*) AS `COMMENTCOUNT` FROM `kvalue`,`kcomments` WHERE `kvalue`.`QID` = `kcomments`.`QID` GROUP BY `kvalue`.QID) T on `kvalue`.`QID` = T.`QID` 
left join (SELECT `kvalue`.QID,COUNT(*) AS `JOINCOUNT` FROM `kvalue`,`kactivityusers` WHERE `kvalue`.`QID` = `kactivityusers`.`AID` GROUP BY `kvalue`.QID) T2 on `kvalue`.`QID` = T2.`QID` 
WHERE `kvalue`.ISCHECKDE = 1 
AND `kvalue`.`UID`=".$uid." order by `kvalue`.`QID` desc limit ".$mstart.",5";
	 $res = mysql_query($sql);
	 $arr = array();
	 while($row = mysql_fetch_assoc($res)){
			$arr[] = $row;
	 }
	 die(json_encode($arr));