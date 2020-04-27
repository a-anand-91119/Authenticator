<?php

include_once('../common_utils/db_util.php');
include_once('../common_utils/utilities.php');

// inputs from JsonObjectRequest needs to be fetched like below.
$json = json_decode(file_get_contents('php://input'), true);

$user_name = $json['name'];
$user_username = $json['username'];
$user_password = $json['password'];
$user_mobile_number = $json['mobile_number'];
$user_email_address = $json['email_address'];
$user_profile_picture = $json['profile_picture'];
$user_profile_name = $json['file_name'];

$response_array = array();

$server_path = "https://apis.notyouraveragedev.in/android/login_registration/profile_pictures/";
// this path is going to be with respect to utilities.php
$absolute_path = "../login_registration/profile_pictures/";

$checkUserExistsQuery = "SELECT aud_username FROM auth_user_data WHERE aud_username = ?";

$no_of_rows = get_no_of_rows($conn, $checkUserExistsQuery, "s", array($user_username));

if ($no_of_rows <= 0) {
    // username doesn't exists. can create user
    $hashed_password = password_hash($user_password, PASSWORD_DEFAULT);
    $user_insert_query = "INSERT INTO auth_user_data (aud_full_name, aud_username, aud_hash, aud_email, aud_phone, aud_profile_img) VALUES (?,?,?,?,?,?)";
    // appending serverpath and file name
    $file_path = $server_path . $user_username . $user_profile_name;

    try {
        begin_transaction($conn);
        execute_query($conn, $user_insert_query, "ssssss", array($user_name, $user_username, $hashed_password, $user_email_address, $user_mobile_number, $file_path));

        base64_to_jpeg($user_profile_picture, $absolute_path . $user_username . $user_profile_name);

        commit_transaction($conn);

        $response_array["status"] = "SUCCESS";
    } catch (Exception $e) {
        // unexpected error
        $response_array["status"] = "FAILURE";
        $response_array["error"] = $e->getMessage();
        rollback_transaction($conn);
    }
} else {
    // username already exists
    $response_array["status"] = "FAILURE";
    $response_array["errorMessage"] = "Username Already Exists";
}

echo json_encode($response_array);

close_connection($conn);
exit();
