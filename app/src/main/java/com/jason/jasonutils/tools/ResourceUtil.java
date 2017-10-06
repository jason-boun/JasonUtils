package com.jason.jasonutils.tools;

import android.content.Context;

public class ResourceUtil {

	private static final String RESOURCE_CLASS_LAYOUT = "layout";
	private static final String RESOURCE_CLASS_STRING = "string";
	private static final String RESOURCE_CLASS_DRAWABLE = "drawable";
	private static final String RESOURCE_CLASS_STYLE = "style";
	private static final String RESOURCE_CLASS_ID = "id";
	private static final String RESOURCE_CLASS_COLOR = "color";
	private static final String RESOURCE_CLASS_ARRAY = "array";

	private ResourceUtil instance;

	private ResourceUtil() {
	};

	public ResourceUtil getInstance() {
		if (instance == null) {
			synchronized (ResourceUtil.class) {
				if (instance == null) {
					new ResourceUtil();
				}
			}
		}
		return instance;
	}

	public int getLayoutId(Context ctx, String paramString) {
		return ctx.getResources().getIdentifier(paramString, RESOURCE_CLASS_LAYOUT, ctx.getPackageName());
	}

	public int getStringId(Context ctx, String paramString) {
		return ctx.getResources().getIdentifier(paramString, RESOURCE_CLASS_STRING, ctx.getPackageName());
	}

	public int getDrawableId(Context ctx, String paramString) {
		return ctx.getResources().getIdentifier(paramString, RESOURCE_CLASS_DRAWABLE, ctx.getPackageName());
	}

	public int getStyleId(Context ctx, String paramString) {
		return ctx.getResources().getIdentifier(paramString, RESOURCE_CLASS_STYLE, ctx.getPackageName());
	}

	public int getId(Context ctx, String paramString) {
		return ctx.getResources().getIdentifier(paramString, RESOURCE_CLASS_ID, ctx.getPackageName());
	}

	public int getColorId(Context ctx, String paramString) {
		return ctx.getResources().getIdentifier(paramString, RESOURCE_CLASS_COLOR, ctx.getPackageName());
	}

	public int getArrayId(Context ctx, String paramString) {
		return ctx.getResources().getIdentifier(paramString, RESOURCE_CLASS_ARRAY, ctx.getPackageName());
	}

}
