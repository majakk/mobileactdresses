package mobile.actdresses;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.anddev.andengine.engine.LimitedFPSEngine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.ui.livewallpaper.BaseLiveWallpaperService;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.view.RenderSurfaceView;
import org.anddev.andengine.opengl.view.RenderSurfaceView.Renderer;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.TextView;

//import android.util.Log;

public class LiveWallpaper extends BaseLiveWallpaperService implements SharedPreferences.OnSharedPreferenceChangeListener, IOffsetsChanged
{
	// ===========================================================
	// Constants
	// ===========================================================
	
	public static final String SHARED_PREFS_NAME = "livewallpapersettings";
		
	static final int MSG_REGISTER_CLIENT = 1001;
	static final int MSG_UNREGISTER_CLIENT = 1002;
	static final int MSG_SET_INT_VALUE = 1003;

	//Camera Constants
	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 720;

	private String MODE = "eyecandy";
	private Boolean FLARES = true;	
	private Boolean SCROLL = true;	
	private String BRIGHTNESS = "6";
	private String TRANSPARENCY = "1";
	private String FRAMERATE = "32";
	private String RED = "1";
	private String GREEN = "1";
	private String BLUE = "1";
	
	private String creative = "";
	private String preset1 = "55!55!55!39!23"; //Eyecandy
	private String preset2 = "105!105!105!106!122!90"; //Carnivore
	private String preset3 = "13!27"; //Sugarrush
	private String preset4 = "107!155"; //Slender
	
	//Textures
	private Texture mTexture;	
	private TiledTextureRegion mItemsTextureRegion;		
	private TextureRegion mFlare;	
	
	//Camera
	//ScreenOrientation mScreenOrientation;
	int mScreenOrientation;
	Scene mScene;
	Camera mCamera;
	LimitedFPSEngine mEngine;
	
	Configuration mConfig;
	
	//Various
	Random myrand;
	
	
	public int items = 75;
	public static final int flares = 20;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	//Shared Preferences
	//@SuppressWarnings("unused")
	private SharedPreferences mSharedPreferences;
	private boolean mSettingsChanged = false;
	
	//Messenger inMessenger;
	final Messenger inMessenger = new Messenger(new IncomingHandler());
	
	
	
	//NFC Stuff
	//NFCForegroundUtil nfcForegroundUtil = null;	
	
	//List<String> listan;

	//@Override
	public org.anddev.andengine.engine.Engine onLoadEngine()
	{				
		//nfcForegroundUtil = new NFCForegroundUtil(this);
	    //listan = new LinkedList<String>();
		//inMessenger = new Messenger(new IncomingHandler());
		
		
		mSharedPreferences = LiveWallpaper.this.getSharedPreferences(SHARED_PREFS_NAME, 0);
		mSharedPreferences.registerOnSharedPreferenceChangeListener(this);		
		
		MODE = mSharedPreferences.getString("livewallpaper_mode", "eyecandy");
		items = Integer.valueOf(mSharedPreferences.getString("livewallpaper_numofitems", "75"));
	    FLARES = mSharedPreferences.getBoolean("livewallpaper_flares", true);
	    creative = mSharedPreferences.getString("livewallpaper_selectsprites", "");
	    BRIGHTNESS = mSharedPreferences.getString("livewallpaper_brightness", "6");
	    TRANSPARENCY = mSharedPreferences.getString("livewallpaper_transparency", "1");
	    RED = mSharedPreferences.getString("livewallpaper_red", "1");
	    GREEN = mSharedPreferences.getString("livewallpaper_green", "1");
	    BLUE = mSharedPreferences.getString("livewallpaper_blue", "1");	   
	    
		myrand = new Random();
		myrand.setSeed(0);			
					
		Log.d("LWP", "LOAD SCREEN: " + this.mScreenOrientation );
				
			
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		mCamera.reset();		
			
		//mEngine = new org.anddev.andengine.engine.LimitedFPSEngine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera), 32);
		mEngine = new org.anddev.andengine.engine.LimitedFPSEngine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new FillResolutionPolicy(), this.mCamera), Integer.valueOf(FRAMERATE));
				
		return mEngine;
	}

	//@Override
	public void onLoadResources()
	{
		this.getEngine().disableOrientationSensor(this);
		//Set the Base Texture Path
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		/* Items */
		this.mTexture = new Texture(256, 256, TextureOptions.DEFAULT);
		this.mItemsTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "itemset2.png", 0, 0, 16, 16); // 256x256
		this.mEngine.getTextureManager().loadTexture(this.mTexture);		
		
		//Flares
		this.mTexture = new Texture(256, 256, TextureOptions.DEFAULT);
		this.mFlare = TextureRegionFactory.createFromAsset(mTexture, this, "flare.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mTexture);		
	}

	//@Override
	public Scene onLoadScene()	{	
		
		mScene = new Scene();
			
        BuildScene(mScene);
        
        /*this.mScreenOrientation = ((WindowManager) this.getApplication().getSystemService(Service.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
		if(this.mScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
		{
			LiveWallpaper.this.mScene.setScale(1.0f);			
		}
		else //if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)	
		{				
			LiveWallpaper.this.mScene.setScaleY((float)CAMERA_HEIGHT / (float)CAMERA_WIDTH);
			LiveWallpaper.this.mScene.setScaleX((float)CAMERA_WIDTH / (float)CAMERA_HEIGHT);				
		}*/
        
        return mScene;
        
	}	
	 
	//@Override
	public void offsetsChanged(float xOffset, float yOffset, float xOffsetStep,
			float yOffsetStep, int xPixelOffset, int yPixelOffset) {
		// TODO Auto-generated method stub

		
		if(mCamera != null && SCROLL){
			
		    //Emulator has 3 screens
			mCamera.setCenter( ((480 * (xOffset * 0.5f)) + 120) , mCamera.getCenterY() );
			//formel mCamera.setCenter(( (Camera-WIDTH * (screensCount-1)) * xOffset ) - (Camera-WIDTH / 2) ,mCamera.getCenterY() );
		}

	
	}	
	
	public Scene BuildScene(Scene scene){
				
		scene.detachChildren();		
		scene.clearUpdateHandlers();
		scene.reset();
		scene.setScale(1.0f);
		
		final List<Sprite> fp = new LinkedList<Sprite>();
		final List<AnimatedSprite> sp = new LinkedList<AnimatedSprite>();
		
		final double[] phase = new double[items];
		final double[] phase2 = new double[items];
		final double[] ts = new double[items];
		final int[] ypos = new int[items];
		
		final double[] fphase = new double[flares];
		final double[] fphase2 = new double[flares];
		final double[] fts = new double[flares];
		final int[] fypos = new int[flares];
		
		int rn;			
		
		float col[] = new float[3];
		col[0] = Integer.valueOf(RED) * 0.1f;
		col[1] = Integer.valueOf(GREEN) * 0.1f;
		col[2] = Integer.valueOf(BLUE) * 0.1f;
		
		if(MODE.matches("eyecandy") && creative.contentEquals("")){
			scene.setBackground(new ColorBackground(0.0f, 0.2f, 0.2f));
			creative = preset1;
		}
		else if(MODE.matches("carnivore") && creative.contentEquals("")){
			scene.setBackground(new ColorBackground(0.3f, 0.0f, 0.0f));			
			creative = preset2;
		}
		else if(MODE.matches("sugarrush") && creative.contentEquals("")){
			scene.setBackground(new ColorBackground(0.1f, 0.1f, 0.1f));			
			creative = preset3;
		}
		else if(MODE.matches("slender") && creative.contentEquals("")){
			scene.setBackground(new ColorBackground(0.0f, 0.1f, 0.1f));			
			creative = preset4;
		}
		else{
			scene.setBackground(new ColorBackground(col[0], col[1], col[2]));
		}
		/*
		 * 
		 *  Here starts all the adding of sprites
		 * 
		 * */
		
		String adam[];
		//adam = creative.split("\\|");
		adam = creative.split("!");
				
		for(int i = 0; i < items; i++){
			sp.add(i, new AnimatedSprite(0, 0, this.mItemsTextureRegion.clone()));
			rn = Math.abs(myrand.nextInt() % adam.length);
			
			sp.get(i).setCurrentTileIndex(Integer.valueOf(adam[rn]));
										
			scene.attachChild(sp.get(i));	
								
			phase[i] = myrand.nextFloat() * 2 * Math.PI;
			phase2[i] = myrand.nextFloat() * 2 * Math.PI;
			ts[i] = myrand.nextFloat() * 1.8 + 0.2;
			ypos[i] = myrand.nextInt() % 400;
		}
		
		if(FLARES){				
			for(int i = 0; i < flares; i++){
				fp.add(i, new Sprite(0,0,64, 64, mFlare));
				
				scene.attachChild(fp.get(i));
				
				fphase[i] = myrand.nextFloat() * 2 * Math.PI;
				fphase2[i] = myrand.nextFloat() * 2 * Math.PI;
				fts[i] = myrand.nextFloat() * 1.8 + 0.2;
				fypos[i] = myrand.nextInt() % 400;
			}						
		}		
						
		scene.registerUpdateHandler(new IUpdateHandler() {						
			//@Override
			public void reset() { 
				
			}
			
			float cx, cy, scale;
			float trans = 1.0f - Integer.valueOf(TRANSPARENCY) * 0.1f;
			float bright = Integer.valueOf(BRIGHTNESS) * 0.1f;

			//@Override
			public void onUpdate(final float pSecondsElapsed) {
				try{
					for(int i = 0; i < items; i++){						
						cx = 240 + 500.0f * (float)Math.sin(phase[i] + ts[i] * LiveWallpaper.this.mEngine.getSecondsElapsedTotal() / 12.25f);
						cy = 360 + 400.0f * (float)Math.sin(phase2[i] + ts[i] * LiveWallpaper.this.mEngine.getSecondsElapsedTotal() / 16.65f);
						sp.get(i).setPosition(cx-32, cy-32);
												
						sp.get(i).setColor(bright, bright, bright, trans);
						
						scale = 1.5f + 0.5f * (float)ts[i] * (float)Math.cos(ts[i] * LiveWallpaper.this.mEngine.getSecondsElapsedTotal() / 14.35f);
						sp.get(i).setScale(scale * 5);						
						
						sp.get(i).setRotation(40.0f * (float)ts[i] * (float)Math.sin(ts[i] * LiveWallpaper.this.mEngine.getSecondsElapsedTotal() / 3.25f));
					}		
					if(FLARES){				
						for(int i = 0; i < flares; i++){									
							cx = 240 + 500.0f * (float)Math.sin(fphase[i] + fts[i] * LiveWallpaper.this.mEngine.getSecondsElapsedTotal() / 12.25f);
							cy = 360 + 400.0f * (float)Math.sin(fphase2[i] + fts[i] * LiveWallpaper.this.mEngine.getSecondsElapsedTotal() / 16.65f);
							fp.get(i).setPosition(cx-32, cy-32);						
						
							fp.get(i).setColor(bright, bright, bright, trans);
							
							scale = 1.5f + 0.5f * (float)fts[i] * (float)Math.cos(fts[i] * LiveWallpaper.this.mEngine.getSecondsElapsedTotal() / 14.35f);
							fp.get(i).setScale(scale);						
							
							fp.get(i).setRotation(40.0f * (float)fts[i] * (float)Math.sin(fts[i] * LiveWallpaper.this.mEngine.getSecondsElapsedTotal() / 3.25f));						
						}						
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		});		
		
		
		
		return scene;
	}   

	

	//@Override
	public void onLoadComplete()
	{
		//mSettingsChanged = true;
	}
	
	@Override
	protected void onTap(final int pX, final int pY)
	{
		
	}
	
	//@Override
	public void onSharedPreferenceChanged(SharedPreferences pSharedPrefs, String pKey)
	{
		MODE = pSharedPrefs.getString("livewallpaper_mode", "eyecandy");
		items = Integer.valueOf(pSharedPrefs.getString("livewallpaper_numofitems", "75"));
	    FLARES = pSharedPrefs.getBoolean("livewallpaper_flares", true);
	    SCROLL = pSharedPrefs.getBoolean("livewallpaper_scroll", true);
	    creative = pSharedPrefs.getString("livewallpaper_selectsprites", "");
	    BRIGHTNESS = pSharedPrefs.getString("livewallpaper_brightness", "6");
	    TRANSPARENCY = pSharedPrefs.getString("livewallpaper_transparency", "1");
	    FRAMERATE = pSharedPrefs.getString("livewallpaper_framerate", "32");
	    RED = pSharedPrefs.getString("livewallpaper_red", "1");
	    GREEN = pSharedPrefs.getString("livewallpaper_green", "1");
	    BLUE = pSharedPrefs.getString("livewallpaper_blue", "1");
	    	    
	    if(!creative.contentEquals("")){
	    	pSharedPrefs.edit().putString("livewallpaper_testpattern", "");
	    }    
	    
	    //Log.d("LOG", MODE + " " + creative + " " + FLARES);
	    mSettingsChanged = true;
	}		

	//@Override
	public void onUnloadResources() {
		// TODO Auto-generated method stub
		
	}
	
	//@Override
	public void onPauseGame() {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void onResumeGame() {
		// TODO Auto-generated method stub
		super.onResume();
		try{
	        if( mSettingsChanged )
	        {
	                BuildScene( this.getEngine().getScene() );	                
	                mSettingsChanged = false;
	        }
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}

	/*@Override
	public void onGamePaused() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameResumed() {
		super.onResume();
		try{
	        if( mSettingsChanged )
	        {
	                BuildScene( this.getEngine().getScene() );	                
	                mSettingsChanged = false;
	        }
		}catch (Exception e){
			e.printStackTrace();
		}
	}*/
	
	@Override
	public void onResume() {
		super.onResume();
		try{
	        if( mSettingsChanged )
	        {
	                BuildScene( this.getEngine().getScene() );
	                mSettingsChanged = false;
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	//Detects the orientation, but how to CHANGE it?? Ha it works!! mScene scales...
	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		Log.d("LWP", "onSurfaceChanged()" + Integer.toString(newConfig.orientation) );
	
		if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			LiveWallpaper.this.mScene.setScale(1.0f);			
		}
		else //if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)	
		{				
			LiveWallpaper.this.mScene.setScaleY((float)CAMERA_HEIGHT / (float)CAMERA_WIDTH);
			LiveWallpaper.this.mScene.setScaleX((float)CAMERA_WIDTH / (float)CAMERA_HEIGHT);	
		} 
		
		//mSettingsChanged = true;

	}	

	@Override
	public Engine onCreateEngine() {
		// TODO Auto-generated method stub
		
		
		return new MyBaseWallpaperGLEngine(this);
	}


	// ===========================================================
	// Methods
	// ===========================================================	

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	protected class MyBaseWallpaperGLEngine extends GLEngine {
		// ===========================================================
		// Fields
		// ===========================================================
		
		
		private Renderer mRenderer;
		
		private IOffsetsChanged mOffsetsChangedListener = null;

		// ===========================================================
		// Constructors
		// ===========================================================

		public MyBaseWallpaperGLEngine(IOffsetsChanged pOffsetsChangedListener) {
			this.setEGLConfigChooser(false);
			this.mRenderer = new RenderSurfaceView.Renderer(LiveWallpaper.this.mEngine);
			this.setRenderer(this.mRenderer);
			this.setRenderMode(RENDERMODE_CONTINUOUSLY);
			this.mOffsetsChangedListener = pOffsetsChangedListener;
			
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public Bundle onCommand(final String pAction, final int pX, final int pY, final int pZ, final Bundle pExtras, final boolean pResultRequested) {
			if(pAction.equals(WallpaperManager.COMMAND_TAP)) {
				LiveWallpaper.this.onTap(pX, pY);
			} else if (pAction.equals(WallpaperManager.COMMAND_DROP)) {
				LiveWallpaper.this.onDrop(pX, pY);
			}

			return super.onCommand(pAction, pX, pY, pZ, pExtras, pResultRequested);
		}

		@Override
		public void onResume() {
			super.onResume();
			LiveWallpaper.this.getEngine().onResume();
			LiveWallpaper.this.onResume();
		}

		@Override
		public void onPause() {
			super.onPause();
			LiveWallpaper.this.getEngine().onPause();
			LiveWallpaper.this.onPause();
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			if (this.mRenderer != null) {
				// mRenderer.release();
			}
			this.mRenderer = null;
		}
		
		@Override
		public void onOffsetsChanged(float xOffset, float yOffset,
				float xOffsetStep, float yOffsetStep, int xPixelOffset,
				int yPixelOffset) {
			// TODO Auto-generated method stub
			super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep,
					xPixelOffset, yPixelOffset);
			
			if(this.mOffsetsChangedListener != null)
				this.mOffsetsChangedListener.offsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
					
		}
		
		
		
	}

	//@Override
    //public IBinder onBind(Intent intent) {
    //    return mMessenger.getBinder();
    //}
    class IncomingHandler extends Handler { // Handler of incoming messages from clients.
        
        @Override
		public void handleMessage(Message msg) {
			Log.e("MESSAGE", "Got message");
			Bundle data = msg.getData();
			
			Log.d("", data.get("TAEMOT").toString());
			
		}
    }


	
	
}