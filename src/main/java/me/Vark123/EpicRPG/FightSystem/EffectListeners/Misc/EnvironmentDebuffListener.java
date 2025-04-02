package me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.EpicRPGMobManager;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageEffectEvent;

public class EnvironmentDebuffListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	private void onDebuff(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player player = (Player) damager;
		if(player.isInsideVehicle() && player.getVehicle() instanceof AbstractHorse) {
			player.sendMessage(Main.getInstance().getPrefix()+" §cZbyt ciezko walczy sie na koniu!");
			player.sendMessage(Main.getInstance().getPrefix()+" §cNie moge zadac skutecznych obrazen!");
			
			e.setDamage(e.getDamage() * 0.25);
			
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20*3, 2));
			player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 20*3, 2));
			player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
		}
		
		Entity victim = e.getVictim();
		Material m = damager.getLocation().getBlock().getType();
		Material m2 = victim.getLocation().getBlock().getType();
		if((m.equals(Material.WATER) || m.equals(Material.LAVA)
				|| m2.equals(Material.WATER) || m2.equals(Material.LAVA))
				&& !EpicRPGMobManager.getInstance().isWaterMob(victim.getName())) {
			player.sendMessage(Main.getInstance().getPrefix()+" §cWoda stawia straszny opor w walce...");
			player.sendMessage(Main.getInstance().getPrefix()+" §cNie moge tak walczyc!");
			
			e.setDamage(e.getDamage() * 0.05);
			
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20*3, 2));
			player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 20*3, 2));
			player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
		}
	}
	
}
