package me.Vark123.EpicRPG;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import me.Vark123.EpicRPG.Utils.Pair;

@Singleton
public class EpicRPGManager {

	private final static EpicRPGManager instance = new EpicRPGManager();
	
	private final Map<String, Pair<Integer, Integer>> mobExp;
	
	private EpicRPGManager() {
		mobExp = new ConcurrentHashMap<>();
	}
	
	public static EpicRPGManager getInstance() {
		return instance;
	}

	public Map<String, Pair<Integer, Integer>> getMobExp() {
		return mobExp;
	}
	
}
