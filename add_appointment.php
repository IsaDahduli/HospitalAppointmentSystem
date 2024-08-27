<?php
// Start the session
session_start();

// Check if the user is logged in
if (!isset($_SESSION['user_id'])) {
    die(json_encode(array('status' => 'error', 'message' => 'Error: You must be logged in to make an appointment.')));
}

// Retrieve the user_id from the session
$user_id = $_SESSION['user_id'];

// Database connection
$host = 'localhost';
$user = 'root';
$password = '';
$dbname = 'hospitaldb';

$conn = new mysqli($host, $user, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die(json_encode(array('status' => 'error', 'message' => 'Connection failed: ' . $conn->connect_error)));
}

// Getting data from POST request
$department = $_POST['department'] ?? '';
$doctor = $_POST['doctor'] ?? '';
$date = $_POST['date'] ?? '';
$time = $_POST['time'] ?? '';

// Validate and sanitize inputs
$department = $conn->real_escape_string(trim($department));
$doctor = $conn->real_escape_string(trim($doctor));
$date = $conn->real_escape_string(trim($date));
$time = $conn->real_escape_string(trim($time));

// Prepare and bind
$stmt = $conn->prepare("INSERT INTO appointments (user_id, department, doctor, appointment_date, time) VALUES (?, ?, ?, ?, ?)");
$stmt->bind_param("issss", $user_id, $department, $doctor, $date, $time);

// Execute the statement
if ($stmt->execute()) {
    $response = array('status' => 'success', 'message' => 'Appointment added successfully');
} else {
    $response = array('status' => 'error', 'message' => 'Failed to add appointment: ' . $stmt->error);
}

// Close the statement and connection
$stmt->close();
$conn->close();

// Send only one JSON response
echo json_encode($response);
?>
