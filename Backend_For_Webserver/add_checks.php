<?php
require 'db_config.php';
$db = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if((isset($_POST['isbn'])) && (isset($_POST['id'])))
{
        $isbn = $_POST['isbn'];
        $sid = $_POST['id'];

        $sql = "INSERT INTO Checks (BookISBN, CO_Date, CI_Date, Due_Date, Sid) VALUES ('$isbn', now(), NULL, DATE_ADD(now(), INTERVAL 14 DAY), '$sid')";


        if($db->query($sql) === TRUE) {
                $result->message = "Insertion successful";
                echo json_encode($result);
        } else {
                $result->qRes = $db->query($sql);
                $result->message = "Insertion failed";
                echo json_encode($result);
        }
} else {
        $result->message = "POST failed";
        echo json_encode($result);
}

$db->close();
?>
