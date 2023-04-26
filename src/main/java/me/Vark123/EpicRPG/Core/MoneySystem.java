package me.Vark123.EpicRPG.Core;

public class MoneySystem {

	private static final MoneySystem instance = new MoneySystem();
	
	private MoneySystem() {}
	
	public static MoneySystem getInstance() {
		return instance;
	}
	
}
