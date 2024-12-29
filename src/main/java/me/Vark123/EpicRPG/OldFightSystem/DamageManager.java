package me.Vark123.EpicRPG.OldFightSystem;

import lombok.Getter;
import me.Vark123.EpicRPG.OldFightSystem.StatsCalculator.DefenseCalculator;
import me.Vark123.EpicRPG.OldFightSystem.StatsCalculator.IDamageCalculator;
import me.Vark123.EpicRPG.OldFightSystem.StatsCalculator.MagicCalculator;
import me.Vark123.EpicRPG.OldFightSystem.StatsCalculator.MeleeCalculator;
import me.Vark123.EpicRPG.OldFightSystem.StatsCalculator.ProjectileCalculator;

@Deprecated
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
