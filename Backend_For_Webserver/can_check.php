<?php

require 'db_config.php';

$db_con = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if (isset($_POST['id'])) {
        $id= $_POST['id'];

        $query = "SELECT CI_Date FROM Checks WHERE Sid = '$id' AND CI_DATE = '1000-01-01'";
        $sql = $db_con->query($query);

        if (mysqli_num_rows($sql) == 0) {
			$result->message = "no";
		} else {
			$result->message = "yes";
		}
        echo json_encode($result);
} else {
        $result->message = "POST failed";
        echo json_encode($result);
}
$db_con->close();
?>
