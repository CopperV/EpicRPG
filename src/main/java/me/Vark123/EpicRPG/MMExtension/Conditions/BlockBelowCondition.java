package me.Vark123.EpicRPG.MMExtension.Conditions;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.ILocationCondition;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.logging.MythicLogger;
import io.lumine.mythic.core.skills.SkillCondition;

public class BlockBelowCondition extends SkillCondition implements ILocationCondition {

	private Set<Material> blocks = new HashSet<Material>();

	public BlockBelowCondition(String line, MythicLineConfig config) {
		super(line);
		String materials = config.getString(new String[] { "type", "material", "m", "block", "b" }, "STONE",
				new String[0]);
		for (String block : materials.split(",")) {
			try {
				Material m4 = Material.valueOf((String) block.toUpperCase());
				this.blocks.add(m4);
			} catch (Exception ex) {
				MythicLogger.errorConditionConfig(this, config, "'" + block + "' is not a valid Material");
			}
		}
	}

	@Override
	public boolean check(AbstractLocation l) {
		Material mat = BukkitAdapter.adapt(l.clone().add(0, -1, 0)).getBlock().getRelative(BlockFace.DOWN).getType();
		MythicLogger.debug(MythicLogger.DebugLevel.CONDITION, "Checking OnBlock: {0} vs {1}", mat.toString(),
				this.blocks.toString());
		return this.blocks.contains(mat);
	}

}
