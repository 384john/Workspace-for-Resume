<?php

    include("connect.php");

    $data = $_POST['value'];
    $obj = json_decode($data);
    $command = $obj->command;

    if($command == "isFriendAndGetUserInfoById"){
		$uid = $obj->uid;
		$fid = $obj->fid;

		$sql = "select * from `kfriend` where `uid` = '".$uid."' and `fid` = '".$fid."'";
		$res = mysql_query($sql);
		$row = mysql_num_rows($res); 

		if(empty($row)){
			printf("n");
		}else{
			printf("y");
		}

		mysql_free_result($res);

		$sql = "SELECT * FROM `kuser` WHERE `userid` = '".$fid."'";
		//echo($sql);
		$res = mysql_query($sql);
		$arr = array();
		while($row = mysql_fetch_assoc($res)){
			$arr[] = $row;
		}
		die(json_encode($arr));
		
	}else if($command == "isFriendAndGetUserInfoByUsername"){
		$uid = $obj->uid;
		$username = $obj->username;

		$sql = "select * from `kfriend` where `uid` = '".$uid."' and `fid` = (SELECT userid FROM `kuser` WHERE `uname` = '".$username."')";
		$res = mysql_query($sql);
		$row = mysql_num_rows($res); 

		if(empty($row)){
			printf("n");
		}else{
			printf("y");
		}

		mysql_free_result($res);

		$sql = "SELECT * FROM `kuser` WHERE `uname` = '".$username."'";
		//echo($sql);
		$res = mysql_query($sql);
		$arr = array();
		while($row = mysql_fetch_assoc($res)){
			$arr[] = $row;
		}
		die(json_encode($arr));
		
	}else if($command == "isFriendByUsername"){
		$uid = $obj->uid;
		$username = $obj->username;

		$sql = "select * from `kfriend` where `uid` = '".$uid."' and `fid` = '".$fid."'";
		$res = mysql_query($sql);
		$row = mysql_num_rows($res); 

		if(!empty($row)){
			printf("ok");
		}
		
	}else if($command == "getFriendInfoByID"){
    	$uid = $obj->uid;
    	$fid = $obj->fid;

		$sql = "SELECT * FROM `kfriend` inner join `kuser` on `kfriend`.`fid` = `kuser`.`userid` 
		WHERE `uid` = '".$uid."' and `fid` = '".$fid."'";
		//echo($sql);
		$res = mysql_query($sql);
		$arr = array();
		while($row = mysql_fetch_assoc($res)){
			$arr[] = $row;
		}
		die(json_encode($arr));

    }else if($command == "getFriendInfoByUsername"){
    	$uid = $obj->uid;
    	$username = $obj->username;

		$sql = "SELECT * FROM `kfriend` inner join `kuser` on `kfriend`.`fid` = `kuser`.`userid` 
		WHERE `kfriend`.`uid` = '".$uid."' and `kuser`.`uname` = '".$username."'";
		$res = mysql_query($sql);
		$arr = array();
		while($row = mysql_fetch_assoc($res)){
			$arr[] = $row;
		}
		die(json_encode($arr));
//****************************************
    }else if($command == "respond invitation"){
    		$uid = $obj->uid;
    		$fid = $obj->fid;
			$id = $obj->id;
			$status = $obj->status;
			$responsetime = date("Y-m-d H:i");

			$sql = "UPDATE `kinvitation` SET `responsetime` = '".$responsetime."', `status` = '".$status."' WHERE `ID` = '".$id."'";
			//echo $sql;
			$res = mysql_query($sql);
			if($status == "1"){
				$sql = "INSERT INTO `kfriend` 
				(uid, fid, createtime) 
				VALUES 
				(".$uid.",'".$fid."','".$responsetime."');";

				$res = mysql_query($sql);

				$sql = "INSERT INTO `kfriend` 
				(uid, fid, createtime) 
				VALUES 
				(".$fid.",'".$uid."','".$responsetime."');";

				$res = mysql_query($sql);
			}
			printf("ok");

	}else if($command == "all friend"){
			$uid = $obj->uid;
			$mstart = $obj->start;

			$sql = "SELECT * FROM `kfriend` inner join `kuser` on `kfriend`.`fid` = `kuser`.`userid` 
			WHERE `uid` = '".$uid."' order by `kfriend`.`createtime` desc limit ".$mstart.",5";
			//echo($sql);
			$res = mysql_query($sql);
			$arr = array();
			while($row = mysql_fetch_assoc($res)){
				$arr[] = $row;
			}
			die(json_encode($arr));
			
	}else if($command == "select friend"){
			$userid = $obj->userid;

			$sql = "SELECT * FROM `kuser` WHERE `userid` = '".$userid."'";
			$res = mysql_query($sql);
			$arr = array();
			while($row = mysql_fetch_assoc($res)){
				$arr[] = $row;
			}
			die(json_encode($arr));
			
	}else if($command == "del friend"){
			$uid = $obj->uid;
			$fid = $obj->fid;


			$sql = "DELETE FROM `kfriend` WHERE (`uid` = '".$uid."' AND `fid` = '".$fid."') or (`uid` = '".$fid."' AND `fid` = '".$uid."')";
			//echo $sql;
			$res = mysql_query($sql);
			printf("ok");
	}