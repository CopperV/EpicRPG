package me.Vark123.EpicRPG.RuneSystem.SummonSystem.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.SummonLevelCalcEvent;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.SummonRegistry;

public class SummonLevelCalcByStatsListener implements Listener {

	@EventHandler
	private void onCalc(SummonLevelCalcEvent e) {
		Player player = e.getOwner();
		if(!PlayerManager.getInstance().playerExists(player))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		int inteligencja = rpg.getStats().getFinalInteligencja();
		String runeName = e.getRune().getRune().getMythicType();
		
		var def = SummonRegistry.getDefinition(runeName);
		if(def == null)
			return;
		
		var variant = def.getVariantForInt(inteligencja);
		inteligencja -= variant.getRequiredInt();
		double modifier = 0;
		switch(rpg.getInfo().getProffesion()) {
			case "§5Mag":
				modifier = 0.02;
				break;
			case "§cWojownik":
				break;
			case "§2Mysliwy":
				modifier = 0.05;
				break;
			default:
				break;
		}
		
		int bonusLevel = (int) (modifier * (double)inteligencja);
		if(bonusLevel < 0) bonusLevel = 0;
		
		e.increaseLevel(bonusLevel);
	}
	
}
