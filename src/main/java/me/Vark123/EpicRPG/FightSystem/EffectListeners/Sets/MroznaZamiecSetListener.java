package me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;

public class MroznaZamiecSetListener implements Listener {
	
	@EventHandler
	public void onAttack(EpicAttackEvent e) {
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
		String type = rune.getMagicType();
		if(!(type.equalsIgnoreCase("mrok") || type.equalsIgnoreCase("woda")
				|| type.equalsIgnoreCase("natura")))
			return;
		
		Player player = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		
		if(info.getSetCounts().getOrDefault("Mroczna_Zamiec", 0) >= 3)
			e.increaseModifier(0.15);
		if(info.getSetCounts().getOrDefault("Mroczna_Zamiec_H", 0) >= 3)
			e.increaseModifier(0.2);
		if(info.getSetCounts().getOrDefault("Mroczna_Zamiec_M", 0) >= 3)
			e.increaseModifier(0.25);
	}
	
}
