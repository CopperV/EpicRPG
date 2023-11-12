package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.Runes.RytualKrwi;

public class RytualKrwiEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.MAGIC))
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Player))
			return;

		Player p = (Player) victim;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		
		if(!modifiers.hasRytualKrwi())
			return;
		
		ItemStack rune = MythicBukkit.inst().getItemManager().getItemStack("RytualKrwi");
		if(rune == null)
			return;
		
		ItemStackRune ir = new ItemStackRune(rune);
		RytualKrwi runa = new RytualKrwi(ir, p);
		runa.castEffect();
	}

}
