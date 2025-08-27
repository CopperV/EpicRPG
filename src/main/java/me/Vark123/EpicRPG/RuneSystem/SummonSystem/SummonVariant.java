package me.Vark123.EpicRPG.RuneSystem.SummonSystem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class SummonVariant {
	private final String mobType;
	private final int requiredInt;
}
