package me.Vark123.EpicRPG.Players;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import me.Vark123.EpicRPG.Jewelry.Amulet;
import me.Vark123.EpicRPG.Jewelry.Gloves;
import me.Vark123.EpicRPG.Jewelry.JewerlyItem;
import me.Vark123.EpicRPG.Jewelry.Ring;

public class RpgJewelry {
	
	private RpgPlayer rpg;

	private Map<Integer,JewerlyItem> akcesoria;
	
	public RpgJewelry(RpgPlayer rpg) {
		this.rpg = rpg;
		akcesoria = createJewerlyEq();
	}
	
	public RpgPlayer getRpg() {
		return rpg;
	}

	public Map<Integer, JewerlyItem> getAkcesoria() {
		return akcesoria;
	}

	private Map<Integer,JewerlyItem> createJewerlyEq() {
		Map<Integer,JewerlyItem> toReturn = new ConcurrentHashMap<>();
		toReturn.put(0, new Amulet(0));
		toReturn.put(1, new Ring(0));
		toReturn.put(2, new Ring(0));
		toReturn.put(3, new Gloves(0));
		return toReturn;
	}
}
