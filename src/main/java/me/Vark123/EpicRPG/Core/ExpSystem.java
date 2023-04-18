package me.Vark123.EpicRPG.Core;

import java.util.Random;

public class ExpSystem {

	private final static ExpSystem instance = new ExpSystem();
	
	public final int MAX_LEVEL = 95;
	private Random rand;
	
	private ExpSystem() {
		rand = new Random();
	}
	
	public static ExpSystem getInstance() {
		return instance;
	}
	
}
