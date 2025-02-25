package me.Vark123.EpicRPG.Consumables.Impl;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Consumables.AConsumable;
import me.Vark123.EpicRPG.Consumables.IConsumableEffect;
import me.Vark123.EpicRPG.Players.RpgPlayer;

@Accessors(fluent = true)
@Getter
public class DrinkConsumable extends AConsumable {
	
	protected static final int DRINK_DURATION = 20*1;
	protected static final PotionEffect DRINK_EFFECT = new PotionEffect(PotionEffectType.SLOWNESS, DRINK_DURATION, 1);
	
	private IConsumableEffect consumableEffect;
	private Color drinkColor;
	
	public DrinkConsumable(IConsumableEffect consumableEffect, Color drinkColor) {
		this.consumableEffect = consumableEffect;
		this.drinkColor = drinkColor;
	}

	@Override
	public boolean canConsume(RpgPlayer rpg) {
		return super.canConsume(rpg);
	}

	@Override
	public void consume(RpgPlayer rpg, EquipmentSlot slot) {
		super.consume(rpg, slot);
		
		Player p = rpg.getPlayer();
		String world = p.getWorld().getName();
		int step = 4;
		
		new BukkitRunnable() {
			int timer = 0;
			@Override
			public void run() {
				if(isCancelled() || !p.getWorld().getName().equalsIgnoreCase(world) 
						|| p.isDead())
					return;
				
				p.playSound(p, Sound.ENTITY_GENERIC_DRINK, 1, 0.8f);
				p.spawnParticle(Particle.ENTITY_EFFECT, p.getLocation().clone().add(0, 1.25, 0), 
						3, .45, 0.6, .45, 1, drinkColor);
				
				if(timer >= DRINK_DURATION) {
					cancel();
					consumableEffect.playConsumeEffect(rpg);
					return;
				}
				
				timer += step;
			}
		}.runTaskTimer(Main.getInstance(), 0, step);
	}
	
	

}
