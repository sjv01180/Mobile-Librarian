<?php
require 'db_config.php';
$db = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if((isset($_POST['isbn'])) && (isset($_POST['id'])))
{
        $isbn = $_POST['isbn'];
        $fid = $_POST['id'];

        $sql = "INSERT INTO Reserves (BookISBN, CO_Date, CI_Date, Due_Date, Fid) VALUES ('$isbn', now(), NULL, DATE_ADD(now(), INTERVAL 21 DAY), '$fid')";


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
