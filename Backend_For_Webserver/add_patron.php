<?php
require 'db_config.php';

if((isset($_POST['id'])) && (isset($_POST['Fname'])) && (isset($_POST['Lname'])$
                $id = $_POST['id'];
                $fName = $_POST['Fname'];
                $lName = $_POST['Lname'];
                $pType = $_POST['Ptype'];

                switch($pType) {
                        case "STUDENT":
                                $insert = "INSERT INTO Student (Sid, Fname, Lname) VALUES ('$id', '$fName', '$lName')";
                                break;
                        case "FACULTY":
                                $insert = "INSERT INTO Faculty (Sid, Fname, Lname) VALUES ('$id', '$fName', '$lName')";
                                break;
                        default:
                                $result->message = "UNKNOWN PATRON TYPE";
                                echo json_encode($result);
                }

                $sql = $insert;

        if($db_con->query($sql) === TRUE) {
                $result->message = "Insertion successful";
                echo json_encode($result);
        } else {
                $result->message = "Insertion failed";
                echo json_encode($result);
        }
} else {
        $result->message = "POST failed";
        echo json_encode($result);
}

$db_con->close();
?>
