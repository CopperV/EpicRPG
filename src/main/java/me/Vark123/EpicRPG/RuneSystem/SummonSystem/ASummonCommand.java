package me.Vark123.EpicRPG.RuneSystem.SummonSystem;

import java.util.Collection;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.lumine.mythic.core.mobs.ActiveMob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public abstract class ASummonCommand {

	private String commandId;
	protected SummonCommandItem commandItemConfig;
	
	protected ItemStack commandItem;
	
	public ASummonCommand(String commandId, SummonCommandItem commandItemConfig) {
		super();
		this.commandId = commandId;
		this.commandItemConfig = commandItemConfig;
	}
	
	public abstract void apply(Player owner, ActiveMob summon);
	
	public ItemStack getCommandItem() {
		if(commandItem == null) {
			commandItem = new ItemStack(commandItemConfig.materialItem);{
				ItemMeta im = commandItem.getItemMeta();
				im.setDisplayName(ChatColor.translateAlternateColorCodes('&', commandItemConfig.display));
				im.setLore(commandItemConfig.lore.stream()
						.map(line -> ChatColor.translateAlternateColorCodes('&', line))
						.collect(Collectors.toList()));
				commandItem.setItemMeta(im);
			}
		}
		return commandItem;
	}
	
	public String getDisplay() {
		return commandItemConfig.display;
	}

	@Getter
	@AllArgsConstructor
	@Builder
	public static class SummonCommandItem {
		private Material materialItem;
		private String display;
		private Collection<String> lore;
	}
	
}
