/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mobile.actdresses;

//import java.io.IOException;

import android.app.Activity;
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
//import android.widget.Toast;


/**
 * A minimal "Hello, World!" application.
 */
public class HelloActivity extends Activity {
    private final String logTag = "HelloActivity";
    //private int slump = 1;
    
	public HelloActivity() {
    }
	
    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	Log.d(logTag,"onCreate");
        // Set the layout for this activity.  You can find it
        // in res/layout/hello_activity.xml
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.empty);
        
       
        
        //Tweet that works! (Although awkward with that gui popup)
        /*Intent intent = new Intent("com.twidroid.SendTweet");
		   intent.putExtra("com.twidroid.extra.MESSAGE",  
		   "Example tweet from android application");  
		   intent.setType("application/twitter");
		   try {  
		       startActivity(intent);  
		   }  
		   catch (ActivityNotFoundException e) {  
		        
		      Toast.makeText(this, "Twidroid not found.", Toast.LENGTH_SHORT).show();  
		  }  */
    }
    @Override
    public void onPause() {
    	super.onPause();
    	Log.d(logTag,"onPuase");
    
    }
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	Log.d(logTag,"onDestroy");
    
    }
    
}

