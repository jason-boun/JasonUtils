package com.jason.jasonutils.dimesionalcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import com.jason.jasonutils.tools.MLog;
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
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jason.jasonutils.R;

/**
 * 工具类：二维码图片信息生成、保存、解析
 * @author jiabo
 */
public class DimensionaLCodeTool {
	
	private static final String TAG = "DimensionaLCode"; 
	private Context context;
	public DimensionaLCodeTool(Context context){
		this.context = context;
	}

	/**
	 * 生成二维码图片
	 * @param textInfo
	 * @param savePicName
	 * @param showPicImage
	 */
	public Bitmap createImage(String textInfo, String savePicName) {
        try {
            int QR_WIDTH=500;int QR_HEIGHT=500;//设置生成的图片大小
            int photoWidth ,photoHeight ; //头像大小 
            int padding = 8;
            
            if (textInfo == null || "".equals(textInfo) || textInfo.length() < 1) {
                return null;
            }

            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
            
            BitMatrix bitMatrix = new QRCodeWriter().encode(textInfo, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            
            Bitmap[] bitmaps=new Bitmap[2];
            //头像
			bitmaps[1]=getPhotoBitmap(textInfo, new int[]{Math.round(QR_WIDTH*6/25), Math.round(QR_HEIGHT*6/25)});
			photoWidth=bitmaps[1].getWidth();
			photoHeight=bitmaps[1].getHeight();
			
			MLog.i(TAG, "photoWidth="+photoWidth+",photoHeight="+photoHeight);
			
			int startW=QR_WIDTH/2-photoWidth/2;
			int starH=QR_HEIGHT/2-photoHeight/2;
    
            for (int y = 0; y < QR_HEIGHT; y++) 
            {
                for (int x = 0; x < QR_WIDTH; x++) 
                {
                	if(((x<=startW-padding||x>=startW+photoWidth+ padding) || (y<=starH-padding||y>=starH+photoHeight+padding))){
                		if (bitMatrix.get(x, y)){
                            pixels[y * QR_WIDTH + x] = 0xff000000;
                        } else {
                            pixels[y * QR_WIDTH + x] = 0xffffffff;
                        }
                	}
                }
            }
            
            Bitmap bitmapDimenInfo = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,Bitmap.Config.ARGB_8888);
            bitmapDimenInfo.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			bitmaps[0]=bitmapDimenInfo;
            
            //showPicImage.setImageBitmap(combineBitmaps(bitmaps, startW,starH));
            
            String path = Environment.getExternalStorageDirectory().toString();
            System.out.println(path);
            try {
            	Bitmap finalBitmap = combineBitmaps(bitmaps, startW,starH);
				saveMyBitmap(finalBitmap, path, savePicName);
				return finalBitmap;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	/**
	 * 绘制带有头像的Bitamp
	 * @param bitmaps
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap combineBitmaps(Bitmap[] bitmaps,int w,int h) {
		Bitmap newBitmap = Bitmap.createBitmap(bitmaps[0].getWidth(),bitmaps[0].getHeight(), Config.ARGB_8888);
		Canvas cv = new Canvas(newBitmap);	
		for (int i = 0; i < bitmaps.length; i++) {
			if (i == 0) {
				cv.drawBitmap(bitmaps[0], 0, 0, null);
			} else {				
				cv.drawBitmap(bitmaps[i], w, h, null);
			}
			cv.save(Canvas.ALL_SAVE_FLAG);
			cv.restore();
		}
		return newBitmap;
	}
	
	/**
	 * 读取本地数据库头像
	 * @return
	 */
	private Bitmap getPhotoBitmap(String textInfo, int[]params){
		//return drawRectfBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.favicon),10,10), params);
		return drawRectfBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.use_bg_dimen), params);
	}
	
	/**
	 * 画出指定大小的Bitmap
	 * @return
	 */
	public Bitmap drawRectfBitmap(Bitmap srcBitmap, int[]params ){
		Bitmap newBitmap = Bitmap.createBitmap(params[0], params[1], Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		RectF rc0 = new RectF(0,0,params[0],params[1]);
		canvas.drawBitmap(srcBitmap, null, rc0, null);
		return newBitmap;
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
        File f = new File(savePath, picName + ".jpg");
        if(f.exists()){
        	f.delete();
        }
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
    
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx,float roundPy) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPy, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
