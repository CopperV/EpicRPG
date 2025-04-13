package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Effects.DamageCustomEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRunePostDamageEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class RojOwadow extends ACastableRune {

	private static final Random rand = new Random();
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private IRunePostDamageEffect hitEffect;
	
	public RojOwadow(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.7, -0.7, -0.7, 
				0.7, 0.7, 0.7);
		hitEffect = new DamageCustomEffect(
				rune.getDurationTime(),
				20,
				rune.getDamage(),
				this,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_BEE_LOOP_AGGRESSIVE, 0.85f, 1.5f);
					loc.getWorld().spawnParticle(Particle.FALLING_HONEY, loc.clone().add(0,1,0), 20,
							0.5, 0.5, 0.5, rand.nextDouble(0, 0.06));
				});
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.4, 
				3,
				1,
				35, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_BEE_LOOP_AGGRESSIVE, 0.85f, 1.8f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.FALLING_HONEY, loc, 60, 
							0.3f, 0.3f, 0.3f, rand.nextDouble(0.01, 0.04));
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune, hitEffect)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_BEE_LOOP_AGGRESSIVE, 0.85f, 1.5f);
						loc.getWorld().spawnParticle(Particle.FALLING_HONEY, loc.clone().add(0,1,0), 60,
								0.5, 0.5, 0.5, rand.nextDouble(0, 0.06));
					}					
				}, 
				loc -> { });
	}

}
