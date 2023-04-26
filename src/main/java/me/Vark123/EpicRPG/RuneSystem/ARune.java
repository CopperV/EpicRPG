package me.Vark123.EpicRPG.RuneSystem;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class ARune {

	protected ItemStackRune dr;
	protected Player p;
	protected World w;
	protected boolean modifier1 = false;
	protected boolean modifier2 = false;
	
	public ARune(ItemStackRune dr, Player p) {
		this.dr = dr;
		this.p = p;
		this.w = p.getWorld();
	}
	
	public abstract void castSpell();
	
	public boolean hasModifier1() {
		return modifier1;
	}
	
	public boolean hasModifier2() {
		return modifier2;
	}
	
	public World getCastWorld() {
		return w;
	}
	
	public boolean casterInCastWorld() {
		return p.getWorld().getUID().equals(w.getUID());
	}
	
	public boolean entityInCastWorld(Entity entity) {
		return entity.getWorld().getUID().equals(w.getUID());
	}
	
}
