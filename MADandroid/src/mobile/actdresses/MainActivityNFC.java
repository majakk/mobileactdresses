package mobile.actdresses;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityNFC extends Activity {
	static String TAG = "NFCREADER";
	
	static String url1 = "market://search?q=pub:Mobile Life Centre";
	
    /** Called when the activity is first created. */
    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        byte[] byte_id = getIntent().getByteArrayExtra(NfcAdapter.EXTRA_ID);
        
        if(byte_id != null){
        	String myString;
            myString = Integer.toString(byte_id.length);
        	Log.d(TAG, myString);
        }

    }*/
	Bitmap pBlack, pSilver, pOlive, pScratch; 
	
	NFCForegroundUtil nfcForegroundUtil = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //setContentView(R.layout.main);
	    //info = (TextView)findViewById(R.id.title);
	    //pBlack = BitmapFactory.decodeResource(getResources(),R.drawable.black);
        //pSilver = BitmapFactory.decodeResource(getResources(),R.drawable.silver);
        //pOlive = BitmapFactory.decodeResource(getResources(),R.drawable.olive);
        //pScratch = BitmapFactory.decodeResource(getResources(),R.drawable.scratch);

	    nfcForegroundUtil = new NFCForegroundUtil(this);
	}

	public void onPause() {
	    super.onPause();
	    nfcForegroundUtil.disableForeground();
	}   

	public void onResume() {
	    super.onResume();
	    nfcForegroundUtil.enableForeground();

	    if (!nfcForegroundUtil.getNfc().isEnabled())
	    {
	        Toast.makeText(getApplicationContext(), "Please activate NFC and press Back to return to the application!", Toast.LENGTH_LONG).show();
	        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
	    }

	}

	public void onNewIntent(Intent intent) {
	    Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
	    String resultat = "";
	    /*StringBuilder sb = new StringBuilder();
	    for(int i = 0; i < tag.getId().length; i++){
	    	sb.append(new Integer(tag.getId()[i]) + " ");
	    }*/
	    /*listan.add(bytesToHex(tag.getId()));
	    for(int i = 0; i < listan.size(); i++){
	    	resultat += listan.get(i) + "\n";
	    }*/
	    resultat = bytesToHex(tag.getId());
	    //info.setText(resultat); 
	    Log.d(TAG, resultat);
	    
	    if(resultat.regionMatches(0, "D5", 0, 2)){
		    try {		    	
		        pScratch = BitmapFactory.decodeResource(getResources(),R.drawable.design);
				this.setWallpaper(pScratch);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
	    }
	    else if(resultat.regionMatches(0, "30", 0, 2)){
		    try {
		    	//pOlive = BitmapFactory.decodeResource(getResources(),R.drawable.olive);
				//this.setWallpaper(pOlive);
				Intent i = new Intent(Intent.ACTION_VIEW);  
				i.setData(Uri.parse(url1));  
				startActivity(i); 
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
	    }
	    else if(resultat.regionMatches(0, "D4", 0, 2)){
		    try {
		    	Intent intent1 = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);  	
	        	intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	startActivity(intent1);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
	    }
	    	
	    
	    
	    
	    this.finish();
	    
	}
	
	
	
    /**
     *  Convenience method to convert a byte array to a hex string.
     *
     * @param  data  the byte[] to convert
     * @return String the converted byte[]
     */

    public static String bytesToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            buf.append(byteToHex(data[i]).toUpperCase());
            buf.append(" ");
        }
        return (buf.toString());
    }

    /**
     *  method to convert a byte to a hex string.
     *
     * @param  data  the byte to convert
     * @return String the converted byte
     */
    public static String byteToHex(byte data) {
        StringBuffer buf = new StringBuffer();
        buf.append(toHexChar((data >>> 4) & 0x0F));
        buf.append(toHexChar(data & 0x0F));
        return buf.toString();
    }
    
    /**
     *  Convenience method to convert an int to a hex char.
     *
     * @param  i  the int to convert
     * @return char the converted char
     */
    public static char toHexChar(int i) {
        if ((0 <= i) && (i <= 9)) {
            return (char) ('0' + i);
        } else {
            return (char) ('a' + (i - 10));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        Log.d(TAG,"Exited.");
    }

    
   
}
    
