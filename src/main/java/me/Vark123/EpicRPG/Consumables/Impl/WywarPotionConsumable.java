package me.Vark123.EpicRPG.Consumables.Impl;

import org.bukkit.Color;

import me.Vark123.EpicRPG.Players.RpgPlayer;

public class WywarPotionConsumable extends DrinkConsumable {

	private int level;
	private IWywarGetter wywarGetter;
	
	public WywarPotionConsumable(IWywarGetter wywarGetter, IWywarSetter wywarSetter, int level, Color drinkColor) {
		super(rpg -> {
			wywarSetter.setWywarLevel(rpg, level);
		}, drinkColor);
		
		this.level = level;
		this.wywarGetter = wywarGetter;
	}
	
	
	
	@Override
	public boolean canConsume(RpgPlayer rpg) {
		if(!super.canConsume(rpg))
			return false;
		
		return wywarGetter.getWywarLevel(rpg) <= level;
	}



	public interface IWywarGetter {
		public int getWywarLevel(RpgPlayer rpg);
	}
	
	public interface IWywarSetter {
		public void setWywarLevel(RpgPlayer rpg, int level);
	}

}
