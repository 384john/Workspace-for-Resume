<?php

    include("connect.php");

    $data = $_POST['value'];
    $obj = json_decode($data);
    $command = $obj->command;

    if($command == "all invitation"){
		$receiveuid = $obj->receiveuid;
		$mstart = $obj->start;
		$sql = "SELECT * FROM `kinvitation` inner join `kuser` on `kinvitation`.`senduid` = `kuser`.`userid` WHERE `receiveuid` = '".$receiveuid."' and `status` = '0' order by `kinvitation`.`invitetime` desc limit ".$mstart.",5";

		//echo $sql;
		$res = mysql_query($sql);
		$arr = array();
		while($row = mysql_fetch_assoc($res)){
			$arr[] = $row;
		}
		die(json_encode($arr));
			
	}else if($command == "send invitation"){
    	$senduid = $obj->senduid;
    	$receiveusername = $obj->receiveusername;

    	$sql = "select userid from kuser where uname = '".$receiveusername."'";
    	//echo("\n".$sql);
		$res = mysql_query($sql);
		$row = mysql_num_rows($res); 
		if(empty($row)){
			printf("not exist username");

		}else {
			$receiveuid = mysql_result($res, 0, "userid"); 
			mysql_free_result($res);

			$sql = "select * from `kfriend` where `uid` = '".$senduid."' and `fid` = '".$receiveuid."'";
			//echo("\n".$sql);
			$res = mysql_query($sql);
			$row = mysql_num_rows($res); 

			if(!empty($row)){
				printf("exist friend");
			}else{
				mysql_free_result($res);

				$sql = "select * from `kinvitation` where `senduid` = '".$senduid."' and `receiveuid` = '".$receiveuid."' and status = '0'";
				//echo("\n".$sql);
				$res = mysql_query($sql);
				$row = mysql_num_rows($res); 
				if(!empty($row)){
					printf("exist invitation");
				}else{
					mysql_free_result($res);
					$invitetime = date("Y-m-d H:i");
					$sql = "INSERT INTO `kinvitation`
					(senduid, receiveuid, invitetime, responsetime, status) 
					VALUES 
					(".$senduid.",'".$receiveuid."','".$invitetime."','','0');";
					$res = mysql_query($sql);
					//echo("\n".$sql);
					printf("ok");
				}
				
			}

		}


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