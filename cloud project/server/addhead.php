<?php
	include("connect.php");

	$value = $_POST('value');
    $obj=json_decode($value);
	$id = $obj->id;
    $inputfile = $obj->img;
    $outputfile = "./nyulink/Userimg/".$obj->imgname . ".png";
	$file = fopen( $outputfile, "w" );  
    $fwflag = fwrite( $file, base64_decode( $inputfile ) );  
    fclose($file);  
    if ($fwflag > 0) {
	    $sql="UPDATE `kuser` SET `UHEAD`= `".$obj->imgname."` WHERE `kuser`.`userid`=".$id;
        $res = mysql_query($sql);
        printf("ok");
    }else{
    	printf("no");
    }