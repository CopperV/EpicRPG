package me.Vark123.EpicRPG.RuneSystem.Runes.Woda;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Gejzer extends ACastableRune {

	private static final Vector horizontalVector = new Vector(0, 1, 0).normalize().multiply(0.6);
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public Gejzer(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.7, -0.7, -0.7, 
				0.7, 0.7, 0.7);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.1, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.42, 
				3,
				1,
				30, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_SWIM, 1.2f, 0.9f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.POOF, loc, 8, 
							0.12f, 0.12f, 0.12f, 0.02f);
				}, 
				hitCondition, 
				(loc, e) -> {
					new BukkitRunnable() {
						
						@Override
						public void run() {							
							RuneUtils.damage(player, e, rune);

							e.getWorld().playSound(loc, Sound.ENTITY_PLAYER_SPLASH, 1.2f, 0.9f);
							
							Location loc2 = e.getLocation().clone().add(0, 0.1, 0);
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
									
									effectLoc.getWorld().spawnParticle(Particle.FALLING_WATER, effectLoc, 12, .8f, .25f, .8f, .1f);
									effectLoc.add(horizontalVector);
								}
							}.runTaskTimer(Main.getInstance(), 0, 1);

						}
					}.runTaskLater(Main.getInstance(), 20);
				}, 
				loc -> { });
	}

}
