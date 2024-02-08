package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class NocWDzien extends ARune{

	public NocWDzien(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation();
		World w = p.getWorld();
		if(!w.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE).booleanValue()) {
			p.getWorld().playSound(loc, Sound.ENTITY_VILLAGER_HURT, 2, 0.6f);
			w.spawnParticle(Particle.VILLAGER_ANGRY, loc, 25, 0f, 1f, 0f, 0.1f);
			w.spawnParticle(Particle.SMOKE_LARGE, loc, 25, 0f, 1f, 0f, 0.1f);
			w.spawnParticle(Particle.DAMAGE_INDICATOR, loc, 25, 0f, 1f, 0f, 0.1f);
			return;
		}
		
		long time = w.getTime();
		if(time>=13500 && time<=22500) {
			w.setTime(0);
			p.getWorld().playSound(loc, Sound.ITEM_TOTEM_USE, 2, 1);
			w.spawnParticle(Particle.VILLAGER_HAPPY, loc, 25, 0f, 1f, 0f, 0.1f);
			w.spawnParticle(Particle.FIREWORKS_SPARK, loc, 25, 0f, 1f, 0f, 0.1f);
		}else {
			p.getWorld().playSound(loc, Sound.ENTITY_VILLAGER_HURT, 2, 0.6f);
			w.spawnParticle(Particle.VILLAGER_ANGRY, loc, 25, 0f, 1f, 0f, 0.1f);
			w.spawnParticle(Particle.SMOKE_LARGE, loc, 25, 0f, 1f, 0f, 0.1f);
			w.spawnParticle(Particle.DAMAGE_INDICATOR, loc, 25, 0f, 1f, 0f, 0.1f);
		}
	}

}
