package liqw.occasional_flashbang;

import java.util.Random;

import liqw.occasional_flashbang.config.FlashbangConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
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
	private static float lastHealth = -1f;

	public static final SoundEvent FLASHBANG_SOUND = SoundEvent
			.createVariableRangeEvent(Identifier.fromNamespaceAndPath(OccasionalFlashbang.MOD_ID, "flashbang"));

	@Override
	public void onInitializeClient() {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> lastHealth = -1f);

		Registry.register(BuiltInRegistries.SOUND_EVENT,
				Identifier.fromNamespaceAndPath(OccasionalFlashbang.MOD_ID, "flashbang"),
				FLASHBANG_SOUND);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null || client.isPaused() || !FlashbangConfig.enabled)
				return;

			float currentHealth = client.player.getHealth();
			if (lastHealth == -1f) {
				lastHealth = currentHealth;
			}
			float damageTaken = lastHealth - currentHealth;

			boolean shouldTriggerOnChance = FlashbangConfig.chance > 0
					&& RANDOM.nextInt(FlashbangConfig.chance * 20) == 0;
			boolean shouldTriggerOnDamage = FlashbangConfig.damage > 0 && damageTaken >= FlashbangConfig.damage;

			if (shouldTriggerOnChance || shouldTriggerOnDamage) {
				FlashbangManager.trigger(client);
			}

			lastHealth = currentHealth;
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