package liqw.occasional_flashbang;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;

public class FlashbangManager {
    private static long startTime = 0;
    private static int durationMs = 0;

    public static void trigger(int duration, Minecraft client) {
        startTime = Util.getMillis();
        durationMs = duration;

        if (client.player != null) {

        }

        client.player.playSound(OccasionalFlashbangClient.FLASHBANG_SOUND, 1.0f, 1.0f);
    }

    public static float getOpacity() {
        if (startTime == 0)
            return 0;

        long elapsed = Util.getMillis() - startTime;
        if (elapsed >= durationMs) {
            startTime = 0;
            return 0;
        }

        return 1.0f - ((float) elapsed / durationMs);
    }
}
