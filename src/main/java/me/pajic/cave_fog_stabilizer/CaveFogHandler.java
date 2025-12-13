package me.pajic.cave_fog_stabilizer;

import me.pajic.cave_fog_stabilizer.config.ConfigHolder;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

public class CaveFogHandler {

	private static float fogTransitionTimer = getFogTransitionTime();

	public static void handleCaveFog(Args args, ClientLevel level, Camera camera) {
		if (shouldStabilizeFog()) {
			int fogColorVariable = args.get(1);
			int skyColorVariable = args.get(2);
			float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks();
			BlockPos cameraPos = camera.blockPosition();
			int skylight = level.getBrightness(LightLayer.SKY, cameraPos);
			if (cameraPos.getY() < level.getSeaLevel() && skylight == 0) {
				if (fogTransitionTimer > 0) {
					fogTransitionTimer = Math.max(0, fogTransitionTimer - partialTick);
					setLerpedCaveFogColor(args, fogColorVariable, skyColorVariable);
				} else setDefaultCaveFogColor(args);
			} else if (fogTransitionTimer < getFogTransitionTime()) {
				fogTransitionTimer = Math.min(getFogTransitionTime(), fogTransitionTimer + partialTick);
				setLerpedCaveFogColor(args, fogColorVariable, skyColorVariable);
			}
		}
	}

	private static void setLerpedCaveFogColor(Args args, int fogColorVariable, int skyColorVariable) {
		args.set(1, ARGB.srgbLerp(fogTransitionTimer / getFogTransitionTime(), getStabilizedCaveFogColor(), fogColorVariable));
		args.set(2, ARGB.srgbLerp(fogTransitionTimer / getFogTransitionTime(), getStabilizedCaveFogColor(), skyColorVariable));
	}

	private static void setDefaultCaveFogColor(Args args) {
		args.set(1, getStabilizedCaveFogColor());
		args.set(2, getStabilizedCaveFogColor());
	}

	private static boolean shouldStabilizeFog() {
		return !CFS.CONFIG_AVAILABLE || ConfigHolder.shouldStabilizeFog();
	}

	private static float getFogTransitionTime() {
		return CFS.CONFIG_AVAILABLE ? ConfigHolder.getFogTransitionTime() : 100;
	}

	private static int getStabilizedCaveFogColor() {
		return CFS.CONFIG_AVAILABLE ? ConfigHolder.getStabilizedCaveFogColor() : 0x000000;
	}
}
