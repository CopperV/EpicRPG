package me.Vark123.EpicRPG.Reputation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReputationContainer {
	
	private static final ReputationContainer instance = new ReputationContainer();
	
	private Map<String, BaseReputation> container = new ConcurrentHashMap<>();
	
	private ReputationContainer() {
		
		BaseReputation r1 = new BaseReputation("archolos", "§e§lEkspedycja Archolos");
		BaseReputation r2 = new BaseReputation("klan", "§9§lKlan Mlota");
		BaseReputation r3 = new BaseReputation("witcher", "§c§lCech Niedzwiedzia");
		
		container.put(r1.getName(), r1);
		container.put(r2.getName(), r2);
		container.put(r3.getName(), r3);
		
	}
	
	public static ReputationContainer getInstance() {
		return instance;
	}

	public Map<String, BaseReputation> getContainer() {
		return container;
	}

}
