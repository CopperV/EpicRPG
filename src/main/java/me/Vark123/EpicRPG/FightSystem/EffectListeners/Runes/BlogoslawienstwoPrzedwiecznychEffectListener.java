package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.BlogoslawienstwoPrzedwiecznych;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.BlogoslawienstwoPrzedwiecznych_H;
import me.Vark123.EpicRPG.RuneSystem.Runes.Chaos.BlogoslawienstwoPrzedwiecznych_M;
import me.Vark123.EpicRPG.Utils.Utils;

public class BlogoslawienstwoPrzedwiecznychEffectListener implements Listener {
	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity damager = e.getDamager();
		UUID uid = damager.getUniqueId();
		if(Utils.hasEntityBuff(damager, EpicModifierTypes.BLOGOSLAWIENSTWO_PRZEDWIECZNYCH)
				&& BlogoslawienstwoPrzedwiecznych.getAffected().containsKey(uid))
			e.increaseModifier(0.03*BlogoslawienstwoPrzedwiecznych.getAffected().get(uid).getLevel());
		
		if(Utils.hasEntityBuff(damager, EpicModifierTypes.BLOGOSLAWIENSTWO_PRZEDWIECZNYCH_H)
				&& BlogoslawienstwoPrzedwiecznych_H.getAffected().containsKey(uid))
			e.increaseModifier(0.04*BlogoslawienstwoPrzedwiecznych_H.getAffected().get(uid).getLevel());
		
		if(Utils.hasEntityBuff(damager, EpicModifierTypes.BLOGOSLAWIENSTWO_PRZEDWIECZNYCH_M)
				&& BlogoslawienstwoPrzedwiecznych_M.getAffected().containsKey(uid))
			e.increaseModifier(0.05*BlogoslawienstwoPrzedwiecznych_M.getAffected().get(uid).getLevel());
	}

}
