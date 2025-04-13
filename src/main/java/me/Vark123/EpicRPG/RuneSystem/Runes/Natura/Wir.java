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
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.StunRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Wir extends ACastableRune {

	private static final Random rand = new Random();
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private IRunePostDamageEffect hitEffect;
	
	public Wir(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.7, -0.7, -0.7, 
				0.7, 0.7, 0.7);
		
		hitEffect = new DamageCustomEffect(
				rune.getDurationTime(),
				10,
				rune.getDamage() * 0.5,
				this,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_BREEZE_IDLE_AIR, 1.2f, rand.nextFloat(0.1f, 0.7f));

					loc.getWorld().spawnParticle(Particle.SMALL_GUST, loc.clone().add(0,1,0), 12, 
							0.4f, 0.8f, 0.4f, 0.05f);
				});
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.33, 
				2,
				1,
				35, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_BREEZE_SLIDE, 1.2f, 0.9f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.CLOUD, loc, 6, 
							0.2f, 0.2f, 0.2f, 0.02f);
					loc.getWorld().spawnParticle(Particle.WHITE_SMOKE, loc, 4, 
							0.23f, 0.23f, 0.23f, 0.01f);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune, hitEffect))
						StunRuneTemplate.castEffect(this, e);
				}, 
				loc -> { });
	}

}
