package mobile.actdresses;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

public class BroadcastHandler extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	    if (intent.getAction().equalsIgnoreCase(Intent.ACTION_HEADSET_PLUG)) {
	        String data = intent.getDataString();
	        Bundle extraData = intent.getExtras();

	        int st = intent.getIntExtra("state" , -1);
	        String nm = intent.getStringExtra("name");
	        int mic = intent.getIntExtra("microphone", -1);
	        String all = "st="+Integer.toString(st)+" nm="+nm+" mic="+Integer.toString(mic);

	        Log.d("ALL",all);
	        
	        if(st == 1){
	        	Intent intent1 = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
	        	//Intent intent1 = new Intent(Intent.);
	        	/*Uri myResourceToPlay = Uri.parse("file://");
	        	Intent intent1 = new Intent(Intent.ACTION_VIEW, myResourceToPlay);*/

	        	intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	context.startActivity(intent1);
	        }

	        /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        builder.setTitle("Headset broadcast");
	        builder.setMessage(all);
	        builder.setPositiveButton("Okey-dokey", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	            }
	        });
	        builder.create().show();*/
	        
	        
	        
	    }
	}

	

}
