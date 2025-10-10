package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.StunRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class ElektrycznePrzeciazenie extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public ElektrycznePrzeciazenie(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.6, -0.6, -0.6, 
				0.6, 0.6, 0.6);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.4, 
				2,
				1,
				20, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_BEE_LOOP_AGGRESSIVE, 1.1f, 2f);

					var effect = new PotionEffect(PotionEffectType.SLOWNESS, 20*rune.getDurationTime(), 2);
					player.addPotionEffect(effect);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 7, 
							0.15f, 0.15f, 0.15f, 0.05f);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_BEE_HURT, 1f, 2f);

						StunRuneTemplate.castEffect(this, e);
						
						TimingEffectRuneTemplate.castEffect(
								this,
								e,
								__ -> { },
								__ -> { }, 
								new TimingRuneEffect(
										2,
										entity -> {
											Location loc2 = entity.getLocation().clone().add(0, 1, 0);
											loc2.getWorld().spawnParticle(Particle.FIREWORK, loc2, 4,
													0.3f, 0.7f, 0.3f, 0.05f);
										}));
					}
				}, 
				loc -> { });
	}

}
