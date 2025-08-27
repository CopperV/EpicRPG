package me.Vark123.EpicRPG.RuneSystem.SummonSystem;

import java.util.Comparator;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class SummonDefinition {
	private final String runeName;
	private final String wildMobType;
	private final List<SummonVariant> variants;

    public SummonVariant getVariantForInt(int intelligence) {
        return variants.stream()
            .filter(v -> v.getRequiredInt() <= intelligence)
            .max(Comparator.comparingInt(SummonVariant::getRequiredInt))
            .orElse(variants.get(0));
    }
}
