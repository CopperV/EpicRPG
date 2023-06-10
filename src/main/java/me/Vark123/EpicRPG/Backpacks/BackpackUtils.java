package me.Vark123.EpicRPG.Backpacks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import de.tr7zw.nbtapi.NBTItem;
import net.minecraft.nbt.NBTTagCompound;

public class BackpackUtils {

	private BackpackUtils() {}
	
	public static Inventory inventoryFromBase64(String data) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

			for (int i = 0; i < inventory.getSize(); ++i) {
				inventory.setItem(i, (ItemStack) dataInput.readObject());
			}

			dataInput.close();
			return inventory;
		} catch (ClassNotFoundException var5) {
			throw new RuntimeException("Unable to decode the class type.", var5);
		} catch (IOException var6) {
			throw new RuntimeException("Unable to convert Inventory to Base64.", var6);
		}
	}

	public static ItemStack itemstackFromBase64(String base64) {
		ItemStack item = null;

		try {
			ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
			BukkitObjectInputStream is = new BukkitObjectInputStream(in);
			item = (ItemStack) is.readObject();
		} catch (ClassNotFoundException | IOException var4) {
			var4.printStackTrace();
		}

		return item;
	}

	public static String getStringData(ItemStack item, String tag) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound itemCompound = nmsItem.r() ? nmsItem.u() : new NBTTagCompound();
		return itemCompound != null ? itemCompound.l(tag) : null;
	}

	public int getIntData(ItemStack item, String tag) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound itemCompound = nmsItem.r() ? nmsItem.u() : new NBTTagCompound();
		return itemCompound != null ? itemCompound.h(tag) : -9304294;
	}
	
	public static boolean isBuggedBackpack(ItemStack it) {
		NBTItem nbt = new NBTItem(it);
		return nbt.hasTag("BackpackID");
	}
	
}
