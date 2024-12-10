package me.Vark123.EpicRPG.FightSystem.StatsCalculator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Stats.CheckStats;
import me.Vark123.EpicRPG.Utils.Pair;

public class MeleeCalculator implements IDamageCalculator {
	
	private static Map<String, Integer> normalStatsPriority = new LinkedHashMap<>();
	private static Map<String, Integer> critStatsPriority = new LinkedHashMap<>();
	static {
		normalStatsPriority.put("str", 0);
		normalStatsPriority.put("zr", 1);
		normalStatsPriority.put("zd", 2);
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
	public Pair<Double, Boolean> calc(Entity damager, Entity victim, double dmg, Object... args) {
		Pair<Double, Boolean> pair = new Pair<>(dmg, false);
		
		if(!(damager instanceof Player) 
				|| !PlayerManager.getInstance().playerExists((Player) damager)) 
			return pair;

		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		
		if(p.getInventory().getItemInMainHand() != null){
			ChangeStats.change(rpg);
			if(!CheckStats.check(rpg, p.getInventory().getItemInMainHand())) {
				p.sendMessage(Main.getInstance().getPrefix()+" §cNie mozesz uzywac tej broni");
				return new Pair<>(-1., false);
			}
		}
		
		if(p.getInventory().getItemInMainHand().getType().equals(Material.BOW) ||
				p.getInventory().getItemInMainHand().getType().equals(Material.CROSSBOW)) {
			if(!(victim instanceof LivingEntity) || dmg < ((LivingEntity)victim).getHealth()) {
				p.sendMessage(Main.getInstance().getPrefix()+" §cNie mozna bic bronia dystansowa!");
				return new Pair<>(-1., false);
			}
		}
		
		RpgStats stats = rpg.getStats();
		RpgSkills skills = rpg.getSkills();
		RpgModifiers modifiers = rpg.getModifiers();
		
		boolean crit = DamageUtils.checkCrit(rpg, victim);
		if(modifiers.hasPenetracja())
			crit = true;
		pair.setValue(crit);
		
		if(skills.hasPolnocnyBarbarzynca() && !hasWeapon(p)) {
			stats.setFinalObrazenia((int) (stats.getFinalObrazenia() + stats.getFinalSila()*0.8));
			stats.setFinalObrazenia((int) (stats.getFinalObrazenia() + stats.getFinalWytrzymalosc()*0.65));
		}
		
		if(victim instanceof Player) {
			dmg = stats.getFinalObrazenia();
			dmg = DamageUtils.randomizeDamage(dmg);
			pair.setKey(dmg);
			return pair;
		}
		
		dmg = crit ? Math.max(Math.ceil(1.25 * stats.getFinalObrazenia()), 1) : Math.max(stats.getFinalObrazenia(), 1);
		MutableDouble totalDamage = new MutableDouble(dmg);
		Collection<StatInfo> statInfos = getMostSignificantStats(stats, crit);
		if(skills.hasPolnocnyBarbarzynca() || hasWeapon(p)) {
			Collection<Double> statDmgs = statInfos.stream()
					.map(statInfo -> (totalDamage.getValue() * 0.25 + statInfo.getValue()) * statInfo.getFactor())
					.collect(Collectors.toList());
			statDmgs.forEach(totalDamage::add);
		}
		
		dmg = DamageUtils.randomizeDamage(totalDamage.doubleValue(), rpg);
		if(victim instanceof LivingEntity)
			dmg = DamageUtils.randomizeEntityHpDamage(dmg, rpg, (LivingEntity) victim);
		
		pair.setKey(dmg);
		return pair;
	}
	
	private static boolean hasWeapon(Player p) {
		ItemStack hand = p.getInventory().getItemInMainHand();
		if(hand == null || hand.getType().equals(Material.AIR))
			return false;
		if(!hand.hasItemMeta() || !hand.getItemMeta().hasLore())
			return false;
		return hand.getItemMeta().getLore().parallelStream().anyMatch(line -> {
			if(line.contains(" Atrybuty ") || line.contains(" Wymagania "))
				return true;
			return false;
		});
	}
	
	private Collection<StatInfo> getMostSignificantStats(RpgStats stats, boolean isCrit) {
		//INIT
		List<StatInfo> startList = new ArrayList<>(6);
		startList.add(new StatInfo("str", stats.getFinalSila(), stats.getFinalSila(), isCrit ? 4 : 4));
		startList.add(new StatInfo("wytrz", stats.getFinalWytrzymalosc(), stats.getFinalWytrzymalosc(), isCrit ? 1.4 : 1.4));
		startList.add(new StatInfo("zr", stats.getFinalZrecznosc(), stats.getFinalZrecznosc(), isCrit ? 6.5 : 2));
		startList.add(new StatInfo("zd", stats.getFinalZdolnosciMysliwskie(), stats.getFinalZdolnosciMysliwskie(), isCrit ? 2.7 : 1.8));
		startList.add(new StatInfo("int", stats.getFinalInteligencja(), stats.getFinalInteligencja(), isCrit ? 1.8 : 1.5));
		startList.add(new StatInfo("mana", stats.getFinalMana(), stats.getFinalMana() * 0.5, isCrit ? 0.7 : 0.5));
		
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
			pickedList.add(new StatInfo(statInfo.getId(), statInfo.getValue()/2, statInfo.getComparableValue(), statInfo.getFactor()));
		}
		return pickedList;
	}
	
	@Getter
	@AllArgsConstructor
	private class StatInfo {
		private String id;
		private int value;
		private double comparableValue;
		private double factor;
	}

}
