package in.notyouraveragedev.authenticator.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import in.notyouraveragedev.authenticator.R;
import in.notyouraveragedev.authenticator.domain.User;
import in.notyouraveragedev.authenticator.util.Constants;
import in.notyouraveragedev.authenticator.util.Controller;
import in.notyouraveragedev.authenticator.util.PreferenceManager;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView profilePicture;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        User user = (User) getIntent().getSerializableExtra("user");

        initializeUIElements(Objects.requireNonNull(user));
    }

    private void initializeUIElements(User user) {
        final TextView emailAddress = findViewById(R.id.tv_prof_email);
        TextView mobileNumber = findViewById(R.id.tv_prof_mobile);
        TextView fullName = findViewById(R.id.tv_prof_name);
        TextView todaysDate = findViewById(R.id.tv_prof_date);

        profilePicture = findViewById(R.id.iv_prof_image);
        constraintLayout = findViewById(R.id.profile_layout);

        Controller.getInstance().getImageLoader().get(user.getProfileUrl(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Log.i("Image Load Response", String.valueOf(response));
                if (response.getBitmap() != null)
                    profilePicture.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Image Load Error Header", error.networkResponse.allHeaders.toString());
                Snackbar.make(constraintLayout, "Failed To Load Profile Picture", Snackbar.LENGTH_SHORT).show();
            }
        });

        emailAddress.setText(user.getEmailAddress());
        mobileNumber.setText(user.getMobileNumber());
        fullName.setText(user.getFullName());

        Date c = Calendar.getInstance().getTime();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
        todaysDate.setText(df.format(c));

        // adding onclick listener to logout button
        findViewById(R.id.bt_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    /**
     * Method to logout a user.
     * The method removed the user data from shared preference
     * and goes back to the previous activity (Main Activity)
     */
    private void logout() {
        if (PreferenceManager.getInstance(getApplicationContext()).removeData(Constants.LOGGED_IN_USER))
            this.finish();
        else
            Snackbar.make(constraintLayout, "Failed To Logut User", Snackbar.LENGTH_SHORT);
    }
}
