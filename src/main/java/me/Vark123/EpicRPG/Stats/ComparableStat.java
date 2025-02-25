package me.Vark123.EpicRPG.Stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ComparableStat {

	private StatTypes type;
	private int value;
	private double comparableFactor;
	
	@Setter
	private double percentSystemFactor;
	@Setter
	private double additiveSystemFactor;
	
}
