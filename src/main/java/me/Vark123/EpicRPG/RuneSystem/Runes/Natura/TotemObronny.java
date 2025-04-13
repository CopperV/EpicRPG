package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPAllyRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPAllyRuneCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class TotemObronny extends ACastableRune {
	
	
	private Random random = new Random();
	private IRuneHitCondition hitCondition;
	
	public TotemObronny(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPAllyRuneCondition() : new NonPvPAllyRuneCondition();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone();
		double radius = rune.getObszar();
		
		Collection<LivingEntity> affected = new HashSet<>();
		TotemRuneTemplate.castTotem(
				this,
				startLoc,
				rune.getDurationTime(),
				radius,
				4,
				20,
				loc -> {							
					loc.getWorld().playSound(loc, Sound.ITEM_SHIELD_BLOCK, 1, 0.3f);
					loc.getWorld().playSound(loc, Sound.ITEM_SHIELD_BLOCK, 1, 0.6f);
					loc.getWorld().playSound(loc, Sound.ITEM_SHIELD_BLOCK, 1, 0.9f);
					
					new BukkitRunnable() {
						int timer = rune.getDurationTime();
						@Override
						public void run() {
							if(isCancelled())
								return;
							if(timer <= 0 || !casterInCastWorld()) {
								this.cancel();
								return;
							}
							--timer;
							
							affected.stream()
								.filter(entity -> !entityInCastWorld(entity) ||
										loc.distanceSquared(entity.getLocation()) > radius * radius)
								.collect(Collectors.toSet())
								.forEach(entity -> {
									Utils.unsetEntityBuff(entity, EpicModifierTypes.TOTEM_OBRONNY);
									affected.remove(entity);
								});
						}
					}.runTaskTimer(Main.getInstance(), 0, 20);
				},
				loc -> {
					affected.forEach(entity -> {
						loc.getWorld().spawnParticle(Particle.INSTANT_EFFECT, entity.getEyeLocation().clone().add(0,.75,0), 3,
								0.3, 0.5, 0.3, 0.03);
					});
					
					for(int i = 0; i < 8 * rune.getObszar(); ++i) {
						double theta = random.nextDouble(Math.PI*2);
						double force = random.nextDouble(0.05, 0.2);
						double x = radius * Math.sin(theta);
						double z = radius * Math.cos(theta);
						
						Location pos = loc.clone().add(x, 0.1, z);
						pos.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, pos, 0,
								0, 1, 0, force);
					}
				},
				hitCondition,
				(loc, entity) -> {
					if(Utils.hasEntityBuff(entity, EpicModifierTypes.TOTEM_OBRONNY))
						return;
					
					Utils.setEntityBuff(entity, EpicModifierTypes.TOTEM_OBRONNY);
					affected.add(entity);
				},
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_GHAST_SHOOT, 2f, 0.7f);
					
					affected.forEach(entity -> {
						Utils.unsetEntityBuff(entity, EpicModifierTypes.TOTEM_OBRONNY);
					});
				});
	}
	
}
