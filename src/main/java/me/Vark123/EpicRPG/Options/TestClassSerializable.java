package me.Vark123.EpicRPG.Options;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class TestClassSerializable implements ConfigurationSerializable {
	private String s;
	private int i;
	private double d;
	
	public TestClassSerializable() {
		s = "test";
		i = 10;
		d = 3.14;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put("s", s);
		map.put("i", i);
		map.put("d", d);
		return map;
	}
}
