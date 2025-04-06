package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import java.util.Random;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class TajemnyBlaskEffectListener implements Listener {
	
	private static final Random rand = new Random();
	
	@EventHandler
	public void onMysliwyAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		String profession = rpg.getInfo().getProffesion();
		
		if(Utils.hasEntityBuff(damager, EpicModifierTypes.TAJEMNY_BLASK)
				&& profession.equals("§2Mysliwy"))
			e.increaseModifier(0.1 + rand.nextDouble(0.1));
		if(Utils.hasEntityBuff(damager, EpicModifierTypes.TAJEMNY_BLASK_M)
				&& profession.equals("§2Mysliwy"))
			e.increaseModifier(0.1 + rand.nextDouble(0.3));
		
	}
	
	@EventHandler
	public void onWojownikAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MELEE))
			return;
		
		LivingEntity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		String profession = rpg.getInfo().getProffesion();
		
		if(Utils.hasEntityBuff(damager, EpicModifierTypes.TAJEMNY_BLASK)
				&& profession.equals("§cWojownik"))
			e.increaseModifier(0.15);
		if(Utils.hasEntityBuff(damager, EpicModifierTypes.TAJEMNY_BLASK_M)
				&& profession.equals("§cWojownik"))
			e.increaseModifier(0.35);
		
	}
	
	@EventHandler
	public void onWojownikDefense(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MELEE))
			return;
		
		LivingEntity victim = e.getVictim();
		if(!(victim instanceof Player))
			return;
		
		Player p = (Player) victim;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		String profession = rpg.getInfo().getProffesion();
		
		if(Utils.hasEntityBuff(victim, EpicModifierTypes.TAJEMNY_BLASK)
				&& profession.equals("§cWojownik"))
			e.decreaseModifier(0.15);
		if(Utils.hasEntityBuff(victim, EpicModifierTypes.TAJEMNY_BLASK_M)
				&& profession.equals("§cWojownik"))
			e.decreaseModifier(0.35);
		
	}

}
