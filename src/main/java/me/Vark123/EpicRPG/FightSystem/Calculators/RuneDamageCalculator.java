package me.Vark123.EpicRPG.FightSystem.Calculators;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import net.md_5.bungee.api.ChatColor;

public class RuneDamageCalculator implements DamageCalculator {

	@Deprecated
	@Override
	public double calc(Entity damager, Entity victim, double dmg) {
		return 0;
	}

	public double calc(Player p, LivingEntity e, double dmg, ItemStackRune dr) {
		
		if(!PlayerManager.getInstance().playerExists(p))
			return dmg;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgStats stats = rpg.getStats();
		RpgPlayerInfo info = rpg.getInfo();
		RpgModifiers modifiers = rpg.getModifiers();
		ChangeStats.change(rpg, null);
		
		boolean crit = DamageUtils.checkCrit(rpg);
		
		double wplyw = dr.getWplyw();
		double addDmg = 0;
		double dmgKrag = 0;
		double dmgLevel = 0;
		double dmgMana = 0;
		String type = dr.getMagicType().toLowerCase();
		
		if(wplyw > 0) {
			double wplywDmg = rpg.getStats().getFinalObrazenia() / wplyw / 100;
			dmg += wplywDmg;
		}
		
		if(e instanceof Player) {
			switch(type) {
				case "ogien":
					dmg = wplyw*1.8*dmg;
					break;
				case "woda":
					dmg = wplyw*0.9*dmg;
					break;
				case "natura":
					dmg = wplyw*0.8*dmg;
					break;
				case "tajemna":
					dmg = wplyw*0.7*dmg;
					break;
				case "mrok":
					dmg = (wplyw+0.1)*dmg;
					break;
				case "swiatlo":
					dmg = wplyw*1.65*dmg;
					break;
				case "rownowaga":
					dmg = wplyw*1.75*dmg;
					break;
				case "chaos":
					dmg = wplyw*0.5*dmg;
					break;
				case "krew":
					dmg = wplyw*2*dmg;
					break;
				default:
					dmg = wplyw*0.5*dmg;
					break;
			}
			dmgLevel = (dmg*0.01*info.getLevel());
			dmg *= crit ? 1.1 : 0.8;
		} else {
			if(crit) {
				dmg *= 1.75;
				wplyw *= 1.25;
			}
				
			addDmg = wplyw * stats.getFinalInteligencja() * dmg / 100.0;
			dmg += addDmg;
				
			dmgLevel = dmg * 0.005 * info.getLevel();
			dmgMana = dmg * 0.0003 * stats.getFinalMana();
		}
		
		dmgKrag = dmg * 0.05 * stats.getKrag();
		
		dmg += dmgKrag;
		dmg += dmgLevel;
		dmg += dmgMana;
		dmg = DamageUtils.randomizeDamage(dmg);
		
//		OnlinePAFPlayer paf = PAFPlayerManager.getInstance().getPlayer(p);
//		if(paf != null) {
//			Clan klan = ClansManager.getInstance().getClan(paf);
//			if(klan != null) {
//				double attack = EpicClansApi.getInst().getAttackValue(klan);
//				dmg += dmg*attack;
//			}
//		}
		
		double dmgPotionInt = 0;
		double dmgZadzaKrwi = 0;
		double dmgInkantacja = 0;
		double dmgPoswiecenie = 0;
		double dmgZyciodajnaZiemia = 0;
		double dmgZyciodajnaZiemia_M = 0;
		double dmgZakazanyRytual = 0;
		double dmgZakazanyRytual_H = 0;
		double dmgZakazanyRytual_M = 0;
		double dmgZewKrwi = 0;
		double dmgPelnia = 0;
		double dmgSilaRownowagi = 0;
		double dmgSilaRownowagi_H = 0;
		double dmgSilaRownowagi_M = 0;
		
		switch(modifiers.getPotionInteligencja()) {
			case 1:
				dmgPotionInt = dmg*0.15;
				break;
			case 2:
				dmgPotionInt = dmg*0.25;
				break;
			case 3:
				dmgPotionInt = dmg*0.4;
				break;
		}
		if(modifiers.hasInkantacja() && dr.getMagicType().equalsIgnoreCase("ogien")) {
			dmgInkantacja = dmg * 0.3;
		}
		if(modifiers.hasPoswiecenie() && dr.getMagicType().equalsIgnoreCase("swiatlo")) {
			dmgPoswiecenie = dmg * 0.6;
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
		if(modifiers.hasZakazanyRytual() && dr.getMagicType().equalsIgnoreCase("chaos")) {
			dmgZakazanyRytual = dmg * 0.35;
		}
		if(modifiers.hasZakazanyRytual_h() && dr.getMagicType().equalsIgnoreCase("chaos")) {
			dmgZakazanyRytual_H = dmg * 0.4;
		}
		if(modifiers.hasZakazanyRytual_m() && dr.getMagicType().equalsIgnoreCase("chaos")) {
			dmgZakazanyRytual_M = dmg * 0.47;
		}
		if(modifiers.hasZewKrwi() && dr.getMagicType().equalsIgnoreCase("krew")) {
			dmgZewKrwi = dmg * ((double) modifiers.getZewKrwiMod())/100.0;
		}
		if(modifiers.hasPelnia() && dr.getMagicType().equalsIgnoreCase("woda")) {
			dmgPelnia = dmg * 0.45;
		}
		if(modifiers.hasSilaRownowagi()) {
			if(dr.getMagicType().equalsIgnoreCase("rownowaga"))
				dmgSilaRownowagi = dmg * 0.35;
			else
				dmgSilaRownowagi = dmg * 0.2;
		}
		if(modifiers.hasSilaRownowagi_h()) {
			if(dr.getMagicType().equalsIgnoreCase("rownowaga"))
				dmgSilaRownowagi_H = dmg * 0.42;
			else
				dmgSilaRownowagi_H = dmg * 0.25;
		}
		if(modifiers.hasSilaRownowagi_m()) {
			if(dr.getMagicType().equalsIgnoreCase("rownowaga"))
				dmgSilaRownowagi_M = dmg * 0.5;
			else
				dmgSilaRownowagi_M = dmg * 0.33;
		}
		dmg = dmg + dmgPotionInt + dmgZadzaKrwi + dmgInkantacja + dmgPoswiecenie
				+ dmgZyciodajnaZiemia + dmgZakazanyRytual
				+ dmgZakazanyRytual_H + dmgZyciodajnaZiemia_M + dmgZakazanyRytual_M 
				+ dmgZewKrwi + dmgPelnia + dmgSilaRownowagi + dmgSilaRownowagi_H + dmgSilaRownowagi_M;
		
		if(modifiers.hasProwokacja()) {
			dmg /= 100;
		}

		if(ChatColor.stripColor(info.getShortProf().toLowerCase()).equalsIgnoreCase("mag")) {
			dmg *= e instanceof Player ? 1.1 : 1.1;
		}
		
		if(e instanceof Player && DamageUtils.checkDodge((Player) e))
			return -1;
		dmg = DamageUtils.conditionalDebuffDamage(p, e, dmg);
		if(crit)
			DamageUtils.showCritInfo(p);
		
		return dmg;
	}
	
}
