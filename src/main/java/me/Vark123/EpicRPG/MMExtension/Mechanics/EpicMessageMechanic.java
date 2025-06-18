package me.Vark123.EpicRPG.MMExtension.Mechanics;

import java.io.File;
import java.util.Collection;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.placeholders.PlaceholderString;
import io.lumine.mythic.core.logging.MythicLogger;
import io.lumine.mythic.core.skills.SkillAudience;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;
import io.lumine.mythic.core.skills.placeholders.PlaceholderMeta;

public class EpicMessageMechanic extends SkillMechanic implements ITargetedEntitySkill {

	private SkillAudience audience;
	protected PlaceholderString message;

	public EpicMessageMechanic(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
		super(manager, file, line, mlc);

		this.target_creative = true;
		this.message = mlc.getPlaceholderString(new String[] { "message", "msg", "m" }, null, new String[0]);
		if (this.message == null) {
			MythicLogger.errorMechanicConfig(this, mlc, "The 'message' attribute is required.");
			this.message = PlaceholderString.of("INCORRECTLY CONFIGURED. SEE CONSOLE ON RELOAD.");
			return;
		}
		MythicLogger.debug(MythicLogger.DebugLevel.MECHANIC, "Loaded message skill with message {0}",
				this.message.toString());
		this.audience = mlc.getAudience("audience", null);
	}

	@Override
	public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (this.audience != null) {
			String message = this.message.get((PlaceholderMeta) data, target);
			Collection<AbstractPlayer> audience = this.audience.get(data, target);
			for (AbstractEntity abstractEntity : audience) {
				if (!abstractEntity.isPlayer())
					continue;
				MythicLogger.debug(MythicLogger.DebugLevel.MECHANIC, "Sending message {0} to ", this.message,
						abstractEntity.getName());
				abstractEntity.asPlayer().sendMessage(message);
			}
			return SkillResult.SUCCESS;
		}
		if (target.isPlayer()) {
			MythicLogger.debug(MythicLogger.DebugLevel.MECHANIC, "Sending message {0}", this.message);
			target.asPlayer().sendMessage(this.message.get((PlaceholderMeta) data, target));
			return SkillResult.SUCCESS;
		}
		MythicLogger.debug(MythicLogger.DebugLevel.MECHANIC, "Failed to send message to non-player", new Object[0]);
		return SkillResult.INVALID_TARGET;
	}

}
