package mobile.actdresses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class MobileActDresses extends Activity implements OnClickListener {
		  private static final String TAG = "MADmain";
		  Button buttonStart, buttonStop;

		  @Override
		  public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.main);

		    buttonStart = (Button) findViewById(R.id.button1);
		    buttonStop = (Button) findViewById(R.id.button2);

		    buttonStart.setOnClickListener(this);
		    buttonStop.setOnClickListener(this);
		  }

		  public void onClick(View src) {
		    switch (src.getId()) {
		    case R.id.button1:
		      Log.d(TAG, "onClick: starting srvice");
		      startService(new Intent(this, MADService.class));
		      break;
		    case R.id.button2:
		      Log.d(TAG, "onClick: stopping srvice");
		      stopService(new Intent(this, MADService.class));
		      break;
		    }
		  }
}   
    