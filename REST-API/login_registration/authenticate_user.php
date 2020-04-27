<?php

include_once('../common_utils/db_util.php');
include_once('../common_utils/utilities.php');

$return_response = array();

$json = json_decode(file_get_contents('php://input'), true);

$username = $json['username'];
$password = $json['password'];

$check_user_query = "SELECT * FROM auth_user_data WHERE aud_username = ?";

$fetched_result = execute_query($conn, $check_user_query, "s", array($username));

if (count($fetched_result) == 1) {
    if(password_verify($password, $fetched_result[0]['aud_hash'])){
        $return_response['status'] = "SUCCESS";
        $user = array();
        $user["fullName"] = $fetched_result[0]['aud_full_name'];
        $user["emailAddress"] = $fetched_result[0]['aud_email'];
        $user["mobileNumber"] = $fetched_result[0]['aud_phone'];
        $user["profileImageUrl"] = $fetched_result[0]['aud_profile_img'];
        $return_response['user'] = $user;
    }else{
        $return_response['status'] = "FAILURE";
        $return_response['errorMessage'] = "Invalid Credentials";
    }
   
} else {
    $return_response['status'] = "FAILURE";
    $return_response['errorMessage'] = "Invalid Credentials";
}

echo json_encode($return_response);
