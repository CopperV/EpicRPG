package me.Vark123.EpicRPG.OldFightSystem.Listeners.Attack.Magic;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.OldFightSystem.EpicDamageType;
import me.Vark123.EpicRPG.OldFightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.OldRuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.OldRuneSystem.Runes.BlogoslawienstwoPrzedwiecznych;
import me.Vark123.EpicRPG.OldRuneSystem.Runes.BlogoslawienstwoPrzedwiecznych_H;
import me.Vark123.EpicRPG.OldRuneSystem.Runes.BlogoslawienstwoPrzedwiecznych_M;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class RuneMagicModifierListener implements Listener {
	
	private static final Random rand = new Random();
	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.MAGIC))
			return;
		
		Object[] args = e.getArgs();
		if(args == null 
				|| args.length <= 0
				|| !(args[0] instanceof ItemStackRune))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;

		ItemStackRune ir = (ItemStackRune) args[0];
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		double modifier = 0;

		if(modifiers.hasInkantacja() 
				&& ir.getMagicType().equalsIgnoreCase("ogien"))
			modifier += 0.3;
		if(modifiers.hasPoswiecenie() 
				&& ir.getMagicType().equalsIgnoreCase("swiatlo"))
			modifier += 0.6;
		if(modifiers.hasZakazanyRytual() 
				&& ir.getMagicType().equalsIgnoreCase("chaos"))
			modifier += 0.35;
		if(modifiers.hasZakazanyRytual_h() 
				&& ir.getMagicType().equalsIgnoreCase("chaos"))
			modifier += 0.4;
		if(modifiers.hasZakazanyRytual_m() 
				&& ir.getMagicType().equalsIgnoreCase("chaos"))
			modifier += 0.47;
		if(modifiers.hasZewKrwi() 
				&& ir.getMagicType().equalsIgnoreCase("krew"))
			modifier += ((double) modifiers.getZewKrwiMod())/100.0;
		if(modifiers.hasKlatwaKrwi() 
				&& ir.getMagicType().equalsIgnoreCase("krew"))
			modifier += 0.4;
		if(modifiers.hasPelnia() 
				&& ir.getMagicType().equalsIgnoreCase("woda"))
			modifier += 0.45;
		if(modifiers.hasZewNatury() 
				&& ir.getMagicType().equalsIgnoreCase("natura"))
			modifier += 0.4;
		if(modifiers.hasSilaRownowagi()) {
			if(ir.getMagicType().equalsIgnoreCase("rownowaga"))
				modifier += 0.35;
			else
				modifier += 0.2;
		}
		if(modifiers.hasSilaRownowagi_h()) {
			if(ir.getMagicType().equalsIgnoreCase("rownowaga"))
				modifier += 0.42;
			else
				modifier += 0.25;
		}
		if(modifiers.hasSilaRownowagi_m()) {
			if(ir.getMagicType().equalsIgnoreCase("rownowaga"))
				modifier += 0.5;
			else
				modifier += 0.33;
		}
		if(modifiers.hasLaskaBeliara() 
				&& ir.getMagicType().equalsIgnoreCase("mrok"))
			modifier += 0.4;
		if(modifiers.hasWybraniecBeliara() 
				&& ir.getMagicType().equalsIgnoreCase("mrok"))
			modifier += 0.6;
		if(modifiers.hasPaktKrwi() && !modifiers.hasPaktKrwiMeasure() 
				&& ir.getMagicType().equalsIgnoreCase("krew"))
			modifier += 0.75;
		if(modifiers.hasPaktKrwi_h() && !modifiers.hasPaktKrwiMeasure_h() 
				&& ir.getMagicType().equalsIgnoreCase("krew"))
			modifier += 0.85;
		if(modifiers.hasPaktKrwi_m() && !modifiers.hasPaktKrwiMeasure_m() 
				&& ir.getMagicType().equalsIgnoreCase("krew"))
			modifier += 1;
		if(BlogoslawienstwoPrzedwiecznych.getEffected().containsKey(p))
			modifier += BlogoslawienstwoPrzedwiecznych.getEffected().get(p)*0.03;
		if(BlogoslawienstwoPrzedwiecznych_H.getEffected().containsKey(p))
			modifier += BlogoslawienstwoPrzedwiecznych_H.getEffected().get(p)*0.04;
		if(BlogoslawienstwoPrzedwiecznych_M.getEffected().containsKey(p))
			modifier += BlogoslawienstwoPrzedwiecznych_M.getEffected().get(p)*0.05;
		if(modifiers.hasTajemnyBlask_m() && rpg.getInfo().getProffesion().equals("§2Mysliwy")) {
			modifier += rand.nextDouble(0.5) + 0.25;
		} else if(modifiers.hasTajemnyBlask() && rpg.getInfo().getProffesion().equals("§2Mysliwy")) {
			modifier += rand.nextDouble(0.3) + 0.15;
		}
		
		e.increaseModifier(modifier);
	}
	
}
