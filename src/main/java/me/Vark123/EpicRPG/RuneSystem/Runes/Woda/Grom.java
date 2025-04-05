package me.Vark123.EpicRPG.RuneSystem.Runes.Woda;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.RainRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Grom extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private PotionEffect potion;

	public Grom(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				0.6, 0.6, 0.6, 
				0.6, 0.6, 0.6);
		
		potion = new PotionEffect(PotionEffectType.SLOWNESS, 20*15, 1);
	}

	@Override
	public void castSpell() {
		RainRuneTemplate.castRain(
				this,
				castLoc,
				rune.getObszar(),
				rune.getDurationTime(),
				0.6, 
				9,
				10, 
				2,
				1,
				(loc1, loc2) -> {
					return new Vector(0, -1, 0);
				}, 
				35, 
				50,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 3, 0.8f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 10, 
							0.1f, 0.1f, 0.1f, 0.1f);
					loc.getWorld().spawnParticle(Particle.INSTANT_EFFECT, loc, 10, 
							0.1f, 0.1f, 0.1f, 0f);
				}, 
				hitCondition, 
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_FREEZE, 1.1f, 1.3f);

						entity.addPotionEffect(potion);
					}
					
				}, 
				loc -> { },
				loc -> { });
	}

}
