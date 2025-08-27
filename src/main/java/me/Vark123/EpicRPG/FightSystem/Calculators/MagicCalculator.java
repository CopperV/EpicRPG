package me.Vark123.EpicRPG.FightSystem.Calculators;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.Stats.ChangeStats;

public class MagicCalculator implements IDamageCalculator {

	@Override
	public DamageCalculatorResult calc(
			LivingEntity damager, 
			LivingEntity victim,
			double damage,
			Object... args) {
		DamageCalculatorResult result = new DamageCalculatorResult(damage, false);
		
		if(!(damager instanceof Player)
				|| !PlayerManager.getInstance().playerExists((Player) damager))
			return result;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgStats stats = rpg.getStats();
		RpgPlayerInfo info = rpg.getInfo();
		ChangeStats.change(rpg, null, true);
		
		if(args == null || args.length < 1 
				|| !(args[0] instanceof EpicRune))
			return new DamageCalculatorResult(-1, false);
		
		EpicRune rune = (EpicRune) args[0];
		double wplyw = rune.getWplyw();

		if(info.getSetCounts().getOrDefault("Mroczna_Zamiec", 0) > 3) {
			wplyw += 0.1;
		}
		else if(info.getSetCounts().getOrDefault("Mroczna_Zamiec_H", 0) > 3) {
			wplyw += 0.13;
		}
		else if(info.getSetCounts().getOrDefault("Mroczna_Zamiec_M", 0) > 3) {
			wplyw += 0.17;
		}

		if(info.getSetCounts().getOrDefault("Wodny_Krag", 0) > 3) {
			wplyw += 0.01;
		}
		
		//DMG OD OBRAZEN
		damage += (stats.getFinalObrazenia() / Math.max(wplyw, 0.01)) * 0.01;
		//DMG OD INTELIGENCJI
		damage += wplyw * stats.getFinalInteligencja() * damage * 0.01;
		
//		double addDmg = (stats.getFinalObrazenia() / Math.max(wplyw, 0.01)) * 0.01;
//		addDmg += wplyw * stats.getFinalInteligencja() * damage * 0.01;
		
//		damage += addDmg;
		
		boolean crit = DamageUtils.checkCrit(rpg, victim);
		result.isCrit = crit;
		
		damage = DamageUtils.randomizeDamage(rpg, damage);
		
		if(victim instanceof Player) {
			damage *= crit ? 0.3 : 0.1;
		} else {
			damage *= crit ? 1.4 : 1;
		}

		damage = Math.max(damage, 1);
		
		result.damage = damage;
		return result;
	}

}
