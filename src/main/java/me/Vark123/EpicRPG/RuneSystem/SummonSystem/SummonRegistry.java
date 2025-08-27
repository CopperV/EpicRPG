package me.Vark123.EpicRPG.RuneSystem.SummonSystem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class SummonRegistry {

	private static final Map<String, SummonDefinition> DEFINITIONS = new HashMap<>();
	
	public static void registerDefinition(SummonDefinition definition) {
		DEFINITIONS.put(definition.getRuneName(), definition);
	}

    public static SummonDefinition getDefinition(String runeName) {
        return DEFINITIONS.get(runeName);
    }

    public static Collection<SummonDefinition> getAll() {
        return DEFINITIONS.values();
    }
	
}
