package me.Vark123.EpicRPG.FightSystem.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class ProjectileLaunchListener implements Listener {
	
	@EventHandler
	public void onShoot(EntityShootBowEvent e) {
		if(e.isCancelled())
			return;
		
		Entity projectile = e.getProjectile();
		if(!(projectile instanceof AbstractArrow))
			return;
		
		AbstractArrow arrow = (AbstractArrow) projectile;
		arrow.setMetadata("rpg_bow", new FixedMetadataValue(Main.getInstance(), e.getBow()));
		arrow.setMetadata("rpg_force", new FixedMetadataValue(Main.getInstance(), e.getForce()));
		
		double power = e.getForce();
		double dmg = DamageUtils.getProjectileDamage(e.getBow());
		if(dmg > 0) {
			arrow.setDamage(power * dmg);
		}
		
		if(!(arrow.getShooter() instanceof Player))
			return;
		
		Player p = (Player) arrow.getShooter();
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		
		if(!rpg.getSkills().hasUnlimitedArrows())
			return;
		
		if(!arrow.getPickupStatus().equals(PickupStatus.ALLOWED))
			return;
		
		arrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);
		if(e.getBow().getType().equals(Material.CROSSBOW)) {
			ItemStack arr = e.getConsumable();
			p.getInventory().addItem(arr);
		} else {
			e.setConsumeItem(false);
			p.updateInventory();
		}
		
	}

}
