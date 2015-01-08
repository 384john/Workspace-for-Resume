<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Map extends CI_Controller {
	public function __construct() {
		parent::__construct();
	}

	public function index() {
		$result = array();

        $filePath = APPPATH . 'data/tweets.csv';
        $file = fopen($filePath, "r");

        while (!feof($file)) {
        	$row = fgetcsv($file);

            $kw = $this->_parseKeyword($row[1]);
        	$lon = $row[2];
        	$lat = $row[3];
        	$time = $row[5];

            if (is_null($kw)) {
                $kw = ' ';
            }

        	$row_new = array('lat' => $lat, 'lon' => $lon, 'time' => $time, 'keyword' => $kw);
        	array_push($result, $row_new);
        }

        fclose($file);
        // print_r($result);
        $data = array('d' => $result);

		$this->load->view('map', $data);
	}

    private function _parseKeyword($str) {
        $arr = array(' time ', ' you ', ' my ', ' and ');
        $result = null;

        for ($i = 0; $i < sizeof($arr); $i++) {
            $pos = strpos($str, $arr[$i]);

            if ($pos !== false) {
                $result .= trim($arr[$i]) . ', ';
            }
        }

        return $result;
    }
}

?>