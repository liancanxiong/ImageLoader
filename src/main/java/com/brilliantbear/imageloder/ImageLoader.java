package com.brilliantbear.imageloder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.widget.ImageView;

import com.brilliantbear.imageloder.net.OkHttpUtils;
import com.squareup.okhttp.Response;

/**
 * Created by Bear on 2016/2/6.
 */
public class ImageLoader {

    private Context mContext;
    private final NetCache netCache;
    private String path;
    private final DiskCache diskCache;
    private final MemoryCache memoryCache;


    public ImageLoader(Context mContext) {
        this.mContext = mContext;

        path = mContext.getCacheDir().getPath();

        diskCache = new DiskCache(path);
        memoryCache = new MemoryCache();
        netCache = new NetCache(diskCache, memoryCache);
    }

    public void display(ImageView iv, String url) {
        iv.setImageResource(R.drawable.ic_launcher);
        Bitmap bitmap = null;

        bitmap = memoryCache.getBitmapFromMemory(url);
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
            return;
        }

        bitmap = diskCache.getBitmapFromDisk(url);
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
            return;
        }

        netCache.getBitmapFromNet(iv, url);

    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        diskCache.setPath(path);
    }


    public Point loadImageForSize(String url) {
        Point point = new Point();
        try {
            Response response = OkHttpUtils.get(url);
            if (response.code() == 200) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(response.body().byteStream(), null, options);

                point.x = options.outWidth;
                point.y = options.outHeight;


                return point;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
