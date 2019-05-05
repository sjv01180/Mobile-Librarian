<?php
require 'db_config.php';

$db_con = new mysqli($dbhost, $dbuname, $dbpass, $dbname);

if((isset($_POST['isbn'])) && (isset($_POST['title'])) && (isset($_POST['author'])) && (isset($_POST['genre']))) {
        $isbn = $_POST['isbn'];
        $title = $_POST['title'];
        $author = $_POST['author'];
        $genre = $_POST['genre'];

        $sql = "INSERT INTO Books(BookISBN, Title, Author, Genre) VALUES('$isbn', '$title', '$author', '$genre')";
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
