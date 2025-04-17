package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
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

public class ZadzaKrwi extends ACastableRune {
	
	private static final DustOptions dust = new DustOptions(Color.RED, 1.25f);
	private static final Collection<UUID> debuff = new HashSet<>();
	private static final Random rand = new Random();
	private IRuneHitCondition hitCondition;

	private PotionEffect effect;

	public ZadzaKrwi(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? new PvPAllyRuneCondition() : new NonPvPAllyRuneCondition();

		effect = new PotionEffect(PotionEffectType.SPEED, rune.getDurationTime() * 20, 1);
	}

	@Override
	public void castSpell() {
		ACastableRune castableRune = this;
		Location startLoc = castLoc.clone().add(0, 0.1, 0);
		double radius = rune.getObszar();

		Collection<LivingEntity> affected = startLoc.getWorld()
				.getNearbyEntities(startLoc, radius, radius, radius, entity -> {
					if (entity.getLocation().distanceSquared(startLoc) > radius * radius)
						return false;

					if (!(entity instanceof LivingEntity))
						return false;

					LivingEntity le = (LivingEntity) entity;
					return hitCondition.check(player, le);
				}).stream().map(entity -> (LivingEntity) entity).collect(Collectors.toSet());

		AllyRuneUseEvent event = new AllyRuneUseEvent(player, rune, new HashSet<>(affected));
		Bukkit.getPluginManager().callEvent(event);
		affected = event.getAffectedEntities();

		startLoc.getWorld().playSound(startLoc, Sound.ENTITY_RAVAGER_ROAR, 2.5f, 0.7f);

		affected.stream()
			.filter(entity -> !debuff.contains(entity.getUniqueId()))
			.forEach(entity -> {
				BufferRuneTemplate.castEffect(
						castableRune, 
						rune.getName(),
						EpicModifierTypes.ZADZA_KRWI,
						entity,
						target -> {
							target.addPotionEffect(effect);
							target.sendMessage(Main.getInstance().getPrefix()+" §cKrew Ciebie zalewa - Twoi przeciwnicy zaczynaja drzec ze strachu...");
							
							UUID uid = entity.getUniqueId();
							debuff.add(uid);
							new BukkitRunnable() {
								@Override
								public void run() {
									Player player = Bukkit.getPlayer(uid);
									debuff.remove(uid);
									if(player == null || !player.isOnline())
										return;

									player.sendMessage(Main.getInstance().getPrefix()+" §aDebuff runy "+rune.getName()+" skonczyl sie");
									player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 1.5f, 1.5f);
									player.getWorld().spawnParticle(Particle.DUST, player.getLocation(), 10, 0.4f, 0.8f, 0.4f, 0.1f, dust);
								}
							}.runTaskLater(Main.getInstance(), 20*60*10);
						}, target -> {
							target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1,
									0.8f);
	
							Location loc = target.getLocation().clone().add(0, 1, 0);
	
							double force = rand.nextDouble(0.01, 0.05);
							loc.getWorld().spawnParticle(Particle.SMOKE, loc, 6, 0.4f, 0.8f, 0.4f, force);
						}, new TimingRuneEffect(4, target -> {
							Location _loc = target.getLocation().clone().add(0, 1, 0);
	
							double force = rand.nextDouble(0.12, 0.3);
							_loc.getWorld().spawnParticle(Particle.DUST, _loc, 4, 0.4f, 0.9f, 0.4f, force, dust);
						}));
				}
			);
	}

}
