package liqw.occasional_flashbang;

import java.util.Random;

import liqw.occasional_flashbang.config.FlashbangConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
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
	private static final Identifier FLASHBANG_IDENTIFIER = Identifier.fromNamespaceAndPath(OccasionalFlashbang.MOD_ID,
			"flashbang");

	public static final SoundEvent FLASHBANG_SOUND = SoundEvent
			.createVariableRangeEvent(FLASHBANG_IDENTIFIER);

	@Override
	public void onInitializeClient() {
		AutoConfig.register(FlashbangConfig.class, GsonConfigSerializer::new);

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> lastHealth = -1f);

		Registry.register(BuiltInRegistries.SOUND_EVENT,
				FLASHBANG_IDENTIFIER,
				FLASHBANG_SOUND);

		FlashbangConfig config = AutoConfig.getConfigHolder(FlashbangConfig.class).getConfig();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {

			OccasionalFlashbang.LOGGER.debug("{} {} {}", config.enabled, config.chance, config.damage);

			if (client.player == null || client.isPaused() || !config.enabled)
				return;

			float currentHealth = client.player.getHealth();
			if (lastHealth == -1f) {
				lastHealth = currentHealth;
			}
			float damageTaken = lastHealth - currentHealth;

			boolean shouldTriggerOnChance = config.chance > 0
					&& RANDOM.nextInt(config.chance * 20) == 0;
			boolean shouldTriggerOnDamage = config.damage > 0 && damageTaken >= config.damage;

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