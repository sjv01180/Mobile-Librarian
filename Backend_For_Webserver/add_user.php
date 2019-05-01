<?php
require 'db_config.php';

$db_con = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if((isset($_POST['name'])) && (isset($_POST['pass'])) && (isset($_POST['role']))) {
        $name = $_POST['name'];
        $pass = $_POST['pass'];
        $role = $_POST['role'];

                $insert = "INSERT INTO Userbase (Name, Passwrd, Role) VALUES ('$name', '$pass', '$role')";
                if ($db_con->query($insert) === TRUE) {
                        $result->message = "Insertion successful";
                        echo json_encode($result);
                } else {
                        $result->message = "Insertion failed";
                        echo json_encode($result);
                }
} else {
        $result->message = "POST failed";
        echo json_encode($result);
}
$db_con->close();
?>
