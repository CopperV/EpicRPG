package me.Vark123.EpicRPG.MMExtension.Targeters;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.mobs.MobExecutor;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.targeters.IEntitySelector;
import io.lumine.mythic.core.utils.annotations.MythicTargeter;
import me.Vark123.EpicRPG.Utils.Utils;

@MythicTargeter(aliases={"APIR","EPIR"}, description="Targets all possible enemies in radius")
public class AlliesPlayersNearOriginTargeter extends IEntitySelector {
	
	private final MobExecutor manager = MythicBukkit.inst().getMobManager();
	
	private PlaceholderDouble radius;
	
	public AlliesPlayersNearOriginTargeter(SkillExecutor exec, MythicLineConfig mlc) {
		super(exec, mlc);
        this.radius = PlaceholderDouble.of(mlc.getString(new String[]{"radius", "r"}, "5", new String[0]));
	}

    @Override
    public Collection<AbstractEntity> getEntities(SkillMetadata data) {
        SkillCaster am = data.getCaster();
        
        AbstractEntity aCaster = am.getEntity();
		Entity caster = BukkitAdapter.adapt(aCaster);
		
        AbstractLocation loc = data.getOrigin();
        double r = radius.get(data);
        double sqrRadius = r*r;
        
        Collection<AbstractEntity> targets = aCaster.getWorld().getEntitiesNearLocation(loc, r, aTarget -> {
        	if(loc.distanceSquared(aTarget) > sqrRadius)
        		return false;
        	
        	Entity target = BukkitAdapter.adapt(aTarget);
        	if(!(target instanceof LivingEntity leTarget))
        		return false;
        	
        	if(caster instanceof Player player) {
        		return Utils.isEntityAlly(player, leTarget);
        	}

    		if(aTarget.getBukkitEntity() instanceof Player)
        		return true;
    		
    		if(manager.isActiveMob(aCaster)) {
    			ActiveMob mob = manager.getMythicMobInstance(aCaster);
    			if(mob.getOwnerUUID().isPresent() && Bukkit.getPlayer(mob.getOwnerUUID().get()) != null)
    				return Utils.isEntityAlly((LivingEntity) caster, leTarget);
    		}
    		
    		return target instanceof Player || Utils.isEntityMythicMobAlly(aTarget);
        });
        
        return targets;
    }
}
