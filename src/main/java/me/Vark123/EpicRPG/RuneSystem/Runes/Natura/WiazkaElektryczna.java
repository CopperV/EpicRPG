package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.MultipleProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class WiazkaElektryczna extends ACastableRune {

	private static final Random rand = new Random();
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public WiazkaElektryczna(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.65, -0.65, -0.65, 
				0.65, 0.65, 0.65);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		MultipleProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.44, 
				3,
				1,
				30, 
				4,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ITEM_TRIDENT_RIPTIDE_2, 1.2f, 0.8f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 6, 
							0.12f, 0.12f, 0.12f, 0.03f);
					loc.getWorld().spawnParticle(Particle.ENCHANT, loc, 4, 
							0.1f, 0.1f, 0.1f, 0.04f);
				}, 
				hitCondition, 
				(loc, e) -> {
					e.getWorld().spawnParticle(Particle.END_ROD, e.getLocation().clone().add(0,1,0), 15, 
							0.4f, 0.4f, 0.4f, 0.06f);
					e.getWorld().playSound(loc, Sound.BLOCK_AMETHYST_BLOCK_HIT, 1, 0.7f);
					
					if(RuneUtils.damage(player, e, rune)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1f, 1.33f);
						spellEffect(e.getLocation().clone());
					}
				}, 
				loc -> { });
	}
	
	private void spellEffect(Location loc) {
		int count = 3 + rand.nextInt(2);
		for(int i = 0; i < count; ++i) {
			double offsetX = (rand.nextDouble() - 0.5) * 2;
            double offsetZ = (rand.nextDouble() - 0.5) * 2;
            double offsetY = 2 + rand.nextDouble() * 2;
            
            Location next = loc.clone().add(new Vector(offsetX, offsetY, offsetZ));
            
            Utils.drawLine(Particle.FIREWORK, loc, next, 0.1, 2, 0.05f, 0.05f, 0.05f, 0.02f);
		
            loc = next;
		}
	}

}
