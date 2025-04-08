package me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo;

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
import me.Vark123.EpicRPG.RuneSystem.RuneEffectType;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPAllyRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPAllyRuneCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class SilaJednosci extends ACastableRune {
	
	private Random random = new Random();
	private IRuneHitCondition hitCondition;
	
	public SilaJednosci(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPAllyRuneCondition() : new NonPvPAllyRuneCondition();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone();
		double radius = rune.getObszar();
		
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.SILA_JEDNOSCI,
				player,
				target -> { },
				target -> { });
		
		Collection<LivingEntity> affected = new HashSet<>();
		TotemRuneTemplate.castTotem(
				this,
				startLoc,
				rune.getDurationTime(),
				radius,
				4,
				20,
				loc -> {							
					loc.getWorld().playSound(loc, Sound.ITEM_MACE_SMASH_GROUND, 1.25f, 0.8f);
					
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
									Utils.unsetEntityEffect(entity, RuneEffectType.SILA_JEDNOSCI_EFFECT);
									affected.remove(entity);
								});
						}
					}.runTaskTimer(Main.getInstance(), 0, 20);
				},
				loc -> {
					affected.forEach(entity -> {
						loc.getWorld().spawnParticle(Particle.END_ROD, entity.getLocation().clone().add(0,1,0), 3,
								0.4, 0.8, 0.4, 0.02);
					});
					
					for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI*2/(6*rune.getObszar()))) {
						double x = radius * Math.sin(theta);
						double z = radius * Math.cos(theta);
						Location tmp = loc.clone().add(x, 0.1, z);
						player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, tmp, 0, 0, 1, 0, random.nextDouble(0.01,0.07));
					}
					for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI*2/(8*rune.getObszar()))) {
						double x = radius * Math.sin(theta);
						double z = radius * Math.cos(theta);
						Location tmp = loc.clone().add(x, 0.1, z);
						player.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, tmp, 0, 0, 1, 0, random.nextDouble(0.03,0.1));
					}
				},
				hitCondition,
				(loc, entity) -> {
//					if(entity.equals(player))
//						return;
//					if(Utils.hasEntityBuff(entity, EpicModifierTypes.SILA_JEDNOSCI))
//						return;
					if(Utils.hasEntityEffect(player, entity, RuneEffectType.SILA_JEDNOSCI_EFFECT))
						return;
					
					Utils.setEntityEffect(player, entity, RuneEffectType.SILA_JEDNOSCI_EFFECT);
					affected.add(entity);
				},
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1.5f, 0.8f);
					
					affected.forEach(entity -> {
						Utils.unsetEntityEffect(entity, RuneEffectType.SILA_JEDNOSCI_EFFECT);
					});
				});
	}

}
