package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class TrupiJek extends ACastableRune {

	private static DustOptions dust1 = new DustOptions(Color.fromRGB(0, 128, 0), 0.8f);
	private static DustOptions dust2 = new DustOptions(Color.fromRGB(0, 85, 0), 1.2f);
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public TrupiJek(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.8, -0.8, -0.8, 
				0.8, 0.8, 0.8);
	}

	@Override
	public void castSpell() {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgStats stats = rpg.getStats();
		if(stats.getPresentMana() < 1)
			return;
		
		int mana = (int) Math.min(Math.min(stats.getFinalMana() * 0.2, stats.getPresentMana()), 400);
		double damage = mana * 40;
		stats.removePresentManaSmart(mana);
		
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.4, 
				2,
				1,
				30, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_SKELETON_HORSE_HURT, 2f, 0.5f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.DUST, loc, 8, 
							0.3f, 0.3f, 0.3f, 0, dust1);
				}, 
				hitCondition, 
				(loc, e) -> {
					spellEffect(loc, damage);
				}, 
				loc -> {
					spellEffect(loc, damage);
				});
	}
	
	private void spellEffect(Location loc, double dmg) {
		loc.getWorld().playSound(loc, Sound.ENTITY_PHANTOM_HURT, 2f, 0.5f);
		loc.getWorld().spawnParticle(Particle.DUST, loc, 50, 
				2f, 2f, 2f, 0.2f, dust2);
		
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
