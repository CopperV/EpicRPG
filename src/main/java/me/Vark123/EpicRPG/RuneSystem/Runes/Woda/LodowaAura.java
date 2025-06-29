package me.Vark123.EpicRPG.RuneSystem.Runes.Woda;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlotGroup;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class LodowaAura extends ACastableRune {

	private Random rand = new Random();
	private IRuneHitCondition hitCondition;
	
	public LodowaAura(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		castLoc.getWorld().playSound(castLoc, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 10, 0.5f);
		castLoc.getWorld().spawnParticle(Particle.WAX_OFF, player.getLocation().clone().add(0,1,0), 9, 0.4f, 0.8f, 0.4f, 0.1f);
		
		double radius = rune.getObszar();
		int amount = castLoc.getWorld().getNearbyEntities(castLoc, radius, radius, radius, entity -> {
			if(entity.getLocation().distanceSquared(castLoc) > radius * radius)
				return false;
			
			if(!(entity instanceof LivingEntity))
				return false;
			
			LivingEntity le = (LivingEntity) entity;
			if(hitCondition != null)
				return hitCondition.check(player, le);
			return true;
		}).size();
		
		if(amount < 1)
			return;
		double value = 50 * amount;
		
		AttributeModifier modifier = new AttributeModifier(
				new NamespacedKey(Main.getInstance(), rune.getMythicType().toLowerCase()),
				value,
				Operation.ADD_NUMBER,
				EquipmentSlotGroup.ANY);
		
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.LODOWA_AURA,
				player,
				target -> {
					target.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).addModifier(modifier);
					target.setAbsorptionAmount(target.getAbsorptionAmount() + value);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 2, 0.5f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.05);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 20, 0.4f, 0.8f, 0.4f, force);
					
					double value2 = Math.min(value, target.getAbsorptionAmount());
					target.setAbsorptionAmount(target.getAbsorptionAmount() - value2);
					target.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).removeModifier(modifier);
				},
				new TimingRuneEffect(3, target -> {
					castLoc.getWorld().spawnParticle(Particle.WAX_OFF, player.getLocation().clone().add(0,1,0), 9, 0.4f, 0.8f, 0.4f, 0.1f);
				}));
	}

}
