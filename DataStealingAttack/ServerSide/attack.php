<?php
/**

AUTHOR: Soteris Demetriou

*/

echo "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">
<html xmlns=\"http://www.w3.org/1999/xhtml\">";
echo "<head>";
echo "<title> Attacker.com </title>";

echo "</head>";

echo "<body>";
echo "<div style=\"margin: 0 auto; width: 60%; font-size: x-large;\" >";

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
	
	 
	echo "<h1><center>ATTACK.com</center></h1>";
         

	 $query = "SELECT * FROM BODYMEDIA";
	
	$result = mysqli_query($con, $query);
	
        if( mysqli_num_rows($result) >= 1){
           echo "<h2>BODYMEDIA LINK ARMBAND DATA</h2>";
        }

	while($row = mysqli_fetch_array($result))
	  {
		
		echo "<h1>BODYMEDIA DATA</h1>"; 
		echo "batteryLevel = " . $row['batteryLevel'] . "<br />";
		echo "memoryLevel = " .  $_row['memoryLevel'] . "<br />";
		echo "stepsToday = " .  $_row['stepsToday'] . "<br />";
		echo "stepsYesterday" .  $_row['stepsYesterday'] . "<br />";
		echo "stepsCumulative = " .  $_row['stepsCumulative'] . "<br />";
		echo "caloriesToday = " .  $_row['caloriesToday'] . "<br />";
		echo "caloriesYesterday = " .  $_row['caloriesYesterday'] . "<br />";
		echo "caloriesCumulative = ".  $_row['caloriesCumulative'] . "<br />";
		echo "metsToday = " .  $_row['metsToday'] . "<br />";
		echo "metsYesterday = " .  $_row['metsYesterday'] . "<br />";
		echo "metsCurrent = " .  $_row['metsCurrent'] . "<br />";
		echo "moderateActivityToday = " .  $_row['moderateActivityToday'] . "<br />";
		echo "moderateActivityYesterday = " .  $_row['moderateActivityYesterday'] . "<br />";
		echo "moderateActivityCumulative = " .  $_row['moderateActivityCumulative'] . "<br />";
		echo "vigorousActivityToday = " .  $_row['vigorousActivityToday'] . "<br />";
		echo "vigorousActivityYesterday = " .  $_row['vigorousActivityYesterday'] . "<br />";
		echo "vigorousActivityCumulative = " .  $_row['vigorousActivityCumulative'] . "<br />";
		echo "heartbeats = " .  $_row['heartbeats']  . "<br />";
		echo "heartRate = " .  $_row['heartRate'] . "<br />";
		echo "walkingDistanceToday = " .  $_row['walkingDistanceToday'] . "<br />";
		echo "walkingDistanceYesterday = " .  $_row['walkingDistanceYesterday'] . "<br />";
		echo "walkingDistanceCumulative = " .  $_row['walkingDistanceCumulative'] . "<br />";
		echo "walkingSpeedCurrent = " .  $_row['walkingSpeedCurrent'] . "<br />";
		echo "<br>";
	  }
	  
	  
            

	  $query = "SELECT temp FROM THERMOMETER";
	
	$result = mysqli_query($con, $query);

        if( mysqli_num_rows($result) >= 1){
           echo "<h2>iTHERMOMETER DATA</h2>";
        }	

	while($row = mysqli_fetch_array($result))
	  {
                  
		  echo "Temperature: " . $row['temp'];
		  echo "<br>";
	  }
	
	
	mysqli_close($con);
echo "</div>";
echo "</body>";
echo "</html>";

?>
