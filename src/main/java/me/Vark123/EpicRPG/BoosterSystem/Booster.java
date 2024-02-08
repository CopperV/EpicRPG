package me.Vark123.EpicRPG.BoosterSystem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Booster {
	private String name;
	private String display;
	private double modifier;
}
