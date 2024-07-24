package me.Vark123.EpicRPG.Potions;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class PotionManager {

	private static final PotionManager instance = new PotionManager();
	
	public final int POTION_CD = 1450;
	public final PotionEffect DRINK_EFFECT = new PotionEffect(PotionEffectType.SLOW, 20*1, 1);
	private Map<UUID, Long> potionCooldown = new ConcurrentHashMap<>();
	
	private PotionManager() {}
	
	public static PotionManager getInstance() {
		return instance;
	}
	
	public boolean drinkPotion(Player p, EquipmentSlot potionSlot) {
		if(!PlayerManager.getInstance().playerExists(p))
			return false;
		
		ItemStack potion = p.getInventory().getItem(potionSlot);
		RpgPotionType type = PotionUtils.getPotionType(potion);
		if(type.equals(RpgPotionType.NONE))
			return false;
		PotionUseEvent event = new PotionUseEvent(p, potion, type);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return false;
		
		RpgPotionEffect effect = PotionUtils.getPotionEffect(type, potion);
		
		PlayerInventory inv = p.getInventory();
		int amount = inv.getItem(potionSlot).getAmount();
		if(amount == 0)
			inv.setItem(potionSlot, null);
		else
			inv.getItem(potionSlot).setAmount(amount - 1);
		
		playDrinkEffect(p);
		addPotionEffect(p, effect);
		potionCooldown.put(p.getUniqueId(), System.currentTimeMillis());
		return true;
	}
	
	public void playDrinkEffect(Player p) {
		p.addPotionEffect(DRINK_EFFECT);
		new BukkitRunnable() {
			int timer = 0;

			@Override
			public void run() {
				if (timer > 4) {
					this.cancel();
					return;
				}
				p.playSound(p, Sound.ENTITY_GENERIC_DRINK, 1, 1);
				p.spawnParticle(Particle.SPELL_MOB, p.getLocation().add(0, 1.25, 0), 0, 1, 0.2667, 0.4, 1);
				++timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 4);
	}
	
	public void addPotionEffect(Player p, RpgPotionEffect effect) {
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		new BukkitRunnable() {
			@Override
			public void run() {
				effect.potionEffect(rpg);
			}
		}.runTaskLater(Main.getInstance(), 20*1);
	}
	
	public boolean canDrinkPotion(Player p) {
		UUID id = p.getUniqueId();
		if (!potionCooldown.containsKey(id))
			return true;
		long last = potionCooldown.get(id);
		return ((System.currentTimeMillis() - last) > POTION_CD);
	}
	
}
