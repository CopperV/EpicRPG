package me.Vark123.EpicRPG.MMExtension.Mechanics;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.adapters.AbstractVector;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.ITargetedLocationSkill;
import io.lumine.mythic.api.skills.Skill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.api.skills.placeholders.PlaceholderInt;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;
import me.Vark123.EpicRPG.Main;

public class EchoMechanic extends SkillMechanic implements ITargetedEntitySkill, ITargetedLocationSkill {

	private Skill tickSkill;
	private Skill hitSkill;
	private PlaceholderInt amount;
	private PlaceholderDouble hR;
	private PlaceholderDouble vR;

	private PlaceholderInt duration;
	private PlaceholderDouble range;
	private PlaceholderDouble velocity;
	
	private PlaceholderDouble yOffset;
	
	public EchoMechanic(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
		super(manager, file, line, mlc);
		
		this.amount = mlc.getPlaceholderInteger(new String[] {"amount", "a"}, 1, new String[0]);
		this.duration = mlc.getPlaceholderInteger(new String[] {"duration", "d"}, 5, new String[0]);
		this.hR = mlc.getPlaceholderDouble(new String[] {"hR"}, 1.25, new String[0]);
		this.vR = mlc.getPlaceholderDouble(new String[] {"vR"}, hR.get(), new String[0]);
		this.range = mlc.getPlaceholderDouble(new String[] {"range", "mr"}, 10, new String[0]);
		this.velocity = mlc.getPlaceholderDouble(new String[] {"velocity", "v"}, 2.5, new String[0]);
		this.yOffset = mlc.getPlaceholderDouble(new String[] {"syo", "yo", "offset", "yOffset"}, 0.5, new String[0]);
		
		if(mlc.getString("oT")!=null) {
			Optional<Skill> tmp =  MythicBukkit.inst().getSkillManager().getSkill(mlc.getString("oT"));
			if(tmp.isPresent()) {
				tickSkill = tmp.get();
			}
		}
		if(mlc.getString("oH")!=null) {
			Optional<Skill> tmp =  MythicBukkit.inst().getSkillManager().getSkill(mlc.getString("oH"));
			if(tmp.isPresent()) {
				hitSkill = tmp.get();
			}
		}
		
	}

	@Override
	public boolean execute(SkillMetadata arg0) {
		return super.execute(arg0);
	}

	@Override
	public SkillResult castAtLocation(SkillMetadata arg0, AbstractLocation arg1) {
		AbstractLocation startLocation = arg0.getCaster().getLocation().clone().add(0, yOffset.get(arg0.getCaster()), 0);
		AbstractLocation endLocation = arg1.clone();
		endLocation.setY(startLocation.getY());
		
		AbstractVector vec = new AbstractVector(endLocation.getX() - startLocation.getX(), 0, endLocation.getZ() - startLocation.getZ()).normalize().multiply(velocity.get(arg0.getCaster())*0.1);
		
		int interval = 1;
		
		new BukkitRunnable() {

			int timer = 0;
			double droga = 0;
			int hitted = 0;
			
			@Override
			public void run() {
				
				if(timer>duration.get(arg0.getCaster())) {
					this.cancel();
					return;
				}
				if(droga>range.get(arg0.getCaster())) {
					this.cancel();
					return;
				}
				if(hitted>=amount.get(arg0.getCaster())) {
					this.cancel();
					return;
				}
				
				arg0.setOrigin(startLocation);
				if(tickSkill != null) {
					tickSkill.execute(arg0);
				}
				
				startLocation.add(vec);
				Location tmp = BukkitAdapter.adapt(startLocation);
				if(tmp.getBlock().getType().isSolid()) {
					Block b = tmp.getBlock();
					startLocation.subtract(vec);
					BlockFace face = b.getFace(BukkitAdapter.adapt(startLocation).getBlock());
					if(face.getModX() != 0) {
						vec.setX(vec.getX()*(-1));
					}else {
						vec.setZ(vec.getZ()*(-1));
					}
					++hitted;
				}
				
				if(hitSkill != null) {
					Collection<Entity> list = tmp.getWorld().getNearbyEntities(tmp, hR.get(arg0.getCaster()), vR.get(arg0.getCaster()), hR.get(arg0.getCaster()));
					Player p = null;
					double distance = Double.MAX_VALUE;
					for(Entity e : list) {
						if(!(e instanceof Player)) continue;
						if(arg0.getCaster().getEntity().equals(BukkitAdapter.adapt(e)))
							continue;
						if(e.getLocation().distance(tmp) < distance) {
							p = (Player) e;
							distance = e.getLocation().distance(tmp);
						}
					}
					if(p != null) {
						AbstractEntity aE = BukkitAdapter.adapt(p);
						SkillMetadata data;
						data = arg0;
						data.setTrigger(aE);
						data.setEntityTarget(aE);
						data.setOrigin(startLocation);
						hitSkill.execute(data);
						this.cancel();
						return;
					}
				}
				
				timer += interval;
				droga += vec.length();
				
			}
		}.runTaskTimer(Main.getInstance(), 0, interval);
		
		return SkillResult.SUCCESS;
	}

	@Override
	public SkillResult castAtEntity(SkillMetadata arg0, AbstractEntity arg) {
		AbstractLocation arg1 = arg.getLocation();
		AbstractLocation startLocation = arg0.getOrigin().clone().add(0, yOffset.get(arg), 0);
		AbstractLocation endLocation = arg1.clone();
		endLocation.setY(startLocation.getY());
		
		AbstractVector vec = new AbstractVector(endLocation.getX() - startLocation.getX(), 0, endLocation.getZ() - startLocation.getZ()).normalize().multiply(velocity.get(arg)*0.1);
		
		int interval = 1;
		
		new BukkitRunnable() {

			int timer = 0;
			double droga = 0;
			int hitted = 0;
			
			@Override
			public void run() {
				
				if(timer>duration.get(arg)) {
					this.cancel();
					return;
				}
				if(droga>range.get(arg)) {
					this.cancel();
					return;
				}
				if(hitted>=amount.get(arg)) {
					this.cancel();
					return;
				}
				
				arg0.setOrigin(startLocation);
				if(tickSkill != null) {
					tickSkill.execute(arg0);
				}
				
				startLocation.add(vec);
				Location tmp = BukkitAdapter.adapt(startLocation);
				if(tmp.getBlock().getType().isSolid()) {
					Block b = tmp.getBlock();
					startLocation.subtract(vec);
					BlockFace face = b.getFace(BukkitAdapter.adapt(startLocation).getBlock());
					if(face.getModX() != 0) {
						vec.setX(vec.getX()*(-1));
					}else {
						vec.setZ(vec.getZ()*(-1));
					}
					++hitted;
				}
				
				if(hitSkill != null) {
					Collection<Entity> list = tmp.getWorld().getNearbyEntities(tmp, hR.get(arg), vR.get(arg), hR.get(arg));
					Player p = null;
					double distance = Double.MAX_VALUE;
					for(Entity e : list) {
						if(!(e instanceof Player)) continue;
						if(arg0.getCaster().getEntity().equals(BukkitAdapter.adapt(e)))
							continue;
						if(e.getLocation().distance(tmp) < distance) {
							p = (Player) e;
							distance = e.getLocation().distance(tmp);
						}
					}
					if(p != null) {
						AbstractEntity aE = BukkitAdapter.adapt(p);
						SkillMetadata data;
						data = arg0;
						data.setTrigger(aE);
						data.setEntityTarget(aE);
						data.setOrigin(startLocation);
						hitSkill.execute(data);
						this.cancel();
						return;
					}
				}
				
				timer += interval;
				droga += vec.length();
				
			}
		}.runTaskTimer(Main.getInstance(), 0, interval);
		
		return SkillResult.SUCCESS;
	}

}
