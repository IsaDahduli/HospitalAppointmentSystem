<?php
// Start the session
session_start();

// Set response content type
header('Content-Type: application/json');

// Check if the user is logged in
if (!isset($_SESSION['user_id'])) {
    echo json_encode(array('status' => 'error', 'message' => 'You must be logged in to view appointments.'));
    exit;
}

// Retrieve the user_id from the session
$user_id = $_SESSION['user_id'];

// Database connection details
$host = 'localhost';
$user = 'root';
$password = '';
$dbname = 'hospitaldb';

// Create a new MySQLi connection
$conn = new mysqli($host, $user, $password, $dbname);

// Check the connection
if ($conn->connect_error) {
    echo json_encode(array('status' => 'error', 'message' => 'Connection failed: ' . $conn->connect_error));
    exit;
}

// Prepare the SQL statement to select appointments for the logged-in user
$stmt = $conn->prepare("SELECT id, department, doctor, DATE(appointment_date) AS date, time FROM appointments WHERE user_id = ?");
$stmt->bind_param("i", $user_id);

// Execute the statement
$stmt->execute();

// Get the result
$result = $stmt->get_result();

// Fetch all appointments
$appointments = array();
while ($row = $result->fetch_assoc()) {
    $appointments[] = $row;
}

// Close the statement and connection
$stmt->close();
$conn->close();

// Return the appointments as a JSON response
if (!empty($appointments)) {
    echo json_encode(array('status' => 'success', 'appointments' => $appointments));
} else {
    echo json_encode(array('status' => 'success', 'message' => 'No appointments found for this user.'));
}
?>
