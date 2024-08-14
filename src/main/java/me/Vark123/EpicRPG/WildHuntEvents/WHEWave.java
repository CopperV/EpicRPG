package me.Vark123.EpicRPG.WildHuntEvents;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class WHEWave {

	private Map<String, Integer> mobs;
	
}
