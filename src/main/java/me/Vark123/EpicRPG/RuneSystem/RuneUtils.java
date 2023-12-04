package me.Vark123.EpicRPG.RuneSystem;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.DamageManager;
import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.ManualDamage;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Utils.Pair;

public class RuneUtils {
	
	private RuneUtils() {};
	
	public static void krewPrzodkowEffect(RpgPlayer rpg) {
		Player p = rpg.getPlayer();
		double restoreHp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.08;
		RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, restoreHp);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled())
			p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation(), 10, 0.5F, 0.5F, 0.5F, 0.1f, new DustOptions(Color.fromRGB(154, 3, 67), 1.25f));
	}
	
	public static Optional<RuneEffect> getCustomTimeEffect(
			final double damage, 
			final RuneTimeEffect type,
			final EpicDamageType damageType) {
		switch(type) {
			case BLOOD:
				return Optional.of(new RuneEffect() {
					public void playEffect(Player damager, LivingEntity victim, ItemStackRune ir) {
						new BukkitRunnable() {
							int timer = 7;
							DustOptions dust = new DustOptions(Color.fromRGB(192, 32, 32), 1.25f);
							@Override
							public void run() {
								if(timer <= 0
										|| victim == null
										|| victim.isDead()) {
									this.cancel();
									return;
								}
								--timer;
								
								Pair<Boolean, Double> result = doPrivateDamage(damager, victim, damage, damageType);
								if(!result.getKey()) {
									this.cancel();
									return;
								}
								
								victim.getWorld().spawnParticle(Particle.REDSTONE, victim.getLocation().add(0,1,0), 8, 0.5f, 0.6f, 0.5f, 0.1f, dust);
							}
						}.runTaskTimer(Main.getInstance(), 0, 20);	
					}
				});
			case FIRE:
				return Optional.of(new RuneEffect() {
					public void playEffect(Player damager, LivingEntity victim, ItemStackRune ir) {
						new BukkitRunnable() {
							int timer = 4;
							@Override
							public void run() {
								if(timer <= 0
										|| victim == null
										|| victim.isDead()) {
									this.cancel();
									return;
								}
								--timer;
								
								Pair<Boolean, Double> result = doPrivateDamage(damager, victim, damage, damageType);
								if(!result.getKey()) {
									this.cancel();
									return;
								}
								
								victim.getWorld().spawnParticle(Particle.FLAME, victim.getLocation().add(0,1,0), 8, 0.5f, 0.6f, 0.5f, 0.1f);
							}
						}.runTaskTimer(Main.getInstance(), 0, 20);	
					}
				});
			case POISON:
				return Optional.of(new RuneEffect() {
					public void playEffect(Player damager, LivingEntity victim, ItemStackRune ir) {
						new BukkitRunnable() {
							int timer = 10;
							@Override
							public void run() {
								if(timer <= 0
										|| victim == null
										|| victim.isDead()) {
									this.cancel();
									return;
								}
								--timer;
								
								Pair<Boolean, Double> result = doPrivateDamage(damager, victim, damage, damageType);
								if(!result.getKey()) {
									this.cancel();
									return;
								}
								
								victim.getWorld().spawnParticle(Particle.SLIME, victim.getLocation().add(0,1,0), 8, 0.5f, 0.6f, 0.5f, 0.1f);
							}
						}.runTaskTimer(Main.getInstance(), 0, 10);	
					}
				});
			default:
				return Optional.empty();
		}
		
	}
	
	private static Pair<Boolean, Double> doPrivateDamage(LivingEntity damager, LivingEntity victim, double dmg, EpicDamageType damageType){
		if(!io.lumine.mythic.bukkit.BukkitAdapter
				.adapt(victim).isDamageable()){
			return new Pair<Boolean, Double>(false, dmg);
		}
		
		Pair<Double, Boolean> damageInfo = new Pair<>(dmg, false);
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, victim, DamageCause.CUSTOM, dmg);
		if(victim instanceof Player) {
			//TODO
			//REWORK PVP SYSTEM ON MAP
			RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
			ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(victim.getLocation()));
			State flag = set.queryValue(null, Flags.PVP);
			if(flag != null && flag.equals(State.DENY)) {
				return new Pair<Boolean, Double>(false, dmg);
			}
			
			Pair<Double, Boolean> defenseInfo = DamageManager.getInstance()
					.getDefenseCalculator().calc(damager, victim, dmg);
			
			EpicDefenseEvent defenseEvent = new EpicDefenseEvent(damager, victim, damageType,
					defenseInfo.getKey(), 1, defenseInfo);
			Bukkit.getPluginManager().callEvent(defenseEvent);
			
			if(defenseEvent.isCancelled()) 
				return new Pair<Boolean, Double>(false, dmg);
			
			dmg = defenseEvent.getDmg() * defenseEvent.getModifier();
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) victim);
			int level = rpg.getInfo().getLevel();
			if(dmg < ((level / 10.) + 2))
				dmg = (level / 10.) + 2;
		}
		
		EpicEffectEvent effectEvent = new EpicEffectEvent(damager, victim, damageType,
				dmg, 1, damageInfo);
		Bukkit.getPluginManager().callEvent(effectEvent);
		
		if(effectEvent.isCancelled())
			return new Pair<Boolean, Double>(false, dmg);
		
		if(effectEvent.getDmg() <= 0)
			return new Pair<Boolean, Double>(false, dmg);
		
		dmg = effectEvent.getFinalDamage();
		if(dmg <= 0) 
			return new Pair<Boolean, Double>(false, dmg);
		
		if(!ManualDamage.doDamageWithCheck(damager, victim, dmg, event)){
			return new Pair<Boolean, Double>(false, dmg);
		};
		return new Pair<Boolean, Double>(true, dmg);
	}
	
}
