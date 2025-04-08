package me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPAllyRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPAllyRuneCondition;

public class BlogoslawionaZiemia extends ACastableRune {
	
	private Random random = new Random();
	private IRuneHitCondition hitCondition;
	
	public BlogoslawionaZiemia(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPAllyRuneCondition() : new NonPvPAllyRuneCondition();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone();
		double radius = rune.getObszar();
		
		TotemRuneTemplate.castTotem(
				this,
				startLoc,
				rune.getDurationTime(),
				radius,
				4,
				20,
				loc -> {							
					loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1.5f, 1f);
				},
				loc -> {
					for(double i = 0; i < radius; i+=0.5) {
						for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI*2/(3*rune.getObszar()))) {
							double x = i * Math.sin(theta);
							double z = i * Math.cos(theta);
							Location tmp = loc.clone().add(x, 0.1, z);
							player.getWorld().spawnParticle(Particle.END_ROD, tmp, 0, 0, 1, 0, random.nextDouble(0.01, 0.06));
						}
					}
				},
				hitCondition,
				(loc, entity) -> {
					if(!(entity instanceof Player target))
						return;
					
					RpgStats stats = rpgPlayer.getStats();
					double value = stats.getFinalMana()*0.25 + stats.getFinalWytrzymalosc()*0.1 + stats.getFinalSila()*0.05;
					
					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(target);
					rpg.getStats().addPresentManaSmart((int) value);
					
					loc.getWorld().spawnParticle(Particle.END_ROD, target.getLocation().clone().add(0,1,0), 8,
							0.4, 0.8, 0.4, 0.03);
				},
				loc -> { });
	}

}
