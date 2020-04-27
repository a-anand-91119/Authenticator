package in.notyouraveragedev.authenticator.util;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * A singleton class to manage volley ImageLoader, Cache and Request Queues
 * <p>
 * Created by A Anand on 25-04-2020
 */
public class Controller extends Application {
    // singleton object
    private static Controller controller;

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private static final String TAG = Controller.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        controller = this;
    }

    public Controller() {

    }

    /**
     * Method to returns the instance of the Controller Class
     *
     * @return the instance of the controller class
     */
    public static synchronized Controller getInstance() {
        return controller;
    }

    /**
     * Method returns the instance of volley request queue.
     * If the queue has not been initialized yet, then the method initializes the request queue
     * with application context, so that it can be used anywhere in he application
     *
     * @return a volley request queue
     */
    private RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        return requestQueue;
    }

    /**
     * Method returns the instance of volley image loader.
     * If the image loader has not been initialized yet, then the method initializes the image loader
     * and returns it
     *
     * @return the instance of volley image loader
     */
    public ImageLoader getImageLoader() {
        if (imageLoader == null)
            imageLoader = new ImageLoader(requestQueue, new Cache());
        return imageLoader;
    }

    /**
     * Method adds a request to the request queue. If a tag is specified, then this tag will
     * be set in the request, otherwise the default tag will be used.
     *
     * @param request the request to be added to the request queue
     * @param tag     optional tag for the request
     * @param <T>     any type of request can be accepted
     */
    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(request);
    }

}
