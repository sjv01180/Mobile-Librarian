<?php
require 'db_config.php';

$db_con = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if ($db_con->connect_error) {
        $result->message = "POST failed";
     echo json_encode($result);
}

$query = "SELECT * FROM Books";
$sql = $db_con->query($query);

$result = array();
while($row = mysqli_fetch_assoc($sql))
{
        $book = $row;
        array_push($result, $book);
}
echo json_encode($result);

$db_con->close();

?>
