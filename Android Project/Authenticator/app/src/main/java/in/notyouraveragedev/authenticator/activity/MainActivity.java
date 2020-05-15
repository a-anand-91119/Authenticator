package in.notyouraveragedev.authenticator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import in.notyouraveragedev.authenticator.R;
import in.notyouraveragedev.authenticator.domain.User;
import in.notyouraveragedev.authenticator.util.Constants;
import in.notyouraveragedev.authenticator.util.Controller;
import in.notyouraveragedev.authenticator.util.PreferenceManager;
import in.notyouraveragedev.authenticator.util.Utilities;

/**
 * Main Activity Class.
 * This class implements View.OnClickListener to capture Button / Textview presses
 * <p>
 * Created by A Anand on 25-04-2020
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Declaring variables for UI elements that needs to be accessed
    private TextInputLayout userNameLayout;
    private TextInputLayout passwordLayout;

    private ProgressBar loginProgressbar;

    private ConstraintLayout constraintLayout;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferenceManager = PreferenceManager.getInstance(getApplicationContext());

        // check whether user logged-in data exists
        if (preferenceManager.contains(Constants.LOGGED_IN_USER)) {
            openProfileActivity();
        }

        // initializing UI Elements
        initializeUIElements();

    }

    /**
     * The Method initializes the UI Elements
     */
    private void initializeUIElements() {
        userNameLayout = findViewById(R.id.til_username);
        passwordLayout = findViewById(R.id.til_password);
        loginProgressbar = findViewById(R.id.progressbar);
        constraintLayout = findViewById(R.id.login_layout);

        findViewById(R.id.bt_sign_in).setOnClickListener(this);
        findViewById(R.id.tv_sign_up).setOnClickListener(this);

        Utilities.addTextWatcher(3, 32, Objects.requireNonNull(userNameLayout.getEditText()), userNameLayout);
        Utilities.addTextWatcher(3, 32, Objects.requireNonNull(passwordLayout.getEditText()), passwordLayout);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sign_in:
                // initiate sign in operation
                if (validateInputs()) {
                    initiateLoginProcess();
                }
                break;
            case R.id.tv_sign_up:
                // initiate sign up process
                goToSignUpActivity();
                break;
        }
    }

    /**
     * Method opens the register activity. If the user has entered a username without any errors,
     * then this data will be passed on to the register activity where it will be set
     * to the username field
     */
    private void goToSignUpActivity() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);

        if (!Objects.requireNonNull(userNameLayout.getEditText()).getText().toString().isEmpty() &&
                !userNameLayout.isErrorEnabled())
            registerIntent.putExtra("username",
                    Objects.requireNonNull(userNameLayout.getEditText()).getText().toString());

        this.startActivity(registerIntent);
    }

    /**
     * Method initiates the login process. It creates a new {@link JsonObjectRequest}
     * and adds it to the volley {@link RequestQueue}.
     */
    private void initiateLoginProcess() {
        Snackbar.make(constraintLayout, "Authenticating User", Snackbar.LENGTH_SHORT).show();
        loginProgressbar.setVisibility(View.VISIBLE);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", Objects.requireNonNull(userNameLayout.getEditText()).getText().toString());
        parameters.put("password", Objects.requireNonNull(passwordLayout.getEditText()).getText().toString());

        Controller.getInstance().addToRequestQueue(new JsonObjectRequest(Request.Method.POST,
                Constants.LOGIN_URL, new JSONObject(parameters), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Success Response:", String.valueOf(response));
                loginProgressbar.setVisibility(View.INVISIBLE);
                try {
                    if (response.getString(Constants.STATUS).equals(Constants.SUCCESS)) {
                        storeUserDataInPreference(response);
                        openProfileActivity();
                    } else {
                        Snackbar.make(constraintLayout, response.getString(Constants.ERROR_MESSAGE), Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Snackbar.make(constraintLayout, "Invalid Response From Server", Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error Response:", String.valueOf(error));
                loginProgressbar.setVisibility(View.INVISIBLE);
                Snackbar.make(constraintLayout, "Invalid Response From Server", Snackbar.LENGTH_LONG).show();
            }
        }), Constants.LOGIN_TAG);

    }

    /**
     * Method to store the user object from the response into shared preference
     *
     * @param response the JSON response from the server
     * @throws JSONException
     */
    private void storeUserDataInPreference(JSONObject response) throws JSONException {
        User user = createUserObject(response.getJSONObject("user"));
        preferenceManager.saveObject(Constants.LOGGED_IN_USER, user);
    }

    /**
     * Method to open the profile activity.
     * The method checks the validity of the stored data before logging in the user
     */
    private void openProfileActivity() {
        Intent loginSuccessIntent = new Intent(MainActivity.this, ProfileActivity.class);
        Object fromPreference = preferenceManager.fetchObject(Constants.LOGGED_IN_USER, User.class);
        if (fromPreference instanceof User) {
            loginSuccessIntent.putExtra("user", (User) fromPreference);
            startActivity(loginSuccessIntent);
        } else {
            preferenceManager.removeData(Constants.LOGGED_IN_USER);
            Snackbar.make(constraintLayout, "User Not Logged In", Snackbar.LENGTH_SHORT);
        }
    }

    /**
     * Creates a {@link User} from the {@link JSONObject} received from server
     *
     * @param json the json object from which user needs to be created
     * @return a {@link User} object
     * @throws JSONException if error occurs while getting data from json object
     */
    private User createUserObject(JSONObject json) throws JSONException {
        User user = new User();
        user.setFullName(json.getString("fullName"));
        user.setEmailAddress(json.getString("emailAddress"));
        user.setMobileNumber(json.getString("mobileNumber"));
        user.setProfileUrl(json.getString("profileImageUrl"));
        return user;
    }

    /**
     * Method checks whether there are any errors on the input fields.
     * If username or password fields are empty, then the respective {@link TextInputLayout}
     * error is enabled.
     * <p>
     * Both the username and password fields are checked for errors.
     *
     * @return true if there are no errors, otherwise returns false
     */
    private boolean validateInputs() {
        if (Objects.requireNonNull(userNameLayout.getEditText()).getText().toString().isEmpty()) {
            userNameLayout.setError("Enter Your Username");
            userNameLayout.setErrorEnabled(true);
        }
        if (Objects.requireNonNull(passwordLayout.getEditText()).getText().toString().isEmpty()) {
            passwordLayout.setError("Enter Your Password");
            passwordLayout.setErrorEnabled(true);
        }
        return !userNameLayout.isErrorEnabled() && !passwordLayout.isErrorEnabled();
    }
}
