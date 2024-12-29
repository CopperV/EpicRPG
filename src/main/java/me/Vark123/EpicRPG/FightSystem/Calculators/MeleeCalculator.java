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
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Stats.CheckStats;
import me.Vark123.EpicRPG.Utils.Utils;

public class MeleeCalculator implements IDamageCalculator {
	
	private static Map<String, Integer> normalStatsPriority = new LinkedHashMap<>();
	private static Map<String, Integer> critStatsPriority = new LinkedHashMap<>();
	static {
		normalStatsPriority.put("str", 0);
		normalStatsPriority.put("zd", 1);
		normalStatsPriority.put("zr", 2);
		normalStatsPriority.put("wytrz", 3);
		normalStatsPriority.put("int", 4);
		normalStatsPriority.put("mana", 5);

		critStatsPriority.put("zr", 0);
		critStatsPriority.put("str", 1);
		critStatsPriority.put("zd", 2);
		critStatsPriority.put("int", 3);
		critStatsPriority.put("wytrz", 4);
		critStatsPriority.put("mana", 5);
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
		
		if(Utils.hasWeapon(p)) {
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
			
		}
		
		RpgStats stats = rpg.getStats();
		RpgSkills skills = rpg.getSkills();
		RpgModifiers modifiers = rpg.getModifiers();
		
		boolean crit = DamageUtils.checkCrit(rpg, victim);
		if(!crit && modifiers.hasActiveModifier(EpicModifierTypes.PENETRACJA))
			crit = true;
		result.isCrit = crit;
		
		if(!Utils.hasWeapon(p)) {
			if(skills.hasPolnocnyBarbarzynca()) {
				stats.setFinalObrazenia((int) (stats.getFinalObrazenia() + stats.getFinalSila()*0.8));
				stats.setFinalObrazenia((int) (stats.getFinalObrazenia() + stats.getFinalWytrzymalosc()*0.65));
			} else {
				stats.setFinalObrazenia((int) (stats.getFinalObrazenia() * 0.1));
			}
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
		Collection<StatInfo> statInfos = getMostSignificantStats(stats, crit);
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
	
	private Collection<StatInfo> getMostSignificantStats(RpgStats stats, boolean isCrit) {
		//INIT
		List<StatInfo> startList = new ArrayList<>(6);
		startList.add(new StatInfo("str", stats.getFinalSila(), stats.getFinalSila(), 1, 1, isCrit ? 0.004 : 0.004));
		startList.add(new StatInfo("wytrz", stats.getFinalWytrzymalosc(), stats.getFinalWytrzymalosc(), 1, 1, isCrit ? 0.002 : 0.0017));
		startList.add(new StatInfo("zr", stats.getFinalZrecznosc(), stats.getFinalZrecznosc(), 1, 1, isCrit ? 0.0052 : 0.0024));
		startList.add(new StatInfo("zd", stats.getFinalZdolnosciMysliwskie(), stats.getFinalZdolnosciMysliwskie(), 1, 1, isCrit ? 0.0028 : 0.0026));
		startList.add(new StatInfo("int", stats.getFinalInteligencja(), stats.getFinalInteligencja(), 1, 1, isCrit ? 0.0023 : 0.002));
		startList.add(new StatInfo("mana", stats.getFinalMana(), stats.getFinalMana() * 0.5, 1, 2, isCrit ? 0.001 : 0.0008));
		
		//SORT
		List<StatInfo> sortedList = startList.stream()
				.filter(stat -> stat.getComparableValue() > 0)
				.sorted((stat1, stat2) -> {
					int compare = Double.compare(stat1.comparableValue, stat2.comparableValue);
					if(compare != 0)
						return Math.negateExact(compare);
					return isCrit ? 
							Integer.compare(critStatsPriority.getOrDefault(stat1.getId(), 6), critStatsPriority.getOrDefault(stat2.getId(), 6)) :
							Integer.compare(normalStatsPriority.getOrDefault(stat1.getId(), 6), normalStatsPriority.getOrDefault(stat2.getId(), 6));
				})
				.collect(Collectors.toList());
		
		//PICK
		List<StatInfo> pickedList = new LinkedList<>();
		if(sortedList.size() > 0)
			pickedList.add(sortedList.get(0));
		if(sortedList.size() > 1) {
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
