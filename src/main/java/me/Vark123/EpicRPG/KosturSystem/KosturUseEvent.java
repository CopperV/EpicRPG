package me.Vark123.EpicRPG.KosturSystem;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.ItemExecutor;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.RuneManager;

public class KosturUseEvent implements Listener {

	@EventHandler
	public void onUse(PlayerInteractEvent e) {
		ItemStack kostur = e.getItem();
		if (kostur == null || kostur.getType().equals(Material.AIR))
			return;

		NBTItem nbt = new NBTItem(kostur);
		if (!nbt.hasTag("Rozdzka") || !nbt.hasTag("MYTHIC_TYPE"))
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
		if(!nbt.hasTag(key))
			return;
		String mm = nbt.getString(key);
		if(mm.equalsIgnoreCase("-")) {
			p.getWorld().spawnParticle(Particle.DRAGON_BREATH, p.getEyeLocation(), 5, 0.25, 0.25, 0.25, 0.1);
			return;
		}
		
		ItemExecutor manag = MythicBukkit.inst().getItemManager();
		ItemStack rune = manag.getItemStack(mm);
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		
		if(!p.isSneaking()) {
			if(!RuneManager.getInstance().castRune(rpg, rune)) {
				p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, p.getEyeLocation(), 15, 0.25, 0.25, 0.25, 0.1);
			}
		} else {
			RuneManager.getInstance().regenTimePass(p, rune);
		}
		
	}

}
