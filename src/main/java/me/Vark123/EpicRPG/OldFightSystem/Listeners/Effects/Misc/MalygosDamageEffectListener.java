package me.Vark123.EpicRPG.OldFightSystem.Listeners.Effects.Misc;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.OldFightSystem.Events.EpicEffectEvent;

public class MalygosDamageEffectListener implements Listener {
	
	private static final Random rand = new Random();

	@EventHandler(priority = EventPriority.HIGH)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		if(!(damager instanceof Projectile))
			return;
		
		Projectile projectile = (Projectile) damager;
		damager = (Entity) projectile.getShooter();
		if(!(damager instanceof Player))
			return;
		
		if(victim.getName().equals("§3§lMalygos - Legendary Boss") && e.getFinalDamage() > 10_000) {
			e.setDmg(e.getDmg() * 0.5);
		}
		if(victim.getName().equals("§3§lMalygos - Legendary Boss")) {
			ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(victim);
			if(aMob.getStance().equals("phase_3") && e.getFinalDamage() < 12_000)
				e.setDmg(e.getDmg() * 0.5);
			if(aMob.getStance().equals("phase_4"))
				e.setDmg(e.getDmg() * (rand.nextDouble(0.5)+0.5));
		}
	}

}
