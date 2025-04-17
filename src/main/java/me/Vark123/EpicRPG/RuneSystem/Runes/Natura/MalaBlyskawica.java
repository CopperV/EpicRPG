package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class MalaBlyskawica extends ACastableRune {
	
	private static final Random rand = new Random();
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;

	public MalaBlyskawica(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-1.5, -0.5, -1.5, 
				1.5, 1.5, 1.5);
	}

	@Override
	public void castSpell() {
		Block block = player.getTargetBlock(new HashSet<>(Arrays.asList(Material.AIR, Material.WATER, Material.LAVA)),
				25);

		Location hit = block.getLocation().clone().add(0, 0, 0);

		spellEffect(hit);
		hit.getWorld().playSound(hit, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.2f, 1.25f);

		BoundingBox localBoundigBox = BoundingBox.of(hit, boundingBox.getWidthX() * 0.5, boundingBox.getHeight() * 0.5,
				boundingBox.getWidthZ() * 0.5);
		hit.getWorld().getNearbyEntities(localBoundigBox, entity -> {
			if (!(entity instanceof LivingEntity))
				return false;

			LivingEntity le = (LivingEntity) entity;
			return hitCondition.check(player, le);
		}).stream().min((e1, e2) -> {
			double dist1 = e1.getLocation().distanceSquared(hit);
			double dist2 = e2.getLocation().distanceSquared(hit);
			if (dist1 == dist2)
				return 0;
			return dist1 < dist2 ? -1 : 1;
		}).map(e -> (LivingEntity) e).ifPresent(entity -> {
			RuneUtils.damage(player, entity, rune);
		});
	}
	
	private void spellEffect(Location loc) {
		int count = 3 + rand.nextInt(3);
		for(int i = 0; i < count; ++i) {
			double offsetX = (rand.nextDouble() - 0.5) * 3;
            double offsetZ = (rand.nextDouble() - 0.5) * 3;
            double offsetY = 2 + rand.nextDouble() * 3;
            
            Location next = loc.clone().add(new Vector(offsetX, offsetY, offsetZ));
            
            Utils.drawLine(Particle.FIREWORK, loc, next, 0.1, 2, 0.05f, 0.05f, 0.05f, 0.02f);
		
            loc = next;
		}
	}

}
