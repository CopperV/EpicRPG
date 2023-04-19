package me.Vark123.EpicRPG.Players;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Main;

public class RpgSkills {

	private RpgPlayer rpg;
	
	private boolean hungerless;
	private boolean unlimitedArrows;
	private boolean manaReg;
	private boolean slugaBeliara;
	private boolean magKrwi;
	private boolean ciosKrytyczny;
	private boolean magnetyzm;
	private boolean silaZywiolow;
	private boolean polnocnyBarbarzynca;
	private boolean rozprucie;
	
	private BukkitTask manaRegTask;
	private BukkitTask magnetyzmTask;
	
	public RpgSkills(RpgPlayer rpg) {
		this.rpg = rpg;
	}
	
	public RpgSkills(RpgPlayer rpg, ResultSet set) throws SQLException {
		this.rpg = rpg;
		set.first();
		
		this.manaReg = set.getBoolean("player_skills.manaReg");
		this.unlimitedArrows = set.getBoolean("player_skills.unlimitArr");
		this.hungerless = set.getBoolean("player_skills.foodless");
		this.slugaBeliara = set.getBoolean("player_skills.slugaBeliara");
		this.magKrwi = set.getBoolean("player_skills.magKrwi");
		this.ciosKrytyczny = set.getBoolean("player_skills.ciosKrytyczny");
		this.magnetyzm = set.getBoolean("player_skills.magnetyzm");
		this.silaZywiolow = set.getBoolean("player_skills.silaZywiolow");
		this.polnocnyBarbarzynca = set.getBoolean("player_skills.polnocnyBarbarzynca");
		this.rozprucie = set.getBoolean("player_skills.rozprucie");
	
		if(this.manaReg)
			createManaRegTask();
		if(this.magnetyzm)
			createMagnetyzmTask();
	}
	
	public RpgSkills(RpgPlayer rpg, YamlConfiguration fYml) {
		this.rpg = rpg;
		
		if(fYml.contains("manaReg")) {
			this.manaReg = fYml.getBoolean("manaReg");
		}
		if(fYml.contains("unlimitArr")) {
			this.unlimitedArrows = fYml.getBoolean("unlimitArr");
		} 
		if(fYml.contains("foodless")) {
			this.hungerless = fYml.getBoolean("foodless");
		} 
		if(fYml.contains("slugaBeliara")) {
			this.slugaBeliara = fYml.getBoolean("slugaBeliara");
		}
		if(fYml.contains("magKrwi")) {
			this.magKrwi = fYml.getBoolean("magKrwi");
		}
		if(fYml.contains("ciosKrytyczny")) {
			this.ciosKrytyczny = fYml.getBoolean("ciosKrytyczny");
		} 
		if(fYml.contains("magnetyzm")) {
			this.magnetyzm = fYml.getBoolean("magnetyzm");
		} 
		if(fYml.contains("silaZywiolow")) {
			this.silaZywiolow = fYml.getBoolean("silaZywiolow");
		}
		if(fYml.contains("polnocnyBarbarzynca")) {
			this.polnocnyBarbarzynca = fYml.getBoolean("polnocnyBarbarzynca");
		}
		if(fYml.contains("rozprucie")) {
			this.rozprucie = fYml.getBoolean("rozprucie");
		}
		
		if(this.manaReg)
			createManaRegTask();
		if(this.magnetyzm)
			createMagnetyzmTask();
	}
	
	//TODO
	public void createManaRegTask() {
		if(!manaReg)
			return;
		RpgStats stats = rpg.getStats();
		this.manaRegTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), ()->{
			
			if(stats.getPresentMana() != stats.getFinalMana()) {
				int tmp = (int)(stats.getFinalMana() * 0.0125);
				if(tmp == 0) tmp = 1;
				stats.addPresentManaSmart(tmp);
//				RpgScoreBoard.createScore(this.p);
			}
			
		}, 0, 10);
	}
	
	public void createMagnetyzmTask() {
		if(!magnetyzm)
			return;
		Player p = rpg.getPlayer();
		this.magnetyzmTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), ()->{
			List<Entity> list = p.getNearbyEntities(10, 10, 10);
			for(Entity e : list) {
				if(!(e instanceof Item))
					continue;
				Vector vec = new Vector(p.getLocation().getX() - e.getLocation().getX(),
						p.getLocation().getY() - e.getLocation().getY(),
						p.getLocation().getZ() - e.getLocation().getZ());
				vec.normalize().multiply(0.15);
				e.setVelocity(vec);
			}
		}, 0, 5);
	}
	
	public void endTasks() {
		if(manaRegTask != null && !manaRegTask.isCancelled())
			manaRegTask.cancel();
		if(magnetyzmTask != null && !magnetyzmTask.isCancelled())
			magnetyzmTask.cancel();
	}

	public RpgPlayer getRpg() {
		return rpg;
	}

	public boolean hasHungerless() {
		return hungerless;
	}

	public void setHungerless(boolean hungerless) {
		this.hungerless = hungerless;
	}

	public boolean hasUnlimitedArrows() {
		return unlimitedArrows;
	}

	public void setUnlimitedArrows(boolean unlimitedArrows) {
		this.unlimitedArrows = unlimitedArrows;
	}

	public boolean hasManaReg() {
		return manaReg;
	}

	public void setManaReg(boolean manaReg) {
		this.manaReg = manaReg;
	}

	public boolean hasSlugaBeliara() {
		return slugaBeliara;
	}

	public void setSlugaBeliara(boolean slugaBeliara) {
		this.slugaBeliara = slugaBeliara;
	}

	public boolean hasMagKrwi() {
		return magKrwi;
	}

	public void setMagKrwi(boolean magKrwi) {
		this.magKrwi = magKrwi;
	}

	public boolean hasCiosKrytyczny() {
		return ciosKrytyczny;
	}

	public void setCiosKrytyczny(boolean ciosKrytyczny) {
		this.ciosKrytyczny = ciosKrytyczny;
	}

	public boolean hasMagnetyzm() {
		return magnetyzm;
	}

	public void setMagnetyzm(boolean magnetyzm) {
		this.magnetyzm = magnetyzm;
	}

	public boolean hasSilaZywiolow() {
		return silaZywiolow;
	}

	public void setSilaZywiolow(boolean silaZywiolow) {
		this.silaZywiolow = silaZywiolow;
	}

	public boolean hasPolnocnyBarbarzynca() {
		return polnocnyBarbarzynca;
	}

	public void setPolnocnyBarbarzynca(boolean polnocnyBarbarzynca) {
		this.polnocnyBarbarzynca = polnocnyBarbarzynca;
	}

	public boolean hasRozprucie() {
		return rozprucie;
	}

	public void setRozprucie(boolean rozprucie) {
		this.rozprucie = rozprucie;
	}
	
}