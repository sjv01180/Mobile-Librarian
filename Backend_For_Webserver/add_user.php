<?php
require 'db_config.php';

if((isset($_POST['name'])) && (isset($_POST['pass'])) && (isset($_POST['role']))) {
        $name = $_POST['name'];
        $pass = $_POST['pass'];
        $role = $_POST['role'];

        $CheckExists = "SELECT * FROM Userbase WHERE name='$name'";

        $check = mysqli_fetch_array(mysqli_query($con,$CheckExists));

        if(isset($check)){
                $result->message = "User Already Exist";
                echo json_encode($result);
        }
        else{
                switch($role) {
                        case "CIRC":
                                $insert = "INSERT INTO Userbase (Name, Passwrd, IsCirc, IsStock) VALUES ('$name', '$pass', 1, 0)";
								if($db_con->query($insert) === TRUE) {
									$result->message = "User inserted successfully: " . $name;
									echo json_encode($result);
								} else {
									$result->message = "Failed to insert user into database";
									echo json_encode($result);
								}
								break;
                        case "STOCK":
                                $insert = "INSERT INTO Userbase (Name, Passwrd, IsCirc, IsStock) VALUES ('$name', '$pass', 0, 1)";
                                if($db_con->query($insert) === TRUE) {
									$result->message = "User inserted successfully: " . $name;
									echo json_encode($result);
								} else {
									$result->message = "Failed to insert user into database";
									echo json_encode($result);
								}
								break;
                        default:
                                $result->message = "UNKNOWN ROLE IDENTIFIED" . $role;
                                echo json_encode($result);
                                break;
                }
        }
} else {
        $result->message = "POST failed";
        echo json_encode($result);
}
$db_con->close();
?>
