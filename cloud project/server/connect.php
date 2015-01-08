<?php

    error_reporting(E_ALL ^ E_DEPRECATED);
    $host="nyu-lin-2.cq3bkrwi7ogk.us-east-1.rds.amazonaws.com";
	$db_user="root";//user
	$db_pass="rootroot";//password
	$db_name="nyulink";//database
	$timezone="America/New_York";

    $link = mysql_connect($host,$db_user,$db_pass);
	mysql_select_db($db_name,$link);
   	mysql_query('set names utf8');
   	header("Content-Type: text/html; charset=utf-8");
	date_default_timezone_set($timezone); //New York time
?>