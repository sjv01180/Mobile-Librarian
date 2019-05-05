<?php

require 'db_config.php';

$db_con = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if (isset($_POST['isbn'])) {
        $isbn = $_POST['isbn'];

        $query = "SELECT * FROM Reserves AS R INNER JOIN Books AS B ON R.BookISBN = B.BookISBN INNER JOIN Faculty AS F ON R.Fid = F.Fid WHERE R.BookISBN = '$isbn'";
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
