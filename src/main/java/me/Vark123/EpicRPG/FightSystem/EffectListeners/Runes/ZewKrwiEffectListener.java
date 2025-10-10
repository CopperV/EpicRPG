package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;

public class ZewKrwiEffectListener implements Listener {
	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MAGIC))
			return;
		
		Object[] args = e.getArgs();
		if(args == null 
				|| args.length < 2
				|| !(args[1] instanceof EpicRune))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;

		EpicRune rune = (EpicRune) args[1];
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		
		if(!rune.getMagicType().equalsIgnoreCase("krew"))
			return;
		if(modifiers.hasActiveModifier(EpicModifierTypes.ZEW_KRWI))
			e.increaseModifier(modifiers.getZewKrwiMod());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onMeasure(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!(e.getVictim() instanceof Player victim))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(victim);
		RpgModifiers modifiers = rpg.getModifiers();
		if(modifiers.hasActiveModifier(EpicModifierTypes.ZEW_KRWI))
			modifiers.addZewKrwiMod(0.02);
	}
	
}
