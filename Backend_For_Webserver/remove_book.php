<?php
require 'db_config.php';

$db_con = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if(isset($_POST['isbn'])) {
        $isbn = $_POST['isbn'];
        $removal_reserve = "DELETE FROM Reserves WHERE BookISBN = '$isbn'";
        $removal_check = "DELETE FROM Checks WHERE BookISBN = '$isbn'";
        $removal_book = "DELETE FROM Books WHERE BookISBN = '$isbn'";

    if(($db_con->query($removal_check) === TRUE) && ($db_con->query($removal_reserve) === TRUE) && ($db_con->query($removal_book) === TRUE)) {
                $result->message = "Removal successful";
                echo json_encode($result);
        } else {
                $result->message = "Removal failed";
                echo json_encode($result);
        }


} else {
        $result->message = "POST failed";
        echo json_encode($result);
}
$db_con->close();

?>
