package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLivingEntityEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLocationEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.MissileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class KrzykUmarlych extends ACastableRune {

	private static DustOptions dust = new DustOptions(Color.fromRGB(0, 85, 0), 1.25f);
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	private IRuneLocationEffect onStartEffect;
	private IRuneLocationEffect onTickEffect;
	private IRuneLivingEntityEffect onHitEffect;
	private IRuneLocationEffect onEndEffect;
	
	private int manaToTake;
	private double damage;
	
	public KrzykUmarlych(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.85, -0.85, -0.85, 
				0.85, 0.85, 0.85);

		RpgStats stats = rpgPlayer.getStats();
		manaToTake = (int) Math.min(stats.getPresentMana(), 2500);
		damage = manaToTake * 50;
		
		onStartEffect = loc -> {
			loc.getWorld().playSound(loc, Sound.ENTITY_SKELETON_HORSE_HURT, 2f, 0.4f);
		};
		onTickEffect = loc -> {
			loc.getWorld().spawnParticle(Particle.DUST, loc, 8, 
					0.3f, 0.3f, 0.3f, 0, dust);
		};
		onEndEffect = loc -> {
			spellEffect(loc, damage);
		};
		onHitEffect = (loc, entity) -> onEndEffect.playEffect(loc);
	}

	@Override
	public void castSpell() {
		if(manaToTake < 1)
			return;
		
		rpgPlayer.getStats().removePresentManaSmart(manaToTake);
		
		double angle = 30;
		Location startLoc = player.getLocation().clone().add(0, 1.5, 0);
		
		player.getWorld().getNearbyEntities(startLoc, 35, 35, 35, entity -> {
			if(!(entity instanceof LivingEntity le))
				return false;
			
			if(Utils.getAngle(player, entity) > angle)
				return false;
			
			return hitCondition.check(player, le);
		}).stream().map(entity -> (LivingEntity) entity)
			.min((e1, e2) -> {
				double comp1 = (e1.getLocation().distance(startLoc) / angle
						- e2.getLocation().distance(startLoc) / angle) * 0.8;
				double comp2 = ((Utils.getAngle(player, e1)
						- Utils.getAngle(player, e2))) / angle * 0.2;
				return Double.compare(comp1 + comp2, 0);
			})
			.ifPresentOrElse(
					target -> castMissile(startLoc, target), 
					() -> castProjectile(startLoc)
			);
	}
	
	private void castProjectile(Location startLoc) {
		ProjectileRuneTemplate.castProjectile(
				this,
				startLoc,
				startLoc.getDirection().normalize(),
				0.2,
				2,
				1,
				40,
				boundingBox,
				onStartEffect,
				onTickEffect,
				hitCondition,
				onHitEffect,
				onEndEffect
		);
	}
	
	private void castMissile(Location startLoc, LivingEntity target) {
		MissileRuneTemplate.castMissile(
				this,
				startLoc,
				target,
				0.2,
				2,
				1,
				40,
				boundingBox,
				onStartEffect,
				onTickEffect,
				hitCondition,
				onHitEffect,
				onEndEffect
		);
	}
	
	private void spellEffect(Location loc, double dmg) {
		loc.getWorld().playSound(loc, Sound.ENTITY_PHANTOM_HURT, 2f, 0.2f);
		loc.getWorld().spawnParticle(Particle.DUST, loc, 100, 
				5f, 5f, 5f, 0.33f, dust);
		
		InstantRangeRuneTemplate.castEffect(
				this,
				loc,
				rune.getObszar(),
				0,
				0,
				_loc -> {
					
				},
				hitCondition,
				(_loc, e) -> {
					RuneUtils.damage(player, e, rune, dmg);
				});
	}

}
