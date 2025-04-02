package me.Vark123.EpicRPG.RuneSystem.Runes.Ogien;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Effects.DamageBurnEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRunePostDamageEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class WulkanicznyGejzer extends ACastableRune {

	private static final Vector horizontalVector = new Vector(0, 1, 0).normalize().multiply(0.6);
	private static final Random rand = new Random();
	private IRuneHitCondition hitCondition;
	private IRunePostDamageEffect hitEffect;
	
	public WulkanicznyGejzer(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		hitEffect = new DamageBurnEffect(5, rune.getDamage() * 0.1, this);
	}

	@Override
	public void castSpell() {
		ACastableRune castableRune = this;
		Location startLoc = player.getLocation().clone().add(0, 1, 0);
		Collection<Entity> hitted = new HashSet<>();
				
		InstantRangeRuneTemplate.castEffect(
				this,
				startLoc,
				rune.getObszar(),
				0,
				0,
				loc -> {
					loc.getWorld().playSound(loc, Sound.BLOCK_LAVA_EXTINGUISH, 1.5f, 0.7f);
					loc.getWorld().spawnParticle(Particle.LAVA, loc, 12, .4f, .8f, .4f, .1f);
				}, 
				hitCondition, 
				(loc, entity) -> {
					new BukkitRunnable() {
						int timer = 5*3;
						@Override
						public void run() {
							if(isCancelled())
								return;
							if(!casterInCastWorld() || player.isDead() || !player.isOnline()) {
								cancel();
								return;
							}
							if(timer <= 0) {
								InstantRangeRuneTemplate.castEffect(
										castableRune, 
										loc, 
										3,
										0,
										0, 
										loc2 -> {
											loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_DEATH, 1.2f, .7f);
											
											new BukkitRunnable() {
												Location effectLoc = loc2.clone();
												@Override
												public void run() {
													if(isCancelled())
														return;
													if(loc2.distanceSquared(effectLoc) > 7*7 || !casterInCastWorld()) {
														cancel();
														return;
													}
													
													effectLoc.getWorld().spawnParticle(Particle.FALLING_LAVA, effectLoc, 12, .8f, .25f, .8f, .1f);
													effectLoc.add(horizontalVector);
												}
											}.runTaskTimer(Main.getInstance(), 0, 1);
										}, 
										hitCondition, 
										(loc2, entity2) -> {
											if(hitted.contains(entity2))
												return;
											hitted.add(entity2);

											RuneUtils.damage(player, entity2, rune, hitEffect);
										});
								
								cancel();
								return;
							}
							--timer;

							loc.getWorld().spawnParticle(Particle.LAVA, loc, 8, .25f, .25f, .25f, .15f);
							if(rand.nextDouble() < 0.2)
								loc.getWorld().playSound(loc, Sound.BLOCK_LAVA_POP, rand.nextFloat(.5f) + .75f, rand.nextFloat(.5f) + .75f);
						}
					}.runTaskTimer(Main.getInstance(), 0, 4);
				});
	}

}
