package me.Vark123.EpicRPG.HorseSystem.Horses;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftEntity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicRPG.HorseSystem.AEpicHorse;
import net.minecraft.world.entity.EntityLiving;

public class DeathRider extends AEpicHorse {

	public DeathRider() {
		super(Horse.class, 1000, 0.675);
	}

	@Override
	public void summonMount(Player p) {
		Horse horse = (Horse) p.getWorld().spawn(p.getLocation(), mount);
		horse.setOwner(p);
		horse.setAdult();
		horse.setTamed(true);
		horse.addPassenger(p);
		horse.setCustomName("§7Kon " + p.getName());
		horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
		horse.setColor(Color.WHITE);
		horse.getAttribute(Attribute.MAX_HEALTH).setBaseValue(health);
		horse.setHealth(health);
		horse.setStyle(Style.NONE);

		ItemStack armor = new ItemStack(Material.LEATHER_HORSE_ARMOR);
		LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
		meta.setColor(org.bukkit.Color.fromRGB(62, 61, 83));
		armor.setItemMeta(meta);
		horse.getInventory().setArmor(armor);
		((EntityLiving)((CraftEntity)horse).getHandle())
			.craftAttributes.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(speed);
	}

	@Override
	public ItemStack getMenuMountItem() {
		ItemStack it = new ItemStack(Material.SKELETON_SKULL);
		
		ItemMeta im = it.getItemMeta();
		im.setDisplayName("§x§4§C§5§E§6§7&lJezdziec Smierci");
		im.setLore(Arrays.asList(
				"",
				"§4★ §8Zycie: §c"+String.format("%.2f", health),
				"§4★ §8Predkosc: §7"+String.format("%.2f", speed*100)+"%"));
		it.setItemMeta(im);
		
		EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
		comp.setString("mount_summon_class", getClass().getName());
		comp.applyTo(it);
//		it = NBT.itemStackFromNBT(comp);
		
		return it;
	}

}
