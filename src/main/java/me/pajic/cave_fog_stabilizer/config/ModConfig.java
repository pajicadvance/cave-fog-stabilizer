package me.pajic.cave_fog_stabilizer.config;

import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.pajic.cave_fog_stabilizer.CFS;
import net.minecraft.resources.Identifier;

@Version(version = 1)
public class ModConfig extends Config {
    public ModConfig() {
        super(Identifier.fromNamespaceAndPath(CFS.MOD_ID, "config"));
    }

	public ValidatedBoolean stabilizeCaveFog = new ValidatedBoolean(true);
	public ValidatedInt standardCaveFogBrightness = new ValidatedInt(0, 100, 0);
	public ValidatedInt customCaveFogBrightness = new ValidatedInt(20, 100, 0);
	public ValidatedInt fogTransitionTime = new ValidatedInt(5, 10, 0);
}
