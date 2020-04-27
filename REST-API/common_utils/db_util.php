<?php
# settings to display errors detailed information.
# useful during development phase, afterwards you may chose to remove them
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

# db connection paramters
# default: root
$db_user = "<DATABASE_USER>";
# default : EMPTY
$db_password = "<DATABASE_USER_PASSWORD>";
# your database name
$db_name = "<DATABASE_NAME>";
# can be set as localhost, if the REST API and database will be on the same server
# in case of local web servers, this can be localhost
$db_host = "<DATABASE_HOST>";
# can be avoided. default: 3306
$db_port = "<DATA_BASE_PORT>";


/**  establishing connection to database
 * here im using object oriented approch
 * if you don't wnat to provide a database port you can remove the $db_port parameter
 */
$conn = new mysqli($db_host, $db_user, $db_password, $db_name, $db_port);

# checking whether connection is successful.
# if db connection cannot be obtained, then exit
if (mysqli_connect_errno()) {
    printf("Connect failed: %s\n", mysqli_connect_error());
    exit();
}

# introducing transactions
# this is used during user creation
function begin_transaction($conn)
{
    $conn->query("BEGIN");
}

# commit transaction
function commit_transaction($conn)
{
    $conn->query("COMMIT");
}

# rollback transaction
function rollback_transaction($conn)
{
    $conn->query("ROLLBACK");
}

/** 
 * PS: This is the only method that needs to be called to perform db query operations
 * 
 * ***************************************************************************************
 * Usage:
 * 
 * $query = SELECT * FROM <MYTABLE> WHERE <MYCOl> = ?; //MYCOL is varchar type
 * $mycolval = "test";
 * $result = execute_query($conn, $query, "s", array($mycolval));
 * 
 * $query = SELECT * FROM <MYTABLE>;
 * $result = execute_query($conn, $query, null, null);
 * 
 * ***************************************************************************************
 * 
 * Method to execute a query.
 * for select queries the method uses the result metadata information to get the target fields
 * then the data corresponding to each column is fetched to create an array or associative arrays
 *
 * Sample Structure:
 * Consider a query: SELECT col1, col2 from TABLE;
 * This query fetches 3 records from the database. the return value of this method will be
 *
 * results = array(
 *          array("col1" => "val1", "col2" => "val2"),
 *          array("col1" => "val1", "col2" => "val2"),
 *          array("col1" => "val1", "col2" => "val2")
 * )
 * 
 * conn: the mysqli connection link
 * query: the query to be executed as a string
 * type: the types of the variables to be binded to the query
 * data: the variables to be binded passed as an array
 */
function execute_query($conn, $query, $type, $data)
{
    $results = array();

    $statement = execute($conn, $query, $type, $data);

    // fetching results
    // Get metadata for field names
    $meta = $statement->result_metadata();

    // checking whether the query returns data
    if (gettype($meta) != 'boolean') {
        // Dynamically creating an array of variables to use to bind the results
        while ($field = $meta->fetch_field()) {
            $var = $field->name;
            $$var = null;
            $fields[$var] = &$$var;
        }

        // Bind Results
        call_user_func_array(array($statement, 'bind_result'), $fields);

        // filling the array of arrays
        $i = 0;
        $results = array();
        while ($statement->fetch()) {
            $results[$i] = array();
            foreach ($fields as $k => $v)
                $results[$i][$k] = $v;
            $i++;
        }
    }


    $statement->close();

    return $results;
}

/**
 * This Method performs the actual DB QUERY operation. It binds the variable to the query if provided.
 */
function execute($conn, $query, $type, $data)
{
    $statement = $conn->prepare($query);
    if (isset($data)) {
        $bind_variables = array();
        $bind_variables[] = &$type;

        for ($i = 0; $i < count($data); $i++)
            $bind_variables[] = &$data[$i];

        call_user_func_array(array($statement, 'bind_param'), $bind_variables);
    }

    $statement->execute();
    return $statement;
}

/**
 * method retuns the no of rows of the result.
 */
function get_no_of_rows($conn, $query, $type, $data)
{
    $result = execute_query($conn, $query, $type, $data);
    return count($result);
}

/**
 * Method to close the mysqli connection
 */
function close_connection($conn)
{
    mysqli_close($conn);
}
