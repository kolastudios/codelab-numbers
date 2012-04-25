package com.bootcamp.firstgame;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class CampNumber extends Sprite {
	public int myValue = -1;
	
	public CampNumber(float pX, float pY, TextureRegion pTextureRegion, int value) {
		super(pX, pY, pTextureRegion);
		this.myValue = value;
	}
	
	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY){
		switch(pSceneTouchEvent.getAction()){
		case TouchEvent.ACTION_DOWN:
			this.setScale(1.25f);
		break;
		case TouchEvent.ACTION_UP:
			Utils.Log("Next Number: " + BootcampActivity.nextNumber + " My Value: " + this.myValue);
			if(BootcampActivity.nextNumber == this.myValue){
				BootcampActivity.removeEntity(this);
				BootcampActivity.nextNumber += 1;
			}else{
				this.setScale(1.0f);
			}
			break;
		}
		return true;
	}
}
