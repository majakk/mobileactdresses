package mobile.actdresses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity{    	
	
	protected static final String TAG = "MainActivity";
    static String url1 = "market://search?q=pub:Coldstream Solutions";
    static String url2 = "https://play.google.com/store/apps/details?id=studio.coldstream.minecraftlwp";
    static String url3 = "http://db.tt/KuDw85j";
    
	
	Button b1;
	Button b2;
	//Button b3;
	//Button b4;
	//Button b5;
	
	AlertDialog.Builder builder;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
               
        

		
		b1 = (Button)findViewById(R.id.button1);
		b1.setOnClickListener(new OnClickListener() 
        {
			//@Override
			public void onClick(View v)
			{     			       			
    			Toast.makeText(MainActivity.this, "Choose '" + getText(R.string.app_name) + "' from the list to start the Live Wallpaper",Toast.LENGTH_LONG).show();
    			    			
    			try {
					startActivity(new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER));    			
					MainActivity.this.finish();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(MainActivity.this, "No default launcher detected :(",Toast.LENGTH_LONG).show();
	    			
					e.printStackTrace();
				}
    			
    			/*Intent startMain = new Intent(Intent.ACTION_MAIN);
    			startMain.addCategory(Intent.CATEGORY_HOME);
    			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			startActivity(startMain);*/
            }			
        });
		
		b2 = (Button)findViewById(R.id.button2);
		b2.setOnClickListener(new OnClickListener() 
        {
			//@Override
			public void onClick(View v)
			{     			       			
    			//Toast.makeText(MainActivity.this, "Choose '" + getText(R.string.app_name) + "' from the list to start the Live Wallpaper.",Toast.LENGTH_LONG).show();
    			
    			//startActivity(LiveWallpaperSettings.class);
    			/*Intent mainIntent = new Intent(MainActivity.this, LiveWallpaperSettings.class);
    			MainActivity.this.startActivity(mainIntent);
    			MainActivity.this.finish();*/
				
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				    //@Override
				    public void onClick(DialogInterface dialog, int which) {
				        switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				            //Yes button clicked
				        	PackageManager pm = getApplicationContext().getPackageManager(); 
							pm.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
				            break;

				        case DialogInterface.BUTTON_NEGATIVE:
				            //No button clicked
				            break;
				        }
				    }
				};

				builder = new AlertDialog.Builder(MainActivity.this);
				builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

				
				//PackageManager pm = getApplicationContext().getPackageManager(); 
				//pm.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            }			
        });		
		
		
		/*b3 = (Button)findViewById(R.id.button3);
		b3.setOnClickListener(new OnClickListener() 
        {
			//@Override
			public void onClick(View v)
			{     			       			
    			//Toast.makeText(MainActivity.this, "Choose '" + getText(R.string.app_name) + "' from the list to start the Live Wallpaper.",Toast.LENGTH_LONG).show();
    			
    			//startActivity(LiveWallpaperSettings.class);
    			
				share();
            }			
        });*/
		
		/*b4 = (Button)findViewById(R.id.button4);
		b4.setOnClickListener(new OnClickListener() 
        {
			//@Override
			public void onClick(View v)
			{     			       			
    			//Toast.makeText(MainActivity.this, "Choose '" + getText(R.string.app_name) + "' from the list to start the Live Wallpaper.",Toast.LENGTH_LONG).show();
    			
    			//startActivity(LiveWallpaperSettings.class);
    			
				Intent i = new Intent(Intent.ACTION_VIEW);  
				i.setData(Uri.parse(url1));  
				startActivity(i);  
            }			
        });*/
		
		/*b5 = (Button)findViewById(R.id.button5);
		b5.setOnClickListener(new OnClickListener() 
        {
			//@Override
			public void onClick(View v)
			{     			       			
    			//Toast.makeText(MainActivity.this, "Choose '" + getText(R.string.app_name) + "' from the list to start the Live Wallpaper.",Toast.LENGTH_LONG).show();
    			
    			//startActivity(LiveWallpaperSettings.class);
    			
				Intent i = new Intent(Intent.ACTION_VIEW);  
				i.setData(Uri.parse(url3));  
				startActivity(i);  
            }			
        });*/
		
		
    }
    
    public void share() {
        Intent i = null;
        String msg ="Awesome Minecraft Live Wallpaper - Fully Customizable Creative Mode! " + url2;

        i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");

        i.putExtra(Intent.EXTRA_TEXT, msg);
        i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name) + " \""
                + "Share title"+ "\" : " + getResources().getString(R.string.app_name));

        startActivity(Intent.createChooser(i, "Share " + getText(R.string.app_name)));
    }

    
    public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
		MainActivity.this.startActivity(mainIntent);
		MainActivity.this.finish();
	}
    
    @Override
	protected void onResume() {
		/*
		 * onResume is is always called after onStart, even if the app hasn't been
		 * paused
		 *
		 * add location listener and request updates every 1000ms or 10m
		 */
		
		
		
		super.onResume();
	}
    
    @Override
	protected void onPause() {
		/* GPS, as it turns out, consumes battery like crazy */
		
		super.onResume();
	}
       
    
    protected void onStop() {
		/* may as well just finish since saving the state is not important for this toy app */
		
		//Log.v(tag, "Released wlock??");		
		
		finish();
		super.onStop();
	}    
    
    
}
