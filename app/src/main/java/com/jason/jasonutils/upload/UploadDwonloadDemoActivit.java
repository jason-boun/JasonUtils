package com.jason.jasonutils.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;

public class UploadDwonloadDemoActivit extends Activity implements OnClickListener {
	
	private EditText et_upload_file_path;
	private Button bt_upload_demo,bt_download_demo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_download_demo_activity);
		init();
		initDownLoad();
	}

	private void init() {
		et_upload_file_path = (EditText) findViewById(R.id.et_upload_file_path);
		
		bt_upload_demo = (Button) findViewById(R.id.bt_upload_demo);
		bt_download_demo = (Button) findViewById(R.id.bt_download_demo);
		
		bt_upload_demo.setOnClickListener(this);
		bt_download_demo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_upload_demo:
			upload();
			break;
		case R.id.bt_download_demo:
			download();
			break;
		}
	}
	
	/**
	 * 上传文件
	 */
	public void upload(){
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://192.168.1.100:8080/web/UploadServlet");;
		try {
			String path = et_upload_file_path.getText().toString().trim();
			if(TextUtils.isEmpty(path)){
				return ;
			}
			File file = new File(path);
			if(file.exists()&&file.length()>0){
				Part[] parts = {new StringPart("nameaaaa", "valueaaa"),  new StringPart("namebbb", "valuebbb"), new FilePart("pic", new File(file.getAbsolutePath()))};
				post.setRequestEntity(new MultipartRequestEntity(parts, post.getParams()));
				client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
				int status = client.executeMethod(post);
				if(status ==200){
					Toast.makeText(this, "上传成功", 1).show();
				}else{
					Toast.makeText(this, "上传失败", 1).show();
				}
			}else{
				Toast.makeText(this, "上传的文件不存在", 0).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			post.releaseConnection();
		}
	}
	
	/********************************************* 文件下载、断点续传 ***************************************************************/
	protected static final int DOWN_LOAD_ERROR = 1;
	protected static final int SERVER_ERROR = 2;
	public static final int DOWN_LAOD_FINSIH = 3;
	public static final int UPDATE_TEXT = 4;
	private EditText et_path;
	private ProgressBar pb; //下载的进度条.
	public static int threadCount = 3;
	public static int runningThread = 3 ;
	public int currentProcess = 0; //当前进度.
	private TextView tv_process;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DOWN_LOAD_ERROR:
				Toast.makeText(getApplicationContext(), "下载失败", 0).show();
				break;
			case SERVER_ERROR:
				Toast.makeText(getApplicationContext(), "服务器 错误,下载失败", 0).show();
				break;
			case DOWN_LAOD_FINSIH:
				Toast.makeText(getApplicationContext(), "文件下载完毕", 0).show();
				break;
			case UPDATE_TEXT:
				tv_process.setText("当前进度:"+pb.getProgress()*100/pb.getMax());
				break;
			}
		};
	};
	
	public void initDownLoad(){
		et_path = (EditText) this.findViewById(R.id.et_path);
		pb = (ProgressBar) findViewById(R.id.pb);
		tv_process = (TextView) findViewById(R.id.tv_process);
	}
	
	/**
	 * 下载文件
	 */
	public void download(){
		final String path = et_path.getText().toString().trim();
		if (TextUtils.isEmpty(path)) {
			Toast.makeText(this, "下载路径错误", 0).show();
			return;
		}
		currentProcess = 0;
		new Thread() {
			public void run() {
				try {
					//String path = "http://192.168.1.100:8080/360.exe";
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET");
					int code = conn.getResponseCode();
					if (code == 200) {
						int length = conn.getContentLength();// 服务器返回的数据的长度 实际上就是文件的长度
						pb.setMax(length);//设置进度条的最大值.
						RandomAccessFile raf = new RandomAccessFile("/sdcard/setup.exe", "rwd");// 在客户端本地 创建出来一个大小跟服务器端文件一样大小的临时文件
						raf.setLength(length);// 指定创建的这个文件的长度
						raf.close();

						// 假设是3个线程去下载资源.平均每一个线程下载的文件的大小.
						int blockSize = length / threadCount;
						for (int threadId = 1; threadId <= threadCount; threadId++) {
							int startIndex = (threadId - 1) * blockSize;// 每个线程下载的开始位置
							int endIndex = threadId * blockSize - 1;
							if (threadId == threadCount) {// 最后一个线程 下载的长度结尾处为文件的结尾处。
								endIndex = length;
							}
							System.out.println("线程:" + threadId + "下载:---"+ startIndex + "--->" + endIndex);
							new DownloadThread(path, threadId, startIndex, endIndex).start();
						}
					} else {
						handler.sendEmptyMessage(SERVER_ERROR);
					}
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(DOWN_LOAD_ERROR);
				}
			};
		}.start();
	}

	/**
	 * 下载文件的子线程 每一个线程 下载对应位置的文件
	 */
	public  class DownloadThread extends Thread {
		private int threadId;
		private int startIndex;
		private int endIndex;
		private String path;

		/**
		 * @param path 下载文件在服务器上的路径
		 * @param threadId  线程id
		 * @param startIndex 线程下载的开始位置
		 * @param endIndex 线程下载的结束位置.
		 */
		public DownloadThread(String path, int threadId, int startIndex, int endIndex) {
			this.threadId = threadId;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.path = path;
		}

		@Override
		public void run() {
			try {
				// 检查是否存在 记录下载长度的文件 ,如果存在读取这个文件的数据.
				//------------------替换成存数据库-------------------------
				File tempFile = new File("/sdcard/"+threadId + ".txt");
				if (tempFile.exists() && tempFile.length() > 0) {
					FileInputStream fis = new FileInputStream(tempFile);
					byte[] temp = new byte[1024];
					int leng = fis.read(temp);
					String downloadLen = new String(temp, 0, leng);
					int downloadlenInt = Integer.parseInt(downloadLen);
					
					int alreadyDownlodint = downloadlenInt - startIndex ;
					currentProcess+=alreadyDownlodint; //计算上次断点 已经下载的文件的长度.
					startIndex = downloadlenInt;//修改下载的真实的开始位置.
					fis.close();
				}
				//--------------------------------------------
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				// 重要: 请求服务器下载部分的文件 指定文件的位置.
				conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
				System.out.println("线程真实下载:" + threadId + "下载:---" + startIndex + "--->" + endIndex);
				conn.setConnectTimeout(5000);
				int code = conn.getResponseCode(); // 从服务器请求全部资源 200 ok,如果从服务器请求部分资源 206 ok
				if (code == 206) {
					InputStream is = conn.getInputStream();// 已经设置了 请求的位置
					RandomAccessFile raf = new RandomAccessFile("/sdcard/setup.exe", "rwd");
					// 随机写文件的时候 从哪个位置开始写
					raf.seek(startIndex);// 定位文件
					int len = 0;
					byte[] buffer = new byte[1024];
					int total = 0;// 已经下载的数据长度
					while ((len = is.read(buffer)) != -1) {
						RandomAccessFile file = new RandomAccessFile("/sdcard/"+threadId + ".txt", "rwd");// 作用: 记录当前线程下载的数据长度
						raf.write(buffer, 0, len);
						total += len;
						file.write(( total+startIndex+"").getBytes());//记录的是 下载位置.
						file.close();
						
						//更新进度条
						synchronized (UploadDwonloadDemoActivit.this) {
							currentProcess+=len;//获取所有线程下载的总进度.
							pb.setProgress(currentProcess);//更改界面上progressbar 进度条的进度 ，进度条对话框 可以直接在子线程里面更新ui，内部代码 特殊处理
							handler.sendEmptyMessage(UPDATE_TEXT);
						}
					}
					is.close();
					raf.close();
					System.out.println("线程:" + threadId + "下载完毕了...");
				} else {
					System.out.println("线程:" + threadId + "下载失败...");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				threadFinish();
			}
		}

		private synchronized void threadFinish() {
			runningThread --;
			if(runningThread==0){//所有的线程 已经执行完毕了,清楚掉 下载的记录，如何去判断应用程序已经下载完毕.
				for(int i= 1;i<=3;i++){
					File file = new File("/sdcard/"+i+".txt");
					file.delete();
				}
				System.out.println("文件下载完毕 ,删除所有的下载记录.");
				handler.sendEmptyMessage(DOWN_LAOD_FINSIH);
			}
		}
	}
}
