<?php
require 'db_config.php';

$db_con = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if(isset($_POST['isbn'])) {
        $isbn = $_POST['isbn'];

        $update = "UPDATE Checks SET CI_Date = now() WHERE CI_Date = '1000-01-01' AND BookISBN = '$isbn'";

        if($db_con->query($update) === TRUE) {
                $result->message = "student update successful";
                echo json_encode($result);
        } else {
                $result->message = "student update failed";
                echo json_encode($result);
        }

} else {
        $result->message = "POST failed";
        echo json_encode($result);
}
$db_con->close();

?>