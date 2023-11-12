package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.EpicRPGMobManager;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;

public class ConditionalDebuffEffectModifierListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOW)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		if(p.isInsideVehicle()
				&& p.getVehicle() instanceof AbstractHorse) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cZbyt ciezko walczy sie na koniu!");
			p.sendMessage(Main.getInstance().getPrefix()+" §cNie moge zadac skutecznych obrazen!");
			e.setDmg(e.getDmg() / 4.);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*2, 2));
			p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
		}
		
		Entity victim = e.getVictim();
		Material m = damager.getLocation().getBlock().getType();
		Material m2 = victim.getLocation().getBlock().getType();
		if((m.equals(Material.WATER) || m.equals(Material.LAVA)
				|| m2.equals(Material.WATER) || m2.equals(Material.LAVA))
				&& EpicRPGMobManager.getInstance().isWaterMob(victim.getName())) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cWoda stawia straszny opor w walce...");
			p.sendMessage(Main.getInstance().getPrefix()+" §cNie moge tak walczyc!");
			e.setDmg(e.getDmg() / 20.);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*2, 2));
			p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
		}
	}

}
