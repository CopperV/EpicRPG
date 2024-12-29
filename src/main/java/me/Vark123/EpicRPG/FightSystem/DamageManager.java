package me.Vark123.EpicRPG.FightSystem;

import lombok.Getter;
import me.Vark123.EpicRPG.FightSystem.Calculators.DefenseCalculator;
import me.Vark123.EpicRPG.FightSystem.Calculators.IDamageCalculator;
import me.Vark123.EpicRPG.FightSystem.Calculators.MagicCalculator;
import me.Vark123.EpicRPG.FightSystem.Calculators.MeleeCalculator;
import me.Vark123.EpicRPG.FightSystem.Calculators.ProjectileCalculator;

@Getter
public final class DamageManager {

	private static final DamageManager instance = new DamageManager();
	
	private final IDamageCalculator meleeCalculator;
	private final IDamageCalculator projectileCalculator;
	private final IDamageCalculator magicCalculator;
	private final IDamageCalculator defenseCalculator;
	
	private DamageManager() {
		this.meleeCalculator = new MeleeCalculator();
		this.projectileCalculator = new ProjectileCalculator();
		this.magicCalculator = new MagicCalculator();
		this.defenseCalculator = new DefenseCalculator();
	}
	
	public static final DamageManager get() {
		return instance;
	}
	
}
