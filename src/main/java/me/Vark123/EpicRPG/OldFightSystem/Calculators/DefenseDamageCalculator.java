package me.Vark123.EpicRPG.OldFightSystem.Calculators;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.ManualDamage;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.Runes.SilaJednosci;
import net.md_5.bungee.api.ChatColor;

public class DefenseDamageCalculator implements DamageCalculator {

	@Override
	public double calc(Entity damager, Entity victim, double dmg) {
		
		if(!(victim instanceof Player))
			return dmg;
		Player p = (Player) victim;
		if(!PlayerManager.getInstance().playerExists(p))
			return dmg;

		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgStats stats = rpg.getStats();
		RpgPlayerInfo info = rpg.getInfo();
		RpgModifiers modifiers = rpg.getModifiers();
		
		if(modifiers.hasSilaJednosci() && SilaJednosci.getGlobalEffected().containsKey(rpg.getPlayer())) {
			SilaJednosci rune = SilaJednosci.getGlobalEffected().get(rpg.getPlayer());
			Player caster = rune.getCaster();
			Location castLoc = rune.getLoc();
			if(caster.isOnline() && caster.getWorld().getUID().equals(castLoc.getWorld().getUID())
					&& caster.getLocation().distance(castLoc) <= rune.getRune().getObszar() && !caster.equals(p)) {
				dmg *= 0.6;

				EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, caster, DamageCause.CUSTOM, dmg);
				Bukkit.getPluginManager().callEvent(event);
				
				if(damager instanceof LivingEntity){
					ManualDamage.doDamage((LivingEntity) damager, caster, event.getDamage(), event);
				}
				else{
					ManualDamage.doDamage(caster, event.getDamage(), event);
				}
				
				caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.9f);
				victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.9f);
				caster.getWorld().spawnParticle(Particle.WAX_ON, caster.getLocation().add(0,1,0), 8, 0.5f, 0.5f, 0.5f, 0.1f);
				victim.getWorld().spawnParticle(Particle.WAX_OFF, victim.getLocation().add(0,1,0), 8, 0.5f, 0.5f, 0.5f, 0.1f);
			}
		}
		
		if(damager instanceof Player 
				&& PlayerManager.getInstance().playerExists((Player) damager)) {
			
			if(ChatColor.stripColor(info.getShortProf().toLowerCase()).equalsIgnoreCase("woj")) {
				dmg -= 0.2*stats.getFinalOchrona();
			} else {
				dmg -= 0.1*stats.getFinalOchrona();
			}
			
		} else {
			int wytrzymalosc = stats.getFinalWytrzymalosc() < 0 ? 0 : stats.getFinalWytrzymalosc();
			
			double def;
			if(info.getLevel() < 70) {
				def = 0.13 * wytrzymalosc * stats.getFinalOchrona() / (100*0.05*info.getLevel());
			} else {
				def = 0.13 * wytrzymalosc * stats.getFinalOchrona() / (100*0.05*70);
			}
			
			double def2 = stats.getFinalOchrona()*0.35;
			if(ChatColor.stripColor(info.getShortProf().toLowerCase()).equalsIgnoreCase("woj")) {
				dmg -= (def+def2)*1.1;
			} else {
				dmg -= def;
				dmg -= def2;
			}
			
//			OnlinePAFPlayer paf = PAFPlayerManager.getInstance().getPlayer(p);
//			if(paf != null) {
//				Clan klan = ClansManager.getInstance().getClan(paf);
//				if(klan != null) {
//					double defense = EpicClansApi.getInst().getDefenseValue(klan);
//					dmg -= dmg*defense;
//				}
//			}
			
			double dmgPotion = 0;
			double dmgGruboskornosc = 0;
			double dmgTotemObronny = 0;
			double dmgAuraRozproszenia = 0;
			double dmgZyciodajnaZiemia = 0;
			double dmgZyciodajnaZiemia_M = 0;
			double dmgSilaJednosci = 0;
			
			switch(modifiers.getPotionWytrzymalosc()) {
				case 1:
					dmgPotion = dmg*0.1;
					break;
				case 2:
					dmgPotion = dmg*0.25;
					break;
				case 3:
					dmgPotion = dmg*0.4;
					break;
			}

			if(modifiers.hasGruboskornosc()) {
				dmgGruboskornosc = stats.getKrag()*0.1*dmg;
			}
			
			if(modifiers.hasTotemObronny()) {
				dmgTotemObronny = dmg*0.25;
			}
			
			if(modifiers.hasAuraRozproszenia()) {
				dmgAuraRozproszenia = dmg*0.15;
			}
			
			if(modifiers.hasZyciodajnaZiemia()) {
				dmgZyciodajnaZiemia = dmg*0.15;
			}
			
			if(modifiers.hasZyciodajnaZiemia_m()) {
				dmgZyciodajnaZiemia_M = dmg*0.2;
			}

			dmg = dmg - dmgPotion - dmgGruboskornosc - dmgTotemObronny 
					- dmgAuraRozproszenia - dmgZyciodajnaZiemia 
					- dmgZyciodajnaZiemia_M - dmgSilaJednosci;
			
			if(modifiers.hasTarczaCienia()) {
				int dmgTarczaCienia = (int) (stats.getPresentMana() < dmg/2 ? stats.getPresentMana() : dmg/2);
				if(dmgTarczaCienia > 0) {
					dmg -= dmgTarczaCienia;
					stats.removePresentMana(dmgTarczaCienia);
					victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 0.9f, 1.1f);
					victim.getWorld().spawnParticle(Particle.SOUL, victim.getLocation().add(0,1,0), 25, 0.5f, 0.5f, 0.5f, 0.2f);
				}
			}
			
			if(modifiers.hasProwokacja()) {
				dmg = Math.ceil(dmg/10);
			}
			
			if(dmg < ((info.getLevel()/10.)+2)) {
				dmg = info.getLevel()/10.+2;
			}
			
			if(modifiers.hasSfera()) {
				if(!(damager instanceof Player && PlayerManager.getInstance().playerExists((Player) damager)) 
						&& PlayerManager.getInstance().getRpgPlayer((Player) damager).getModifiers().hasSfera()) {
					double tmpDmg = dmg * 0.25;
					if(modifiers.hasInkantacja()) tmpDmg *= 1.3;
					
					EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(victim, damager, DamageCause.CONTACT, tmpDmg);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()) {
						ManualDamage.doDamage((Player) victim, (LivingEntity) damager, tmpDmg, event);
						victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
					}

					
				}
			}

			if(modifiers.hasTotemObronny()) {
				if(!(damager instanceof Player && PlayerManager.getInstance().playerExists((Player) damager)) 
						&& PlayerManager.getInstance().getRpgPlayer((Player) victim).getModifiers().hasTotemObronny()) {
					EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(victim, damager, DamageCause.MAGIC, dmg * 0.25);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()) {
						ManualDamage.doDamage((Player) victim, (LivingEntity) damager, dmg * 0.25, event);
						victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1, 0.5f);
					}
				}
			}
			
		}
		
		return dmg;
	}

}
