package me.Vark123.EpicRPG.RuneSystem.Runes.Summons;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import io.lumine.mythic.core.skills.variables.Variable;
import io.lumine.mythic.core.skills.variables.VariableType;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLocationEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.SummonRuneTemplate;

public class PrzyzwanieZombie extends ACastableRune {

	private Map<String, Variable> variables = new HashMap<>();

	private IRuneLocationEffect spawnEffect = loc -> {
		loc.getWorld().playSound(loc, Sound.ENTITY_HUSK_AMBIENT, 1.25f, 0.9f);
		
		Location partLoc = loc.clone().add(0, 1, 0);
		partLoc.getWorld().spawnParticle(Particle.SMOKE, partLoc, 20, 0.5f, 1.1f, 0.5f, 0.01f);
		partLoc.getWorld().spawnParticle(Particle.DRAGON_BREATH, partLoc, 12, 0.5f, 1.1f, 0.5f, 0.01f);
	};
	
	public PrzyzwanieZombie(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		variables.put("tauntable", Variable.ofType(VariableType.INTEGER, 1));
	}

	@Override
	public void castSpell() {
		
		Location spawnLoc = player.getLocation().clone().add(0, 0, 0);
		double radius = 1.5;
		
		SummonRuneTemplate.castSummon(
				this,
				spawnLoc,
				radius,
				spawnEffect, 
				spawnEffect,
				variables);
		
	}

}
