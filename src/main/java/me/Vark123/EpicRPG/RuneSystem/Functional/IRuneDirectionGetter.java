package me.Vark123.EpicRPG.RuneSystem.Functional;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public interface IRuneDirectionGetter {

	public Vector getVector(Location casterLoc, Location point);
	
}
