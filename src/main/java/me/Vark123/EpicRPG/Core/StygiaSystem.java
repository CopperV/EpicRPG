package me.Vark123.EpicRPG.Core;

import org.bukkit.Bukkit;

import me.Vark123.EpicOptions.OptionsAPI;
import me.Vark123.EpicRPG.Core.Events.StygiaModifyEvent;
import me.Vark123.EpicRPG.Options.Serializables.ResourcesInfoSerializable;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgVault;

public class StygiaSystem {

	private static final StygiaSystem instance = new StygiaSystem();
	
	public final int MIN_STYGIA_LEVEL = 60;
	
	public StygiaSystem() {}
	
	public static StygiaSystem getInstance() {
		return instance;
	}
	
	public void addStygia(RpgPlayer rpg, int amount, String reason) {
		StygiaModifyEvent event = new StygiaModifyEvent(rpg, amount, 1, reason);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		int _amount = (int) (event.getAmount()*event.getModifier());
		if(_amount == 0)
			return;

		RpgVault vault = rpg.getVault();
		vault.addStygia(_amount);

		OptionsAPI.get().getPlayerManager().getPlayerOptions(rpg.getPlayer())
			.ifPresent(op -> {
				op.getPlayerOptionByID("epicrpg_resources")
					.ifPresent(pOption -> {
						ResourcesInfoSerializable option = (ResourcesInfoSerializable) pOption.getValue();
						if(!option.isStygiaInfo())
							return;
						rpg.getPlayer().sendMessage("§3§o+"+ _amount +" stygia §7[§3§o"+vault.getStygia()+" stygia§7]");
					});
			});
	}
	
	public void addMobStygia(RpgPlayer rpg, int xp) {
		addStygia(rpg, xp/100, "mob");
	}
	
	public void addQuestStygia(RpgPlayer rpg, int xp) {
		addStygia(rpg, xp/400, "mob");
	}
	
}
