package me.Vark123.EpicRPG.Core;

public class CoinsSystem {
	
	private static final CoinsSystem instance = new CoinsSystem();
	
	private CoinsSystem() {}
	
	public static CoinsSystem getInstance() {
		return instance;
	}

}
