package me.Vark123.EpicRPG.RuneSystem.Runes.Woda;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class EksplozjaLodu extends ACastableRune {
	
	private IRuneHitCondition hitCondition;

	public EksplozjaLodu(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}
	
	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.25, 0);
		
		InstantRangeRuneTemplate.castEffect(
				this,
				startLoc,
				rune.getObszar(),
				0,
				0,
				loc -> {
					loc.getWorld().playSound(loc, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.2f, 1.5F);

					double r = 2;
					double x,y,z,theta;
					for(int i = 0; i < 40; ++i) {
						theta = Math.random()*Math.PI*2;
						x = r * Math.sin(theta);
						y = Math.random()*2;
						z = r * Math.cos(theta);
						spellEffect(loc.clone(), new Vector(x, y, z).normalize().multiply(0.6));
					}
				},
				hitCondition,
				(loc, entity) -> {
					RuneUtils.damage(player, entity, rune);
					
					loc.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 0.8f, 0.5F);
				});
	}

	private void spellEffect(Location loc, Vector vec) {
		new BukkitRunnable() {
			int timer = 0;
			@Override
			public void run() {
				if(timer >= 20) {
					this.cancel();
					return;
				}
				
				loc.getWorld().spawnParticle(Particle.END_ROD, loc, 4, 0.06f, 0.06f, 0.06f, 0.01f);
				loc.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, loc, 3, 0.06f, 0.06f, 0.06f, 0.01f);
				
				loc.add(vec);
				++timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
