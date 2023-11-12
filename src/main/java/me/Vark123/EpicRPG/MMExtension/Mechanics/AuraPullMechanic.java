package me.Vark123.EpicRPG.MMExtension.Mechanics;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;
import io.lumine.mythic.core.skills.auras.AuraRegistry;

public class AuraPullMechanic extends SkillMechanic implements ITargetedEntitySkill {

	private String auraName;
	private boolean toOrigin;
	private PlaceholderDouble velocity;

	public AuraPullMechanic(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
		super(manager, file, line, mlc);
		this.velocity = mlc.getPlaceholderDouble(new String[] {"velocity", "v"}, 1.0D, new String[0]);
		this.toOrigin = mlc.getBoolean(new String[] {"toorigin", "to"}, false);
		this.auraName = mlc.getString(new String[] {"auraname", "aura", "a"}, "nullAura", new String[0]);
	}

	@Override
	public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		AuraRegistry registry = MythicBukkit.inst().getSkillManager().getAuraManager().getAuraRegistry(target);
		int stacks = 0;
		if(registry.hasAura(auraName)) stacks = registry.getStacks(auraName);
		
		double velocity = this.velocity.get(data, target) * stacks / 10.0D;
		Location l;
		if(this.toOrigin) {
			l = BukkitAdapter.adapt(data.getOrigin());
		}else {
			l = BukkitAdapter.adapt(data.getCaster().getEntity()).getLocation();
		}
		Entity t = BukkitAdapter.adapt(target);
	    
	    double distance = l.distance(t.getLocation());
	    double modxz = distance * 0.5D * velocity;
	    double mody = distance * 0.34D * velocity;
	    mody = l.getY() - target.getLocation().getY() != 0.0D ? mody * (Math.abs(l.getY() - target.getLocation().getY()) * 0.5D) : mody;
	    
	    Vector v = t.getLocation().toVector().subtract(l.toVector()).normalize().multiply(velocity);
	    v.setX(v.getX() * -1.0D * modxz);
	    v.setZ(v.getZ() * -1.0D * modxz);
	    v.setY(v.getY() * -1.0D * mody);
	    if (v.length() > 4.0D) {
	      v = v.normalize().multiply(4);
	    }
	    if (Double.isNaN(v.getX())) {
	      v.setX(0);
	    }
	    if (Double.isNaN(v.getY())) {
	      v.setY(0);
	    }
	    if (Double.isNaN(v.getZ())) {
	      v.setZ(0);
	    }
	    t.setVelocity(v);
		
		return SkillResult.SUCCESS;
	}

}
