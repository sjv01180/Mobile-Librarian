<?php
require 'db_config.php';

if((isset($_POST['name'])) && (isset($_POST['pass']))) {
	$name = $_POST['name'];
    $pass = $_POST['pass'];
	
	$CheckSQL = "SELECT IsCirc, IsStock FROM Userbase WHERE name='$name' AND pass='$pass'";
 
	$check = mysqli_fetch_assoc(mysqli_query($con,$CheckSQL));
 
	if(isset($check)) {
		if($check['IsCirc'] == 1) {
			$result->message = "CIRC";
		}
		else if($check['IsStock'] == 1) {
			$result->message = "STOCK";
		}
		else {
			$result->message = "UNKNOWN LIBRARY POSITION";
		}
		
        echo json_encode($result);
	} else {
		$result->message = "Invalid Username or Password";
        echo json_encode($result);
	}
	
} else {
        $result->message = "POST failed";
        echo json_encode($result);
}
$db_con->close();

    

    
?>
