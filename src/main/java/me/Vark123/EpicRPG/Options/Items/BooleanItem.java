package me.Vark123.EpicRPG.Options.Items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.Vark123.EpicOptions.MenuSystem.IOptionItem;
import me.Vark123.EpicOptions.PlayerSystem.OPlayer;
import me.Vark123.EpicOptions.PlayerSystem.PlayerOption;

@Getter
@AllArgsConstructor
@Builder
public class BooleanItem implements IOptionItem<Boolean> {

	private String display;
	private List<String> lore;
	private Material material;
	private int slot;
	
	@Override
	public ItemStack getItem(OPlayer player, PlayerOption<Boolean> option) {
		ItemStack it = new ItemStack(material);
		String isOn = option.getValue() ? "§a§lWLACZONY" : "§c§lWYLACZONY";

		ItemMeta im = it.getItemMeta();
		im.setDisplayName(display+": "+isOn);
		im.setLore(Arrays.asList(" ","§7Kompas w postaci BOSS-BARU,","§7ktory zawsze dokladnie wskaze Ci kierunek","§7Twojej podrozy"));
		it.setItemMeta(im);
		
		return it;
	}

	@Override
	public int getSlot() {
		return slot;
	}

}
