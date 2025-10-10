package me.Vark123.EpicRPG.FightSystem.Calculators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.Config;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Stats.CheckStats;
import me.Vark123.EpicRPG.Stats.ComparableStat;
import me.Vark123.EpicRPG.Stats.StatTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class MeleeCalculator implements IDamageCalculator {
	
	private static Map<StatTypes, Integer> normalStatsPriority = new LinkedHashMap<>();
	private static Map<StatTypes, Integer> critStatsPriority = new LinkedHashMap<>();
	static {
		normalStatsPriority.put(StatTypes.SILA, 0);
		normalStatsPriority.put(StatTypes.ZDOLNOSCI_MYSLIWSKIE, 1);
		normalStatsPriority.put(StatTypes.ZRECZNOSC, 2);
		normalStatsPriority.put(StatTypes.WYTRZYMALOSC, 3);
		normalStatsPriority.put(StatTypes.INTELIGENCJA, 4);
		normalStatsPriority.put(StatTypes.MANA, 5);

		critStatsPriority.put(StatTypes.ZRECZNOSC, 0);
		critStatsPriority.put(StatTypes.SILA, 1);
		critStatsPriority.put(StatTypes.ZDOLNOSCI_MYSLIWSKIE, 2);
		critStatsPriority.put(StatTypes.INTELIGENCJA, 3);
		critStatsPriority.put(StatTypes.WYTRZYMALOSC, 4);
		critStatsPriority.put(StatTypes.MANA, 5);
	}

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

		boolean hasWeapon = Utils.hasWeapon(p);
		if(hasWeapon) {
			if(p.getInventory().getItemInMainHand().getType().equals(Material.BOW) ||
					p.getInventory().getItemInMainHand().getType().equals(Material.CROSSBOW)) {
				p.sendMessage(Main.getInstance().getPrefix()+" §cNie mozna bic bronia dystansowa!");
				result.damage = -1;
				return result;
			}
			
			ChangeStats.change(rpg);
			
			if(!CheckStats.check(rpg, p.getInventory().getItemInMainHand())) {
				p.sendMessage(Main.getInstance().getPrefix()+" §cNie mozesz uzywac tej broni");
				result.damage = -1;
				return result;
			}
		} else {
			ChangeStats.change(rpg);
		}
		
		RpgStats stats = rpg.getStats();
		RpgSkills skills = rpg.getSkills();
		
		boolean crit = DamageUtils.checkCrit(rpg, victim);
		result.isCrit = crit;
		
		if(!hasWeapon && skills.hasPolnocnyBarbarzynca()) {
			stats.setFinalObrazenia((int) (stats.getFinalObrazenia() + stats.getFinalSila()*0.8));
			stats.setFinalObrazenia((int) (stats.getFinalObrazenia() + stats.getFinalWytrzymalosc()*0.65));
		}
		
		if(victim instanceof Player) {
			damage = stats.getFinalObrazenia();
			damage = DamageUtils.randomizeDamage(rpg, damage);
			damage *= crit ? 0.3 : 0.1;
			damage = Math.max(damage, 1);
			
			result.damage = damage;
			return result;
		}
		
		damage = (crit ? 1.25 : 1) * stats.getFinalObrazenia();
		damage = Math.max(damage, 1);
		
		int level = rpg.getInfo().getLevel();
		MutableDouble presentDamage = new MutableDouble(damage);
		MutableDouble totalDamage = new MutableDouble(damage);
		Collection<ComparableStat> statInfos = getMostSignificantStats(stats, crit);
		statInfos.stream()
			.map(statInfo -> {
				double max = level * Config.get().getMaxLearnedStatPerLevel() * (1/statInfo.getComparableFactor()) * Config.get().getLearnBreakFactor();
				if(statInfo.getValue() <= max) {
					double s1 = statInfo.getPercentSystemFactor() * statInfo.getValue();
					double s2 = statInfo.getAdditiveSystemFactor() * statInfo.getValue();
					return s1 * presentDamage.doubleValue() + s2;
				}
				
				double statValue = max + Config.get().getStatBreakFactor() * (statInfo.getValue() - max);
				double s1 = statInfo.getPercentSystemFactor() * statValue;
				double s2 = statInfo.getAdditiveSystemFactor() * statValue;
				return s1 * presentDamage.doubleValue() + s2;
			})
			.forEach(totalDamage::add);
		
		damage = totalDamage.doubleValue();
		damage = DamageUtils.randomizeDamage(rpg, damage);

		if(!hasWeapon && !skills.hasPolnocnyBarbarzynca()) {
			damage *= 0.1;
		}
		
		result.damage = damage;
		return result;
	}
	
	private Collection<ComparableStat> getMostSignificantStats(RpgStats stats, boolean isCrit) {
		//INIT
		List<ComparableStat> startList = new ArrayList<>(6);
		startList.add(new ComparableStat(StatTypes.SILA, stats.getFinalSila(), 1, isCrit ? 0.0033 : 0.0029, isCrit ? 0.4 : 0.35));
		startList.add(new ComparableStat(StatTypes.WYTRZYMALOSC, stats.getFinalWytrzymalosc(), 1, isCrit ? 0.0015 : 0.0013, isCrit ? 0.23 : 0.2));
		startList.add(new ComparableStat(StatTypes.ZRECZNOSC, stats.getFinalZrecznosc(), 1, isCrit ? 0.0027 : 0.0015, isCrit ? 0.475 : 0.38));
		startList.add(new ComparableStat(StatTypes.ZDOLNOSCI_MYSLIWSKIE, stats.getFinalZdolnosciMysliwskie(), 1, isCrit ? 0.0022 : 0.002, isCrit ? 0.32 : 0.3));
		startList.add(new ComparableStat(StatTypes.INTELIGENCJA, stats.getFinalInteligencja(), 1, isCrit ? 0.0018 : 0.0015, isCrit ? 0.27 : 0.23));
		startList.add(new ComparableStat(StatTypes.MANA, stats.getFinalMana(), 0.5, isCrit ? 0.0008 : 0.0006, isCrit ? 0.12 : 0.09));
		
		//SORT
		List<ComparableStat> sortedList = startList.stream()
				.filter(stat -> stat.getComparableFactor() > 0)
				.sorted((stat1, stat2) -> {
					int compare = Double.compare(stat1.getComparableFactor() * stat1.getValue(), stat2.getComparableFactor() * stat2.getValue());
					if(compare != 0)
						return Math.negateExact(compare);
					Map<StatTypes, Integer> priorityMap = isCrit ? critStatsPriority : normalStatsPriority;
					return Integer.compare(priorityMap.get(stat1.getType()), priorityMap.get(stat2.getType()));
				})
				.collect(Collectors.toList());
		
		List<ComparableStat> pickedList = new LinkedList<>();
		if(sortedList.size() > 0)
			pickedList.add(sortedList.get(0));
		if(sortedList.size() > 1) {
			ComparableStat stat = sortedList.get(1);
			stat.setPercentSystemFactor(stat.getPercentSystemFactor() * 0.5);
			stat.setAdditiveSystemFactor(stat.getAdditiveSystemFactor() * 0.5);
			pickedList.add(stat);
		}
		
		return pickedList;
	}
	
	@Getter
	@AllArgsConstructor
	private class StatInfo {
		private String id;
		private int value;
		private double comparableValue;
		private double statFactor;
		private double learnFactor;
		private double factor;
	}

}
