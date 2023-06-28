package me.Vark123.EpicRPG.Reputation;

public class BaseReputation {
	
	private String name;
	private String display;
	
	public BaseReputation(String name, String display) {
		this.name = name;
		this.display = display;
	}

	public String getName() {
		return name;
	}

	public String getDisplay() {
		return display;
	}

}
