package me.Vark123.EpicRPG.FightSystem;

import lombok.Getter;
import me.Vark123.EpicRPG.FightSystem.StatsCalculator.DefenseCalculator;
import me.Vark123.EpicRPG.FightSystem.StatsCalculator.IDamageCalculator;
import me.Vark123.EpicRPG.FightSystem.StatsCalculator.MagicCalculator;
import me.Vark123.EpicRPG.FightSystem.StatsCalculator.MeleeCalculator;
import me.Vark123.EpicRPG.FightSystem.StatsCalculator.ProjectileCalculator;

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
		this.defenseCalculator = new DefenseCalculator();
		this.magicCalculator = new MagicCalculator();
	}

	public static DamageManager getInstance() {
		return instance;
	}

}
