package com.jason.jasonutils.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {
	 
    private MemoryCache memoryCache = new MemoryCache();
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    // 线程池
    private ExecutorService executorService;

    public ImageLoader(Context context) {
        executorService = Executors.newFixedThreadPool(5);
    }

    /**
     * 最主要的方法
     */
    public void DisplayImage(ImageGetter imageGetter, ImageView imageView, boolean isLoadOnlyFromCache) {
        imageViews.put(imageView, imageGetter.getKey());
        Bitmap bitmap = memoryCache.get(imageGetter.getKey()); // 先从内存缓存中查找
        if (bitmap != null&& !bitmap.isRecycled()) {
        	imageView.setImageBitmap(bitmap);
        }else if (!isLoadOnlyFromCache){// 若没有的话则开启新线程加载图片
        	try{
        		queuePhoto(imageGetter, imageView);
        	}catch(OutOfMemoryError e){
        		e.printStackTrace();
        	}
        }
    }

    private void queuePhoto(ImageGetter imageGetter, ImageView imageView) {
        executorService.submit(new PhotosLoader(new PhotoToLoad(imageGetter, imageView)));
    }

    /**
     * 最后从指定的url中下载图片
     */
    private Bitmap getBitmap(ImageGetter imageGetter) {
        Bitmap bitmap = imageGetter.getBitmap();// 
        return zoomImg(bitmap, 90, 90);
    }

    /**
     * decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
     */
    private Bitmap decodeFile(File f) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            final int REQUIRED_SIZE = 100;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    /**
     * 封装对应的getter和imageView
     */
    private class PhotoToLoad {
        public ImageGetter imageGetter;
        public ImageView imageView;

        public PhotoToLoad(ImageGetter imageGetter, ImageView i) {
            this.imageGetter = imageGetter;
            imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            if (imageViewReused(photoToLoad)){
            	return;
            }
            Bitmap bmp = getBitmap(photoToLoad.imageGetter);
            memoryCache.put(photoToLoad.imageGetter.getKey(), bmp);
            if (imageViewReused(photoToLoad)){
            	return;
            }
            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
            ((Activity) photoToLoad.imageView.getContext()).runOnUiThread(bd);// 更新的操作放在UI线程中
        }
    }

    /**
     * 防止图片错位
     * @param photoToLoad
     * @return
     */
    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.imageGetter.getKey()))
            return true;
        return false;
    }

    /**
     * 用于在UI线程中更新界面
     */
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad)){
            	return;
            }
            if (bitmap != null&& !bitmap.isRecycled()){
            	photoToLoad.imageView.setImageBitmap(bitmap);
            }
        }
    }

    public void clearCache() {
        memoryCache.clear();
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                        break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            Log.e("", "CopyStream catch Exception...");
        }
    }

	// 缩放图片
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scale = 0;
		if (width <= newWidth && height <= newHeight) {
			scale = 1;
		} else if (width > height && width > newWidth) {
			scale = ((float) newWidth) / width;
		} else if (height > width && height > newHeight) {
			scale = ((float) newHeight) / height;
		} else {
			scale = ((float) newHeight) / height;
		}

		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		return newbm;
	}
    
}