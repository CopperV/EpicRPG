package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Healing.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

public class RozerwanieDuszy extends ARune {

	public RozerwanieDuszy(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {

		RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
		
		int mana = rpg.getFinalmana() / 20;
		if(rpg.getPresent_mana() < mana) mana = rpg.getPresent_mana();
		if(mana <= 0)
			return;
		double hp = mana * 0.75;
		
		RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, hp);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		rpg.removePresentMana(mana);
		
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.9f, 0.8f);
		
		Location tmp = p.getLocation().add(0, 1.25, 0);
		for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/16)) {
			double x = 4 * Math.sin(theta);
			double z = 4 * Math.cos(theta);
			p.getWorld().spawnParticle(Particle.NAUTILUS, tmp, 0, x, 0, z, 0.2);
		}
	}

}
