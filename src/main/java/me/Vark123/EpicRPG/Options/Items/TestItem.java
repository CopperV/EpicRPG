package me.Vark123.EpicRPG.Options.Items;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.Vark123.EpicOptions.MenuSystem.IOptionItem;
import me.Vark123.EpicOptions.PlayerSystem.OPlayer;
import me.Vark123.EpicOptions.PlayerSystem.PlayerOption;
import me.Vark123.EpicRPG.Options.TestClassSerializable;

@Getter
@AllArgsConstructor
@Builder
public class TestItem implements IOptionItem<TestClassSerializable> {

	@Override
	public ItemStack getItem(OPlayer player, PlayerOption<TestClassSerializable> option) {
		ItemStack it = new ItemStack(Material.ACACIA_BUTTON);

		ItemMeta im = it.getItemMeta();
		im.setDisplayName("Testowe ustawienie");
		im.setLore(Arrays.asList(" ","Testowe lore"));
		it.setItemMeta(im);
		
		return it;
	}

	@Override
	public int getSlot() {
		return 4;
	}
}
