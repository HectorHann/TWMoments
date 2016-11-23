package com.han.moments.imageloader;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.han.moments.http.HttpTools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by Han on 2016/11/22.
 */
public class ImageLoader {
    private static final String TAG = ImageLoader.class.getSimpleName();
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;

    private static int sImageWidth;

    private static Context sContext;
    private LruCache<String, Bitmap> mLruCache;
    private DiskLruCache mDiskCache;

    public static void init(Context context, int width) {
        sContext = context;
        sImageWidth = width;
    }

    public static ImageLoader getInstance() {
        return Holder.instance;
    }

    static class Holder {
        private static ImageLoader instance = new ImageLoader();
    }


    private ImageLoader() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int maxSize = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        try {
            mDiskCache = DiskLruCache.open(getCacheDir(), getAppVersion(), 1, 10 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(final ImageView imageView, final String url) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SUCCESS:
                        Log.i(TAG, "load success. url=" + url);
                        imageView.setImageBitmap((Bitmap) msg.obj);
                        break;
                    case FAILED:
                        Log.i(TAG, "load failed. url=" + url);
                        imageView.setImageBitmap(null);
                        break;
                }
            }
        };

        Bitmap bmp = getFromLru(url);
        if (null != bmp) {
            Log.d(TAG, "getFromLru url=" + url);
            Message msg = handler.obtainMessage(SUCCESS, bmp);
            msg.sendToTarget();
            return;
        }

        bmp = getFromDisk(url);
        if (null != bmp) {
            Log.d(TAG, "getFromDisk url=" + url);
            Message msg = handler.obtainMessage(SUCCESS, bmp);
            msg.sendToTarget();
            return;
        }

        HttpTools.getInstance().downPic(new Subscriber<ResponseBody>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Message msg = handler.obtainMessage(FAILED);
                msg.sendToTarget();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                Bitmap bitmap = writetoSDCard(responseBody, MD5.getMD5(url));
                bitmap = scaleImage(bitmap);

                try {
                    addToDisk(url, bitmap);
                    addToLru(url, bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage(SUCCESS, bitmap);
                msg.sendToTarget();
            }
        }, url);
    }

    private Bitmap scaleImage(Bitmap bmp) {
        int sample = bmp.getWidth() / sImageWidth;
        if (sample <= 0) {
            sample = 1;
        }
        int height = bmp.getHeight() / sample;
        return ThumbnailUtils.extractThumbnail(bmp, sImageWidth, height);
    }

    private Bitmap getFromLru(String url) {
        return mLruCache.get(MD5.getMD5(url));
    }

    private void addToLru(String url, Bitmap bmp) {
        if (getFromLru(url) == null) {
            mLruCache.put(MD5.getMD5(url), bmp);
        }
    }

    private Bitmap getFromDisk(String url) {
        Bitmap bmp = null;
        try {
            DiskLruCache.Snapshot snapshot = mDiskCache.get(MD5.getMD5(url));
            InputStream in = snapshot.getInputStream(0);
            bmp = BitmapFactory.decodeStream(in);
            addToLru(url, bmp);
            in.close();
        } catch (Exception e) {
        }
        return bmp;
    }

    private void addToDisk(String url, Bitmap bmp) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] buf = baos.toByteArray();

        DiskLruCache.Editor editor = mDiskCache.edit(MD5.getMD5(url));
        OutputStream out = editor.newOutputStream(0);
        out.write(buf);
        out.flush();
        editor.commit();
    }

    private File getCacheDir() {
        File dir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = new File(sContext.getExternalCacheDir().getPath() + File.separator + "images" + File.separator);
        } else {
            dir = new File(sContext.getCacheDir().getPath() + File.separator + "images" + File.separator);
        }
        return dir;
    }

    private int getAppVersion() {
        PackageInfo pi;
        try {
            pi = sContext.getPackageManager().getPackageInfo(sContext.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Bitmap writetoSDCard(ResponseBody body, String filename) {
        Bitmap bitmap = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            File futureStudioIconFile = new File(sContext.getExternalFilesDir(null) + File.separator + filename);
            byte[] fileReader = new byte[4096];

            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;

            inputStream = body.byteStream();
            outputStream = new FileOutputStream(futureStudioIconFile);

            while (true) {
                int read = inputStream.read(fileReader);

                if (read == -1) {
                    break;
                }

                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;
            }

            outputStream.flush();

            bitmap = BitmapFactory.decodeFile(futureStudioIconFile.getPath());
            if (inputStream != null) {
                inputStream.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {

        }
        return bitmap;
    }
}