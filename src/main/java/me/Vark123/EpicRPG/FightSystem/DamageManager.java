package me.Vark123.EpicRPG.FightSystem;

import me.Vark123.EpicRPG.FightSystem.Calculators.AttackDamageCalculator;
import me.Vark123.EpicRPG.FightSystem.Calculators.DamageCalculator;
import me.Vark123.EpicRPG.FightSystem.Calculators.DefenseDamageCalculator;
import me.Vark123.EpicRPG.FightSystem.Calculators.ProjectileDamageCalculator;
import me.Vark123.EpicRPG.FightSystem.Calculators.RuneDamageCalculator;

public class DamageManager {
	
	private static final DamageManager instance = new DamageManager();
	
	private DamageCalculator attackCalculator;
	private DamageCalculator projectileCalculator;
	private DamageCalculator defenseCalculator;
	private RuneDamageCalculator runeCalculator;
	
	private DamageManager() {
		this.attackCalculator = new AttackDamageCalculator();
		this.projectileCalculator = new ProjectileDamageCalculator();
		this.defenseCalculator = new DefenseDamageCalculator();
		this.runeCalculator = new RuneDamageCalculator();
	}

	public static DamageManager getInstance() {
		return instance;
	}

	public DamageCalculator getAttackCalculator() {
		return attackCalculator;
	}

	public DamageCalculator getProjectileCalculator() {
		return projectileCalculator;
	}

	public DamageCalculator getDefenseCalculator() {
		return defenseCalculator;
	}

	public RuneDamageCalculator getRuneCalculator() {
		return runeCalculator;
	}

}
