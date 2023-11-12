package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Custom;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;

public class HpDisplayEffectListener implements Listener {
	
	private static Map<Player,BukkitTask> entitiesHPRemover = new ConcurrentHashMap<>();
	private static Map<Player,BossBar> entitiesHPBars = new ConcurrentHashMap<>();
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();

		if(!(victim instanceof LivingEntity))
			return;
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		ActiveMob mob = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(victim);
		if(mob == null) 
			return;
		if(mob.getType().usesBossBar())
			return;
		
		double damage = e.getDmg();
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
						0 : Math.ceil((lVictim.getHealth() - damage)))+" §4❤");
		
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
	}

}
