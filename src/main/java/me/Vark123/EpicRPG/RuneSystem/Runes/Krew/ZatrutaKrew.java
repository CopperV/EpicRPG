package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
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

public class ZatrutaKrew extends ACastableRune {

	private static final Random rand = new Random();
	private static final DustOptions dust = new DustOptions(Color.fromRGB(170, 83, 3), 1.6f);
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private IRunePostDamageEffect hitEffect;
	
	public ZatrutaKrew(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.65, -0.65, -0.65, 
				0.65, 0.65, 0.65);
		hitEffect = new DamageCustomEffect(
				rune.getDurationTime(),
				30,
				rune.getDamage()*1.5,
				this,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_BIG_FALL, 0.9f, rand.nextFloat(0.5f, 0.65f));
					loc.getWorld().spawnParticle(Particle.DUST, loc.clone().add(0,1,0), 6,
							0.4, 0.4, 0.4, 0.02, dust);
				});
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.39, 
				3,
				1,
				33, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_EVOKER_FANGS_ATTACK, 1f, 1.7f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.DUST, loc, 8, 
							0.22f, 0.22f, 0.22f, rand.nextDouble(0.1, 0.24), dust);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune, hitEffect)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 0.9f, 0.8f);
						loc.getWorld().spawnParticle(Particle.DUST, loc.clone().add(0,1,0), 6,
								0.4, 0.4, 0.4, 0.3, dust);
					}					
				}, 
				loc -> { });
	}

}
