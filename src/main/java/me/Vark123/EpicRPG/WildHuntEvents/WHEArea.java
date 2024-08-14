package me.Vark123.EpicRPG.WildHuntEvents;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.lumine.mythic.api.adapters.AbstractLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class WHEArea {

	private Map<String, Collection<String>> messages;
	private List<AbstractLocation> locations;
	private TreeMap<Integer, WHEWave> waves;
	
}
