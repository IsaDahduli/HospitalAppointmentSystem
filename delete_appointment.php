<?php
// Database connection
$host = 'localhost';
$user = 'root';
$password = '';
$dbname = 'hospitaldb';

$conn = new mysqli($host, $user, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Get the user_id and appointment_id from the POST request
$user_id = $_GET['user_id'];
$appointment_id = $_GET['appointment_id'];

// Prepare the SQL statement
$stmt = $conn->prepare("DELETE FROM appointments WHERE user_id = ? AND id = ?");
$stmt->bind_param("ii", $user_id, $appointment_id);

// Execute the statement
if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Appointment deleted successfully."]);
} else {
    echo json_encode(["status" => "error", "message" => "Failed to delete appointment."]);
}

// Close the statement
$stmt->close();
?>
