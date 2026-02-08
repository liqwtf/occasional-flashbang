package liqw.occasional_flashbang;

import java.util.Random;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class OccasionalFlashbangClient implements ClientModInitializer {
	private static final Random RANDOM = new Random();
	private static final int FLASH_DURATION = 3000;
	private static final int CHANCE = 20;

	public static final SoundEvent FLASHBANG_SOUND = SoundEvent
			.createVariableRangeEvent(Identifier.fromNamespaceAndPath(OccasionalFlashbang.MOD_ID, "flashbang"));

	@Override
	public void onInitializeClient() {
		Registry.register(BuiltInRegistries.SOUND_EVENT,
				Identifier.fromNamespaceAndPath(OccasionalFlashbang.MOD_ID, "flashbang"),
				FLASHBANG_SOUND);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null && RANDOM.nextInt(CHANCE * 20) == 0) {
				FlashbangManager.trigger(FLASH_DURATION, client);
			}
		});

		HudElementRegistry.attachElementBefore(VanillaHudElements.HOTBAR,
				Identifier.fromNamespaceAndPath(OccasionalFlashbang.MOD_ID, "flashbang"),
				OccasionalFlashbangClient::render);
	}

	private static void render(GuiGraphics graphics, DeltaTracker tickCounter) {
		float opacity = FlashbangManager.getOpacity();
		if (opacity <= 0)
			return;

		int alpha = (int) (opacity * 255);
		int whiteWithAlpha = (alpha << 24) | 0xFFFFFF;

		graphics.fill(0, 0, graphics.guiWidth(), graphics.guiHeight(), whiteWithAlpha);
	}

}