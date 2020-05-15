package in.notyouraveragedev.authenticator.util;

/**
 * Constants Class
 * <p>
 * Created by A Anand on 25-04-2020
 */
public class Constants {

    public static final String LOGIN_URL = "https://apis.notyouraveragedev.in/android/login_registration/authenticate_user.php";
    public static final String REGISTER_URL = "https://apis.notyouraveragedev.in/android/login_registration/create_user.php";

    /*

    If you are testing on local web server use the below IP address.
    For emulators localhost / 127.0.0.1 will map to the emulator itself.
    To access the local web server on your computer or laptop you need to use this ip address

     public static final String LOGIN_URL = "http://10.0.2.2/android/login_registration/authenticate_user.php";
     public static final String REGISTER_URL = "http://10.0.2.2/android/login_registration/create_user.php";

    */
    public static final String LOGIN_TAG = "LOGIN_REQUEST";
    public static final String REGISTER_TAG = "REGISTER_REQUEST";
    public static final int IMAGE_REQUEST_CODE = 1000;
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";
    public static final String STATUS = "status";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String LOGGED_IN_USER = "loggedInUser";
}
