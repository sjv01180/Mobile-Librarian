<?php
require 'db_config.php';
$db = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if(isset($_POST['isbn'])) {
                $isbn = $_POST['isbn'];

        $query = "SELECT * FROM Checks WHERE CI_Date = '1000-01-01' AND BookISBN = '$isbn'";
                $sql1 = $db->query($query);

        if(mysqli_num_rows($sql1) == 0) {
                        $query = "SELECT * FROM Reserves WHERE CI_Date = '1000-01-01' AND BookISBN = '$isbn'";
                        $sql2 = $db->query($query);
                        if(mysqli_num_rows($sql2) == 0) {
                                $result->message = "SELECTION ERROR";
                        } else {
                                $result->message = "FACULTY";
                        }
                } else {
                        $result->message = "STUDENT";
                }
                echo json_encode($result);
} else {
        $result->message = "POST failed";
        echo json_encode($result);
}

$db->close();
?>
