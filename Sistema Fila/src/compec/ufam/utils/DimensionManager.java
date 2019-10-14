package compec.ufam.utils;

import java.awt.Dimension;

public class DimensionManager {

	public static int getWidthScale(Dimension dimension, double scale) {
		
		double width = dimension.getWidth();
		int result = (int) (width/scale);
		
		return result;
	}
	
	public static int getHeigthScale(Dimension dimension, double scale) {
		
		double heigth = dimension.getHeight();
		int result = (int) (heigth/scale);
		
		return result;
	}
	
}
