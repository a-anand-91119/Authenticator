package in.notyouraveragedev.authenticator.util;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

/**
 * Utilities Class providing common methods
 * <p>
 * Created by A Anand on 25-04-2020
 */
public class Utilities {

    /**
     * Method adds a {@link TextWatcher} to the provided {@link EditText}.
     * The text watched checks whether the input is in the range 3 - 32
     *
     * @param min             minimum length of the input
     * @param max             maximum length of the input
     * @param editText        the editText to which the {@link TextWatcher} needs to be added
     * @param textInputLayout the {@link TextInputLayout} to show the errors
     */
    public static void addTextWatcher(final int min, final int max, final EditText editText,
                                      final TextInputLayout textInputLayout) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    textInputLayout.setError("Enter Your " + editText.getHint());
                    textInputLayout.setErrorEnabled(true);
                } else if (s.length() > max) {
                    // if no other error is being displayed currently
                    if (TextUtils.isEmpty(textInputLayout.getError())) {
                        textInputLayout.setError("A Maximum Of " + max + " Characters Are Allowed");
                        textInputLayout.setErrorEnabled(true);
                    }
                } else if (s.length() < min) {
                    // if no other error is being displayed currently
                    if (TextUtils.isEmpty(textInputLayout.getError())) {
                        textInputLayout.setError("A Minimum Of " + min + " Characters Are Required");
                        textInputLayout.setErrorEnabled(true);
                    }
                } else {
                    textInputLayout.setError(null);
                    textInputLayout.setErrorEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * Method converts a given bitmap into a {@link java.util.Base64} encoded string.
     * Since bitmap cannot be converted directly into String, first the bitmap is converted into an array,
     * and then the method encode this array into a string.
     *
     * @param bitmap the bitmap to be converted into string
     * @return the bitmap image as a base64 encoded string
     */
    public static String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Method checks whether all fields have some values or not. If the fields are empty, then
     * an error message will be set. A custom error message can also be passed.
     *
     * @param textInputLayout the textInputLayout to check checked
     * @param errorMessage    the custom error message
     * @return true if textInputLayout contains some entries, otherwise returns false
     */
    public static boolean checkForEmptyTextInputLayout(TextInputLayout textInputLayout, String errorMessage) {
        if (TextUtils.isEmpty(Objects.requireNonNull(textInputLayout.getEditText()).getText().toString())) {
            textInputLayout.setError(errorMessage != null ? errorMessage : "Enter Your " + textInputLayout.getHint());
            textInputLayout.setErrorEnabled(true);
            return true;
        }
        return false;
    }

    /**
     * Method gets the file name from the file uri.
     * It uses a cursor to query the uri and find the file name index.
     * After which the file name is fetched from the cursor entry;
     *
     * @param uri      the uri of the file whose name needs to be fetched
     * @param activity the context of the activity (from where the method was called)
     * @return the file name
     */
    public static String getFileNameFromUri(Uri uri, Activity activity) {
        /*
         * Using the image uri query the server app to get the file's display name and size.
         */
        try (Cursor returnCursor = activity.getContentResolver()
                .query(uri, null, null, null, null)) {
            /*
             * Get the column indexes of the data in the Cursor, move to the first row in the Cursor, get the data,
             * and display it.
             */
            int nameIndex = Objects.requireNonNull(returnCursor).getColumnIndex(OpenableColumns.DISPLAY_NAME);

            returnCursor.moveToFirst();

            return returnCursor.getString(nameIndex);
        }
    }
}
