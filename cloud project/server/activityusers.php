<?php 
    include("connect.php");

    $data = $_POST['value'];
    $obj = json_decode($data);
    $command = $obj->command;

if($command == "isJoinActivity"){
		$uid = $obj->uid;
		$aid = $obj->aid;
    	$receiveusername = $obj->receiveusername;

    	$sql = "select * from kactivityusers where uid = '".$uid."' and aid = '".$aid."'";
    	//echo("\n".$sql);
		$res = mysql_query($sql);
		$row = mysql_num_rows($res); 
		if(!empty($row)){
			printf("ok");
		}
			
	}else if($command == "joinActivity"){
    		$uid = $obj->uid;
			$aid = $obj->aid;
			$createtime = date("Y-m-d H:i");

			$sql = "INSERT INTO `kactivityusers` 
				(uid, aid, createtime) 
				VALUES 
				(".$uid.",'".$aid."','".$createtime."');";

			$res = mysql_query($sql);
			printf("y");
			$sql = "SELECT `kuser`.* FROM `kactivityusers` inner join `kuser` on `kactivityusers`.`uid`= `kuser`.`userid` 
		     WHERE `kactivityusers`.`aid` = '".$aid."'";

			 $res = mysql_query($sql);
			 $arr = array();
			 while($row = mysql_fetch_assoc($res)){
					$arr[] = $row;
			 }
			 die(json_encode($arr));

	}else if($command == "quitActivity"){
    	$uid = $obj->uid;
    	$aid = $obj->aid;

    	$sql = "delete from kactivityusers where uid = '".$uid."' and aid = '".$aid."'";
		$res = mysql_query($sql);
		printf("n");
		$sql = "SELECT `kuser`.* FROM `kactivityusers` inner join `kuser` on `kactivityusers`.`uid`= `kuser`.`userid` 
		     WHERE `kactivityusers`.`aid` = '".$aid."'";

			 $res = mysql_query($sql);
			 $arr = array();
			 while($row = mysql_fetch_assoc($res)){
					$arr[] = $row;
			 }
			 die(json_encode($arr));

    }else if($command == "getJoinActivityUsersInfo"){
    		$uid = $obj->uid;
    		$aid = $obj->aid;

    		$sql = "select * from kactivityusers where uid = '".$uid."' and aid = '".$aid."'";
	    	//echo($sql);
			$res = mysql_query($sql);
			$row = mysql_num_rows($res); 
			if(empty($row)){
				printf("n");
			}else{
				printf("y");
				mysql_free_result($res);
			}

			$sql = "SELECT `kuser`.* FROM `kactivityusers` inner join `kuser` on `kactivityusers`.`uid`= `kuser`.`userid` 
		     WHERE `kactivityusers`.`aid` = '".$aid."'";
		     //echo("/n".$sql);

			 $res = mysql_query($sql);
			 $arr = array();
			 while($row = mysql_fetch_assoc($res)){
					$arr[] = $row;
			 }
			 die(json_encode($arr));

	}