package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Runes.Krew.RytualKrwi;
import me.Vark123.EpicRPG.Utils.Utils;

public class RytualKrwiEffectListener implements Listener {

	private static ItemStack rune = null;
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity victim = e.getVictim();
		if(!(victim instanceof Player p))
			return;
		
		if(!Utils.hasEntityBuff(victim, EpicModifierTypes.RYTUAL_KRWI))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		
		if(rune == null) {
			rune = MythicBukkit.inst().getItemManager().getItemStack("RytualKrwi");
		}
		
		EpicRune rune = new EpicRune(RytualKrwiEffectListener.rune);
		RytualKrwi rytualKrwi = new RytualKrwi(rpg, rune);
		rytualKrwi.castEffect();
	}

}
