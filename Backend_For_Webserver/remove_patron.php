<?php
require 'db_config.php';

$db_con = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if(isset($_POST['id']) && isset($_POST['role'])) {
                $id = $_POST['id'];
				$pType = $_POST['role'];

                switch($pType) {
                        case "STUDENT":
                                $removal = "DELETE FROM Student WHERE Sid = '$id'";
                                break;
                        case "FACULTY":
                                $removal = "DELETE FROM Faculty WHERE Fid = '$id'";
                                break;
                        default:
                                $result->message = "UNKNOWN PATRON TYPE";
                                echo json_encode($result);
                }

                $sql = $insert;

        if($db_con->query($sql) === TRUE) {
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
