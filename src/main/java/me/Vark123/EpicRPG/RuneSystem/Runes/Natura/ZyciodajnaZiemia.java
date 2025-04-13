package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPAllyRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPAllyRuneCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class ZyciodajnaZiemia extends ACastableRune {
	
	private static final Collection<UUID> debuff = new HashSet<>();
	private static final Collection<UUID> localDebuff = new HashSet<>();
	private static final BlockData data = Bukkit.createBlockData(Material.GRASS_BLOCK);
	private static final DustOptions dust = new DustOptions(Color.RED, 1.25f);
	
	private Random random = new Random();
	private IRuneHitCondition hitCondition;
	
	public ZyciodajnaZiemia(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPAllyRuneCondition() : new NonPvPAllyRuneCondition();
	}

	@Override
	public void castSpell() {
		int level = rpgPlayer.getInfo().getLevel();
		double hpRegenAmount = 10 + 0.25 * level;
		int manaRegenAmount = 25 + (int) (0.45 * level);
		
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
					loc.getWorld().playSound(loc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2f, 1.4f);
					
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
									Utils.unsetEntityBuff(entity, EpicModifierTypes.ZYCIODAJNA_ZIEMIA);
									affected.remove(entity);
								});
						}
					}.runTaskTimer(Main.getInstance(), 0, 20);
				},
				loc -> {
					affected.forEach(entity -> {
						loc.getWorld().spawnParticle(Particle.HEART, entity.getLocation().clone().add(0,.1,0), 3,
								0.4, 0.05, 0.4, 0.03);
					});
					
					for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI*2/(8*rune.getObszar()))) {
						double x = radius * Math.sin(theta);
						double z = radius * Math.cos(theta);
						Location tmp = loc.clone().add(x, 0.1, z);
						player.getWorld().spawnParticle(Particle.BLOCK, tmp, 3, 
								.2, .2, .2, .15, data);
					}
				},
				hitCondition,
				(loc, entity) -> {
					if(!(entity instanceof Player))
						return;
					
					UUID uid = entity.getUniqueId();
					if(debuff.contains(entity.getUniqueId()))
						return;
					
					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) entity);
					rpg.getStats().addPresentManaSmart(manaRegenAmount);
					RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, hpRegenAmount);
					Bukkit.getPluginManager().callEvent(event);
					
					double manaParticles = Math.max(manaRegenAmount*0.5, 1);
					double hpParticles = Math.max(hpRegenAmount*0.5, 1);
					double particles = (manaParticles + hpParticles) * 0.5;
					for(int i = 0; i < manaParticles; ++i) {
						double r = random.nextDouble(1.25);
						double angle = random.nextDouble(Math.PI * 2);
						
						double x = r * Math.sin(angle);
						double z = r * Math.cos(angle);
						double force = random.nextDouble(0.02, 0.15);
						loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, loc.clone().add(x,0,z), 0,
								0, 1, 0, force);
					}
					for(int i = 0; i < hpParticles; ++i) {
						double r = random.nextDouble(1.25);
						double angle = random.nextDouble(Math.PI * 2);
						
						double x = r * Math.sin(angle);
						double z = r * Math.cos(angle);
						double force = random.nextDouble(0.02, 0.15);
						loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, loc.clone().add(x,0,z), 0,
								0, 1, 0, force);
					}
					for(int i = 0; i < particles; ++i) {
						double r = random.nextDouble(1.25);
						double angle = random.nextDouble(Math.PI * 2);
						
						double x = r * Math.sin(angle);
						double z = r * Math.cos(angle);
						double force = random.nextDouble(0.05, 0.2);
						loc.getWorld().spawnParticle(Particle.END_ROD, loc.clone().add(x,0,z), 0,
								0, 1, 0, force);
					}
					
					if(Utils.hasEntityBuff(entity, EpicModifierTypes.ZYCIODAJNA_ZIEMIA))
						return;
					
					Utils.setEntityBuff(entity, EpicModifierTypes.ZYCIODAJNA_ZIEMIA);
					affected.add(entity);
					
					if(!localDebuff.contains(uid))
						localDebuff.add(uid);
					
				},
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_GHAST_SHOOT, 2f, 0.7f);
					
					affected.forEach(entity -> {
						Utils.unsetEntityBuff(entity, EpicModifierTypes.ZYCIODAJNA_ZIEMIA);
					});
					localDebuff.stream().collect(Collectors.toSet())
						.forEach(uid -> {
							runDebuffTimer(uid);
							localDebuff.remove(uid);
						});
				});
	}

	private void runDebuffTimer(UUID uid) {
		debuff.add(uid);
		new BukkitRunnable() {
			@Override
			public void run() {
				Entity entity = Bukkit.getEntity(uid);
				debuff.remove(uid);
				if(entity == null || entity.isDead() ||
						!(entity instanceof Player && ((Player) entity).isOnline()))
					return;

				Player player = (Player) entity;
				player.sendMessage(Main.getInstance().getPrefix()+" §aDebuff runy "+rune.getName()+" skonczyl sie");
				player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 1.5f, 1.5f);
				player.getWorld().spawnParticle(Particle.DUST, player.getLocation(), 10, 0.4f, 0.8f, 0.4f, 0.1f, dust);
			}
		}.runTaskLater(Main.getInstance(), 20*60*15);
	}
	
}
