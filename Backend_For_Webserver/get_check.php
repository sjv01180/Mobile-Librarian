<?php

require 'db_config.php';

$db_con = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if (isset($_POST['isbn'])) {
        $isbn = $_POST['isbn'];

        $query = "SELECT * FROM Checks AS C INNER JOIN Books AS B ON C.BookISBN = B.BookISBN INNER JOIN Student AS S ON C.Sid = S.Sid WHERE C.BookISBN = '$isbn'";
        $sql = $db_con->query($query);

        while($row = mysqli_fetch_assoc($sql))
        {
                $result = $row;
        }
        echo json_encode($result);
} else {
        $result->message = "POST failed";
        echo json_encode($result);
}
$db_con->close();
?>
