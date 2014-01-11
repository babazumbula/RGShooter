package com.real.garant.shooter;

import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	   
	private static final String TAG = "error_tags";
	private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
    	   // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
    	
        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
          	mCamera.setPreviewDisplay(holder);
            Camera.Parameters parameters=mCamera.getParameters();
            List<Size> sizes=parameters.getSupportedPictureSizes();
            parameters.setPictureSize(sizes.get(sizes.size()-1).width, sizes.get(sizes.size()-1).height);
            List<String> focusModes = parameters.getSupportedFocusModes();
            parameters. setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);  
            mCamera.setParameters(parameters);
            mCamera.startPreview();
  
            
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
       
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
       
    }

    
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
     
    }
    
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    	
    	
    }



 

    private Camera.AutoFocusCallback mAutoFocusListener =  
    	    new Camera.AutoFocusCallback() {    
    	    @Override  
    	    public void onAutoFocus(boolean success, Camera camera) {  
    	      Log.i(TAG,"AutoFocus xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx: " + success);  
    	      camera.autoFocus(null);  
    	     
    	    }  
    	  }; 



}
