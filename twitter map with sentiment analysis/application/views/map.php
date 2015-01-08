<!DOCTYPE html>
<html>
  <head>
    <style type="text/css">
      html {
        height: 100%
      }
      body {
        height: 100%
      }
    </style>

    <link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css">
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?libraries=visualization"></script>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js" ></script>
    <script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
    <script src="http://d3js.org/d3.v3.min.js"></script>
  </head>

  <body>
    <script type="text/javascript">
      var map;
      var arrLat = new Array();
      var arrLon = new Array();
      var arrTime = new Array();
      var arrKw = new Array();
      var taxiData = [];
      var newTaxiData = [];

      var markersArray = [];
      var filterArray = [];
      var t, count;
      var flag = false;

      $(document).ready(function() {
        $("#selectChange").change(function() {
          var currentOpt = $(this).val();
          currentOpt = currentOpt.trim();

          if (currentOpt != "") {
            flag = true;
            filter(currentOpt);
          } else {
            flag = false;
            initialize();
          }
        });
      });

      function initialize() {
        // alert($("#selectChange").val());
        var mapOptions = {
          center: new google.maps.LatLng(28.201970, 2.500008),
          zoom: 2,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };

        map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);

        getData();

        if (!flag) {
          for (var i = 0; i < arrLat.length; i++) {
            taxiData.push(new google.maps.LatLng(arrLat[i], arrLon[i]));
            newTaxiData.push([arrLat[i], arrLon[i], arrKw[i]]);
          }
        }

        generateHeatMap();
        generateMarkers();
      }

      function getData() {
        console.log("<?php foreach($d as $data): ?>");
        arrLat.push("<?php echo $data['lat']; ?>");
        arrLon.push("<?php echo $data['lon']; ?>");
        arrTime.push("<?php echo $data['time']; ?>");
        arrKw.push("<?php echo $data['keyword']; ?>");
        console.log("<?php endforeach; ?>");
      }

      function generateHeatMap() {
        var pointArray = new google.maps.MVCArray(taxiData);
        var heatmap = new google.maps.visualization.HeatmapLayer({
          data: pointArray
        });

        heatmap.setMap(map);
      }

      function generateMarkers() {
        t = taxiData.length;
        count = 0;

        addMarkers();
        setTimeout(function() {
          showMarkers();
        }, 2000);
      }

      function addMarkers() {
        for (var i = 0; i < taxiData.length; i++) {
          var marker = new google.maps.Marker({
            position: taxiData[i]
          });

          markersArray.push(marker);
        }
      }

      function showMarkers() {
        if (markersArray.length > 0) {
          markersArray[count].setMap(map);
          count++;
          
          if (count > t - 1) {
            deleteMarkers();
          } else {
            setTimeout("showMarkers()", 500);
          }
        }
      }

      // Deletes all markers in the array by removing references to them
      function deleteMarkers() {
        if (markersArray.length > 0) {
          for (i in markersArray) {
            markersArray[i].setMap(null);
          }

          markersArray.length = 0;
          generateMarkers();
        }
      }

      function filter(val) {
        filterArray = [];

        if (newTaxiData.length > 0) {
          for (var i = 0; i < newTaxiData.length; i++) {
            if (newTaxiData[i][2].indexOf(val) != -1) {
              filterArray.push(new google.maps.LatLng(newTaxiData[i][0], newTaxiData[i][1]));
            }
          }

          taxiData = filterArray;
          initialize();
        }
      }

      google.maps.event.addDomListener(window, "load", initialize);
    </script>

    <div style="width: 60%; height: 120px; margin-left: 20%; margin-bottom: 40px">
      <div style="padding: 20px">
        <h1 style="display: inline-block; margin-left: 42%">TwittMap</h1>
        <h5 style="display: inline-block; margin-left: 10px; color: #ccc">A live map of tweet streaming</h5>
      </div>
      <div style="margin-left: 20%">
        <p style="display: inline-block; font-size: 12pt; padding: 10px">Keyword Filter</p>
        <select id="selectChange" class="form-control" style="width: 150px; display: inline-block">
          <option value="" selected>----- ALL -----</option>
          <option value="time">time</option>
          <option value="you">you</option>
          <option value="my">my</option>
          <option value="and">and</option>
        </select>
        <input type="button" class="btn btn-primary" value="Clear Markers" style="width: 150px; display: inline-block; margin-left: 10px" 
        onclick="deleteMarkers()">
      </div>
    </div>
    <div id="map-canvas" style="height: 70%; width: 60%; margin-left: 20%"></div>
  </body>
</html>