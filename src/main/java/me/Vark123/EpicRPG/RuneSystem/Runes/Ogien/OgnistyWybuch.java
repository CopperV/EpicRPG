package me.Vark123.EpicRPG.RuneSystem.Runes.Ogien;

import java.util.Random;

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
import me.Vark123.EpicRPG.RuneSystem.Effects.DamageBurnEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRunePostDamageEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class OgnistyWybuch extends ACastableRune {
	private static final Random rand = new Random();
	private IRuneHitCondition hitCondition;
	private IRunePostDamageEffect hitEffect;

	public OgnistyWybuch(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		hitEffect = new DamageBurnEffect(8, rune.getDamage() * 0.1, this);
	}
	
	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.25, 0);
		int timer = 4;
		
		InstantRangeRuneTemplate.castEffect(
				this, 
				startLoc,
				rune.getObszar(),
				0,
				20*timer,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_AMBIENT, 2, 0.5f);
					
					new BukkitRunnable() {
						int _timer = timer*20;
						@Override
						public void run() {
							if(isCancelled())
								return;
							if(!casterInCastWorld()) {
								cancel();
								return;
							}
							if(_timer <= 0) {
								castEffect(loc);
								
								cancel();
								return;
							}
							--_timer;
							
							prepareEffect(loc);
						}
					}.runTaskTimer(Main.getInstance(), 0, 1);
				}, 
				hitCondition, 
				(loc, entity) -> {
					RuneUtils.damage(player, entity, rune, hitEffect);
				});
	}
	
	private void prepareEffect(Location loc) {
		for(int i = 0; i < 6; ++i) {
			double radius = rand.nextDouble(1.5) + 1;
			double angle = rand.nextDouble(Math.PI * 2);
			
			double x = Math.sin(angle) * radius;
			double y = rand.nextDouble(3.5) - 1;
			double z = Math.cos(angle) * radius;
			
			Location source = loc.clone().add(x, y, z);
			Vector dir = new Vector(loc.getX() - source.getX(),
					loc.getY() - source.getY(),
					loc.getZ() - source.getZ()).normalize();
			
			loc.getWorld().spawnParticle(Particle.FLAME, source, 0, dir.getX(), dir.getY(), dir.getZ(), radius/20.f);
		}
	}
	
	private void castEffect(Location loc) {
		loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_BREAK_BLOCK, 2f, 0.6F);
		for(int i = 0; i < 80; ++i) {
			double radius = rand.nextDouble(4) + 2;
			double angle = rand.nextDouble(Math.PI * 2);
			
			double x = Math.sin(angle) * radius;
			double y = rand.nextDouble(6) - 1;
			double z = Math.cos(angle) * radius;
			
			Location target = loc.clone().add(x, y, z);
			Vector dir = new Vector(target.getX() - loc.getX(),
					target.getY() - loc.getY(),
					target.getZ() - loc.getZ()).normalize();
			
			loc.getWorld().spawnParticle(Particle.FLAME, loc, 0, dir.getX(), dir.getY(), dir.getZ(), radius/4.f);
		}
	}

}
