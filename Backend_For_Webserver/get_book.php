<?php
require 'db_config.php';

$db_con = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if(isset($_POST['isbn'])) {
        $isbn = $_POST['isbn'];

        $CheckSQL = "SELECT * FROM Books WHERE BookISBN = '$isbn'";

        $check = mysqli_fetch_assoc($db_con->query($CheckSQL));

        if(isset($check)) {
				$result->message = "query successful";
                $result->isbn = $check['BookISBN'];
				$result->title = $check['Title'];
				$result->author = $check['Author'];
				$result->genre = $check['Genre'];
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
