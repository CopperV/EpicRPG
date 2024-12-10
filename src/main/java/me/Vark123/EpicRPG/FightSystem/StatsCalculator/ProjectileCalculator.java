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
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Stats.CheckStats;
import me.Vark123.EpicRPG.Utils.Pair;

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

		crossbowStatsPriority.put("zd", 0);
		crossbowStatsPriority.put("wytrz", 1);
		crossbowStatsPriority.put("zr", 2);
		crossbowStatsPriority.put("str", 3);
		crossbowStatsPriority.put("mana", 4);
		crossbowStatsPriority.put("int", 5);
	}

	@Override
	public Pair<Double, Boolean> calc(Entity damager, Entity victim, double dmg, Object... args) {
		Pair<Double, Boolean> pair = new Pair<>(dmg, false);
		
		if(!(damager instanceof Projectile))
			return pair;
		
		Projectile projectile = (Projectile) damager;
		if(!(projectile instanceof AbstractArrow))
			return pair;
		
		if(!(projectile.getShooter() instanceof Entity))
			return pair;
		
		boolean extraFlag = false;
		if(args != null && args.length > 0
				&& args[0] instanceof Double)
			extraFlag = true;
		
		Entity shooter = (Entity) projectile.getShooter();
		if(!(shooter instanceof Player) 
				|| !PlayerManager.getInstance().playerExists((Player) shooter)) {
			double newDmg = ((AbstractArrow) projectile).getDamage();
			pair.setKey(newDmg);
			return pair;
		}
		
		if(!projectile.hasMetadata("rpg_bow")) {
			damager.sendMessage(Main.getInstance().getPrefix()+" §cBlad z Metadata strzaly. Zglos ten blad administratorowi");
			return new Pair<>(-1., false);
		}
		if(!projectile.hasMetadata("rpg_force")) {
			damager.sendMessage(Main.getInstance().getPrefix()+" §cBlad z Metadata force. Zglos ten blad administratorowi");
			return new Pair<>(-1., false);
		}
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) shooter);
		RpgStats stats = rpg.getStats();
		ItemStack bow = (ItemStack) projectile.getMetadata("rpg_bow").get(0).value();
		ChangeStats.change(rpg, bow);
		if(!CheckStats.check(rpg, bow)) {
			damager.sendMessage(Main.getInstance().getPrefix()+" §cNie mozesz uzywac "+bow.getItemMeta().getDisplayName());
			return new Pair<>(-1., false);
		}
		
		boolean crit = DamageUtils.checkCrit(rpg, victim);
		pair.setValue(crit);
		if(victim instanceof Player) {
			dmg = DamageUtils.randomizeDamage(dmg);
			pair.setKey(dmg);
			return pair;
		}
		
		dmg = extraFlag ? (double) args[0] + stats.getObrazenia() + stats.getPotionObrazenia() : stats.getFinalObrazenia();
		dmg *= crit ? 1.44 : 1;
		
		MutableDouble totalDamage = new MutableDouble(dmg);
		Collection<StatInfo> statInfos = getMostSignificantStats(stats, crit, bow.getType());
		Collection<Double> statDmgs = statInfos.stream()
				.map(statInfo -> (totalDamage.getValue() * 0.35 + statInfo.getValue()) * statInfo.getFactor())
				.collect(Collectors.toList());
		statDmgs.forEach(totalDamage::add);
		
		dmg = DamageUtils.randomizeDamage(totalDamage.doubleValue(), rpg);
		if(victim instanceof LivingEntity)
			dmg = DamageUtils.randomizeEntityHpDamage(dmg, rpg, (LivingEntity) victim);

		if(!extraFlag) {
			float force = projectile.getMetadata("rpg_force").get(0).asFloat();
			dmg *= force;
		}
		
		pair.setKey(dmg);
		return pair;
	}
	
	private Collection<StatInfo> getMostSignificantStats(RpgStats stats, boolean isCrit, Material bow) {
		//INIT
		List<StatInfo> startList = new ArrayList<>(6);
		switch(bow) {
			case CROSSBOW:
				startList.add(new StatInfo("str", stats.getFinalSila(), stats.getFinalSila(), isCrit ? 4.8 : 3.4));
				startList.add(new StatInfo("wytrz", stats.getFinalWytrzymalosc(), stats.getFinalWytrzymalosc(), isCrit ? 7.5 : 6));
				startList.add(new StatInfo("zr", stats.getFinalZrecznosc(), stats.getFinalZrecznosc(), isCrit ? 5.5 : 4));
				startList.add(new StatInfo("zd", stats.getFinalZdolnosciMysliwskie(), stats.getFinalZdolnosciMysliwskie(), isCrit ? 9.8 : 7.3));
				startList.add(new StatInfo("int", stats.getFinalInteligencja(), stats.getFinalInteligencja(), isCrit ? 3.5 : 2.8));
				startList.add(new StatInfo("mana", stats.getFinalMana(), stats.getFinalMana() * 0.5, isCrit ? 2 : 1.5));
				break;
			case BOW:
				startList.add(new StatInfo("str", stats.getFinalSila(), stats.getFinalSila(), isCrit ? 4.8 : 3.4));
				startList.add(new StatInfo("wytrz", stats.getFinalWytrzymalosc(), stats.getFinalWytrzymalosc(), isCrit ? 5.2 : 3.4));
				startList.add(new StatInfo("zr", stats.getFinalZrecznosc(), stats.getFinalZrecznosc(), isCrit ? 7.5 : 6));
				startList.add(new StatInfo("zd", stats.getFinalZdolnosciMysliwskie(), stats.getFinalZdolnosciMysliwskie(), isCrit ? 9.8 : 7.3));
				startList.add(new StatInfo("int", stats.getFinalInteligencja(), stats.getFinalInteligencja(), isCrit ? 3.5 : 2.8));
				startList.add(new StatInfo("mana", stats.getFinalMana(), stats.getFinalMana() * 0.5, isCrit ? 2 : 1.5));
				break;
			default:
				startList.add(new StatInfo("str", stats.getFinalSila(), stats.getFinalSila(), isCrit ? 4.8 : 3.4));
				startList.add(new StatInfo("wytrz", stats.getFinalWytrzymalosc(), stats.getFinalWytrzymalosc(), isCrit ? 7.5 : 6));
				startList.add(new StatInfo("zr", stats.getFinalZrecznosc(), stats.getFinalZrecznosc(), isCrit ? 5.5 : 4));
				startList.add(new StatInfo("zd", stats.getFinalZdolnosciMysliwskie(), stats.getFinalZdolnosciMysliwskie(), isCrit ? 9.8 : 7.3));
				startList.add(new StatInfo("int", stats.getFinalInteligencja(), stats.getFinalInteligencja(), isCrit ? 3.5 : 2.8));
				startList.add(new StatInfo("mana", stats.getFinalMana(), stats.getFinalMana() * 0.5, isCrit ? 2 : 1.5));
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
			pickedList.add(new StatInfo(statInfo.getId(), statInfo.getValue() / 2, statInfo.getComparableValue(),
					statInfo.getFactor()));
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
