<?php

require 'db_config.php';

$db_con = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if (isset($_POST['isbn'])) {
        $isbn = $_POST['isbn'];

        $query = "SELECT CO_Date FROM Checks AS C WHERE C.BookISBN = '$isbn'";
        $sql = $db_con->query($query);
                if(mysqli_num_rows($sql) == 0) {
                        $query = "SELECT CO_Date FROM Reserves AS R WHERE R.BookISBN = '$isbn'";
                        $sql = $db->query($query);
                        if(mysqli_num_rows($sql) == 0) {
                                $result->CO_Date = "0000/00/00";
                                $result->message = "SELECTION ERROR";
                        } else {
                                $result =  mysqli_fetch_assoc($sql);
                        }
                } else {
                        $result = mysqli_fetch_assoc($sql);
                }
                echo json_encode($result);
} else {
        $result->message = "POST failed";
     echo json_encode($result);
}
$db_con->close();
?>
