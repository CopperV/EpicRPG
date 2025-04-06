package me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;

public class TeleportacjaKrotkodystansowa extends ACastableRune {
	
	public TeleportacjaKrotkodystansowa(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		Location start = player.getLocation().add(0, 0.2, 0);
		Vector vec = start.getDirection().setY(0).normalize().multiply(0.25);
		Location end = start.clone();
		while(end.distance(start) < rune.getObszar()) {
			Location next = end.clone().add(vec);
			next = checkBlock(next.clone());
			if(next == null)
				break;
			end = next;
		}
		player.teleport(end);
		spellEffect(end);
		spellEffect(start);
		player.playSound(start, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 10, .5f);
	}

	private Location checkBlock(Location loc) {
		if(!checkArea(loc))
			return null;
		
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
	
	private boolean checkArea(final Location loc) {

		if(
				loc.clone().add(-1, 1, 0).getBlock().getType().isSolid() ||
				loc.clone().add(0, 1, 1).getBlock().getType().isSolid() ||
				loc.clone().add(0, 1, -1).getBlock().getType().isSolid() ||
				loc.clone().add(1, 1, 0).getBlock().getType().isSolid())
		{
			return false;
		}
		return true;
	}
	
	private void spellEffect(Location loc) {
		double lenght = 1.5;
		for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/64)) {
			double x = lenght * Math.sin(theta);
			double z = lenght * Math.cos(theta);
			Location tmp = loc.clone().add(x, 0.1, z);
			player.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, tmp, 0, 0, 0.05, 0, 0.02);
			player.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, tmp, 0, 0, 0.1, 0, 0.04);
			player.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, tmp, 0, 0, 0.15, 0, 0.06);
			player.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, tmp, 0, 0, 0.2, 0, 0.08);
			player.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, tmp, 0, 0, 0.25, 0, 0.1);
		}
	}

}
