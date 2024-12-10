package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class TajemnyBlask extends ARune {
	
	private static List<Player> debuff = new ArrayList<>();
	private List<Player> debuff_tmp = new ArrayList<>();

	public TajemnyBlask(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		
		Location loc = p.getLocation().clone();
		loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar(), e -> {
			if(e.getLocation().distanceSquared(loc) > dr.getObszar()*dr.getObszar())
				return false;
			if(!(e instanceof Player))
				return false;
			Player _p = (Player) e;
			if(debuff.contains(_p))
				return false;
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(_p);
			if(rpg.getModifiers().hasTajemnyBlask())
				return false;
			return true;
		})
		.stream()
		.map(e -> PlayerManager.getInstance().getRpgPlayer((Player) e))
		.forEach(rpg -> {
			Player _p = rpg.getPlayer();
			Location _loc = _p.getLocation().clone().add(0,1,0);

			_p.sendMessage("§7[§6EpicRPG§7] §aOdkrywasz arkana magii tajemnej. Twoje zdolnosci wzrosly!");
			_p.playSound(_p, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1, 0.8f);
			_loc.getWorld().spawnParticle(Particle.FLASH, _loc,13, 0, 0, 0, 0);
			
			debuff_tmp.add(_p);
			rpg.getModifiers().setTajemnyBlask(true);
			MutableDouble modifier = new MutableDouble(1);
			switch(rpg.getInfo().getProffesion()) {
				case "§cWojownik":
					modifier.setValue(1.35);
					break;
				case "§5Mag":
					modifier.setValue(1.5);
					break;
				case "§2Mysliwy":
					modifier.setValue(1);
					break;
			}
			
			new BukkitRunnable() {
				double time = modifier.getValue() * dr.getDurationTime();
				double timer = time;
				BossBar bar = Bukkit.createBossBar(dr.getName()+"§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
					bar.setVisible(true);
					bar.addPlayer(_p);
					bar.setProgress(timer/time);
				}
				@Override
				public void run() {
					if(timer <= 0 || !casterInCastWorld() 
							|| !_p.getWorld().getName().equals(p.getWorld().getName())) {
						bar.removeAll();
						bar.setVisible(false);
						_p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
						_p.getWorld().playSound(_p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1.7f);
						rpg.getModifiers().setTajemnyBlask(false);
						
						this.cancel();
						return;
					}

					bar.setTitle(dr.getName()+"§f: "+(int)timer+" sekund");
					bar.setProgress(timer/time);
					
					--timer;
				}
			}.runTaskTimer(Main.getInstance(), 0, 20);
		});
		
		debuff.addAll(debuff_tmp);
		new BukkitRunnable() {
			DustOptions dust = new DustOptions(Color.RED, 1);
			@Override
			public void run() {
				debuff_tmp.stream().forEach(tmp -> {
					if(debuff.contains(tmp))
						debuff.remove(tmp);
					if(!tmp.isOnline())
						return;
					tmp.sendMessage("§7[§6EpicRPG§7] §aDebuff runy "+dr.getName()+" skonczyl sie");
					tmp.playSound(p.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 5, 1.5f);
					tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp.getLocation(), 10, 0.5f, 0.5f, 0.5f, 0.1f,dust);
				});
			}
		}.runTaskLater(Main.getInstance(), 60*20*20);
	}

}
