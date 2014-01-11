package com.real.garant.shooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class CustomDrawableView extends View{
	 
	Context ctx;
	private ShapeDrawable mDrawable;
	Paint paint = new Paint();
	public CustomDrawableView(Context context) {
		super(context);

		  ctx = context;
	      int x = 10;
	      int y = 10;
	      int width = 300;
	      int height = 300;

	      mDrawable = new ShapeDrawable(new OvalShape());
	      mDrawable.getPaint().setColor(0xff74AC23);
	      mDrawable.setBounds(x, y, x + width, y + height);
	      }

	      protected void onDraw(Canvas canvas) {
	    	  
		        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		        Display display = wm.getDefaultDisplay();
		        Point size = new Point();
		        display.getSize(size);
		        int width = size.x;
		        int height = size.y;
		    	
		        paint.setColor(Color.RED);
		        canvas.drawRect(20, 20, 80, 23, paint);			// up-lh
		    	
		        paint.setColor(Color.RED);
		        canvas.drawRect(20, 20, 23, 80, paint);			// up-lv
		        
		        paint.setColor(Color.RED);
		        canvas.drawRect(width-80, 20, width-20, 23, paint);		// up-rh
		        
		        paint.setColor(Color.RED);
		        canvas.drawRect(width-23, 20, width-20, 80, paint);		// up-rv
		        
		        paint.setColor(Color.RED);
		        canvas.drawRect(20, height-70, 80, height-73, paint);			// down-lh
		        
		        paint.setColor(Color.RED);
		        canvas.drawRect(20, height-130, 23, height-73, paint);			//down-lv
		        
		        paint.setColor(Color.RED);
		        canvas.drawRect(width-80, height-70, width-20, height-73, paint);			// down-rh
		        
		        paint.setColor(Color.RED);
		        canvas.drawRect(width-23, height-130, width-20, height-73, paint);			//down-rv

	      


	}

}
