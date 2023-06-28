package me.Vark123.EpicRPG.Reputation;

public enum ReputationLevels {

	NIEUFNY("Nieufny", 300, 0),
	NEUTRALNY("Neutralny", 1000, 1),
	PRZYJAZNY("Przyjazny", 2500, 2),
	UHONOROWANY("Honorowany", 5000, 3),
	CZCZONY("Czczony", 10000, 4),
	PODNIOSLY("Podniosly", 20000, 5),
	DOSKONALY("Doskonaly", 0, 6);
	
	private String name;
	private int amount;
	private int id;

	ReputationLevels(String name, int amount, int id) {
		this.name = name;
		this.amount = amount;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public int getAmount() {
		return amount;
	}

	public int getId() {
		return id;
	}
	
	public static ReputationLevels getReputationLevelById(int id) {
		switch(id) {
			case 0:
				return NIEUFNY;
			case 1:
				return NEUTRALNY;
			case 2:
				return PRZYJAZNY;
			case 3:
				return UHONOROWANY;
			case 4:
				return CZCZONY;
			case 5:
				return PODNIOSLY;
			case 6:
				return DOSKONALY;
			default:
				return null;
		}
	}
	
}
