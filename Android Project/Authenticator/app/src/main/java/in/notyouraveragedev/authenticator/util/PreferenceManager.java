package in.notyouraveragedev.authenticator.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Preference Manager class handles all the operations on the Android SharedPreference.
 * <p>
 * Created by A Anand on 14-05-2020
 */
public class PreferenceManager {

    /**
     * Name of the SharedPreferences
     */
    private static final String PREFERENCE_NAME = "in.notyouraveragedev.Authenticator";

    /**
     * Singleton Instance of this class.
     * This will be the only instance of this class
     */
    private static PreferenceManager preferenceManager = null;
    private SharedPreferences sharedPreferences;
    /**
     * Gson instance to serialize and deserialize objects
     */
    private Gson gson;

    /**
     * Private Constructor
     */
    private PreferenceManager(Context applicationContext) {
        gson = new Gson();
        initializeSharedPreference(applicationContext);
    }

    /**
     * method to initialize the shared preference
     *
     * @param applicationContext the application context
     */
    private void initializeSharedPreference(Context applicationContext) {
        sharedPreferences = applicationContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Method to returns the singleton instance of the PreferenceManager class
     *
     * @return the instance of preference manager
     */
    public static PreferenceManager getInstance(Context applicationContext) {
        if (preferenceManager == null)
            preferenceManager = new PreferenceManager(applicationContext);
        return preferenceManager;
    }

    /**
     * Method to save objects into shared preference.
     * The object is serialize into json and is stored in shared preference.
     *
     * @param key   the key against which data needs to be stored
     * @param value the object to be saved
     * @return true is object was saved successfully, false if object could not be saved
     */
    public boolean saveObject(String key, Object value) {
        return sharedPreferences.edit().putString(key, gson.toJson(value)).commit();
    }

    /**
     * Fetched the object stored against the key from shared preference.
     * If object is not available, then null will be returned
     *
     * @param key         the key against which object needs to be fetched
     * @param targetClass the Class to which the object belongs to
     * @return the object stored in the shared preference
     */
    public Object fetchObject(String key, Class targetClass) {
        Object storedData = sharedPreferences.getString(key, null);
        if (storedData != null)
            storedData = gson.fromJson((String) storedData, targetClass);
        return storedData;
    }

    /**
     * Method to store String data into shared preference against the specified key.
     *
     * @param key   the key against which data needs to be stored
     * @param value the data to be stored
     * @return true, if data is stored successfully, otherwise returns false
     */
    public boolean saveString(String key, String value) {
        return sharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * method to fetch String data stored in shared preference against the specified key.
     *
     * @param key the key against which data needs to be fetched
     * @return the data stored against the specified key
     */
    public Object fetchString(String key) {
        return sharedPreferences.getString(key, null);
    }

    /**
     * Method to store Integer data into shared preference against the specified key.
     *
     * @param key   the key against which data needs to be stored
     * @param value the data to be stored
     * @return true, if data is stored successfully, otherwise returns false
     */
    public boolean saveInteger(String key, Integer value) {
        return sharedPreferences.edit().putInt(key, value).commit();
    }

    /**
     * method to fetch Integer data stored in shared preference against the specified key.
     *
     * @param key the key against which data needs to be fetched
     * @return the data stored against the specified key
     */
    public Object fetchInteger(String key) {
        return sharedPreferences.getInt(key, Integer.MIN_VALUE);
    }

    /**
     * Method to store Long data into shared preference against the specified key.
     *
     * @param key   the key against which data needs to be stored
     * @param value the data to be stored
     * @return true, if data is stored successfully, otherwise returns false
     */
    public boolean saveLong(String key, Long value) {
        return sharedPreferences.edit().putLong(key, value).commit();
    }

    /**
     * method to fetch Long data stored in shared preference against the specified key.
     *
     * @param key the key against which data needs to be fetched
     * @return the data stored against the specified key
     */
    public Object fetchLong(String key) {
        return sharedPreferences.getLong(key, Long.MIN_VALUE);
    }

    /**
     * Method to store Boolean data into shared preference against the specified key.
     *
     * @param key   the key against which data needs to be stored
     * @param value the data to be stored
     * @return true, if data is stored successfully, otherwise returns false
     */
    public boolean saveBoolean(String key, Boolean value) {
        return sharedPreferences.edit().putBoolean(key, value).commit();
    }

    /**
     * method to fetch Boolean data stored in shared preference against the specified key.
     *
     * @param key the key against which data needs to be fetched
     * @return the data stored against the specified key
     */
    public Object fetchBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * Method to store Float data into shared preference against the specified key.
     *
     * @param key   the key against which data needs to be stored
     * @param value the data to be stored
     * @return true, if data is stored successfully, otherwise returns false
     */
    public boolean saveFloat(String key, Float value) {
        return sharedPreferences.edit().putFloat(key, value).commit();
    }

    /**
     * method to fetch Float data stored in shared preference against the specified key.
     *
     * @param key the key against which data needs to be fetched
     * @return the data stored against the specified key
     */
    public Object fetchFloat(String key) {
        return sharedPreferences.getFloat(key, Float.MIN_VALUE);
    }

    /**
     * Removes the data present in the shared preference against the specified key
     *
     * @param key the key against which data needs to be removed
     * @return true if data was removed successfully, otherwise returns false
     */
    public Boolean removeData(String key) {
        return sharedPreferences.edit().remove(key).commit();
    }

    /**
     * Method to check whether there is any data stored against the specified key
     *
     * @param key the key to be checked
     * @return true if some data is present against the specified key, otherwise returns false
     */
    public Boolean contains(String key) {
        return sharedPreferences.contains(key);
    }
}
