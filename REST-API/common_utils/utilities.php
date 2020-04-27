<?php

/**
 * Method decodes a base64 string (image encoded as string in this example) 
 * and writes to the file specified by $output_file
 */
function base64_to_jpeg($base64_string, $output_file)
{
    // opening file
    $file = fopen($output_file, "wb");

    // write image to file
    fwrite($file, base64_decode($base64_string));

    fclose($file);

    return $output_file;
}

/**
 * to print new line
 * mainly for debugging purposes if needed
 */
function newLine(){
    echo "</br>";
}
