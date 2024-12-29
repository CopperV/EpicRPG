package me.Vark123.EpicRPG.RuneSystem;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune.RuneLockerTypes;
import me.Vark123.EpicRPG.RuneSystem.Events.RuneCastCostCalcEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.RuneCastGlobalCdCalcEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.RuneCastRuneCdCalcEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.RuneUseEvent;
import me.Vark123.EpicRPG.RuneSystem.Runes.Ogien.OgnistaStrzala;

@Getter
public final class RuneManager {

	private static final RuneManager instance = new RuneManager();
	
	public final long RUNE_GLOBAL_CD_DEFAULT_VALUE = 1000;
	
	private final Map<UUID, Date> globalCd;
	private final Map<UUID, Map<String, Date>> runeCd;
	
	private RuneManager() {
		globalCd = new ConcurrentHashMap<>();
		runeCd = new ConcurrentHashMap<>();
	}
	
	public static final RuneManager get() {
		return instance;
	}
	
	public boolean tryCastRune(RpgPlayer rpg, ItemStack itRune) {
		Player p = rpg.getPlayer();
		if(hasGlobalCd(p))
			return false;
		
		EpicRune rune = new EpicRune(itRune);
		if(rune.isClassRequired()) {
			String proffesion = ChatColor.stripColor(rpg.getInfo().getProffesion());
			if(!proffesion.equalsIgnoreCase(rune.getKlasa())) {
				p.sendMessage("§7[§bEpicRPG§7] §cTylko "+rune.getKlasa()+" §cmoze uzyc tej runy!");
				return false;
			}
		}
		
		if(rpg.getStats().getKrag() < rune.getKrag()) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cMusisz posiadac §7§o"+rune.getKrag()+" §ckrag magii, by uzyc "+rune.getName());
			return false;
		}
		
		if(!rune.canUseInPlayerLocation(p)) {
			return false;
		}
		
		if(!isRegenTimePassed(p, rune)) {
			return false;
		}
		
		RuneCastCostCalcEvent calcCostEvent = new RuneCastCostCalcEvent(rpg, rune);
		Bukkit.getPluginManager().callEvent(calcCostEvent);
		if(calcCostEvent.isCancelled())
			return false;
		
		int finalCost = calcCostEvent.getFinalCost();
		if(rune.isHpInsteadMana()) {
			if(!hasEnoughHp(rpg, finalCost))
				return false;
		} else {
			if(!hasEnoughMana(rpg, finalCost))
				return false;
		}
		
		ACastableRune castableRune = getRune(rpg, rune, itRune);
		
		RpgModifiers modifiers = rpg.getModifiers();
		for(RuneLockerTypes locker : castableRune.lockers) {
			if(modifiers.hasActiveLocker(locker)) {
				p.sendMessage(Main.getInstance().getPrefix()+" "+locker.getMessage());
				return false;
			}
		}
		
		createGlobalCooldown(rpg, rune);
		
		if(!isSilaZywiolowEffect(rpg)) {
			if(rune.isHpInsteadMana())
				spendHp(rpg, finalCost);
			else
				spendMana(rpg, finalCost);
			createRegenTime(rpg, rune);
		} else {
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0.5f);
			p.getWorld().spawnParticle(Particle.NAUTILUS, p.getLocation().add(0, 1, 0), 20, 0.75, 0.5, 0.75, 0.2);
		}
		
		RuneUseEvent useEvent = new RuneUseEvent(rpg, rune);
		Bukkit.getPluginManager().callEvent(useEvent);
		
		rpg.displayUpdate();
		castableRune.castSpell();

		//TODO
		//Implementacja Lodowego Bloku
		rpg.getPlayer().sendMessage("Do implementacji - Lodowy Blok");
		
		return true;
	}
	
	public boolean isRegenTimePassed(Player p, EpicRune rune) {
		UUID uid = p.getUniqueId();
		String mythicType = rune.getMythicType();
		if(!runeCd.containsKey(uid) 
				|| !runeCd.get(uid).containsKey(mythicType))
			return true;
		
		Date regenDate = runeCd.get(uid).get(mythicType);
		Date now = new Date();
		if(now.before(regenDate)) {
			double cooldownInSeconds = (double)(regenDate.getTime() - now.getTime())/1000.;
			if(cooldownInSeconds < 2) {
				p.playSound(p, Sound.ENTITY_ZOMBIFIED_PIGLIN_AMBIENT, 2, .7f);
				p.spawnParticle(Particle.SMOKE, p.getLocation().add(0,1,0), 6, .4f, .4f, .4f, .03f);
			}
			
			p.sendMessage("§7[§6EpicRPG§7] §cRuny "+rune.getName()+" §cbedziesz mogl uzyc za §7"+String.format("%.2f", cooldownInSeconds)+" §csekund");
			return false;
		}
		return true;
	}
	
	public void createRegenTime(RpgPlayer rpgPlayer, EpicRune rune) {
		Player p = rpgPlayer.getPlayer();
		UUID uid = p.getUniqueId();
		if(!runeCd.containsKey(uid)) {
			runeCd.put(uid, new ConcurrentHashMap<>());
		}
		
		RuneCastRuneCdCalcEvent event = new RuneCastRuneCdCalcEvent(rpgPlayer, rune);
		Bukkit.getPluginManager().callEvent(event);

		Date now = new Date();
		long cd = event.getFinalCd();
		
		Date cdDate = new Date(now.getTime() + cd);
		runeCd.get(uid).put(rune.getMythicType(), cdDate);
	}

	public boolean hasGlobalCd(Player p) {
		return globalCd.containsKey(p.getUniqueId()) && globalCd.get(p.getUniqueId()).after(new Date());
	}
	
	public void createGlobalCooldown(RpgPlayer rpgPlayer, EpicRune rune) {
		RuneCastGlobalCdCalcEvent event = new RuneCastGlobalCdCalcEvent(rpgPlayer, rune);
		Bukkit.getPluginManager().callEvent(event);
		
		Date now = new Date();
		long globalCd = event.getFinalGlobalCd();
		
		Date cd = new Date(now.getTime() + globalCd);
		this.globalCd.put(rpgPlayer.getPlayer().getUniqueId(), cd);
	}
	
	public boolean hasEnoughHp(RpgPlayer rpg, int cost) {
		Player p = rpg.getPlayer();
		if(!rpg.getSkills().hasMagKrwi()) {
			p.sendMessage("§7[§6EpicRPG§7] §cMusisz byc magiem krwi, by moc uzyc tej runy!");
			return false;
		}
		
		double presentHealth = p.getHealth();
		if(cost > (presentHealth + 1))
			return true;
		
		p.sendMessage("§7[§6EpicRPG§7] §cNie masz wystarczajaco zycia by uzyc tej runy!");
		return false;
		
	}
	
	public void spendHp(RpgPlayer rpg, int cost) {
		//TODO
		//Zadawanie sobie obrazen
		rpg.getPlayer().sendMessage("Do implementacji - pobieranie zycia");
	}
	
	public boolean hasEnoughMana(RpgPlayer rpg, int cost) {
		Player p = rpg.getPlayer();
		RpgStats stats = rpg.getStats();
		
		if(stats.getPresentMana() >= cost)
			return true;
		
		if(rpg.getSkills().hasMagKrwi()) {
			if(cost > stats.getFinalMana()) {
				p.sendMessage("§7[§6EpicRPG§7] §cMasz za malo maksymalnej many, by uzyc tej runy jako Mag Krwi!");
				return false;
			}
			
			cost = (int) Math.ceil(cost*0.25);
			double presentHealth = p.getHealth();
			if(cost > (presentHealth + 1))
				return true;
			
			p.sendMessage("§7[§6EpicRPG§7] §cNie masz ani many, ani zycia by uzyc tej runy!");
			return false;
		}

		p.sendMessage("§7[§6EpicRPG§7] §cMasz za malo many by uzyc tej runy!");
		return false;
	}
	
	public void spendMana(RpgPlayer rpg, int cost) {
		RpgStats stats = rpg.getStats();
		if(stats.getPresentMana() >= cost) {
			stats.removePresentManaSmart(cost);
			return;
		}

		cost = (int) Math.ceil(cost*0.25);
		//TODO
		//Zadawanie sobie obrazen
		rpg.getPlayer().sendMessage("Do implementacji - pobieranie zycia");
	}
	
	public boolean isSilaZywiolowEffect(RpgPlayer rpgPlayer) {
		if(!rpgPlayer.getSkills().hasSilaZywiolow())
			return false;
		
		Random rand = new Random();
		return (rand.nextInt(100) < 5);
	}
	
	public ACastableRune getRune(RpgPlayer rpgPlayer, EpicRune rune, ItemStack itRune) {
		switch(itRune.getType()) {
			case MUSIC_DISC_11:
				switch(rune.getMythicType()) {
					
				}
				break;
			case MUSIC_DISC_13:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_5:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_BLOCKS:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_CAT:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_CHIRP:
				switch(rune.getMythicType()) {
				}
				
				break;
			case MUSIC_DISC_CREATOR:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_CREATOR_MUSIC_BOX:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_FAR:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_MALL:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_MELLOHI:
				switch(rune.getMythicType()) {
					case "OgnistaStrzala":			return new OgnistaStrzala(rpgPlayer, rune);
				}
				break;
			case MUSIC_DISC_OTHERSIDE:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_PIGSTEP:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_PRECIPICE:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_RELIC:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_STAL:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_STRAD:
				switch(rune.getMythicType()) {
				
				}
				break;
			case MUSIC_DISC_WAIT:
				switch(rune.getMythicType()) {
					
				}
				break;
			case MUSIC_DISC_WARD:
				switch(rune.getMythicType()) {
				
				}
				break;
		default:
			return null;
		}
		return null;
	}
}
