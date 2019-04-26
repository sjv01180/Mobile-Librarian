<?php
require 'db_config.php';

if((isset($_POST['name'])) && (isset($_POST['pass'])) && (isset($_POST['role']))) {
	$name = $_POST['name'];
    $pass = $_POST['pass'];
	$role = $_POST['role'];
	
	$encryptPass = md5($pass);
	
	$CheckExists = "SELECT * FROM Userbase WHERE name='$name'";
 
	$check = mysqli_fetch_array(mysqli_query($con,$CheckExists));
 
	if(isset($check)){
		$result->message = "User Already Exist";
		echo json_encode($result);
	}
	else{ 
		switch($role) {
			case "CIRC":
				$query = "INSERT INTO Userbase(Name, Passwrd, IsCirc, IsStock) VALUES('$name', '$encryptPass', TRUE, FALSE)";
				break;
			case "STOCK":
				$query = "INSERT INTO Userbase(Name, Passwrd, IsCirc, IsStock) VALUES('$name', '$encryptPass', FALSE, TRUE)";
				break;
		}

		$sql = $query;

		if($db_con->query($sql) === TRUE) {
			$result->message = "User inserted successfully" . $name;
			echo json_encode($result);
		} else {
			$result->qRes = $db_con->query($sql);
			$result->message = "Failed to insert user into database";
			echo json_encode($result);
		}
	}
} else {
        $result->message = "POST failed";
        echo json_encode($result);
}
$db_con->close();

    

    
?>
