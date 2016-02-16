package com.brilliantbear.imageloder;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Bear on 2016/2/6.
 */
public class MemoryCache {
    private LruCache<String, Bitmap> cache;

    public MemoryCache() {
        long maxMemoty = Runtime.getRuntime().maxMemory() / 8;
        cache = new LruCache<String, Bitmap>((int) maxMemoty) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }


    public Bitmap getBitmapFromMemory(String url) {
        return cache.get(url);
    }


    public void setBitmapToMemory(String url, Bitmap bitmap) {
        cache.put(url, bitmap);
    }
}
