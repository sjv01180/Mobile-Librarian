<?php

require 'db_config.php';

$db_con = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if ($db_con->connect_error) {
     $result->message = "POST failed";
     echo json_encode($result);
}

$query = "SELECT * FROM Reserves AS R INNER JOIN Books AS B ON R.BookISBN = B.BookISBN INNER JOIN Faculty AS F ON R.Fid = F.Fid";
$sql = $db_con->query($query);

$result = array();
while($row = mysqli_fetch_assoc($sql))
{
        $order = $row;
        array_push($result, $order);
}
echo json_encode($result);

$db_con->close();
?>
