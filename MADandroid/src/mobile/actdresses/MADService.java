package mobile.actdresses;

import java.io.IOException;
import java.util.List;

import android.app.Service;
import android.util.Log;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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
	protected static final int BG1 = 0x1000;
	protected static final int BG2 = 0x1001;
	protected static final int BG3 = 0x1002;
	
	private int counter = 0;
	private SensorManager sensorManager;
    private float myAzimuth = 0;
    private float myPitch = 0;
    private float myRoll = 0;
    private double abssum = 0;
    private int current = -1;
   
    Bitmap pScratch, pBluegreen, pBubbles; 
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
		
		pScratch = BitmapFactory.decodeResource(getResources(),R.drawable.scratch);
        pBluegreen = BitmapFactory.decodeResource(getResources(),R.drawable.bluegreen);
        pBubbles = BitmapFactory.decodeResource(getResources(),R.drawable.bubbles);
        
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
	            SensorManager.SENSOR_DELAY_NORMAL);
		
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
		if(counter>=5){
			counter =0;
			
			//if(event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
			//	Log.d(TAG, "Unreliable 2");
			myAzimuth = Math.round(event.values[0]);
			myPitch = Math.round(event.values[1]);
			myRoll = Math.round(event.values[2]);
			abssum = Math.sqrt(myAzimuth * myAzimuth + myPitch * myPitch + myRoll * myRoll);
			
			//Log.d(TAG,"onSensorChanged:"+abssum);   	
			
			if (abssum < 65 && current != 0){
				Log.d(TAG,"Icon displayed");
				current = 0;
				counter = -50;
				//this.updateWallpaper(pScratch);
				//Toast.makeText(this, "0", Toast.LENGTH_LONG).show();
				Message m1 = new Message();
            	m1.what = BG1;                            
            	messageHandler.sendMessage(m1);
			}
			else if(abssum >= 65 && abssum <= 110 && current != 1){
				Log.d(TAG,"Adam displayed");
				current = 1;
				counter = -50;
				//this.updateWallpaper(pBluegreen);
				//Toast.makeText(this, "1", Toast.LENGTH_LONG).show();
				Message m1 = new Message();
            	m1.what = BG2;                            
            	messageHandler.sendMessage(m1);
				
			}
			else if(abssum > 110 && current != 2){
				Log.d(TAG,"Bertil displayed");
				current = 2;  
				counter = -50;
				//this.updateWallpaper(pBubbles);
				//Toast.makeText(this, "2", Toast.LENGTH_LONG).show();  
				Message m1 = new Message();
            	m1.what = BG3;                            
            	messageHandler.sendMessage(m1);
			}
		}
         
    }
	
	//Method for setting the wallpaper
	public void updateWallpaper(Bitmap a){
		try{			
			sv.setWallpaper(a);
		}catch(IOException ez){
			ez.printStackTrace();    
		}
		
	}
	
	private Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){			
				case BG1:
					MADService.this.updateWallpaper(pScratch);
					break;
				case BG2:
					MADService.this.updateWallpaper(pBluegreen);
					break;
				case BG3:
					MADService.this.updateWallpaper(pBubbles);
					break;		
				default:
			}
		}
	};


	
}


