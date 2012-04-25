package com.bootcamp.firstgame;

import java.util.ArrayList;
import java.util.Collections;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

public class BootcampActivity extends BaseGameActivity {
	
	protected Engine mEngine;
	public static Camera mCamera;
	public static Scene mScene;
	
	public static BaseGameActivity bc;
	
	public static final int CAMERA_WIDTH = 480;
	public static final int CAMERA_HEIGHT = 720;
	public static final int NUMBER_WIDTH = 87;
	public static final int NUMBER_HEIGHT = 87;
	
    private BitmapTextureAtlas mainAtlas;
    private TextureRegion numberTextureRegion;
    
    public ArrayList<CampNumber> campNumbers = null;
    public ArrayList<GridLocation> gridLocations = null;
    
    public static int nextNumber = 1;

	public Engine onLoadEngine() {
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		mEngine = new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera).setNeedsSound(true));
		mEngine.disableOrientationSensor(this);
		return mEngine;
	}

	public void onLoadResources() {
		campNumbers = new ArrayList<CampNumber>();
		gridLocations = new ArrayList<GridLocation>();
		
		BitmapTextureAtlasTextureRegionFactory.setCreateTextureRegionBuffersManaged(false);
		mainAtlas = new BitmapTextureAtlas(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mainAtlas, this, "gfx/atlas.png", 0, 0);
		
		int x = 0, y = 0, loop = 1;
		CampNumber campNumber = null;
		for(int i = 1; i <= 30; i++){
			numberTextureRegion = TextureRegionFactory.extractFromTexture(this.mainAtlas, x, y, NUMBER_WIDTH, NUMBER_HEIGHT, false);
			
			campNumber = new CampNumber((float)x, (float)y, numberTextureRegion, i);
			campNumbers.add(campNumber);
			
			GridLocation g = new GridLocation(x, y);
			this.gridLocations.add(g);
			
			/* Logic to select next box */
			x = x + NUMBER_WIDTH;
			if(i % 5 == 0){
				x = 0;
				y = NUMBER_HEIGHT * loop;
				loop++;
			}
		}
		
		this.mEngine.getTextureManager().loadTexture(this.mainAtlas);
	}

	public Scene onLoadScene() {
		bc = this;
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		mScene = new Scene();
		mScene.setOnAreaTouchTraversalFrontToBack();
		mScene.setTouchAreaBindingEnabled(true);
		
		return mScene;
	}

	public void onLoadComplete() {
		this.newGame();
	}
	
	public void newGame(){
		Collections.shuffle(gridLocations);
		int i = 0;
		for(CampNumber cn: this.campNumbers){
			//tempNumbers.add(cn);
			GridLocation g = this.gridLocations.get(i);
			cn.setPosition(g.x, g.y);
			BootcampActivity.mScene.attachChild(cn);
			BootcampActivity.mScene.registerTouchArea(cn);
			i++;
		}
	}
	
	public static void removeEntity(final Entity entity){
		BootcampActivity.bc.runOnUpdateThread(new Runnable(){
			public void run(){
				BootcampActivity.mScene.detachChild(entity);
				BootcampActivity.mScene.unregisterTouchArea((ITouchArea)entity);
			}
		});
	}
}