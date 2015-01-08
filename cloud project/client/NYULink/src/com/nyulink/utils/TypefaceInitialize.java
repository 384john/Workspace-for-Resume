package com.nyulink.utils;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceInitialize {
	private Context context;
	
	public TypefaceInitialize(Context context) {
		this.context = context;
	}
	
	public TypefaceCollection Regular() {
		TypefaceCollection typeface = new TypefaceCollection.Builder()
	        .set(Typeface.NORMAL, Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"))
	        .create();
		TypefaceHelper.init(typeface);
		return typeface;
	}
	
	public TypefaceCollection Medium() {
		TypefaceCollection typeface = new TypefaceCollection.Builder()
	        .set(Typeface.NORMAL, Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf"))
	        .create();
		TypefaceHelper.init(typeface);
		return typeface;
	}
	
	public TypefaceCollection Thin() {
		TypefaceCollection typeface = new TypefaceCollection.Builder()
	        .set(Typeface.NORMAL, Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf"))
	        .create();
		TypefaceHelper.init(typeface);
		return typeface;
	}
	
	public TypefaceCollection Light() {
		TypefaceCollection typeface = new TypefaceCollection.Builder()
	        .set(Typeface.NORMAL, Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"))
	        .create();
		TypefaceHelper.init(typeface);
		return typeface;
	}
	
	public TypefaceCollection Bold() {
		TypefaceCollection typeface = new TypefaceCollection.Builder()
	        .set(Typeface.NORMAL, Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf"))
	        .create();
		TypefaceHelper.init(typeface);
		return typeface;
	}
	
	public TypefaceCollection Black() {
		TypefaceCollection typeface = new TypefaceCollection.Builder()
	        .set(Typeface.NORMAL, Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Black.ttf"))
	        .create();
		TypefaceHelper.init(typeface);
		return typeface;
	}
	
	public TypefaceCollection BoldSlab() {
		TypefaceCollection typeface = new TypefaceCollection.Builder()
	        .set(Typeface.NORMAL, Typeface.createFromAsset(context.getAssets(), "fonts/RobotoSlab-Bold.ttf"))
	        .create();
		TypefaceHelper.init(typeface);
		return typeface;
	}
	
	public TypefaceCollection LightSlab() {
		TypefaceCollection typeface = new TypefaceCollection.Builder()
	        .set(Typeface.NORMAL, Typeface.createFromAsset(context.getAssets(), "fonts/RobotoSlab-Light.ttf"))
	        .create();
		TypefaceHelper.init(typeface);
		return typeface;
	}
	
	public TypefaceCollection RegularSlab() {
		TypefaceCollection typeface = new TypefaceCollection.Builder()
	        .set(Typeface.NORMAL, Typeface.createFromAsset(context.getAssets(), "fonts/RobotoSlab-Regular.ttf"))
	        .create();
		TypefaceHelper.init(typeface);
		return typeface;
	}
	
	public TypefaceCollection BoldItalic() {
		TypefaceCollection typeface = new TypefaceCollection.Builder()
	        .set(Typeface.NORMAL, Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-BoldItalic.ttf"))
	        .create();
		TypefaceHelper.init(typeface);
		return typeface;
	}
	
	public TypefaceCollection ThinItalic() {
		TypefaceCollection typeface = new TypefaceCollection.Builder()
	        .set(Typeface.NORMAL, Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-ThinItalic.ttf"))
	        .create();
		TypefaceHelper.init(typeface);
		return typeface;
	}
	
	public TypefaceCollection LightItalic() {
		TypefaceCollection typeface = new TypefaceCollection.Builder()
	        .set(Typeface.NORMAL, Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-LightItalic.ttf"))
	        .create();
		TypefaceHelper.init(typeface);
		return typeface;
	}
	
}
