package me.Vark123.EpicRPG.Options.Items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.Vark123.EpicOptions.MenuSystem.IOptionItem;
import me.Vark123.EpicOptions.OptionSystem.ISerializable;
import me.Vark123.EpicOptions.PlayerSystem.OPlayer;
import me.Vark123.EpicOptions.PlayerSystem.PlayerOption;

@Getter
@AllArgsConstructor
@Builder
public class AdvancedOptionItem<T extends ISerializable> implements IOptionItem<T> {

	private String display;
	private List<String> lore;
	private Material material;
	private int slot;
	
	@Override
	public ItemStack getItem(OPlayer player, PlayerOption<T> option) {
		ItemStack it = new ItemStack(material);

		ItemMeta im = it.getItemMeta();
		im.setDisplayName(display);
		im.setLore(lore);
		it.setItemMeta(im);
		
		return it;
	}

	@Override
	public int getSlot() {
		return slot;
	}

}
