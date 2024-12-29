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

public class ProjectileCalculator implements IDamageCalculator {
	
	private static Map<String, Integer> bowStatsPriority = new LinkedHashMap<>();
	private static Map<String, Integer> crossbowStatsPriority = new LinkedHashMap<>();
	static {
		bowStatsPriority.put("zd", 0);
		bowStatsPriority.put("zr", 1);
		bowStatsPriority.put("wytrz", 2);
		bowStatsPriority.put("str", 3);
		bowStatsPriority.put("mana", 4);
		bowStatsPriority.put("int", 5);

		crossbowStatsPriority.put("wytrz", 0);
		crossbowStatsPriority.put("zd", 1);
		crossbowStatsPriority.put("zr", 2);
		crossbowStatsPriority.put("str", 3);
		crossbowStatsPriority.put("mana", 4);
		crossbowStatsPriority.put("int", 5);
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
		Collection<StatInfo> statInfos = getMostSignificantStats(stats, crit, bow.getType());
		statInfos.stream()
			.map(statInfo -> {
				double max = level * Config.get().getMaxLearnedStatPerLevel() * statInfo.learnFactor * Config.get().getLearnBreakFactor();
				if(statInfo.getValue() < max) {
					return statInfo.getFactor() * statInfo.getValue() * statInfo.getStatFactor();
				}
				
				double tmp = max + Config.get().getStatBreakFactor() * (statInfo.getValue() - max);
				return statInfo.getFactor() * tmp * statInfo.getStatFactor();
			})
			.map(_damage -> _damage * presentDamage.doubleValue())
			.forEach(totalDamage::add);
		
		damage = totalDamage.doubleValue();
		damage = DamageUtils.randomizeDamage(rpg, damage);
		if(victim instanceof LivingEntity)
			damage = DamageUtils.randomizeEntityHpDamage(damage, rpg, victim);
		
		result.damage = damage;
		return result;
	}
	
	private Collection<StatInfo> getMostSignificantStats(RpgStats stats, boolean isCrit, Material bow) {
		//INIT
		List<StatInfo> startList = new ArrayList<>(6);
		switch(bow) {
			case CROSSBOW:
				startList.add(new StatInfo("str", stats.getFinalSila(), stats.getFinalSila(), 1, 1, isCrit ? 0.0042 : 0.0042));
				startList.add(new StatInfo("wytrz", stats.getFinalWytrzymalosc(), stats.getFinalWytrzymalosc(), 1, 1, isCrit ? 0.0076 : 0.0075));
				startList.add(new StatInfo("zr", stats.getFinalZrecznosc(), stats.getFinalZrecznosc(), 1, 1, isCrit ? 0.0056 : 0.0032));
				startList.add(new StatInfo("zd", stats.getFinalZdolnosciMysliwskie(), stats.getFinalZdolnosciMysliwskie(), 1, 1, isCrit ? 0.0096 : 0.0072));
				startList.add(new StatInfo("int", stats.getFinalInteligencja(), stats.getFinalInteligencja(), 1, 1, isCrit ? 0.0034 : 0.003));
				startList.add(new StatInfo("mana", stats.getFinalMana(), stats.getFinalMana() * 0.5, 1, 2, isCrit ? 0.0026 : 0.0022));
				break;
			case BOW:
				startList.add(new StatInfo("str", stats.getFinalSila(), stats.getFinalSila(), 1, 1, isCrit ? 0.004 : 0.004));
				startList.add(new StatInfo("wytrz", stats.getFinalWytrzymalosc(), stats.getFinalWytrzymalosc(), 1, 1, isCrit ? 0.0044 : 0.0042));
				startList.add(new StatInfo("zr", stats.getFinalZrecznosc(), stats.getFinalZrecznosc(), 1, 1, isCrit ? 0.0056 : 0.0048));
				startList.add(new StatInfo("zd", stats.getFinalZdolnosciMysliwskie(), stats.getFinalZdolnosciMysliwskie(), 1, 1, isCrit ? 0.008 : 0.0068));
				startList.add(new StatInfo("int", stats.getFinalInteligencja(), stats.getFinalInteligencja(), 1, 1, isCrit ? 0.0031 : 0.0028));
				startList.add(new StatInfo("mana", stats.getFinalMana(), stats.getFinalMana() * 0.5, 1, 2, isCrit ? 0.0023 : 0.002));
				break;
			default:
				startList.add(new StatInfo("str", stats.getFinalSila(), stats.getFinalSila(), 1, 1, isCrit ? 0.0042 : 0.0042));
				startList.add(new StatInfo("wytrz", stats.getFinalWytrzymalosc(), stats.getFinalWytrzymalosc(), 1, 1, isCrit ? 0.0076 : 0.0075));
				startList.add(new StatInfo("zr", stats.getFinalZrecznosc(), stats.getFinalZrecznosc(), 1, 1, isCrit ? 0.0056 : 0.0032));
				startList.add(new StatInfo("zd", stats.getFinalZdolnosciMysliwskie(), stats.getFinalZdolnosciMysliwskie(), 1, 1, isCrit ? 0.0096 : 0.0072));
				startList.add(new StatInfo("int", stats.getFinalInteligencja(), stats.getFinalInteligencja(), 1, 1, isCrit ? 0.0034 : 0.003));
				startList.add(new StatInfo("mana", stats.getFinalMana(), stats.getFinalMana() * 0.5, 1, 2, isCrit ? 0.0026 : 0.0022));
				break;
		}
		
		// SORT
		List<StatInfo> sortedList = startList.stream().filter(stat -> stat.getComparableValue() > 0)
				.sorted((stat1, stat2) -> {
					int compare = Double.compare(stat1.comparableValue, stat2.comparableValue);
					if (compare != 0)
						return Math.negateExact(compare);
					switch(bow) {
						case CROSSBOW:
							return Integer.compare(crossbowStatsPriority.getOrDefault(stat1.getId(), 6),
									crossbowStatsPriority.getOrDefault(stat2.getId(), 6));
						case BOW:
							return Integer.compare(bowStatsPriority.getOrDefault(stat1.getId(), 6),
									bowStatsPriority.getOrDefault(stat2.getId(), 6));
						default:
							return 0;
					}
				}).collect(Collectors.toList());

		// PICK
		List<StatInfo> pickedList = new LinkedList<>();
		if (sortedList.size() > 0)
			pickedList.add(sortedList.get(0));
		if (sortedList.size() > 1) {
			StatInfo statInfo = sortedList.get(1);
			pickedList.add(new StatInfo(statInfo.getId(), statInfo.getValue(), statInfo.getComparableValue(), 0.5, statInfo.getLearnFactor(), statInfo.getFactor()));
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
