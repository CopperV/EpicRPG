package me.Vark123.EpicRPG.FightSystem.StatsCalculator;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Stats.CheckStats;
import me.Vark123.EpicRPG.Utils.Pair;

public class ProjectileCalculator implements IDamageCalculator {

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
		RpgPlayerInfo info = rpg.getInfo();
		ItemStack bow = (ItemStack) projectile.getMetadata("rpg_bow").get(0).value();
		ChangeStats.change(rpg, bow);
		if(!CheckStats.check(rpg, bow)) {
			damager.sendMessage(Main.getInstance().getPrefix()+" §cNie mozesz uzywac "+bow.getItemMeta().getDisplayName());
			return new Pair<>(-1., false);
		}
		
		boolean crit = DamageUtils.checkCrit(rpg);
		pair.setValue(crit);
		if(victim instanceof Player) {
			double value = crit ? .3 : .15;
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
		
		if(extraFlag)
			dmg = (double) args[0] + stats.getObrazenia() + stats.getPotionObrazenia();
		else
			dmg = stats.getFinalObrazenia();
		
		if(crit) {
			dmg = Math.ceil(1.3 * dmg);
			wspDmgZd = 1.35;
			wspDmgZr = 0.45;
			wspDmgStr = 0.38;
			wspDmgInt = 0.05;
		}else {
			wspDmgZd = 1.1;
			wspDmgZr = 0.3;
			wspDmgStr = 0.25;
			wspDmgInt = 0.02;
		}
	
		if(info.getLevel() < 70) {
			dmgZd = wspDmgZd * stats.getFinalZdolnosci() * dmg / (100*0.04*info.getLevel());
			dmgZr = wspDmgZr * stats.getFinalZrecznosc() * dmg / (100*0.04*info.getLevel());
			dmgStr = wspDmgStr * stats.getFinalSila() * dmg / (100*0.04*info.getLevel());
			dmgInt = wspDmgInt * stats.getFinalInteligencja() * dmg / (100*0.04*info.getLevel());
			dmgWytrz = wspDmgWytrz * stats.getFinalWytrzymalosc() * dmg / (100*0.04*info.getLevel());
			dmgMana = wspDmgMana * stats.getFinalMana() * dmg / (100*0.04*info.getLevel());
		} else {
			dmgZd = wspDmgZd * stats.getFinalZdolnosci() * dmg / (100*0.04*70);
			dmgZr = wspDmgZr * stats.getFinalZrecznosc() * dmg / (100*0.04*70);
			dmgStr = wspDmgStr * stats.getFinalSila() * dmg / (100*0.04*70);
			dmgInt = wspDmgInt * stats.getFinalInteligencja() * dmg / (100*0.04*70);
			dmgWytrz = wspDmgWytrz * stats.getFinalWytrzymalosc() * dmg / (100*0.04*70);
			dmgMana = wspDmgMana * stats.getFinalMana() * dmg / (100*0.04*70);
		}
		
		dmg = dmg + dmgZd + dmgZr + dmgStr + dmgInt + dmgWytrz + dmgMana;
		dmg = DamageUtils.randomizeDamage(dmg, stats);

		if(!extraFlag) {
			float force = projectile.getMetadata("rpg_force").get(0).asFloat();
			dmg *= force;
		}
		
		pair.setKey(dmg);
		return pair;
	}

}
