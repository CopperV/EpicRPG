package me.Vark123.EpicRPG.RuneSystem.Runes.Ogien;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Effects.DamageBurnEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRunePostDamageEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.StunRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class MasowaPirokineza extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	private IRunePostDamageEffect hitEffect;

	public MasowaPirokineza(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		
		hitEffect = new DamageBurnEffect(rune.getDurationTime(), rune.getDamage(), this);
	}
	
	@Override
	public void castSpell() {
		PotionEffect slow = new PotionEffect(PotionEffectType.SLOWNESS, 20 * rune.getDurationTime(), 2);
		player.addPotionEffect(slow);
		
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		InstantRangeRuneTemplate.castEffect(
				this,
				startLoc,
				rune.getObszar(),
				0,
				0,
				loc -> { },
				hitCondition,
				(loc, entity) -> {
					createLine(startLoc, entity.getLocation().clone().add(0,1,0));
					
					RuneUtils.damage(player, entity, rune, hitEffect);
					StunRuneTemplate.castEffect(this, entity);
				});
	}
	
	private void createLine(Location from, Location to) {
		double space = 0.1;
		Vector p1 = new Vector(from.getX(), from.getY(), from.getZ());
		Vector p2 = new Vector(to.getX(), to.getY(), to.getZ());
		double distance = from.distance(to);
		Vector vec = p2.clone().subtract(p1).normalize().multiply(space);
		for (double length = 0; length < distance; p1.add(vec), length += space) {
			from.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, p1.getX(), p1.getY(), p1.getZ(), 3, 0.1F, 0.1F, 0.1F, 0.01F);
		}
	}

}
