package me.Vark123.EpicRPG.FightSystem.Modifiers.ModList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifier;

public class HPInfo implements DamageModifier {
	
	private static Map<Player,BukkitTask> entitiesHPRemover = new ConcurrentHashMap<>();
	private static Map<Player,BossBar> entitiesHPBars = new ConcurrentHashMap<>();

	@Override
	public double modifyDamage(Entity _damager, Entity victim, double damage, DamageCause cause) {

		if(!(victim instanceof LivingEntity))
			return damage;
		
		Entity damager;
		switch(cause) {
			case PROJECTILE:
				if(_damager instanceof AbstractArrow)
					damager = (Entity) ((AbstractArrow)_damager).getShooter();
				else
					damager = _damager;
				break;
			default:
				damager = _damager;
		}
		if(!(damager instanceof Player))
			return damage;
		Player p = (Player) damager;
		
		if(victim.getName().toLowerCase().contains("boss") && !victim.getName().toLowerCase().contains("miniboss"))
			return damage;
		
		BossBar bar;
		if(entitiesHPBars.containsKey(p)) {
			bar = entitiesHPBars.get(p);
			entitiesHPRemover.get(p).cancel();
			entitiesHPRemover.remove(p);
		}else {
			bar = Bukkit.createBossBar(" ", BarColor.WHITE, BarStyle.SEGMENTED_10);
		}
		
		LivingEntity lVictim = (LivingEntity) victim;
		
		bar.setTitle(victim.getName()+" §7- §c"
				+(int)((lVictim).getHealth() - damage < 0 ? 
						0 : Math.ceil((lVictim.getHealth() - damage))+" §4❤"));
		
		if(((lVictim).getHealth() - damage < 0 ?
				0 : (lVictim).getHealth() - damage) > (lVictim).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
			bar.setProgress(1);
		else
			bar.setProgress(((lVictim).getHealth() - damage < 0 ?
					0 : (lVictim).getHealth() - damage) / (lVictim).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		bar.setVisible(true);
		bar.addPlayer(p);

		BukkitTask task = new BukkitRunnable() {
			
			@Override
			public void run() {
				bar.removePlayer(p);
				entitiesHPBars.remove(p);
				entitiesHPRemover.remove(p);
				
			}
		}.runTaskLater(Main.getInstance(), 20*5);
		
		entitiesHPBars.put(p, bar);
		entitiesHPRemover.put(p, task);
		
		return damage;
	}

}
