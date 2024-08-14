package me.Vark123.EpicRPG.WildHuntEvents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Files.FileOperations;
import me.Vark123.EpicRPG.Players.PlayerManager;

@Getter
public final class WHEManager {

	private static final WHEManager inst = new WHEManager();
	
	private final Random rand = new Random();
	
	private int minPlayersOnline;
	private int minPlayerLevel;
	private int spawnInterval;
	private int waveInterval;
	
	private List<WHEArea> areas = new ArrayList<>();
	private WHEArea pickedArea;
	
	private WHEManager() {
		
	}
	
	public static final WHEManager get() {
		return inst;
	}
	
	public void setup() {
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(FileOperations.getEvents());
		
		minPlayersOnline = fYml.getInt("min-players", 1);
		minPlayerLevel = fYml.getInt("min-level", 80);
		spawnInterval = fYml.getInt("spawn-interval", 4);
		waveInterval = fYml.getInt("wave-interval", 90);
		
		ConfigurationSection areasSection = fYml.getConfigurationSection("areas");
		areasSection.getKeys(false).stream()
			.filter(areasSection::isConfigurationSection)
			.map(areasSection::getConfigurationSection)
			.forEach(areaSection -> {
				Map<String, Collection<String>> messages = new LinkedHashMap<>();
				ConfigurationSection messageSection = areaSection.getConfigurationSection("messages");
				messageSection.getKeys(false).stream()
					.filter(messageSection::isList)
					.forEach(_id -> {
						List<String> message = messageSection.getStringList(_id)
								.stream()
								.map(line -> ChatColor.translateAlternateColorCodes('&', line))
								.collect(Collectors.toList());
						messages.put(_id, message);
					});
				
				List<AbstractLocation> locations = new ArrayList<>();
				ConfigurationSection locationSection = areaSection.getConfigurationSection("locations");
				String world = locationSection.getString("world", "F_RPG");
				ConfigurationSection coordsSection = locationSection.getConfigurationSection("coords");
				coordsSection.getKeys(false).stream()
					.filter(coordsSection::isConfigurationSection)
					.map(coordsSection::getConfigurationSection)
					.forEach(coordSection -> {
						AbstractLocation loc = new AbstractLocation(world, 
								coordSection.getDouble("x"),
								coordSection.getDouble("y"),
								coordSection.getDouble("z"));
						locations.add(loc);
					});
				
				TreeMap<Integer, WHEWave> waves = new TreeMap<>();
				ConfigurationSection wavesSection = areaSection.getConfigurationSection("waves");
				wavesSection.getKeys(false).stream()
					.filter(wavesSection::isConfigurationSection)
					.filter(key -> !key.contains("."))
					.filter(StringUtils::isNumeric)
					.map(Integer::parseInt)
					.forEach(number -> {
						Map<String, Integer> mobs = new LinkedHashMap<>();
						ConfigurationSection mobSection = wavesSection.getConfigurationSection(number+".mobs");
						mobSection.getKeys(false).stream()
							.filter(mobSection::isInt)
							.forEach(mob -> mobs.put(mob, mobSection.getInt(mob)));
						
						WHEWave wave = new WHEWave(mobs);
						waves.put(number, wave);
					});
				
				WHEArea area = new WHEArea(messages, locations, waves);
				areas.add(area);
			});
	}
	
	public void firstAnnouncement() {
		WHEArea area = getPickedArea();
		if(area == null)
			return;
		
		Collection<String> message = area.getMessages().getOrDefault("first", new LinkedList<>());
		Bukkit.getOnlinePlayers().forEach(p -> {
			p.playSound(p, Sound.ENTITY_WITHER_AMBIENT, 1, .6f);
			message.forEach(p::sendMessage);
		});
	}
	
	public void secondAnnouncement() {
		WHEArea area = getPickedArea();
		if(area == null)
			return;
		
		Collection<String> message = area.getMessages().getOrDefault("second", new LinkedList<>());
		Bukkit.getOnlinePlayers().forEach(p -> {
			p.playSound(p, Sound.ENTITY_WITHER_AMBIENT, 1.2f, .5f);
			p.spawnParticle(Particle.END_ROD, p.getLocation().clone().add(0,1,0), 12,
					.4, 1, .4, .12);
			message.forEach(p::sendMessage);
		});
	}
	
	public void thirdAnnouncement() {
		WHEArea area = getPickedArea();
		if(area == null)
			return;
		
		long count = Bukkit.getOnlinePlayers()
				.stream()
				.map(PlayerManager.getInstance()::getRpgPlayer)
				.map(rpg -> rpg.getInfo().getLevel())
				.filter(lvl -> lvl >= minPlayerLevel)
				.count();

		if(count < minPlayersOnline) {
			cancelAnnouncement();
			return;
		}
		
		pickedArea = null;
		
		Collection<String> message = area.getMessages().getOrDefault("third", new LinkedList<>());
		Bukkit.getOnlinePlayers().forEach(p -> {
			p.playSound(p, Sound.ENTITY_WITHER_AMBIENT, 1.5f, 1.2f);
			p.spawnParticle(Particle.END_ROD, p.getLocation().clone().add(0,1,0), 25,
					.4, 1, .4, .25);
			message.forEach(p::sendMessage);
		});
		
		new BukkitRunnable() {
			@Override
			public void run() {
				Thread waveThread = new Thread(new Runnable() {
					@Override
					public void run() {
						for(WHEWave wave : area.getWaves().values()) {
							Thread mobThread = new Thread(new Runnable() {
								@Override
								public void run() {
									for(String mob : wave.getMobs().keySet()) {
										for(int i = 0; i < wave.getMobs().get(mob); ++i) {
											AbstractLocation loc = area.getLocations()
													.get(rand.nextInt(area.getLocations().size()));
											new BukkitRunnable() {
												@Override
												public void run() {
													MythicBukkit.inst().getMobManager().spawnMob(mob, loc);
												}
											}.runTask(Main.getInstance());
											;
											try {
												Thread.sleep(spawnInterval*1000);
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
										}
									}
								}
							});
							
							mobThread.start();
							try {
								mobThread.join();
								Thread.sleep(waveInterval*1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				});
				
				waveThread.start();
			}
		}.runTask(Main.getInstance());
		
	}
	
	public void cancelAnnouncement() {
		WHEArea area = getPickedArea();
		if(area == null)
			return;
		
		pickedArea = null;
		
		Collection<String> message = area.getMessages().getOrDefault("cancel", new LinkedList<>());
		Bukkit.getOnlinePlayers().forEach(p -> {
			p.playSound(p, Sound.ENTITY_WITHER_DEATH, 1.4f, .8f);
			p.spawnParticle(Particle.SNOWFLAKE, p.getLocation().clone().add(0,1,0), 12,
					.4, 1, .4, .12);
			message.forEach(p::sendMessage);
		});
	}
	
	private WHEArea getPickedArea() {
		if(pickedArea != null)
			return pickedArea;
		
		WHEArea pickedOne = areas.get(rand.nextInt(areas.size()));
		this.pickedArea = pickedOne;
		return pickedOne;
	}
	
}
