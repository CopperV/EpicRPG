package me.Vark123.EpicRPG.Core;

import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgVault;

public class StygiaSystem {

	private static final StygiaSystem instance = new StygiaSystem();
	
	public final int MIN_STYGIA_LEVEL = 60;
	
	public StygiaSystem() {}
	
	public static StygiaSystem getInstance() {
		return instance;
	}
	
	public void addMobStygia(RpgPlayer rpg, int xp) {
		RpgPlayerInfo info = rpg.getInfo();
		RpgVault vault = rpg.getVault();
		if(info.getLevel() < 60)
			return;
		Player p = rpg.getPlayer();
		int stygia;
		if(p.hasPermission("rpg.vip")) {
			stygia = (int) (((double) xp) * 1.5 / 100);
		} else {
			stygia = xp/100;
		}
		
		if(stygia == 0)
			return;
		vault.addStygia(stygia);
	}
	
	public void addQuestStygia(RpgPlayer rpg, int xp) {
		RpgPlayerInfo info = rpg.getInfo();
		RpgVault vault = rpg.getVault();
		if(info.getLevel() < 60)
			return;
		Player p = rpg.getPlayer();
		int stygia = xp/400;
		vault.addStygia(stygia);
		p.sendMessage("§3§o+"+ stygia +" stygia §7[§3§o"+vault.getStygia()+" stygia§7]");
	}
	
}
