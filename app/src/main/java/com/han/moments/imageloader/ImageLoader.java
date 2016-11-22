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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import rx.Subscriber;

public class ImageLoader {
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;

    private static int sImageWidth;

    private static Context sContext;
    private static ImageLoader instance;
    private static ExecutorService sThreadPool = Executors.newCachedThreadPool();
    private LruCache<String, Bitmap> mLruCache;
    private DiskLruCache mDiskCache;

    /**
     * 初始化ImageLoader
     *
     * @param context
     * @param width   预想的图片宽度
     */
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
        // end

        // begin 初始化DiskLruCache
        try {
            mDiskCache = DiskLruCache.open(getCacheDir(), getAppVersion(), 1, 10 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // end
    }

    public void load(final ImageView imageView, final String url) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SUCCESS:
                        imageView.setImageBitmap((Bitmap) msg.obj);
                        break;
                    case FAILED:
                        imageView.setImageBitmap(null);
                        break;
                }
            }
        };

        Bitmap bmp = getFromLru(url);
        if (null != bmp) {
            Log.i(ImageLoader.class.getSimpleName(), "getFromLru");
            Message msg = handler.obtainMessage(SUCCESS, bmp);
            msg.sendToTarget();
            return;
        }

        bmp = getFromDisk(url);
        if (null != bmp) {
            Log.i(ImageLoader.class.getSimpleName(), "getFromDisk");
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

    // 缩放图片
    private Bitmap scaleImage(Bitmap bmp) {
        int sample = bmp.getWidth() / sImageWidth;
        if (sample <= 0){
            sample = 1;
        }
        int height = bmp.getHeight() / sample;
        return ThumbnailUtils.extractThumbnail(bmp, sImageWidth, height);
    }

    // 从memory中获取
    private Bitmap getFromLru(String url) {
        return mLruCache.get(MD5.getMD5(url));
    }

    // 添加到内存中
    private void addToLru(String url, Bitmap bmp) {
        if (getFromLru(url) == null) {
            System.out.println("++addToLru");
            mLruCache.put(MD5.getMD5(url), bmp);
        }
    }

    // 从本地缓存获取
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

    // 添加到本地缓存
    private void addToDisk(String url, Bitmap bmp) throws Exception {
        System.out.println("+++addtoDisk");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] buf = baos.toByteArray();

        DiskLruCache.Editor editor = mDiskCache.edit(MD5.getMD5(url));
        OutputStream out = editor.newOutputStream(0);
        out.write(buf);
        out.flush();
        editor.commit();
    }

    // 获取缓存目录
    private File getCacheDir() {
        File dir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = new File(sContext.getExternalCacheDir().getPath() + File.separator + "images" + File.separator);
        } else {
            dir = new File(sContext.getCacheDir().getPath() + File.separator + "images" + File.separator);
        }
        return dir;
    }

    // 获取软件版本
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
                Log.d(this.toString(), "file download: " + fileSizeDownloaded + " of " + fileSize);
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