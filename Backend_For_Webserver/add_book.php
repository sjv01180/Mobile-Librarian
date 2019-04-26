<?php
require 'db_config.php';

if((isset($_POST['isbn'])) && (isset($_POST['title'])) && (isset($_POST['author'])) && (isset($_POST['genre']))) {
	$isbn = $_POST['isbn'];
    $title = $_POST['title'];
	$author = $_POST['author'];
	$genre = $_POST['genre'];
	
	$CheckExists = "SELECT * FROM Books WHERE BookISBN='$isbn'";
 
	$check = mysqli_fetch_array(mysqli_query($con,$CheckExists));
 
	if(isset($check)){
		$result->message = "Book Already Exist";
		echo json_encode($result);
	}
	else{ 
		$sql = "INSERT INTO Books(BookISBN, Title, Author, Genre) VALUES('$isbn', '$title', '$author', '$genre')";

		if($db_con->query($sql) === TRUE) {
			$result->message = "Successfully inserted book into database";
			echo json_encode($result);
		} else {
			$result->qRes = $db_con->query($sql);
			$result->message = "Failed to insert book into database";
			echo json_encode($result);
		}
	}
} else {
        $result->message = "POST failed";
        echo json_encode($result);
}
$db_con->close();

    

    
?>
