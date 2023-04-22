package me.Vark123.EpicRPG.Utils;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;

public class Utils {

	private Utils() {}
	
	public static String convertToClassConvention(String s) {
		String[] tab = s.split(" ");
		StringBuilder toReturn = new StringBuilder(tab[0]);
		for(int i = 1; i < tab.length; ++i) {
			toReturn.append(StringUtils.capitalize(tab[i]));
		}
		return toReturn.toString();
	}
	
	public static Direction getLookingDirection(Location loc, boolean combineDirections) {
		float yaw = loc.getYaw();
		if(yaw < 0)
			yaw += 360;
		double offset = combineDirections ? 22.5 : 0;
		double offset2 = combineDirections ? 0 : 45;
		int divide = combineDirections ? 45 : 90;
		
		int index = ((int)((yaw + offset2) % 360 + offset)) / divide;
		return combineDirections ? Direction.doubleDirection[index] : Direction.Directions[index];
	}
	
}
