package com.brilliantbear.imageloder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Bear on 2016/2/6.
 */
public class DiskCache {
    private String path;

    public DiskCache(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public Bitmap getBitmapFromDisk(String url) {

        if (!TextUtils.isEmpty(path)) {
            try {
                String fileName = MD5Encoder.encode(url);
                File file = new File(path, fileName);

                if (file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                    return bitmap;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;
    }


    public void setBitmapToDisk(String url, Bitmap bitmap) {
        try {
            String filename = MD5Encoder.encode(url);
            File file = new File(path, filename);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
