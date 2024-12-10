package me.Vark123.EpicRPG.FightSystem.Listeners.Sets;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class MroznaZamiec_HSetListener implements Listener {
	
	@EventHandler
	public void onAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.MAGIC))
			return;
		
		Object[] args = e.getArgs();
		if(args == null 
				|| args.length <= 0
				|| !(args[0] instanceof ItemStackRune))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;

		ItemStackRune ir = (ItemStackRune) args[0];
		Player p = (Player) damager;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		int amount = info.getSetCounts().getOrDefault("Mroczna_Zamiec_H", 0);
		if(amount < 3)
			return;
		
		String type = ir.getMagicType();
		if(!(type.equalsIgnoreCase("mrok") || type.equalsIgnoreCase("woda")
				|| type.equalsIgnoreCase("natura")))
			return;
		
		e.increaseModifier(0.2);
	}
	
}
