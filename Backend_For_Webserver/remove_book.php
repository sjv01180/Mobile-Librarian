<?php
require 'db_config.php';

$db_con = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if(isset($_POST['isbn'])) {
        $isbn = $_POST['isbn'];

        $removal = "DELETE FROM Books WHERE BookISBN = '$isbn'";

    if($db_con->query($removal) === TRUE) {
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
