package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;

public class NocWDzien extends ACastableRune {

	public NocWDzien(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		Location loc = player.getLocation().clone().add(0,1,0);
		World world = player.getWorld();
		
		if(!world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE).booleanValue()) {
			player.sendMessage(Main.getInstance().getPrefix()+" §cW tym swiecie nie mozna zmieniac czasu!");
			
			world.playSound(loc, Sound.ENTITY_VILLAGER_NO, 2, 0.8f);
			
			world.spawnParticle(Particle.ANGRY_VILLAGER, loc, 25, 0.4f, 1f, 0.4f, 0.1f);
			world.spawnParticle(Particle.LARGE_SMOKE, loc, 25, 0.4f, 1f, 0.4f, 0.02f);
			world.spawnParticle(Particle.DAMAGE_INDICATOR, loc, 25, 0.4f, 1f, 0.4f, 0.1f);
			return;
		}
		
		long time = world.getTime();
		if(time>=13500 && time<=22500) {
			world.setTime(0);
			world.playSound(loc, Sound.ITEM_TOTEM_USE, 1, 1);
			world.spawnParticle(Particle.TOTEM_OF_UNDYING, loc.clone().add(0,1,0),
					25, 0.6, 0.6, 0.6, 0.2);
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 0));
		} else {
			player.sendMessage(Main.getInstance().getPrefix()+" §cRuny tej mozesz uzyc tylko w nocy!");
			
			world.playSound(loc, Sound.ENTITY_VILLAGER_NO, 2, 0.8f);
			
			world.spawnParticle(Particle.ANGRY_VILLAGER, loc, 25, 0.4f, 1f, 0.4f, 0.1f);
			world.spawnParticle(Particle.LARGE_SMOKE, loc, 25, 0.4f, 1f, 0.4f, 0.02f);
			world.spawnParticle(Particle.DAMAGE_INDICATOR, loc, 25, 0.4f, 1f, 0.4f, 0.1f);
			return;
		}
	}

}
