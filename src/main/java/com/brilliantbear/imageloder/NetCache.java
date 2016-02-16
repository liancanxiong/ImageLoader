package com.brilliantbear.imageloder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.brilliantbear.imageloder.net.OkHttpUtils;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Bear on 2016/2/6.
 */
public class NetCache {

    private DiskCache diskCache;
    private MemoryCache memoryCache;

    public NetCache(DiskCache diskCache, MemoryCache memoryCache) {
        this.diskCache = diskCache;
        this.memoryCache = memoryCache;
    }

    public NetCache(DiskCache diskCache) {
        this.diskCache = diskCache;
    }

    public NetCache(MemoryCache memoryCache) {
        this.memoryCache = memoryCache;
    }

    public NetCache() {
    }

    public void getBitmapFromNet(ImageView iv, String url) {
        iv.setTag(url);
        new BitmapTask().execute(iv, url);
    }


    class BitmapTask extends AsyncTask<Object, Void, Bitmap> {

        private ImageView iv;
        private String url;

        @Override
        protected Bitmap doInBackground(Object... params) {
            iv = (ImageView) params[0];
            url = (String) params[1];

            return downloadBitmap(url);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null && url.equals(iv.getTag())) {
                iv.setImageBitmap(bitmap);
            }
        }
    }

    private Bitmap downloadBitmap(String url) {
        try {
            Response response = OkHttpUtils.get(url);
            if (response.code() == 200) {
                Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());

                if (diskCache != null) {
                    diskCache.setBitmapToDisk(url, bitmap);
                }

                if (memoryCache != null) {
                    memoryCache.setBitmapToMemory(url, bitmap);
                }
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
