package me.Vark123.EpicRPG.HorseSystem.Horses;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPG.HorseSystem.AEpicHorse;
import net.minecraft.world.entity.EntityLiving;

public class ESkeletonHorse extends AEpicHorse {

	public ESkeletonHorse() {
		super(SkeletonHorse.class, 260., 0.6);
	}

	@Override
	public void summonMount(Player p) {
		SkeletonHorse horse = (SkeletonHorse) p.getWorld().spawn(p.getLocation(), mount);
		horse.setOwner(p);
		horse.setAdult();
		horse.setTamed(true);
		horse.addPassenger(p);
		horse.setCustomName("§7Kon " + p.getName());
		horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
		horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
		horse.setHealth(health);
		((EntityLiving)((CraftEntity)horse).getHandle())
			.craftAttributes.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
	}

	@Override
	public ItemStack getMenuMountItem() {
		ItemStack it = new ItemStack(Material.BONE_MEAL);
		
		ItemMeta im = it.getItemMeta();
		im.setDisplayName("§8Kon - Szkielet");
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
