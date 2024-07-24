package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.ManualDamage;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class KlatwaKrwi extends ARune {

	private static final DustOptions dust = new DustOptions(Color.fromRGB(205, 0, 0), 0.2f);
	
	public KlatwaKrwi(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		
		modifiers.setKlatwaKrwi(true);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EVOKER_PREPARE_SUMMON, 1, 0.7f);
		p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().add(0,1,0), 18, 0.4f, 0.6f, 0.4f, 0.1f, dust);
		p.sendMessage("§7[§6EpicRPG§7] §aUzyles runy "+dr.getName());
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("§x§9§a§0§3§4§3§lKlatwa Krwi§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
				bar.setVisible(true);
				bar.addPlayer(p);
				bar.setProgress(timer/time);
			}
			
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					bar.removeAll();
					bar.setVisible(false);
					p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1, 0.6f);
					modifiers.setKlatwaKrwi(false);
					this.cancel();
					return;
				}

				p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().add(0,1,0), 18, 0.4f, 0.6f, 0.4f, 0.1f, dust);
				
				bar.setTitle("§x§9§a§0§3§4§3§lKlatwa Krwi§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				int price = dr.getPrice();
				if(rpg.getModifiers().hasZrodloNatury()){
					price *= 0.8;
				}
				EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, p, DamageCause.MAGIC, price);
				Bukkit.getPluginManager().callEvent(event);
				
				ManualDamage.doDamage(p, price, event);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}

}
