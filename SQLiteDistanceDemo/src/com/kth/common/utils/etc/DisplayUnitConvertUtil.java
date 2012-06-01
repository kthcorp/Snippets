package com.kth.common.utils.etc;

import android.content.Context;

public class DisplayUnitConvertUtil {

	  private static final float DEFAULT_HDIP_DENSITY_SCALE = 1.5f;
	  
	  public static float DPFromPixel(Context context, int pixel)
	  {
	    float scale = context.getResources().getDisplayMetrics().density;
	    
	    return (pixel / DEFAULT_HDIP_DENSITY_SCALE * scale);
	  }
	  
	  
	  public static float PixelFromDP(Context context, int DP)
	  {
	    float scale = context.getResources().getDisplayMetrics().density;
	    
	    return (DP / scale * DEFAULT_HDIP_DENSITY_SCALE);
	  }

}
