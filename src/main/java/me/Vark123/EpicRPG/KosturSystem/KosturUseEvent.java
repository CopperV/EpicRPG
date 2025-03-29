package me.Vark123.EpicRPG.KosturSystem;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.ItemExecutor;
import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneManager;
import me.Vark123.EpicRPG.Utils.Utils;

public class KosturUseEvent implements Listener {

	@EventHandler
	public void onUse(PlayerInteractEvent e) {
		ItemStack kostur = e.getItem();
		if (kostur == null || kostur.getType().equals(Material.AIR))
			return;

		EpicComponent comp = new EpicComponent(kostur, MythicBukkit.inst());
		if (!comp.hasKey("rozdzka") || !Utils.isMythicMobItem(kostur))
			return;

		Player p = e.getPlayer();
		KosturManager manager = KosturManager.getInstance();
		Action action = e.getAction();
		if (!manager.isUsingCombo(p, kostur) || manager.isComboExpired(p, kostur)) {
			if (!(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)))
				return;
			manager.createCombo(p, kostur);
			return;
		}

		if (!(action.equals(Action.RIGHT_CLICK_AIR) 
				|| action.equals(Action.RIGHT_CLICK_BLOCK)
				|| action.equals(Action.LEFT_CLICK_BLOCK) 
				|| action.equals(Action.LEFT_CLICK_AIR)))
			return;
		if(!manager.isCooldownPass(p, kostur))
			return;

		boolean isRight = action.toString().toUpperCase().contains("RIGHT");
		manager.updateCombo(p, kostur, isRight);
		
		if(!manager.isComboFinished(p, kostur))
			return;
		
		String key = manager.finishCombo(p, kostur);
		if(!comp.hasKey(key))
			return;
		String mm = comp.getString(key);
		if(mm.equalsIgnoreCase("-")) {
			p.getWorld().spawnParticle(Particle.DRAGON_BREATH, p.getEyeLocation(), 5, 0.25, 0.25, 0.25, 0.1);
			return;
		}
		
		ItemExecutor manag = MythicBukkit.inst().getItemManager();
		ItemStack rune = manag.getItemStack(mm);
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		
		if(!p.isSneaking()) {
			if(!RuneManager.get().tryCastRune(rpg, rune)) {
				p.getWorld().spawnParticle(Particle.SMOKE, p.getEyeLocation(), 15, 0.25, 0.25, 0.25, 0.1);
			}
		} else {
			RuneManager.get().isRegenTimePassed(p, new EpicRune(rune));
		}
		
	}

}
