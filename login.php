<?php
header('Content-Type: application/json');

session_start();

// Clear any previous output
ob_clean();

$servername = "localhost";
$username = "root"; 
$password = ""; 
$dbname = "hospitaldb";
$host = 'localhost';

try {
    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("Connection failed: " . $e->getMessage());
}

// Check if the request method is POST
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Get the POSTed data
    $phone = $_POST['phone'];
    $password = $_POST['password'];

    // Prepare SQL query
    $stmt = $conn->prepare("SELECT * FROM users WHERE phone = :phone");
    $stmt->bindParam(':phone', $phone);
    $stmt->execute();
    $user = $stmt->fetch(PDO::FETCH_ASSOC);

    // Check if user exists and password is correct
    if ($user && password_verify($password, $user['password'])) {
        // Set session user_id
        $_SESSION['user_id'] = $user['id'];

        // Success response
        echo json_encode([
            'status' => 'success',
            'message' => 'Login successful',
            'user_id' => $user['id']
        ]);
    } else {
        // Error response
        echo json_encode([
            'status' => 'error',
            'message' => 'Invalid phone number or password'
        ]);
    }
} else {
    // Error response for non-POST request
    echo json_encode([
        'status' => 'error',
        'message' => 'Invalid request method'
    ]);
}
?>
