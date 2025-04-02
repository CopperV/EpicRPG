package me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits;

import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;

public class PvPRuneHitCondition implements IRuneHitCondition {

	@Override
	public boolean check(Player caster, LivingEntity hit) {
		if(hit.equals(caster))
			return false;
		
		boolean pvpFlag = false;
		
		RegionQuery query = WorldGuard.getInstance()
				.getPlatform()
				.getRegionContainer()
				.createQuery();
		ApplicableRegionSet set = query.getApplicableRegions(
				BukkitAdapter.adapt(hit.getLocation()));
		State flag = set.queryValue(null, Flags.PVP);
		pvpFlag = flag != null && flag.equals(State.ALLOW)
				&& !(hit.getWorld().getName().toLowerCase().contains("dungeon") 
						|| hit.getWorld().getName().toLowerCase().contains("raid"));
		
		if(hit instanceof Player) {
			Player _hit = (Player) hit;
			if(_hit.getGameMode().equals(GameMode.SPECTATOR)
					|| _hit.getGameMode().equals(GameMode.CREATIVE))
				return false;
			return pvpFlag;
		}
		
		if(!MythicBukkit.inst().getMobManager().isMythicMob(hit))
			return false;
		
		ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(hit);
		if(aMob.isDead() || aMob.getType().getIsInvincible()
				|| (aMob.hasFaction() && aMob.getFaction().equals("ALLY")))
			return false;
		
		if(!aMob.getEntity().isDamageable())
			return false;
		
		if(aMob.hasFaction() && aMob.getFaction().equals("SUMMONS")) {
			if(aMob.getOwner().isPresent() && aMob.getOwner().get().equals(caster.getUniqueId()))
				return false;
			return pvpFlag;
		}
		
		return true;
	}

}
