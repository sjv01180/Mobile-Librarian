<?php
require 'db_config.php';

if((isset($_POST['name'])) && (isset($_POST['pass']))) {
	$name = $_POST['name'];
    $pass = $_POST['pass'];
	
	$CheckSQL = "SELECT Role FROM Userbase WHERE name='$name' AND pass='$pass'";
 
	$check = mysqli_fetch_assoc(mysqli_query($con,$CheckSQL));
 
	if(isset($check)) {
		$result->message = $check['Role'];
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
