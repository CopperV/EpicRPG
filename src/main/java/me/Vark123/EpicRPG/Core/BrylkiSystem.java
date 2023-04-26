package me.Vark123.EpicRPG.Core;

public class BrylkiSystem {

	private static final BrylkiSystem instance = new BrylkiSystem();
	
	private BrylkiSystem() {}
	
	public static BrylkiSystem getInstance() {
		return instance;
	}
	
}
