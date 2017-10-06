package com.jason.jasonutils.dimesionalcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import com.jason.jasonutils.tools.MLog;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * 工具类：二维码图片信息生成、保存、解析
 * @author jiabo
 */
public class DimensionaLCode {
	

	/**
	 * 生成二维码图片
	 * @param textInfo
	 * @param savePicName
	 * @param showPicImage
	 */
	public void createImage(String textInfo, String savePicName, ImageView showPicImage) {
        try {
            // 需要引入core包
            QRCodeWriter writer = new QRCodeWriter();
            
            int QR_WIDTH=200,QR_HEIGHT=200;//设置生成的图片大小
            Bitmap bitmap = null;

            MLog.i("ecodeingView", "要生成的信息：" + textInfo);
            if (textInfo == null || "".equals(textInfo) || textInfo.length() < 1) {
                return;
            }

            // 把输入的文本转为二维码
            BitMatrix martix = writer.encode(textInfo, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);

            System.out.println("w:" + martix.getWidth() + "h:"  + martix.getHeight());

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(textInfo, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }

            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            
            String path = Environment.getExternalStorageDirectory().toString();
            System.out.println(path);
            
            showPicImage.setImageBitmap(bitmap);
            //saveMyBitmap(bitmap, path, savePicName);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * 打开摄像头
	 * @param act
	 */
	public void openCamera(Activity act){
		final File DIMEN_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/DimenCode");
		final int CAMERA_REQUEST = 1888;
		DIMEN_DIR.mkdirs();
		File mCurrentPhotoFile = new File(DIMEN_DIR, "DimenTemp.png");
		
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
		
		act.startActivityForResult(cameraIntent, CAMERA_REQUEST);
	}
	
	/**
	 * 解析二维码图片
	 * @param bitmap
	 * @param qr_result
	 */
    public void scanningImage(Bitmap bitmap, TextView qr_result) {
    	
    	Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");

        // 获得待解析的图片
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result result;
        try {
            result = reader.decode(bitmap1);
            result=reader.decode(bitmap1, hints );
            // 得到解析后的文字
            qr_result.setText(result.getText());
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 保存二维码图片
     * @param bm
     * @param savePath
     * @param picName
     * @throws IOException
     */
    public void saveMyBitmap(Bitmap bm, String savePath, String picName) throws IOException {
        File f = new File(savePath, picName + ".png");
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
                fOut = new FileOutputStream(f);
                bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }
    }
    
    /**
     * 工具类
     */
    final static class RGBLuminanceSource extends LuminanceSource { 
  	  
 	   private final byte[] luminances; 
 	  
 	   public RGBLuminanceSource(String path) throws FileNotFoundException { 
 	     this(loadBitmap(path)); 
 	   } 
 	  
 	   public RGBLuminanceSource(Bitmap bitmap) { 
 	     super(bitmap.getWidth(), bitmap.getHeight()); 
 	  
 	     int width = bitmap.getWidth(); 
 	     int height = bitmap.getHeight(); 
 	     int[] pixels = new int[width * height]; 
 	     bitmap.getPixels(pixels, 0, width, 0, 0, width, height); 
 	  
 	     // In order to measure pure decoding speed, we convert the entire image to a greyscale array 
 	     // up front, which is the same as the Y channel of the YUVLuminanceSource in the real app. 
 	     luminances = new byte[width * height]; 
 	     for (int y = 0; y < height; y++) { 
 	       int offset = y * width; 
 	       for (int x = 0; x < width; x++) { 
 	         int pixel = pixels[offset + x]; 
 	         int r = (pixel >> 16) & 0xff; 
 	         int g = (pixel >> 8) & 0xff; 
 	         int b = pixel & 0xff; 
 	         if (r == g && g == b) { 
 	           // Image is already greyscale, so pick any channel. 
 	           luminances[offset + x] = (byte) r; 
 	         } else { 
 	           // Calculate luminance cheaply, favoring green. 
 	           luminances[offset + x] = (byte) ((r + g + g + b) >> 2); 
 	         } 
 	       } 
 	     } 
 	   } 
 	  
 	   public byte[] getRow(int y, byte[] row) { 
 	     if (y < 0 || y >= getHeight()) { 
 	       throw new IllegalArgumentException("Requested row is outside the image: " + y); 
 	     } 
 	     int width = getWidth(); 
 	     if (row == null || row.length < width) { 
 	       row = new byte[width]; 
 	     } 
 	     System.arraycopy(luminances, y * width, row, 0, width); 
 	     return row; 
 	   } 
 	  
 	   // Since this class does not support cropping, the underlying byte array already contains 
 	   // exactly what the caller is asking for, so give it to them without a copy. 
 	   public byte[] getMatrix() { 
 	     return luminances; 
 	   } 
 	  
 	   private static Bitmap loadBitmap(String path) throws FileNotFoundException { 
 	     Bitmap bitmap = BitmapFactory.decodeFile(path); 
 	     if (bitmap == null) { 
 	       throw new FileNotFoundException("Couldn't open " + path); 
 	     } 
 	     return bitmap; 
 	   } 
 	  
 	 } 
}
