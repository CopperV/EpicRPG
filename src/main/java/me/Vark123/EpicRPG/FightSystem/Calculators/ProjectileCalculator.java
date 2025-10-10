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
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.Config;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Stats.CheckStats;
import me.Vark123.EpicRPG.Stats.ComparableStat;
import me.Vark123.EpicRPG.Stats.StatTypes;

public class ProjectileCalculator implements IDamageCalculator {
	
	private static Map<StatTypes, Integer> bowStatsPriority = new LinkedHashMap<>();
	private static Map<StatTypes, Integer> crossbowStatsPriority = new LinkedHashMap<>();
	static {
		bowStatsPriority.put(StatTypes.ZDOLNOSCI_MYSLIWSKIE, 0);
		bowStatsPriority.put(StatTypes.ZRECZNOSC, 1);
		bowStatsPriority.put(StatTypes.WYTRZYMALOSC, 2);
		bowStatsPriority.put(StatTypes.SILA, 3);
		bowStatsPriority.put(StatTypes.MANA, 4);
		bowStatsPriority.put(StatTypes.INTELIGENCJA, 5);

		crossbowStatsPriority.put(StatTypes.WYTRZYMALOSC, 0);
		crossbowStatsPriority.put(StatTypes.ZDOLNOSCI_MYSLIWSKIE, 1);
		crossbowStatsPriority.put(StatTypes.ZRECZNOSC, 2);
		crossbowStatsPriority.put(StatTypes.SILA, 3);
		crossbowStatsPriority.put(StatTypes.MANA, 4);
		crossbowStatsPriority.put(StatTypes.INTELIGENCJA, 5);
	}

	@Override
	public DamageCalculatorResult calc(
			LivingEntity damager, 
			LivingEntity victim, 
			double damage, 
			Object... args) {
		DamageCalculatorResult result = new DamageCalculatorResult(damage, false);

		if(args == null || args.length < 1 || !(args[0] instanceof Entity)) {
			result.damage = -1;
			return result;
		}
		
		Entity _projectile = (Entity) args[0];
		if(!(_projectile instanceof AbstractArrow))
			return result;
		
		AbstractArrow projectile = (AbstractArrow) _projectile;
		if(!(damager instanceof Player)
				|| !PlayerManager.getInstance().playerExists((Player) damager)) {
			result.damage = projectile.getDamage();
			return result;
		}
		
		if(!projectile.hasMetadata("rpg_bow")) {
			damager.sendMessage(Main.getInstance().getPrefix()+" §cBlad z Metadata strzaly. Zglos ten blad administratorowi");
			result.damage = -1;
			return result;
		}
		if(!projectile.hasMetadata("rpg_force")) {
			damager.sendMessage(Main.getInstance().getPrefix()+" §cBlad z Metadata force. Zglos ten blad administratorowi");
			result.damage = -1;
			return result;
		}
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) damager);
		RpgStats stats = rpg.getStats();
		ItemStack bow = (ItemStack) projectile.getMetadata("rpg_bow").get(0).value();
		float force = projectile.getMetadata("rpg_force").get(0).asFloat();
		ChangeStats.change(rpg, bow);
		if(!CheckStats.check(rpg, bow)) {
			damager.sendMessage(Main.getInstance().getPrefix()+" §cNie mozesz uzywac "+bow.getItemMeta().getDisplayName());
			result.damage = -1;
			return result;
		}
		
		boolean crit = DamageUtils.checkCrit(rpg, victim);
		result.isCrit = crit;
		if(victim instanceof Player) {
			damage = stats.getFinalObrazenia() * force;
			damage = DamageUtils.randomizeDamage(rpg, damage);
			damage *= crit ? 0.3 : 0.1;
			damage = Math.max(damage, 1);
			
			result.damage = damage;
			return result;
		}
		
		damage = (crit ? 1.35 : 1) * stats.getFinalObrazenia() * force;
		damage = Math.max(damage, 1);
		
		int level = rpg.getInfo().getLevel();
		MutableDouble presentDamage = new MutableDouble(damage);
		MutableDouble totalDamage = new MutableDouble(damage);
		Collection<ComparableStat> statInfos = getMostSignificantStats(stats, crit, bow.getType());
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
		
		result.damage = damage;
		return result;
	}
	
	private Collection<ComparableStat> getMostSignificantStats(RpgStats stats, boolean isCrit, Material bow) {
		//INIT
		Map<StatTypes, Integer> priorityMap;
		List<ComparableStat> startList = new ArrayList<>(6);
		switch(bow) {
			case CROSSBOW:
				startList.add(new ComparableStat(StatTypes.SILA, stats.getFinalSila(), 1, isCrit ? 0.0018 : 0.0018, isCrit ? 0.28 : 0.28));
				startList.add(new ComparableStat(StatTypes.WYTRZYMALOSC, stats.getFinalWytrzymalosc(), 1, isCrit ? 0.0034 : 0.0033, isCrit ? 0.5 : 0.495));
				startList.add(new ComparableStat(StatTypes.ZRECZNOSC, stats.getFinalZrecznosc(), 1, isCrit ? 0.0024 : 0.0014, isCrit ? 0.375 : 0.345));
				startList.add(new ComparableStat(StatTypes.ZDOLNOSCI_MYSLIWSKIE, stats.getFinalZdolnosciMysliwskie(), 1, isCrit ? 0.0042 : 0.0032, isCrit ? 0.64 : 0.47));
				startList.add(new ComparableStat(StatTypes.INTELIGENCJA, stats.getFinalInteligencja(), 1, isCrit ? 0.0015 : 0.0014, isCrit ? 0.225 : 0.195));
				startList.add(new ComparableStat(StatTypes.MANA, stats.getFinalMana(), 0.5, isCrit ? 0.0005 : 0.0005, isCrit ? 0.0825 : 0.0675));

				priorityMap = crossbowStatsPriority;
				break;
			case BOW:
			    startList.add(new ComparableStat(StatTypes.SILA, stats.getFinalSila(), 1, isCrit ? 0.0017 : 0.0017, isCrit ? 0.26 : 0.26));
			    startList.add(new ComparableStat(StatTypes.WYTRZYMALOSC, stats.getFinalWytrzymalosc(), 1, isCrit ? 0.0020 : 0.0018, isCrit ? 0.29 : 0.28));
			    startList.add(new ComparableStat(StatTypes.ZRECZNOSC, stats.getFinalZrecznosc(), 1, isCrit ? 0.0024 : 0.0021, isCrit ? 0.375 : 0.315));
			    startList.add(new ComparableStat(StatTypes.ZDOLNOSCI_MYSLIWSKIE, stats.getFinalZdolnosciMysliwskie(), 1, isCrit ? 0.0035 : 0.0030, isCrit ? 0.525 : 0.44));
			    startList.add(new ComparableStat(StatTypes.INTELIGENCJA, stats.getFinalInteligencja(), 1, isCrit ? 0.0014 : 0.0012, isCrit ? 0.2 : 0.18));
			    startList.add(new ComparableStat(StatTypes.MANA, stats.getFinalMana(), 0.5, isCrit ? 0.0005 : 0.0005, isCrit ? 0.0825 : 0.0675));
				
				priorityMap = bowStatsPriority;
				break;
			default:
				priorityMap = null;
				break;
		}
		
		//SORT
		List<ComparableStat> sortedList = startList.stream()
				.filter(stat -> stat.getComparableFactor() > 0)
				.sorted((stat1, stat2) -> {
					int compare = Double.compare(stat1.getComparableFactor() * stat1.getValue(), stat2.getComparableFactor() * stat2.getValue());
					if(compare != 0)
						return Math.negateExact(compare);
					if(priorityMap == null)
						return 0;
					
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
