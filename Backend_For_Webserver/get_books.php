<?php
require 'db_config.php';

$db_con = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if ($db_con->connect_error) {
        $result->message = "POST failed";
     echo json_encode($result);
}

$query = "SELECT * FROM Books";
$sql = $db_con->query($query);

$i = 0;
$result = array();
while($row = $sql->fetch_assoc())
{
        $book->isbn = $row["BookISBN"];
        $book->title = $row["Title"];
        $book->author = $row["Author"];
        $book->genre = $row["Genre"];
        $result[] = $book;
}
echo json_encode($result);

$db_con->close();

?>
