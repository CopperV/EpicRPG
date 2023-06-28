package me.Vark123.EpicRPG.HorseSystem;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Data;

@Data
public abstract class AEpicHorse {
	
	protected Class<? extends LivingEntity> mount;
	protected double health;
	protected double speed;
	
	public AEpicHorse(Class<? extends LivingEntity> mount, double health, double speed) {
		this.mount = mount;
		this.health = health;
		this.speed = speed;
	}
	
	public abstract void summonMount(Player p);
	public abstract ItemStack getMenuMountItem();

}
