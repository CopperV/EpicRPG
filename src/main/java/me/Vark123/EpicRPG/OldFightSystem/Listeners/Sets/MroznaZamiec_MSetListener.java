package me.Vark123.EpicRPG.OldFightSystem.Listeners.Sets;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.OldFightSystem.EpicDamageType;
import me.Vark123.EpicRPG.OldFightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.OldRuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class MroznaZamiec_MSetListener implements Listener {
	
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
		int amount = info.getSetCounts().getOrDefault("Mroczna_Zamiec_M", 0);
		if(amount < 3)
			return;
		
		String type = ir.getMagicType();
		if(!(type.equalsIgnoreCase("mrok") || type.equalsIgnoreCase("woda")
				|| type.equalsIgnoreCase("natura") || type.equalsIgnoreCase("krew")))
			return;
		
		e.increaseModifier(0.25);
	}
	
}
