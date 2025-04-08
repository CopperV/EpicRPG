package me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlotGroup;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;

public class AuraCzystosci extends ACastableRune {

	private Random rand = new Random();
	
	public AuraCzystosci(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		double value = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.5;
		Bukkit.broadcastMessage("Test1 "+value+" "+player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		
		AttributeModifier modifier = new AttributeModifier(
				new NamespacedKey(Main.getInstance(), rune.getMythicType().toLowerCase()),
				value,
				Operation.ADD_NUMBER,
				EquipmentSlotGroup.ANY);
		Bukkit.broadcastMessage("Test2 "+(modifier.getKey() == null));
		
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.AURA_CZYSTOSCI,
				player,
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.2f, 0.7f);

					target.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).addModifier(modifier);
					target.setAbsorptionAmount(target.getAbsorptionAmount() + value);
					Bukkit.broadcastMessage("Test2 "+value+" "+target.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).getValue());
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.05);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 20, 0.4f, 0.8f, 0.4f, force);
					
					double value2 = Math.min(value, target.getAbsorptionAmount());
					target.setAbsorptionAmount(target.getAbsorptionAmount() - value2);
					target.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).removeModifier(modifier);
				},
				new TimingRuneEffect(4, target -> {
					Location _loc = target.getLocation().clone().add(0, 0.05, 0);

					for(int i = 0; i < 4; ++i) {
						double radius = rand.nextDouble(0.8);
						double angle = rand.nextDouble(Math.PI*2);
						double force = rand.nextDouble(0, 0.1);
						
						double x = radius * Math.sin(angle);
						double y = rand.nextDouble(2);
						double z = radius * Math.cos(angle);
						
						_loc.getWorld().spawnParticle(Particle.OMINOUS_SPAWNING, _loc.clone().add(x,y,z), 0,
								0, 1, 0, force);
					}
					for(int i = 0; i < 2; ++i) {
						double radius = rand.nextDouble(0.8);
						double angle = rand.nextDouble(Math.PI*2);
						double force = rand.nextDouble(0, 0.1);
						
						double x = radius * Math.sin(angle);
						double y = rand.nextDouble(2);
						double z = radius * Math.cos(angle);
						
						_loc.getWorld().spawnParticle(Particle.FIREWORK, _loc.clone().add(x,y,z), 0,
								0, 1, 0, force);
					}
				}));
	}

}
