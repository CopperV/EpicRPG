package me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Events.AllyRuneUseEvent;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPAllyRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPAllyRuneCondition;

public class PoteznaRunaDomisia_H extends ACastableRune {

	private Random rand = new Random();
	private IRuneHitCondition hitCondition;
	
	private Collection<PotionEffect> positiveEffects = new LinkedList<>();
	private PotionEffect negativeEffect;
	
	public PoteznaRunaDomisia_H(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPAllyRuneCondition() : new NonPvPAllyRuneCondition();
		
		positiveEffects.add(new PotionEffect(PotionEffectType.SPEED, rune.getDurationTime()*20, 2));
		positiveEffects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, rune.getDurationTime()*20, 0));
		positiveEffects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, rune.getDurationTime()*20, 0));
		positiveEffects.add(new PotionEffect(PotionEffectType.REGENERATION, rune.getDurationTime()*20, 3));
		positiveEffects.add(new PotionEffect(PotionEffectType.SLOW_FALLING, rune.getDurationTime()*20, 0));
		positiveEffects.add(new PotionEffect(PotionEffectType.CONDUIT_POWER, rune.getDurationTime()*20, 0));
		positiveEffects.add(new PotionEffect(PotionEffectType.HASTE, rune.getDurationTime()*20, 0));
		positiveEffects.add(new PotionEffect(PotionEffectType.STRENGTH, rune.getDurationTime()*20, 1));
		positiveEffects.add(new PotionEffect(PotionEffectType.JUMP_BOOST, rune.getDurationTime()*20, 0));
		positiveEffects.add(new PotionEffect(PotionEffectType.WATER_BREATHING, rune.getDurationTime()*20, 3));
		positiveEffects.add(new PotionEffect(PotionEffectType.SATURATION, rune.getDurationTime()*20, 3));
		positiveEffects.add(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, rune.getDurationTime()*20, 2));
		
		negativeEffect = new PotionEffect(PotionEffectType.NAUSEA, rune.getDurationTime()*20, 1);
	}

	@Override
	public void castSpell() {
		ACastableRune castableRune = this;
		Location startLoc = castLoc.clone().add(0, 0.1, 0);
		double radius = rune.getObszar();
		
		Collection<LivingEntity> affected = startLoc.getWorld()
				.getNearbyEntities(startLoc, radius, radius, radius, entity -> {
					if(entity.getLocation().distanceSquared(startLoc) > radius * radius)
						return false;
					
					if(!(entity instanceof LivingEntity))
						return false;
					
					LivingEntity le = (LivingEntity) entity;
					return hitCondition.check(player, le);
				})
				.stream()
				.map(entity -> (LivingEntity) entity)
				.collect(Collectors.toSet());
		
		AllyRuneUseEvent event = new AllyRuneUseEvent(player, rune, new HashSet<>(affected));
		Bukkit.getPluginManager().callEvent(event);
		affected = event.getAffectedEntities();
		
		startLoc.getWorld().playSound(startLoc, Sound.ITEM_TOTEM_USE, 2.5f, 0.75f);
		
		affected.forEach(entity -> {
			BufferRuneTemplate.castEffect(
					castableRune, 
					rune.getName(),
					EpicModifierTypes.POTEZNA_RUNA_DOMISIA,
					entity,
					target -> {
						target.getWorld().spawnParticle(Particle.FLASH, target.getLocation().clone().add(0,1,0), 1,
								0,0,0,0);
						
						positiveEffects.forEach(effect -> target.addPotionEffect(effect));
						if(rand.nextDouble() < 0.03)
							target.addPotionEffect(negativeEffect);
					},
					target -> {
						target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 0.8f);

						Location loc = target.getLocation().clone().add(0, 1, 0);

						double force = rand.nextDouble(0.01, 0.05);
						loc.getWorld().spawnParticle(Particle.SMOKE, loc, 6, 0.4f, 0.8f, 0.4f, force);
					},
					new TimingRuneEffect(4, target -> {
						Location _loc = target.getLocation().clone().add(0, 1, 0);

						double force = rand.nextDouble(0.01, 0.1);
						_loc.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, _loc, 5, 0.4f, 0.9f, 0.4f, force);
					}));
		});
	}

}
