<?php

/**

AUTHOR: Soteris Demetriou

*/	

function handle_bodymedia_data($con){

	$batteryLevel = $_POST['batteryLevel'];
	$memoryLevel = $_POST['memoryLevel'];
	$stepsToday = $_POST['stepsToday'];
	$stepsYesterday = $_POST['stepsYesterday'];
	$stepsCumulative = $_POST['stepsCumulative'];
	$caloriesToday = $_POST['caloriesToday'];
	$caloriesYesterday = $_POST['caloriesYesterday'];
	$caloriesCumulative = $_POST['caloriesCumulative'];
	$metsToday = $_POST['metsToday'];
	$metsYesterday = $_POST['metsYesterday'];
	$metsCurrent = $_POST['metsCurrent'];
	$moderateActivityToday = $_POST['moderateActivityToday'];
	$moderateActivityYesterday = $_POST['moderateActivityYesterday'];
	$moderateActivityCumulative = $_POST['moderateActivityCumulative'];
	$vigorousActivityToday = $_POST['vigorousActivityToday'];
	$vigorousActivityYesterday = $_POST['vigorousActivityYesterday'];
	$vigorousActivityCumulative = $_POST['vigorousActivityCumulative'];
	$heartbeats = $_POST['heartbeats'];
	$heartRate = $_POST['heartRate'];
	$walkingDistanceToday = $_POST['walkingDistanceToday'];
	$walkingDistanceYesterday = $_POST['walkingDistanceYesterday'];
	$walkingDistanceCumulative = $_POST['walkingDistanceCumulative'];
	$walkingSpeedCurrent = $_POST['walkingSpeedCurrent'];
	
	$query = "INSERT INTO BODYMEDIA VALUES ('" . $batteryLevel . "', '" . $memoryLevel ."','" . $stepsToday . "', '" . $stepsYesterday . "', '" . $stepsCumulative . "', '" . $caloriesToday . "', '" .   $caloriesYesterday . "', '" . $caloriesCumulative . "', '" . $metsToday . "', '" . $metsYesterday . "', '" . $metsCurrent . "', '" . $moderateActivityToday . "', '" . $moderateActivityYesterday . "', '" . $moderateActivityCumulative . "', '" . $vigorousActivityToday . "', '" . $vigorousActivityYesterday . "', '" . $vigorousActivityCumulative . "', '" . $heartbeats . "', '" . $walkingDistanceToday . "', '" . $walkingDistanceYesterday . "', '" . $walkingDistanceCumulative . "', '" . $walkingSpeedCurrent . "' )";
	
	mysqli_query($con, $query);
	
		
}

function handle_iThermometer_data($con){

	$temp = $_POST['temp'];
	echo "Received temperature: " . $temp;
	
	//echo "<input type=\"hidden\" name=\"temp\" value\"".  $_POST['temp'] ."\">";
	$query = "INSERT INTO THERMOMETER (temp) VALUES ('" . $temp . "')";
	echo $query;
	if ($result = mysqli_query($con, $query)){
		echo "SUCCESS" . $result;
	}
	else{
		echo "FAILURE" . $result ;
	}
	
	

}

function handle_pulseOxymeter_data($con){
	$heartrate = $_POST['heartrate'];
	$boxygen = $_POST['boxygen'];
	
	//echo "<input type=\"hidden\" name=\"temp\" value\"".  $_POST['temp'] ."\">";

}

function handle_bloodGlucose_data($con){
        $val = $_POST['val'];
       // echo $val;
      // echo "<input type=\"hidden\" name=\"temp\" value\"".  $_POST['temp'] ."\">";
}

echo "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">
<html xmlns=\"http://www.w3.org/1999/xhtml\">";
echo "<head>";
echo "<title> Attacker.com </title>";
echo "	<script type=\"text/javascript\">
	function submitform(){
		document.getElementById(\"attackform\").submit();
	}	
	
	</script>
	";
echo "</head>";

echo "<body>";



if($_POST['id'] != null){
     $id = $_POST['id'];
	
	$url = "localhost";
	$username = "";
	$password="";
	$database="";
	
	$con = mysqli_connect($url,$username,$password,$database);

	// Check connection
	if (mysqli_connect_errno($con))
	  {
	  echo "Failed to connect to MySQL: " . mysqli_connect_error();
	  }
	
	
	
	//echo "<form action=\"attack.php\" method=\"post\" id=\"attackform\">";
	//echo "<input type=\"hidden\" name=\"id\" value\"".  $_POST['id'] ."\">";
	switch($id){
		case 1:
			//echo "id: " . $id;
			handle_bodymedia_data($con);
			break;
		case 2:
			echo "id: " . $id . "\n";
			handle_iThermometer_data($con);
			break;
		case 3:
			//echo "id: " . $id;
			handle_pulseOxymeter_data($con);
			break;
                case 4:
			//echo "id: " . $id;
			handle_bloodGlucose_data($con);
			break;
		default:
			break;
	}

	mysqli_close($con);
}

echo "</body>";

echo "</http>";

?>
