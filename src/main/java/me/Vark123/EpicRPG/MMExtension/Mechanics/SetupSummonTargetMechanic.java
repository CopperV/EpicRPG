package me.Vark123.EpicRPG.MMExtension.Mechanics;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableObject;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.INoTargetSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.SummonManager;
import me.Vark123.EpicRPG.Utils.Utils;

public class SetupSummonTargetMechanic extends SkillMechanic implements INoTargetSkill {

	private PlaceholderDouble _searchRadius;

	public SetupSummonTargetMechanic(SkillExecutor executor, File file, String line, MythicLineConfig config) {
		super(executor, file, line, config);
		_searchRadius = config.getPlaceholderDouble(new String[] { "searchradius", "radius", "r" }, 40, new String[0]);
	}

	@Override
	public SkillResult cast(SkillMetadata arg0) {
		AbstractEntity caster = arg0.getCaster().getEntity();
		if (!MythicBukkit.inst().getMobManager().isActiveMob(caster))
			return SkillResult.INVALID_CONFIG;

		ActiveMob summon = MythicBukkit.inst().getMobManager().getMythicMobInstance(caster);
		String stance = summon.getStance();
		if (stance == null || stance.isBlank())
			return SkillResult.INVALID_CONFIG;

		if (!SummonManager.get().isSummon(summon))
			return SkillResult.INVALID_CONFIG;

		Player owner = SummonManager.get().getSummonOwner(summon);
		if (owner == null)
			return SkillResult.INVALID_CONFIG;

		AbstractEntity abstractOwner = BukkitAdapter.adapt(owner);
		double radius = _searchRadius.get(arg0.getCaster());
		AbstractLocation ownerLoc = abstractOwner.getLocation();
		Collection<AbstractEntity> entities = ownerLoc.getWorld().getEntitiesNearLocation(ownerLoc, radius, entity -> {
			if (entity == null)
				return false;
			if (entity.getLocation().distanceSquared(ownerLoc) > (radius * radius))
				return false;

			Entity bukkitEntity = entity.getBukkitEntity();

			if (!MythicBukkit.inst().getMobManager().isActiveMob(entity) && !(bukkitEntity instanceof Player))
				return false;

			if (!(bukkitEntity instanceof LivingEntity le))
				return false;
			return !Utils.isEntityAlly(owner, le);
		});

		MutableObject<SkillResult> result = new MutableObject<>(SkillResult.SUCCESS);
		switch (stance.toLowerCase()) {
			case "attack_nearest": {
				AbstractEntity newTarget = entities.stream()
						.min(Comparator.comparingDouble(e -> e.getLocation().distanceSquared(ownerLoc))).orElse(null);
				if (newTarget == null) {
					result.setValue(SkillResult.INVALID_TARGET);
					break;
				}
	
				summon.setTarget(newTarget);
			}
				break;
			case "attack_furthest": {
				AbstractEntity newTarget = entities.stream()
						.max(Comparator.comparingDouble(e -> e.getLocation().distanceSquared(ownerLoc))).orElse(null);
				if (newTarget == null) {
					result.setValue(SkillResult.INVALID_TARGET);
					break;
				}
	
				summon.setTarget(newTarget);
			}
				break;
			case "attack_lowest_hp": {
				AbstractEntity newTarget = entities.stream().min(Comparator.comparingDouble(e -> e.getHealth()))
						.orElse(null);
				if (newTarget == null) {
					result.setValue(SkillResult.INVALID_TARGET);
					break;
				}
	
				summon.setTarget(newTarget);
			}
				break;
			case "attack_highest_hp": {
				AbstractEntity newTarget = entities.stream().max(Comparator.comparingDouble(e -> e.getHealth()))
						.orElse(null);
				if (newTarget == null) {
					result.setValue(SkillResult.INVALID_TARGET);
					break;
				}
	
				summon.setTarget(newTarget);
			}
				break;
			case "attack_lowest_percentage_hp": {
				AbstractEntity newTarget = entities.stream()
						.min(Comparator.comparingDouble(e -> e.getHealth() / e.getMaxHealth())).orElse(null);
				if (newTarget == null) {
					result.setValue(SkillResult.INVALID_TARGET);
					break;
				}
	
				summon.setTarget(newTarget);
			}
				break;
			case "attack_highest_percentage_hp": {
				AbstractEntity newTarget = entities.stream()
						.max(Comparator.comparingDouble(e -> e.getHealth() / e.getMaxHealth())).orElse(null);
				if (newTarget == null) {
					result.setValue(SkillResult.INVALID_TARGET);
					break;
				}
	
				summon.setTarget(newTarget);
			}
				break;
			case "attack_random": {
				List<AbstractEntity> tmpList = entities.stream()
						.filter(entity -> entity.getLocation().distanceSquared(abstractOwner) <= (20*20))
						.collect(Collectors.toList());
				if (tmpList.isEmpty()) {
					result.setValue(SkillResult.INVALID_TARGET);
					break;
				}
	
				Random rand = new Random();
				AbstractEntity newTarget = tmpList.get(rand.nextInt(tmpList.size()));
				summon.setTarget(newTarget);
			}
				break;
			default:
				return SkillResult.INVALID_CONFIG;
		}

		return result.getValue();
	}

}
