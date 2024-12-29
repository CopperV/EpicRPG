package me.Vark123.EpicRPG.OldFightSystem.Listeners.Attack.Misc;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.OldFightSystem.DamageUtils;
import me.Vark123.EpicRPG.OldFightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class MegaCritModifierListener implements Listener {

	@EventHandler
	public void onMegaCrit(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		
		double modifier = DamageUtils.getMegaCritModifier(rpg);
		
		if(modifier > 0) {
			p.getWorld().spawnParticle(Particle.CRIT, e.getVictim().getLocation().clone().add(0,1,0), 30, 0.5f, 0.8f, 0.5f, 0.4f);
			p.playSound(p, Sound.ENTITY_PLAYER_DEATH, 0.8f, 0.6f);
		}
		
		e.increaseModifier(modifier);
	}
	
}
