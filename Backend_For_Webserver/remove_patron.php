<?php
require 'db_config.php';

$db = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if(isset($_POST['id']) && isset($_POST['role'])) {
                $id = $_POST['id'];
                                $pType = $_POST['role'];

                switch($pType) {
                        case "STUDENT":
                                $removal1 = "DELETE FROM Checks WHERE Sid = '$id'";
                                $removal2 = "DELETE FROM Student WHERE Sid = '$id'";
                                break;
                        case "FACULTY":
                                $removal1 = "DELETE FROM Reserves WHERE Fid = '$id'";
                                $removal2 = "DELETE FROM Faculty WHERE Fid = '$id'";
                                break;
                        default:
                                $result->message = "UNKNOWN PATRON TYPE";
                                echo json_encode($result);
                }

        if(($db->query($remove1) === TRUE) && ($db->query($remove2) === TRUE)) {
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

$db->close();
?>