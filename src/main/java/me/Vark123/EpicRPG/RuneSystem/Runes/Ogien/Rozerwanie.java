package me.Vark123.EpicRPG.RuneSystem.Runes.Ogien;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Effects.DamageBurnEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRunePostDamageEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Rozerwanie extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private IRunePostDamageEffect hitEffect;

	private static final double MIN = 1;
	private static final double MAX = 0.4;
	
	public Rozerwanie(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.7, -0.7, -0.7, 
				0.7, 0.7, 0.7);
		hitEffect = new DamageBurnEffect(7, rune.getDamage() * 0.1, this);
	}

	@Override
	public void castSpell() {
		ACastableRune castableRune = this;
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.45, 
				2,
				1,
				30, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 0.7f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.FLAME, loc, 8, 
							0.25f, 0.25f, 0.25f, 0.05f);
					loc.getWorld().spawnParticle(Particle.FALLING_LAVA, loc, 8, 
							0.4f, 0.4f, 0.4f, 0.1f);
				}, 
				hitCondition, 
				(loc, e) -> {
					InstantRangeRuneTemplate.castEffect(
							castableRune, 
							loc,
							rune.getObszar(), 
							0, 
							20*4, 
							loc2 -> {
								new BukkitRunnable() {
									int timer = 4;
									float pitchMod = (float) (0.5 / (timer - 1.));
									float pitch = 1f;
									@Override
									public void run() {
										if(isCancelled())
											return;
										if(!casterInCastWorld()) {
											cancel();
											return;
										}
										
										Location tmp = e.getLocation().clone().add(0, 1, 0);
										if(timer <= 0) {
											tmp.getWorld().playSound(tmp, Sound.ENTITY_WITHER_BREAK_BLOCK, 2, 0.7f);
											tmp.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, tmp, 12, rune.getObszar(), 0, rune.getObszar());
											
											cancel();
											return;
										}
										
										tmp.getWorld().playSound(tmp, Sound.ENTITY_ENDER_DRAGON_GROWL, 1, pitch);
										tmp.getWorld().spawnParticle(Particle.FLAME, tmp, 12, .4f, .4f, .4f, .09f);
										
										--timer;
										pitch += pitchMod;
									}
								}.runTaskTimer(Main.getInstance(), 0, 20);
							},
							hitCondition,
							(loc2, entity) -> {
								if(entity.equals(e)) {
									RuneUtils.damage(player, entity, rune, hitEffect);
									return;
								}

								double dist = entity.getLocation().distance(e.getLocation());
								double closePercent = Math.abs(dist / (double) rune.getObszar() - 1);
								
								double percent = MAX - (MAX-MIN) * closePercent;
								double dmg = rune.getDamage() * percent;

								RuneUtils.damage(player, entity, rune, dmg, new DamageBurnEffect(7, dmg * 0.1, this));
							});
				}, 
				loc -> { });
	}

}
