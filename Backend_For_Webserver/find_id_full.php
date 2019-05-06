<?php
require 'db_config.php';
$db = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if(isset($_POST['id'])) {
        $id = $_POST['id'];
        $query = "SELECT * FROM Student WHERE Sid = '$id'";
        $sql = $db->query($query);

        if(mysqli_num_rows($sql) == 0) {
                        $query = "SELECT * FROM Faculty WHERE Fid = '$id'";
                        $sql = $db->query($query);
                        if(mysqli_num_rows($sql) == 0) {
                                $result->message = "UNKNOWN";
                        } else {
                                $result = mysqli_fetch_assoc($sql);
                        }
                } else {
                        $result = mysqli_fetch_assoc($sql);
                }
                echo json_encode($result);
} else {
        $result->message = "POST failed";
        echo json_encode($result);
}

$db->close();
?>