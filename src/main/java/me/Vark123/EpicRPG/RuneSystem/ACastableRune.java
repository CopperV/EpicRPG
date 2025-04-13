package me.Vark123.EpicRPG.RuneSystem;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import lombok.Getter;
import me.Vark123.EpicRPG.Players.RpgPlayer;

@Getter
public abstract class ACastableRune {

	protected EpicRune rune;
	protected RpgPlayer rpgPlayer;
	protected Player player;
	protected Location castLoc;
	protected World world;
	protected Collection<RuneLockerTypes> lockers;
	
	public ACastableRune(RpgPlayer rpgPlayer, EpicRune rune) {
		this(rpgPlayer, rune, new HashSet<>());
	}
	
	public ACastableRune(RpgPlayer rpgPlayer, EpicRune rune, Collection<RuneLockerTypes> lockers) {
		this.rune = rune;
		this.rpgPlayer = rpgPlayer;
		this.player = rpgPlayer.getPlayer();
		this.castLoc = player.getLocation();
		this.world = castLoc.getWorld();
		this.lockers = new HashSet<>(lockers);
	}
	
	public abstract void castSpell();

	public boolean casterInCastWorld() {
		return player.getWorld().getUID().equals(world.getUID());
	}
	
	public boolean entityInCastWorld(Entity entity) {
		return entity != null && !entity.isDead() 
				&& entity.getWorld().getUID().equals(world.getUID());
	}
	
	@Getter
	public static enum RuneLockerTypes {
		BUFF ("§cUzywasz obecnie innej runy modyfikujacej obrazenia"),
		PROWOKACJA ("§cEfekt prowokacji jest jeszcze aktywny"),
		EKSPLODUJACA_STRZALA ("§cObecnie posiadasz efekt innej eksplodujacej strzaly");
		
		private String message;
		private RuneLockerTypes(String message) {
			this.message = message;
		}
	}
}

