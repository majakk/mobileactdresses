package mobile.actdresses;

import java.io.IOException;
import java.util.List;

import android.app.Service;
import android.util.Log;
import android.os.IBinder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;



public class MADService extends Service implements SensorEventListener{
	private static final String TAG = "MADService";
	private int counter = 0;
	private SensorManager sensorManager;
    private float myAzimuth = 0;
    private float myPitch = 0;
    private float myRoll = 0;
    private float abssum = 0;
    private int current = -1;
   
    Bitmap pIcon, pAdam, pBertil; 
    protected static Service sv; 
    String v;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
	@Override
	public void onCreate() {
		sv = this;
		super.onCreate();
		Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate 1.1 ");
		
		pIcon = BitmapFactory.decodeResource(getResources(),R.drawable.black);
        pAdam = BitmapFactory.decodeResource(getResources(),R.drawable.silver);
        pBertil = BitmapFactory.decodeResource(getResources(),R.drawable.olive);
        
		//Real sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        this.registerReceiver(new BroadcastHandler(), new IntentFilter(Intent.ACTION_HEADSET_PLUG));
      
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
		sensorManager.unregisterListener(this);
		//this.oneThread.interrupt();
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");
		super.onStart(intent, startid);
		
		sensorManager.registerListener(this,
	            sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD ),
	            SensorManager.SENSOR_DELAY_UI);
		
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	      if(accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
	    	  ;
	    	  //Log.d(TAG, "Unreliable 1");
	}

	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

		//Log.d(TAG, Float.toString(SensorManager.getInclination(event.values)));
		
		counter +=1;
		if(counter>=15){
			counter =0;
			
			if(event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
				Log.d(TAG, "Unreliable 2");
			myAzimuth = Math.round(event.values[0]);
			myPitch = Math.round(event.values[1]);
			myRoll = Math.round(event.values[2]);
			abssum = Math.abs(myAzimuth) + Math.abs(myPitch) + Math.abs(myRoll);
			Log.d(TAG,"onSensorChanged:"+abssum);   	
			
			if (abssum < 300 && current != 0){
				Log.d(TAG,"Icon displayed");
				current = 0;
				this.updateWallpaper(pIcon);
				//Toast.makeText(this, "0", Toast.LENGTH_LONG).show();
			}
			else if(abssum >= 200 && abssum <= 1000 && current != 1){
				Log.d(TAG,"Adam displayed");
				current = 1;
				this.updateWallpaper(pAdam);
				//Toast.makeText(this, "1", Toast.LENGTH_LONG).show();
				
			}
			else if(abssum > 1000 && current != 2){
				Log.d(TAG,"Bertil displayed");
				current = 2;    	   
				this.updateWallpaper(pBertil);
				//Toast.makeText(this, "2", Toast.LENGTH_LONG).show();    	   	
			}
		}
         
   }
	
	public void updateWallpaper(Bitmap a){ 
        /*try{
        	Thread.sleep(2000);
        }catch(Exception e){
        	Log.d(TAG,"Thread.sleep:");
        }*/
		
		
		try{
			//sv.clearWallpaper();
			sv.setWallpaper(a);
		}catch(IOException ez){
			ez.printStackTrace();    
		}
		
	}
	

	
}


