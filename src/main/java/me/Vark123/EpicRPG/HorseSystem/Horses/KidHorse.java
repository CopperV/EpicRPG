package me.Vark123.EpicRPG.HorseSystem.Horses;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPG.HorseSystem.AEpicHorse;
import net.minecraft.world.entity.EntityLiving;

public class KidHorse extends AEpicHorse {

	public KidHorse() {
		super(Horse.class, 40., 0.6);
	}

	@Override
	public void summonMount(Player p) {
		Horse horse = (Horse) p.getWorld().spawn(p.getLocation(), mount);
		horse.setOwner(p);
		horse.setBaby();
		horse.setTamed(true);
		horse.addPassenger(p);
		horse.setCustomName("§7Kon " + p.getName());
		horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
		horse.setColor(Color.CHESTNUT);
		horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
		horse.setHealth(health);
		horse.setStyle(Style.WHITEFIELD);
		((EntityLiving)((CraftEntity)horse).getHandle())
			.craftAttributes.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
	}

	@Override
	public ItemStack getMenuMountItem() {
		ItemStack it = new ItemStack(Material.SMOOTH_QUARTZ);
		
		ItemMeta im = it.getItemMeta();
		im.setDisplayName("§aLukrowe Zrebie");
		im.setLore(Arrays.asList(
				"",
				"§4★ §8Zycie: §c"+String.format("%.2f", health),
				"§4★ §8Predkosc: §7"+String.format("%.2f", speed*100)+"%"));
		it.setItemMeta(im);
		
		NBTItem nbt = new NBTItem(it);
		nbt.setString("MountSummonClass", getClass().getName());
		nbt.applyNBT(it);
		
		return it;
	}

}
