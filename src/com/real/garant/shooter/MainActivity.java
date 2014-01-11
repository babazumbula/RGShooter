package com.real.garant.shooter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.googlecode.tesseract.android.TessBaseAPI;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.view.Menu;

public class MainActivity extends Activity {

	protected static final String TAG = "nekiniz";
	public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/RGShooter/";
	public String _path = DATA_PATH + "scannedDoc.jpg";
	public static final String lang = "eng";
	public String recognizedText;
	private Camera mCamera;
	 private CameraPreview mPreview;
	 CustomDrawableView mCustomDrawableView;
	 Button  buttonScan;
	 String fileName;
	 ProgressBar pb;
	 
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	    	
	    	String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };
	    	requestWindowFeature(Window.FEATURE_NO_TITLE);
			for (String path : paths) {
				File dir = new File(path);
				if (!dir.exists()) {
					if (!dir.mkdirs()) {
						Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
						return;
					} else {
						Log.v(TAG, "Created directory " + path + " on sdcard");
					}
				}

			}
			

			if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
				try {

					AssetManager assetManager = getAssets();
					InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
					//GZIPInputStream gin = new GZIPInputStream(in);
					OutputStream out = new FileOutputStream(DATA_PATH + "tessdata/" + lang + ".traineddata");

					// Transfer bytes from in to out
					byte[] buf = new byte[1024];
					int len;
					//while ((lenf = gin.read(buff)) > 0) {
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					in.close();
					//gin.close();
					out.close();
					
					Log.v(TAG, "Copied " + lang + " traineddata");
				} catch (IOException e) {
					Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
				}
			}

	    	
	        super.onCreate(savedInstanceState);
	        
	        setContentView(R.layout.activity_main);
	        buttonScan = (Button) findViewById(R.id.buttonScan);
	        // Create an instance of Camera
	        
	        pb = (ProgressBar) findViewById(R.id.progressBar);
	        // Create our Preview view and set it as the content of our activity.

	    }

	    
	    
	    
	    
	    
	    @Override
		protected void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			
		}






		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			if(mCamera == null){
			mCamera = getCameraInstance();
			}
			mPreview = new CameraPreview(this, mCamera);
		    FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		    mCustomDrawableView = new CustomDrawableView(this);
		    preview.addView(mPreview);
		    preview.addView(mCustomDrawableView);
		        
		        buttonScan.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						// TODO Auto-generated method stub
						mCamera.autoFocus(new AutoFocusCallback(){
							@Override
							public void onAutoFocus(boolean arg0, Camera arg1) {
								mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
								
								
								
							}
						});
						
					}
				});
		        
		}


		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			releaseCamera();
			
		}





		@Override
		protected void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
			
		}


		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			
		}


		public  Camera getCameraInstance(){
			
	        Camera c = null;
	        try {
	            c = Camera.open(); // attempt to get a Camera instance
	        }
	        catch (Exception e){
	        	e.printStackTrace();
	        }
	        return c; // returns null if camera is unavailable
	    }
		
		private void releaseCamera(){
	        if (mCamera != null){
	            mCamera.release();        // release the camera for other applications
	            mCamera = null;
	        }
	    }

		ShutterCallback shutterCallback = new ShutterCallback() {
			public void onShutter() {
				// Log.d(TAG, "onShutter'd")
			}
		};

		PictureCallback rawCallback = new PictureCallback() {
			public void onPictureTaken(byte[] data, Camera camera) {
				// Log.d(TAG, "onPictureTaken - raw");
			}
		};

		PictureCallback jpegCallback = new PictureCallback() {
			public void onPictureTaken(byte[] data, Camera camera) {
				FileOutputStream outStream = null;
				
				try {
					// Write to SD Card

					fileName =  _path; 
					outStream = new FileOutputStream(fileName);
					outStream.write(data);
					outStream.close();
					Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);

					resetCam();

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
				}
				Log.d(TAG, "onPictureTaken - jpeg");

				new MyTask().execute();

			}

		};
		

		
		private void resetCam() {
			mCamera.startPreview();
			
		}
		
		
		class MyTask extends AsyncTask<String, String, String>{



			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				buttonScan.setVisibility(View.INVISIBLE);
				pb.setVisibility(View.VISIBLE);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				Intent i = new Intent(MainActivity.this, DisplayScannedData.class);
				i.putExtra("scannedData", result);
				pb.setVisibility(View.INVISIBLE);
				buttonScan.setVisibility(View.VISIBLE);
				startActivity(i);
				
			}

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				
				Bitmap bitmap = BitmapFactory.decodeFile(_path, options);
				
				try {
				ExifInterface exif = new ExifInterface(_path);
				int exifOrientation = exif.getAttributeInt(
				ExifInterface.TAG_ORIENTATION,
				ExifInterface.ORIENTATION_NORMAL);
				
				Log.v(TAG, "Orient: " + exifOrientation);
				
				int rotate = 0;
				
				switch (exifOrientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
				case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
				case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
				}
				
				Log.v(TAG, "Rotation: " + rotate);
				
				if (rotate != 0) {
				
				// Getting width & height of the given image.
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();
				
				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);
				
				// Rotating Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
				}
				
				// Convert to ARGB_8888, required by tess
				bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
				
				} catch (IOException e) {
				Log.e(TAG, "Couldn't correct orientation: " + e.toString());
				}
				
				// _image.setImageBitmap( bitmap );
				
				Log.v(TAG, "Before baseApi");
				
				TessBaseAPI baseApi = new TessBaseAPI();
				baseApi.setDebug(true);
				baseApi.init(DATA_PATH, lang);
				baseApi.setImage(bitmap);
				
				recognizedText = baseApi.getUTF8Text();
				
				baseApi.end();
				
				return recognizedText;
				
				
			}


			
			
		}

}
