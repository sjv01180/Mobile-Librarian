<?php
require 'db_config.php';

$db_con = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if(isset($_POST['isbn'])) {
        $isbn = $_POST['isbn'];

        $CheckSQL = "SELECT * FROM Books WHERE BookISBN = '$isbn'";

        $check = mysqli_fetch_assoc($db_con->query($CheckSQL));

        if(isset($check)) {
                $result = $check;
                $result["message"] = "query successful";
                echo json_encode($result);
        } else {
                $result->message = "query failed";
                echo json_encode($result);
        }

} else {
        $result->message = "POST failed";
        echo json_encode($result);
}
$db_con->close();
?>
