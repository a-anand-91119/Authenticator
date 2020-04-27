package in.notyouraveragedev.authenticator.util;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Custom Cache class to manage the images sent via volley library
 * <p>
 * Created by A Anand on 25-04-2020
 */
public class Cache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public Cache(int maxSize) {
        super(maxSize);
    }

    /**
     * Constructor to create cache with default size
     */
    public Cache() {
        this(getDefaultCacheSize());
    }

    /**
     * Method returns the default cache size;
     * The default cache size is one eighth of the total memory
     *
     * @return the default cache size
     */
    private static int getDefaultCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        return maxMemory / 8;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }
}
