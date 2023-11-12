package me.Vark123.EpicRPG.OldFightSystem.Modifiers;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.event.EventPriority;

public class DamageModifierManager {

	private static DamageModifierManager instance = new DamageModifierManager();
	
	private Map<EventPriority, List<DamageModifier>> modifiers;
	
	private DamageModifierManager() {
		modifiers = new LinkedHashMap<>();
		for(EventPriority value : EventPriority.values()) {
			List<DamageModifier> tmp = new LinkedList<>();
			modifiers.put(value, tmp);
		}
	}
	
	public void registerModifier(DamageModifier modifier) {
		registerModifier(modifier, EventPriority.NORMAL);
	}
	
	public void registerModifier(DamageModifier modifier, EventPriority priority) {
		modifiers.get(priority).add(modifier);
	}
	
	public Map<EventPriority, List<DamageModifier>> getModifiers() {
		return modifiers;
	}

	public static DamageModifierManager getInstance() {
		return instance;
	}
	
}
