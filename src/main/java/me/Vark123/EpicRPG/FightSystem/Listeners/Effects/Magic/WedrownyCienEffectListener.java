package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.Runes.WedrownyCien;

public class WedrownyCienEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		if(!modifiers.hasWedrownyCien())
			return;

		if(WedrownyCien.getEffected().contains(p))
			WedrownyCien.getEffected().remove(p);

		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1.85f);
		p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, p.getLocation().add(0,1,0), 25, 0.5f, 0.75f, 0.5f, 0.1f);
	}

}
