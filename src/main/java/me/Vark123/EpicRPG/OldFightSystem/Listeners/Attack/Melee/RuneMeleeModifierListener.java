package me.Vark123.EpicRPG.OldFightSystem.Listeners.Attack.Melee;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicRPG.OldFightSystem.EpicDamageType;
import me.Vark123.EpicRPG.OldFightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.OldRuneSystem.Runes.BlogoslawienstwoPrzedwiecznych;
import me.Vark123.EpicRPG.OldRuneSystem.Runes.BlogoslawienstwoPrzedwiecznych_H;
import me.Vark123.EpicRPG.OldRuneSystem.Runes.BlogoslawienstwoPrzedwiecznych_M;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class RuneMeleeModifierListener implements Listener {
	
	private static final Random rand = new Random();
	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.MELEE))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		boolean crit = e.getCalculatedDamage().getValue();
		double modifier = 0;
		
		if(modifiers.hasTrans())
			modifier += 0.5;
		if(modifiers.hasSzalBitewny())
			modifier += 1;
		if(crit && modifiers.hasSkrytobojstwo())
			modifier += 0.4;
		if(modifiers.hasGniew())
			modifier += 0.75;
		if(modifiers.hasMord())
			modifier += 0.9;
		if(modifiers.hasSzalPrzedwiecznych())
			modifier += 0.3;
		if(modifiers.hasSzalPrzedwiecznych_h())
			modifier += 0.44;
		if(modifiers.hasSzalPrzedwiecznych_m())
			modifier += 0.6;
		if(BlogoslawienstwoPrzedwiecznych.getEffected().containsKey(p))
			modifier += BlogoslawienstwoPrzedwiecznych.getEffected().get(p)*0.03;
		if(BlogoslawienstwoPrzedwiecznych_H.getEffected().containsKey(p))
			modifier += BlogoslawienstwoPrzedwiecznych_H.getEffected().get(p)*0.04;
		if(BlogoslawienstwoPrzedwiecznych_M.getEffected().containsKey(p))
			modifier += BlogoslawienstwoPrzedwiecznych_M.getEffected().get(p)*0.05;
		if(modifiers.hasTajemnyBlask_m()) {
			if(rpg.getInfo().getProffesion().equals("§cWojownik"))
				modifier += 0.5;
			else if(rpg.getInfo().getProffesion().equals("§2Mysliwy"))
				modifier += rand.nextDouble(0.5) + 0.25;
		} else if(modifiers.hasTajemnyBlask()) {
			if(rpg.getInfo().getProffesion().equals("§cWojownik"))
				modifier += 0.3;
			else if(rpg.getInfo().getProffesion().equals("§2Mysliwy"))
				modifier += rand.nextDouble(0.3) + 0.15;
		}
		
		if(modifiers.hasCiosWPlecy()) {
			Entity victim = e.getVictim();
			Location loc1 = victim.getLocation();
			Location loc2 = damager.getLocation();
			Vector vec1 = loc1.clone().subtract(loc2).toVector().normalize();
			Vector vec2 = loc1.getDirection();
			
			vec1.setY(0).normalize();
			vec2.setY(0).normalize();
			double calcAngle = Math.toDegrees(vec1.angle(vec2)) - 180;
			if(Math.abs(calcAngle) > 135) {
				p.playSound(p.getLocation(), Sound.BLOCK_GRINDSTONE_USE, 2, 2);
				p.getWorld().spawnParticle(Particle.SWEEP_ATTACK, p.getLocation().add(0,1,0), 7, .5f, .5f, .5f, .05f);
				modifier += 1.25;
			}
		}
		
		ItemStack item = p.getInventory().getItemInMainHand();
		if(item != null && !item.getType().equals(Material.AIR)) {
			EpicComponent compi = new EpicComponent(item);
			if(compi.hasKey("SzponBeliara") && modifiers.hasWybraniecBeliara())
				modifier += 0.3;
		}
		
		e.increaseModifier(modifier);
	}

}
