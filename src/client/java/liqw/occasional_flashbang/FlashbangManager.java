package liqw.occasional_flashbang;

import liqw.occasional_flashbang.config.FlashbangConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;

public class FlashbangManager {
    private static long startTime = 0;

    private static int getDuration() {
        FlashbangConfig config = AutoConfig.getConfigHolder(FlashbangConfig.class).getConfig();

        if (config.lobotomy) {
            return 5000;
        }

        return 3000;
    }

    public static void trigger(Minecraft client) {
        FlashbangConfig config = AutoConfig.getConfigHolder(FlashbangConfig.class).getConfig();
        startTime = Util.getMillis();

        if (client.player != null) {
            if (config.lobotomy) {
                client.player.playSound(OccasionalFlashbangClient.LOBOTOMY_SOUND, 1.0f, 1.0f);
                return;
            }

            client.player.playSound(OccasionalFlashbangClient.FLASHBANG_SOUND, 1.0f, 1.0f);
        }
    }

    public static float getOpacity() {
        if (startTime == 0) return 0;

        long elapsed = Util.getMillis() - startTime;
        if (elapsed >= getDuration()) {
            startTime = 0;
            return 0;
        }

        return 1.0f - ((float) elapsed / getDuration());
    }
}
