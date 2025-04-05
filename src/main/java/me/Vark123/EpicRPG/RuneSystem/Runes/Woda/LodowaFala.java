package me.Vark123.EpicRPG.RuneSystem.Runes.Woda;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.WaveRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class LodowaFala extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	private PotionEffect potion = new PotionEffect(PotionEffectType.SLOWNESS, 20*12, 1);

	public LodowaFala(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1, 0);
		
		WaveRuneTemplate.castWave(
				this,
				startLoc, 
				rune.getObszar(),
				8, 
				0.2, 
				1,
				1, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_BAT_TAKEOFF, 1.4f, 0.5f);
				},
				loc -> {
					loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 4,
							0.1, 1, 0.1, 0.03);
					loc.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 2,
							0.1, 1, 0.1, 0.02);
					loc.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, loc, 1,
							0.1, 1, 0.1, 0.01);
				}, 
				hitCondition, 
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune))
						entity.addPotionEffect(potion);
					
					loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_FREEZE, 1, 0.8f);
				},
				loc -> { });
	}

}
