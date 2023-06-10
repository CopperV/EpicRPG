package me.Vark123.EpicRPG.MMExtension.Mechanics;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.ITargetedLocationSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;

public class ChangeRegionFlagMechanic extends SkillMechanic implements ITargetedEntitySkill, ITargetedLocationSkill {

	private String region;
	private String flag;
	private String value;
	
	public ChangeRegionFlagMechanic(SkillExecutor manager, String line, MythicLineConfig mlc) {
		super(manager, line, mlc);
		this.region = mlc.getString(new String[] {"region"}, "__global__");
		this.flag = mlc.getString(new String[] {"flag"}, "pvp");
		this.value = mlc.getString(new String[] {"state"}, null);
	}

	@Override
	public SkillResult castAtLocation(SkillMetadata data, AbstractLocation loc) {
		ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer().get(
				com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(BukkitAdapter.adapt(loc).getWorld())
				).getRegion(this.region);
		Flag<?> flag = WorldGuard.getInstance().getFlagRegistry().get(this.flag);
		StateFlag stateFlag = (StateFlag) flag;
		region.setFlag(stateFlag, StateFlag.State.valueOf(value.toUpperCase()));
		return SkillResult.SUCCESS;
	}

	@Override
	public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		AbstractLocation loc = target.getLocation();
		ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer().get(
				com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(BukkitAdapter.adapt(loc).getWorld())
				).getRegion(this.region);
		Flag<?> flag = WorldGuard.getInstance().getFlagRegistry().get(this.flag);
		StateFlag stateFlag = (StateFlag) flag;
		region.setFlag(stateFlag, StateFlag.State.valueOf(value.toUpperCase()));
		return SkillResult.SUCCESS;
	}
	
	
}
