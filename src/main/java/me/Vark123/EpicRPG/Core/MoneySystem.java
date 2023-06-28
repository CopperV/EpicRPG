package me.Vark123.EpicRPG.Core;

import me.Vark123.EpicRPG.EpicRPGMobManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class MoneySystem {

	private static final MoneySystem instance = new MoneySystem();
	
	private MoneySystem() {}
	
	public static MoneySystem getInstance() {
		return instance;
	}
	
	public void addMobMoney(RpgPlayer rpg, String mob) {
		rpg.getVault().addMoney(EpicRPGMobManager.getInstance().getMobMoney(mob));
	}
	
}
