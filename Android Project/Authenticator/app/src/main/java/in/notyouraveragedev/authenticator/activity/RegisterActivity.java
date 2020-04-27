package in.notyouraveragedev.authenticator.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import in.notyouraveragedev.authenticator.R;
import in.notyouraveragedev.authenticator.util.Constants;
import in.notyouraveragedev.authenticator.util.Controller;
import in.notyouraveragedev.authenticator.util.Utilities;

/**
 * Register Activity Class.
 * This class implements View.OnClickListener to capture Button / ImageView presses
 * <p>
 * Created by A Anand on 25-04-2020
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    // Declaring variables for UI elements that needs to be accessed
    private TextInputLayout fullNameLayout;
    private TextInputLayout userNameLayout;
    private TextInputLayout emailAddressLayout;
    private TextInputLayout mobileNumberLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmPasswordLayout;
    private CircleImageView profilePictureView;
    private Bitmap profilePicture;
    private ProgressBar registerProgressbar;
    private boolean imageSelected = false;
    private String profilePictureName;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeUIElements(getIntent().getStringExtra("username"));
    }

    private void initializeUIElements(String username) {
        fullNameLayout = findViewById(R.id.til_reg_name);
        userNameLayout = findViewById(R.id.til_reg_username);
        emailAddressLayout = findViewById(R.id.til_reg_email);
        mobileNumberLayout = findViewById(R.id.til_reg_mobile);
        passwordLayout = findViewById(R.id.til_reg_password);
        confirmPasswordLayout = findViewById(R.id.til_reg_confirm_password);
        profilePictureView = findViewById(R.id.iv_reg_profile_picture);
        registerProgressbar = findViewById(R.id.pb_register);
        constraintLayout = findViewById(R.id.register_layout);

        findViewById(R.id.bt_reg_create).setOnClickListener(this);
        profilePictureView.setOnClickListener(this);

        if (username != null)
            Objects.requireNonNull(userNameLayout.getEditText()).setText(username);

        Utilities.addTextWatcher(3, 32, Objects.requireNonNull(fullNameLayout.getEditText()), fullNameLayout);
        Utilities.addTextWatcher(3, 32, Objects.requireNonNull(userNameLayout.getEditText()), userNameLayout);
        Utilities.addTextWatcher(10, 10, Objects.requireNonNull(mobileNumberLayout.getEditText()), mobileNumberLayout);
        Utilities.addTextWatcher(3, 32, Objects.requireNonNull(passwordLayout.getEditText()), passwordLayout);

        Objects.requireNonNull(emailAddressLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s) || !Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    emailAddressLayout.setError("Not A Valid Email Address");
                    emailAddressLayout.setErrorEnabled(true);
                } else {
                    emailAddressLayout.setErrorEnabled(false);
                    emailAddressLayout.setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Objects.requireNonNull(confirmPasswordLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    confirmPasswordLayout.setError("Re-enter Your Password");
                    confirmPasswordLayout.setErrorEnabled(true);
                } else if (!s.toString().equals(passwordLayout.getEditText().getText().toString())) {
                    // if no other error is being displayed currently
                    if (TextUtils.isEmpty(confirmPasswordLayout.getError())) {
                        confirmPasswordLayout.setError("Password Do Not Match");
                        confirmPasswordLayout.setErrorEnabled(true);
                    }
                } else {
                    confirmPasswordLayout.setError(null);
                    confirmPasswordLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_reg_create:
                if (isAllFieldsErrorFree())
                    initiateRegisterProcess();
                break;
            case R.id.iv_reg_profile_picture:
                Intent profilePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                profilePictureIntent.setType("image/*");
                startActivityForResult(profilePictureIntent, Constants.IMAGE_REQUEST_CODE);
                // result of this startActivityForResult will be obtained in onActivityResult
                // where we cross-check the request codes and decide whether to proceed or not
                break;
        }
    }

    /**
     * Method checks whether all fields have some values or not.
     * It also checks whether there are any error in the fields.
     *
     * @return true if registration can start, otherwise false
     */
    private boolean isAllFieldsErrorFree() {
        boolean isReady = true;
        if (Utilities.checkForEmptyTextInputLayout(fullNameLayout, null) ||
                fullNameLayout.isErrorEnabled()) {
            isReady = false;
        }
        if (Utilities.checkForEmptyTextInputLayout(userNameLayout, null) ||
                userNameLayout.isErrorEnabled()) {
            isReady = false;
        }
        if (Utilities.checkForEmptyTextInputLayout(emailAddressLayout, "Enter A Valid Email Address") ||
                emailAddressLayout.isErrorEnabled()) {
            isReady = false;
        }
        if (Utilities.checkForEmptyTextInputLayout(mobileNumberLayout, null) ||
                mobileNumberLayout.isErrorEnabled()) {
            isReady = false;
        }
        if (Utilities.checkForEmptyTextInputLayout(passwordLayout, "Enter A Strong Password") ||
                passwordLayout.isErrorEnabled()) {
            isReady = false;
        }
        if (Utilities.checkForEmptyTextInputLayout(confirmPasswordLayout, "Re-Enter Your Password") ||
                confirmPasswordLayout.isErrorEnabled()) {
            isReady = false;
        }
        if (!imageSelected) {
            Snackbar.make(constraintLayout, "Please Select A Profile Picture", Snackbar.LENGTH_LONG).show();
            isReady = false;
        }
        return isReady;
    }

    /**
     * Method performs the registration operation
     */
    private void initiateRegisterProcess() {
        Snackbar.make(constraintLayout, "Creating A New Account", Snackbar.LENGTH_SHORT).show();
        registerProgressbar.setVisibility(View.VISIBLE);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", Objects.requireNonNull(fullNameLayout.getEditText()).getText().toString());
        parameters.put("username", Objects.requireNonNull(userNameLayout.getEditText()).getText().toString());
        parameters.put("password", Objects.requireNonNull(passwordLayout.getEditText()).getText().toString());
        parameters.put("mobile_number", Objects.requireNonNull(mobileNumberLayout.getEditText()).getText().toString());
        parameters.put("email_address", Objects.requireNonNull(emailAddressLayout.getEditText()).getText().toString());
        parameters.put("profile_picture", Utilities.convertBitmapToString(profilePicture));
        parameters.put("file_name", profilePictureName);

        Controller.getInstance().addToRequestQueue(new JsonObjectRequest(Request.Method.POST, Constants.REGISTER_URL,
                new JSONObject(parameters), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                registerProgressbar.setVisibility(View.INVISIBLE);
                Log.i("Success Response: ", response.toString());
                try {
                    if (response.getString(Constants.STATUS).equals(Constants.SUCCESS)) {
                        Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                        Intent goBackIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        // clearing activity history stack
                        goBackIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(goBackIntent);
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
                registerProgressbar.setVisibility(View.INVISIBLE);
                Log.i("Error Response: ", String.valueOf(error));
                Snackbar.make(constraintLayout, "Invalid Response From Server", Snackbar.LENGTH_LONG).show();
            }
        }), Constants.REGISTER_TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // checking whether this is the result of our image fetch request
        if (requestCode == Constants.IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // image selected
            try {
                // parsing the selection and displaying in imageview
                Uri imageUri = data.getData(); // getting image uri
                profilePictureName = Utilities.getFileNameFromUri(imageUri, this);
                InputStream imageStream = getContentResolver().openInputStream(Objects.requireNonNull(imageUri));
                profilePicture = BitmapFactory.decodeStream(imageStream);
                profilePictureView.setImageBitmap(profilePicture);
                imageSelected = true;
            } catch (Exception e) {
                Log.e("Image Fetching", "Error:" + e);
            }
        }
    }
}
