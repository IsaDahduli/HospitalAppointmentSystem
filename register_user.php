<?php
header('Content-Type: application/json');

// Clear any previous output
ob_clean();

$servername = "localhost";
$username = "root"; 
$password = ""; 
$dbname = "hospitaldb";

$conn = new mysqli("localhost", "root", "", "hospitaldb");

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$first_name = $_POST['first_name'];
$last_name = $_POST['last_name'];
$password = password_hash($_POST['password'], PASSWORD_DEFAULT);  // Hash the password
$email = $_POST['email'];
$phone = $_POST['phone'];


$sql = "INSERT INTO users (first_name, last_name, password, email, phone) VALUES ('$first_name', '$last_name', '$password', '$email', '$phone')";

if ($conn->query($sql) === TRUE) {
    echo json_encode(["status" => "success", "message" => "User registered successfully!"]);
} else {
    echo json_encode(["status" => "error", "message" => "Error: " . $sql . "<br>" . $conn->error]);
}

$conn->close();
?>
