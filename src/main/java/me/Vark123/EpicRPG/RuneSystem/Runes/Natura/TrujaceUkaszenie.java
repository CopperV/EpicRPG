package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Particle.DustOptions;
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

public class TrujaceUkaszenie extends ACastableRune {

	private static final Random rand = new Random();
	private static final DustOptions dust = new DustOptions(Color.fromRGB(0, 140, 0), 0.85f);
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private IRunePostDamageEffect hitEffect;
	private PotionEffect effect;
	
	public TrujaceUkaszenie(RpgPlayer rpgPlayer, EpicRune rune) {
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
					loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 0.9f, rand.nextFloat(1, 1.2f));
					loc.getWorld().spawnParticle(Particle.DUST, loc.clone().add(0,1,0), 6,
							0.4, 0.4, 0.4, 0.02, dust);
				});
		effect = new PotionEffect(PotionEffectType.SLOWNESS, 20*rune.getDurationTime(), 1);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.42, 
				2,
				1,
				28, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_EVOKER_FANGS_ATTACK, 1.1f, 1.3f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.DUST, loc, 6, 
							0.25f, 0.25f, 0.25f, rand.nextDouble(0.01, 0.04), dust);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune, hitEffect)) {
						e.addPotionEffect(effect);
						
						loc.getWorld().playSound(loc, Sound.ENTITY_BEE_STING, 0.9f, 0.5f);
						loc.getWorld().spawnParticle(Particle.DUST, loc.clone().add(0,1,0), 6,
								0.4, 0.4, 0.4, 0.02, dust);
					}					
				}, 
				loc -> { });
	}

}
