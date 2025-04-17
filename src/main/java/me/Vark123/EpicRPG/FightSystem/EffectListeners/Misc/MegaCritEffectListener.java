package me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageEffectEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class MegaCritEffectListener implements Listener {

	@EventHandler
	private void onEffect(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player player))
			return;

		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		Entity victim = e.getVictim();
		
		if(!DamageUtils.checkMegaCrit(rpg, victim))
			return;
		
		e.increaseModifier(rpg.getStats().getFinalWalka() * 0.004);
		
		AbstractEntity ae = BukkitAdapter.adapt(victim);
		ae.setMetadata("MegaCritEffect", true);
	}
	
	@EventHandler
	private void onEffect(EpicPostDamageEffectEvent e) {
		Entity victim = e.getVictim();
		AbstractEntity ae = BukkitAdapter.adapt(victim);
		if(!ae.hasMetadata("MegaCritEffect"))
			return;
		
		ae.removeMetadata("MegaCritEffect");
		if(e.isCancelled())
			return;
		
		LivingEntity damager = e.getDamager();
		victim.getWorld().spawnParticle(Particle.CRIT, victim.getLocation().clone().add(0,1,0), 30, 0.5f, 0.8f, 0.5f, 0.4f);
		damager.getWorld().playSound(damager, Sound.ENTITY_PLAYER_DEATH, 0.8f, 0.6f);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onDamageController(EntityDamageEvent e) {
		AbstractEntity ae = BukkitAdapter.adapt(e.getEntity());
		if(e.isCancelled() && ae.hasMetadata("MegaCritEffect"))
			ae.removeMetadata("MegaCritEffect");
	}
	
}
