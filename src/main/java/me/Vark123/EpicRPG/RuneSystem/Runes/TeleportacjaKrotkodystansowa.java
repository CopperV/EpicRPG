package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

public class TeleportacjaKrotkodystansowa extends ARune {

	public TeleportacjaKrotkodystansowa(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location start = p.getLocation().add(0, 0.2, 0);
		Vector vec = start.getDirection().setY(0).normalize().multiply(0.25);
		Location end = start.clone();
		while(end.distance(start) < dr.getObszar()) {
			Location next = end.clone().add(vec);
			next = checkBlock(next.clone());
			if(next == null)
				break;
			end = next;
		}
		p.teleport(end);
		spellEffect(end);
		spellEffect(start);
		p.playSound(start, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 10, .5f);
	}

	//TODO
	private Location checkBlock(Location loc) {
		Material m = loc.getBlock().getType();
		if(m.isSolid()) {
			String s = m.name().toUpperCase();
			if(s.contains("FENCE") || s.contains("WALL"))
				return null;
			loc.add(0, 1, 0);
			if(loc.getBlock().getType().isSolid())
				return null;
		}else {
			Location tmp = loc.clone().subtract(0, 2, 0);
			Location tmp2 = loc.clone().subtract(0, 1, 0);
			if(!tmp2.getBlock().getType().isSolid()) {
				if(!tmp.getBlock().getType().isSolid())
					return null;
				else {
					loc.subtract(0, 1, 0);
				}
			}
			
		}
		return loc;
	}
	
	private void spellEffect(Location loc) {
		double lenght = 1.5;
		for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/32)) {
			double x = lenght * Math.sin(theta);
			double z = lenght * Math.cos(theta);
			Location tmp = loc.clone().add(x, 0.1, z);
			p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, tmp, 0, 0, 0.05, 0, 0.1);
			p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, tmp, 0, 0, 0.1, 0, 0.15);
			p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, tmp, 0, 0, 0.15, 0, 0.2);
			p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, tmp, 0, 0, 0.2, 0, 0.25);
			p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, tmp, 0, 0, 0.25, 0, 0.3);
		}
	}
	
}
