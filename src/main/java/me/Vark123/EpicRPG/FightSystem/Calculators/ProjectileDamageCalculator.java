package me.Vark123.EpicRPG.FightSystem.Calculators;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Stats.CheckStats;
import net.md_5.bungee.api.ChatColor;

public class ProjectileDamageCalculator implements DamageCalculator {

	@Override
	public double calc(Entity damager, Entity victim, double dmg) {
		
		if(!(damager instanceof Projectile))
			return dmg;
		
		Projectile projectile = (Projectile) damager;
		
		if(!(projectile.getShooter() instanceof Entity))
			return dmg;
		
		boolean crit = false;
		
		damager = (Entity) projectile.getShooter();
		
		if(projectile instanceof AbstractArrow) {
			if(damager instanceof Player
					&& PlayerManager.getInstance().playerExists((Player) damager)) {
				//Prevent self-shooting
				if(victim instanceof Player && damager.equals(victim)) {
					projectile.remove();
					return -1;
				}
				
				RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) damager);
				Player p = rpg.getPlayer();
				RpgStats stats = rpg.getStats();
				RpgSkills skills = rpg.getSkills();
				RpgPlayerInfo info = rpg.getInfo();
				RpgModifiers modifiers = rpg.getModifiers();
				
				if(!projectile.hasMetadata("rpg_bow")) {
					damager.sendMessage(Main.getInstance().getPrefix()+" §cBlad z Metadata strzaly. Zglos ten blad administratorowi");
					return -1;
				}
				if(!projectile.hasMetadata("rpg_force")) {
					damager.sendMessage(Main.getInstance().getPrefix()+" §cBlad z Metadata force. Zglos ten blad administratorowi");
					return -1;
				}
				
				ItemStack bow = (ItemStack) projectile.getMetadata("rpg_bow").get(0).value();
				ChangeStats.change(rpg, bow);
				if(!CheckStats.check(rpg, bow)) {
					damager.sendMessage(Main.getInstance().getPrefix()+" §cNie mozesz uzywac "+bow.getItemMeta().getDisplayName());
					return -1;
				}
				
				crit = DamageUtils.checkCrit(rpg);
				
				double dmgZmysly = 0;
				double dmgOgien = 0;
				double dmgPoison = 0;
				double dmgStrzal = 0;
				double dmgLod = 0;
				double dmgPotZr = 0;
				double dmgPotZd = 0;
				double dmgZadzaKrwi = 0;
				double dmgSwieta = 0;
				double dmgZyciodajnaZiemia = 0;
				double dmgZyciodajnaZiemia_M = 0;
				double dmgKrwawa = 0;
				double dmgEksplodujaca = 0;
				double dmgEksplodujaca_H = 0;
				double dmgEksplodujaca_M = 0;
				
				if(victim instanceof Player) {
					if(crit) {
						dmg = Math.ceil(stats.getFinalObrazenia() * .3);
					} else {
						dmg = Math.ceil(stats.getFinalObrazenia() * .15);
					}
				} else {
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
						wspDmgZd = 1.35;
						wspDmgZr = 0.45;
						wspDmgStr = 0.38;
						wspDmgInt = 0.05;
					}else {
						dmg = Math.ceil(stats.getFinalObrazenia());
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
				}
				
				dmg = DamageUtils.randomizeDamage(dmg);
				float force = projectile.getMetadata("rpg_force").get(0).asFloat();
				dmg *= force;
				
//				OnlinePAFPlayer paf = PAFPlayerManager.getInstance().getPlayer(p);
//				if(paf != null) {
//					Clan klan = ClansManager.getInstance().getClan(paf);
//					if(klan != null) {
//						double attack = EpicClansApi.getInst().getAttackValue(klan);
//						dmg += dmg*attack;
//					}
//				}
				
				if(crit) {
					switch(modifiers.getPotionZrecznosc()) {
						case 1:
							dmgPotZr = dmg*0.15;
							break;
						case 2:
							dmgPotZr = dmg*0.3;
							break;
						case 3:
							dmgPotZr = dmg*0.45;
							break;
					}
				}
				
				switch(modifiers.getPotionZdolnosci()) {
					case 1:
						dmgPotZd = dmg*0.15;
						break;
					case 2:
						dmgPotZd = dmg*0.3;
						break;
					case 3:
						dmgPotZd = dmg*0.45;
						break;
				}
				
				if(modifiers.hasWyostrzoneZmysly()) {
					dmgZmysly = dmg*0.15;
				}
				
				if(modifiers.hasPrecyzyjnyStrzal()) {
					dmgStrzal = dmg*0.5;
				}
				
				if(modifiers.hasLodowaStrzala()) {
					dmgLod = dmg*0.3;
					PotionEffect pe = new PotionEffect(PotionEffectType.SLOW, 20*15, 2);
					((LivingEntity)victim).addPotionEffect(pe);
					Location loc = damager.getLocation().add(0, 1, 0);
					Location loc2 = victim.getLocation().add(0, 1, 0);
					loc.getWorld().spawnParticle(Particle.SNOWBALL, loc, 15, 0.2, 0.2, 0.2, 0.05);
					loc2.getWorld().spawnParticle(Particle.SNOWBALL, loc2, 15, 0.2, 0.2, 0.2, 0.05);
				}
				
				if(modifiers.hasSwietaStrzala()) {
					dmgSwieta = dmg*0.2;
					Location loc = damager.getLocation().add(0, 1, 0);
					Location loc2 = victim.getLocation().add(0, 1, 0);
					loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 15, 0.2, 0.2, 0.2, 0.05);
					loc2.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc2, 15, 0.2, 0.2, 0.2, 0.35);
				}
				if(modifiers.hasKrwawaStrzala()) {
					dmgKrwawa = dmg*0.3;
				}
				
				if(modifiers.hasZadzaKrwi()) {
					dmgZadzaKrwi = dmg * 0.4;
				}
				
				if(modifiers.hasZyciodajnaZiemia()) {
					dmgZyciodajnaZiemia = dmg * 0.2;
				}
				
				if(modifiers.hasZyciodajnaZiemia_m()) {
					dmgZyciodajnaZiemia_M = dmg * 0.25;
				}
				
				if(modifiers.hasEksplodujacaStrzala()) {
					if(bow.getType().equals(Material.CROSSBOW)) {
						int enchant = bow.getEnchantmentLevel(Enchantment.QUICK_CHARGE);
						dmgEksplodujaca = dmg * (0.45 - 0.05*enchant);
					} else {
						dmgEksplodujaca = dmg * 0.3;
					}
				}
				if(modifiers.hasEksplodujacaStrzala_h()) {
					if(bow.getType().equals(Material.CROSSBOW)) {
						int enchant = bow.getEnchantmentLevel(Enchantment.QUICK_CHARGE);
						dmgEksplodujaca_H = dmg * (0.52 - 0.06*enchant);
					} else {
						dmgEksplodujaca_H = dmg * 0.34;
					}
				}
				if(modifiers.hasEksplodujacaStrzala_m()) {
					if(bow.getType().equals(Material.CROSSBOW)) {
						int enchant = bow.getEnchantmentLevel(Enchantment.QUICK_CHARGE);
						dmgEksplodujaca_H = dmg * (0.61 - 0.07*enchant);
					} else {
						dmgEksplodujaca_H = dmg * 0.4;
					}
				}
				
				dmg = dmg + dmgLod + dmgOgien + dmgPoison + dmgStrzal 
						+ dmgZmysly + dmgPotZr + dmgPotZd + dmgZadzaKrwi 
						+ dmgSwieta + dmgZyciodajnaZiemia 
						+ dmgZyciodajnaZiemia_M + dmgKrwawa + dmgEksplodujaca
						+ dmgEksplodujaca_H + dmgEksplodujaca_M;
				
				if(crit && skills.hasCiosKrytyczny()) {
					dmg *= 1.15;
				}
				
				if(modifiers.hasProwokacja()) {
					dmg /= 100;
				}
				
				if(ChatColor.stripColor(info.getShortProf().toLowerCase()).equalsIgnoreCase("mys")) {
					dmg *= 1.15;
				}

				if(modifiers.hasSwietaStrzala()) {
					final double holyDmg = dmg * 0.25;
					victim.getWorld().getNearbyEntities(victim.getLocation(), 4, 4, 4, e -> {
						if(e.equals(p) || !(e instanceof LivingEntity))
							return false;
						if(e.equals(victim))
							return false;
						if(e instanceof Player) {
							RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
							ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
							State flag = set.queryValue(null, Flags.PVP);
							if(flag != null && flag.equals(State.ALLOW)
									&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
								return true;
							return false;
						}
						if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
							return false;
						return true;
					}).stream().forEach(e -> {
						RuneDamage.directDamageEffect(p, (LivingEntity) e, Optional.of(holyDmg), Optional.empty());
					});
				}
			} else {
				double tmpDmg = ((AbstractArrow) projectile).getDamage();
				if(tmpDmg >= 0)
					dmg = tmpDmg;
			}
		}
		
		if(victim instanceof Player
				&& DamageUtils.checkDodge((Player) victim))
			return -1;
		
		if(damager instanceof Player) {
			Player p = (Player) damager;
			dmg = DamageUtils.conditionalDebuffDamage(p, victim, dmg);
			if(crit)
				DamageUtils.showCritInfo(p);
		}
		
		return dmg;
	}

}
