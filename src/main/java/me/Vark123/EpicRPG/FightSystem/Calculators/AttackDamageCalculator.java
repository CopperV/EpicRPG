package me.Vark123.EpicRPG.FightSystem.Calculators;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.clan.api.Clan;
import de.simonsator.partyandfriends.clan.api.ClansManager;
import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicClans.EpicClansApi;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.Runes.CienAssasyna;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Stats.CheckStats;
import net.md_5.bungee.api.ChatColor;

public class AttackDamageCalculator implements DamageCalculator {

	@Override
	public double calc(Entity damager, Entity victim, double dmg) {
		boolean crit = false;
		
		if(damager instanceof Player 
				&& PlayerManager.getInstance().playerExists((Player) damager)) {
			Player p = (Player) damager;
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
			
			if(p.getInventory().getItemInMainHand() != null){
				ChangeStats.change(rpg);
				if(!CheckStats.check(rpg, p.getInventory().getItemInMainHand())) {
					p.sendMessage(Main.getInstance().getPrefix()+" §cNie mozesz uzywac tej broni");
					return -1;
				}
			}
			
			if(p.getInventory().getItemInMainHand().getType().equals(Material.BOW) ||
					p.getInventory().getItemInMainHand().getType().equals(Material.CROSSBOW)) {
				if(!(victim instanceof LivingEntity) || dmg < ((LivingEntity)victim).getHealth()) {
					p.sendMessage(Main.getInstance().getPrefix()+" §cNie mozna bic bronia dystansowa!");
					return -1;
				}
			}
			
			crit = DamageUtils.checkCrit(rpg);
			if(rpg.getModifiers().hasPenetracja())
				crit = true;
			
			RpgStats stats = rpg.getStats();
			RpgSkills skills = rpg.getSkills();
			RpgPlayerInfo info = rpg.getInfo();
			RpgModifiers modifiers = rpg.getModifiers();
			
			double dmgPotZr = 0;
			double dmgPotStr = 0;
			double dmgTrans = 0;
			double dmgSzal = 0;
			double dmgSkrytobojstwo = 0;
			double dmgZadzaKrwi = 0;
			double dmgZyciodajnaZiemia = 0;
			double dmgZyciodajnaZiemia_M = 0;
			double dmgGniew = 0;
			double dmgCiosWPlecy = 0;
			double dmgMord = 0;
			
			if(skills.hasPolnocnyBarbarzynca() && !hasWeapon(p)) {
				stats.setFinalObrazenia((int) (stats.getFinalObrazenia() + stats.getFinalSila()*0.8));
				stats.setFinalObrazenia((int) (stats.getFinalObrazenia() + stats.getFinalWytrzymalosc()*0.65));
			}
			
			if(victim instanceof Player) {
				if(crit) {
					dmg = Math.ceil(0.15 * stats.getFinalObrazenia());
				} else {
					dmg = Math.ceil(0.1 * stats.getFinalObrazenia());
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
					wspDmgStr = 0.75;
					wspDmgZr = 1.3;
					wspDmgZd = 0.05;
					wspDmgInt = 0.05;
				} else {
					dmg = Math.ceil(stats.getFinalObrazenia());
					wspDmgStr = 0.75;
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
				
			}
			
			dmg = DamageUtils.randomizeDamage(dmg);

			OnlinePAFPlayer paf = PAFPlayerManager.getInstance().getPlayer(p);
			if(paf != null) {
				Clan klan = ClansManager.getInstance().getClan(paf);
				if(klan != null) {
					double attack = EpicClansApi.getInst().getAttackValue(klan);
					dmg += dmg * attack;
				}
			}
			
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
			} else {
				switch(modifiers.getPotionSila()) {
					case 1:
						dmgPotStr = dmg*0.1;
						break;
					case 2:
						dmgPotStr = dmg*0.25;
						break;
					case 3:
						dmgPotStr = dmg*0.4;
						break;
				}
			}
			
			if(modifiers.hasTrans()) {
				dmgTrans = dmg*0.5;
			}
			if(modifiers.hasSzalBitewny()) {
				dmgSzal = dmg;
			}
			if(crit && modifiers.hasSkrytobojstwo()) {
				dmgSkrytobojstwo = dmg * 0.4;
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
			if(modifiers.hasWampiryzm()) {
				double restoreHp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.015;
				RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, restoreHp);
				Bukkit.getPluginManager().callEvent(event);
				p.getWorld().spawnParticle(Particle.HEART, p.getLocation(), 5, 0.5F, 0.5F, 0.5F, 0.1f);
			}
			if(modifiers.hasWampiryzm_h()) {
				double restoreHp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.02;
				RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, restoreHp);
				Bukkit.getPluginManager().callEvent(event);
				p.getWorld().spawnParticle(Particle.HEART, p.getLocation(), 5, 0.5F, 0.5F, 0.5F, 0.1f);
			}
			if(modifiers.hasWampiryzm_m()) {
				double restoreHp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.03;
				RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, restoreHp);
				Bukkit.getPluginManager().callEvent(event);
				p.getWorld().spawnParticle(Particle.HEART, p.getLocation(), 5, 0.5F, 0.5F, 0.5F, 0.1f);
			}
			if(modifiers.hasRytualWzniesienia()) {
				int restoreMana = stats.getFinalMana() / 20;
				stats.addPresentManaSmart(restoreMana);
				p.getWorld().spawnParticle(Particle.SOUL, p.getLocation().add(0,1,0), 18, .4f, .4f, .4f, .05f);
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
			}
			if(modifiers.hasGniew()) {
				dmgGniew = dmg * 0.75;
			}
			if(modifiers.hasCiosWPlecy()) {
				Vector pDir = damager.getLocation().getDirection();
				Vector eDir = victim.getLocation().getDirection();
				double xv = pDir.getX() * eDir.getZ() - pDir.getZ() * eDir.getX();
				double zv = pDir.getX() * eDir.getX() + pDir.getZ() * eDir.getZ();
				double angle = Math.atan2(xv, zv);
				double angleInDegrees = (angle * 180) / Math.PI;
				if(Math.abs(angleInDegrees) < 45) {
					p.playSound(p.getLocation(), Sound.BLOCK_GRINDSTONE_USE, 2, 2);
					p.getWorld().spawnParticle(Particle.SWEEP_ATTACK, p.getLocation().add(0,1,0), 7, .5f, .5f, .5f, .05f);
					dmgCiosWPlecy = dmg * 1.25;
				}
			}
			if(modifiers.hasMord()) {
				dmgMord = dmg * 0.9;
			}
			
			dmg = dmg + dmgPotZr + dmgPotStr + dmgTrans + dmgSzal + dmgSkrytobojstwo + dmgZadzaKrwi + dmgZyciodajnaZiemia + 
					dmgZyciodajnaZiemia_M + dmgGniew + dmgCiosWPlecy + dmgMord;	

			if(modifiers.hasCienAssasyna()) {
				int chance = new Random().nextInt(100);
				if(chance < 10) {
					CienAssasyna.spellEffect(rpg.getPlayer(), victim, dmg*0.2);
				}
			}
			if(crit && skills.hasCiosKrytyczny()) {
				dmg *= 1.2;
			}
			if(skills.hasSlugaBeliara()) {
				ItemStack item = p.getInventory().getItemInMainHand();
				if(item!=null && !item.getType().equals(Material.AIR)) {
					NBTItem nbti = new NBTItem(item);
					if(nbti.hasTag("SzponBeliara")) {
						double chance = p.getHealth()/75;
						int rand = new Random().nextInt(100);
						if(rand < chance) {
							double extraDamage = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
							dmg += extraDamage;
							DustOptions dust = new DustOptions(Color.RED, 1.5f);
							victim.getWorld().spawnParticle(Particle.REDSTONE, victim.getLocation().add(0,1,0), 10, 0.5f, 0.5f, 0.5f, 0.15f, dust);
							p.getWorld().playSound(p.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 1, 0.1f);
						}
					}
				}
			}
			if(modifiers.hasProwokacja()) {
				dmg /= 100;
			}
			if(crit && ChatColor.stripColor(info.getShortProf().toLowerCase()).equalsIgnoreCase("mys")) {
				dmg *= 1.15;
			} else if(!crit && ChatColor.stripColor(info.getShortProf().toLowerCase()).equalsIgnoreCase("woj")) {
				dmg *= 1.15;
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
