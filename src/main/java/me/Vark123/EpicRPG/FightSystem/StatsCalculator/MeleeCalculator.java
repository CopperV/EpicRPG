package me.Vark123.EpicRPG.FightSystem.StatsCalculator;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Stats.CheckStats;
import me.Vark123.EpicRPG.Utils.Pair;

public class MeleeCalculator implements IDamageCalculator {

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
		RpgPlayerInfo info = rpg.getInfo();
		RpgModifiers modifiers = rpg.getModifiers();
		
		boolean crit = DamageUtils.checkCrit(rpg);
		if(modifiers.hasPenetracja())
			crit = true;
		pair.setValue(crit);
		
		if(skills.hasPolnocnyBarbarzynca() && !hasWeapon(p)) {
			stats.setFinalObrazenia((int) (stats.getFinalObrazenia() + stats.getFinalSila()*0.8));
			stats.setFinalObrazenia((int) (stats.getFinalObrazenia() + stats.getFinalWytrzymalosc()*0.65));
		}
		
		if(victim instanceof Player) {
			double value = crit ? .15 : .1;
			dmg = stats.getFinalObrazenia() * value;
			dmg = DamageUtils.randomizeDamage(dmg);
			pair.setKey(dmg);
			return pair;
		}
		
		double dmgZr = 0;
		double dmgZd = 0;
		double dmgStr = 0;
		double dmgWytrz = 0;
		double dmgInt = 0;
		double dmgMana = 0;
		
		double wspDmgZr = 0;
		double wspDmgZd = 0;
		double wspDmgStr = 0;
		double wspDmgWytrz = 0;
		double wspDmgInt = 0;
		double wspDmgMana = 0;
		
		if(crit) {
			dmg = Math.ceil(1.3 * stats.getFinalObrazenia());
			wspDmgStr = 0.65;
			wspDmgZr = 0.9;
			wspDmgZd = 0.05;
			wspDmgInt = 0.05;
		} else {
			dmg = Math.ceil(stats.getFinalObrazenia());
			wspDmgStr = 0.65;
			wspDmgZr = 0.45;
			wspDmgZd = 0.01;
			wspDmgInt = 0.01;
		}
		
		if(info.getLevel() < 70) {
			dmgZd = wspDmgZd * stats.getFinalZdolnosci() * dmg / (100*0.05*info.getLevel());
			dmgZr = wspDmgZr * stats.getFinalZrecznosc() * dmg / (100*0.05*info.getLevel());
			dmgStr = wspDmgStr * stats.getFinalSila() * dmg / (100*0.05*info.getLevel());
			dmgInt = wspDmgInt * stats.getFinalInteligencja() * dmg / (100*0.05*info.getLevel());
			dmgWytrz = wspDmgWytrz * stats.getFinalWytrzymalosc() * dmg / (100*0.05*info.getLevel());
			dmgMana = wspDmgMana * stats.getFinalMana() * dmg / (100*0.05*info.getLevel());
		} else {
			dmgZd = wspDmgZd * stats.getFinalZdolnosci() * dmg / (100*0.05*70);
			dmgZr = wspDmgZr * stats.getFinalZrecznosc() * dmg / (100*0.05*70);
			dmgStr = wspDmgStr * stats.getFinalSila() * dmg / (100*0.05*70);
			dmgInt = wspDmgInt * stats.getFinalInteligencja() * dmg / (100*0.05*70);
			dmgWytrz = wspDmgWytrz * stats.getFinalWytrzymalosc() * dmg / (100*0.05*70);
			dmgMana = wspDmgMana * stats.getFinalMana() * dmg / (100*0.05*70);
		}
		
		dmg = dmg + dmgZd + dmgZr + dmgStr + dmgInt + dmgWytrz + dmgMana;
		dmg = DamageUtils.randomizeDamage(dmg, stats);
		
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

}
