package me.Vark123.EpicRPG.Core;

public class StygiaSystem {

	private static final StygiaSystem instance = new StygiaSystem();
	
	public StygiaSystem() {}
	
	public static StygiaSystem getInstance() {
		return instance;
	}
	
}
