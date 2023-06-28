package me.Vark123.EpicRPG.KosturSystem;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

@Getter
public class KosturClickInfo {

	private boolean r1 = true;
	@Setter
	private boolean r2;
	@Setter
	private boolean r3;
	
	private int clickAmount = 1;
	
	private ItemStack kostur;
	private Player player;
	
	private long lastUse = System.currentTimeMillis();
	
	public KosturClickInfo(Player p, ItemStack kostur) {
		this.kostur = kostur;
		this.player = p;
	}
	
	public void updateClicks() {
		++clickAmount;
		lastUse = System.currentTimeMillis();
	}
	
}
