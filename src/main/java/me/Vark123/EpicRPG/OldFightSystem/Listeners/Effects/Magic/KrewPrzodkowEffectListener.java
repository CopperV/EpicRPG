package me.Vark123.EpicRPG.OldFightSystem.Listeners.Effects.Magic;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.OldFightSystem.EpicDamageType;
import me.Vark123.EpicRPG.OldFightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.OldRuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.OldRuneSystem.RuneUtils;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class KrewPrzodkowEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;

		if(!e.getDamageType().equals(EpicDamageType.MAGIC))
			return;

		if(e.getArgs() == null 
				|| e.getArgs().length == 0 
				|| !(e.getArgs()[0] instanceof ItemStackRune))
			return;

		Entity damager = e.getDamager();
		if(damager == null || !(damager instanceof Player))
			return;

		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		
		if(((LivingEntity)e.getVictim()).getHealth() - e.getFinalDamage() >= 1)
			return;
		if(!modifiers.hasKrewPrzodkow())
			return;

		ItemStackRune ir = (ItemStackRune) e.getArgs()[0];
		if(!ir.getMagicType().equalsIgnoreCase("krew"))
			return;

		RuneUtils.krewPrzodkowEffect(rpg);
	}

}
