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

public class PaktKrwiEffectListener implements Listener {
	
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
		if(modifiers.hasActiveModifier(EpicModifierTypes.PAKT_KRWI))
			e.increaseModifier(0.75);
		if(modifiers.hasActiveModifier(EpicModifierTypes.PAKT_KRWI_H))
			e.increaseModifier(0.85);
		if(modifiers.hasActiveModifier(EpicModifierTypes.PAKT_KRWI_M))
			e.increaseModifier(1);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onMeasure(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!(e.getVictim() instanceof Player victim))
			return;
		
		double damage = e.getFinalDamage();
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(victim);
		RpgModifiers modifiers = rpg.getModifiers();
		if(modifiers.hasActiveModifier(EpicModifierTypes.PAKT_KRWI_MEASURE))
			modifiers.setPaktKrwiHp(modifiers.getPaktKrwiHp() - damage);
		if(modifiers.hasActiveModifier(EpicModifierTypes.PAKT_KRWI_MEASURE_H))
			modifiers.setPaktKrwiHp_h(modifiers.getPaktKrwiHp_h() - damage);
		if(modifiers.hasActiveModifier(EpicModifierTypes.PAKT_KRWI_MEASURE_M))
			modifiers.setPaktKrwiHp_m(modifiers.getPaktKrwiHp_m() - damage);
	}
	
}
