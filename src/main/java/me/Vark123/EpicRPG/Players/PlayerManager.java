package me.Vark123.EpicRPG.Players;

import javax.inject.Singleton;

@Singleton
public class PlayerManager {

	private final static PlayerManager instance = new PlayerManager();
	
	private PlayerManager() {
		
	}
	
	public static PlayerManager getInstance() {
		return instance;
	}
	
}
