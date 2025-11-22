package me.pajic.modid.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.modid.ModTemplate;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * You don't need to add mixins to the mixin config, it's handled automatically by Fletching Table.
 * If the mixin is client sided, add the @MixinEnvironment(type = MixinEnvironment.Env.CLIENT) annotation.
 */
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(Minecraft.class)
public class ExampleClientMixin {

	@Inject(
			method = "onGameLoadFinished",
			at = @At("TAIL")
	)
	private void onGameLoadFinished(CallbackInfo ci) {
		ModTemplate.debugLog("Client finished loading!");
	}
}
