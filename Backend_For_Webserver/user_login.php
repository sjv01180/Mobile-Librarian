<?php
require 'db_config.php';

$db_con = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if((isset($_POST['name'])) && (isset($_POST['pass']))) {
        $name = $_POST['name'];
        $pass = $_POST['pass'];

        $CheckSQL = "SELECT Role FROM Userbase WHERE Name = '$name' AND Passwrd = '$pass'";

        $check = mysqli_fetch_array($db_con->query($CheckSQL));

        if(isset($check)) {
                $result->message = $check[0];
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
