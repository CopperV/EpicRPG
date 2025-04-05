package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Utils.Utils;

public class TarczaCieniaEffectListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity victim = e.getVictim();
		if(!(victim instanceof Player))
			return;
		
		if(!Utils.hasEntityBuff(victim, EpicModifierTypes.TARCZA_CIENIA))
			return;
		
		Player p = (Player) victim;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgStats stats = rpg.getStats();
		
		double damage = e.getDamage();
		int dmgTarczaCienia = (int) (stats.getPresentMana() < damage * 0.5 ? stats.getPresentMana() : damage * 0.5);
		if(dmgTarczaCienia <= 0)
			return;
		
		e.setDamage(damage - dmgTarczaCienia);
		stats.removePresentMana(dmgTarczaCienia);
		
		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 0.9f, 1.1f);
		victim.getWorld().spawnParticle(Particle.SCULK_SOUL, victim.getLocation().add(0,1,0), 12, 0.4f, 0.8f, 0.4f, 0.02f);
	}

}
