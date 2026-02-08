package liqw.occasional_flashbang;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;

public class FlashbangManager {
    public static int DURATION = 3000;
    private static long startTime = 0;

    public static void trigger(Minecraft client) {
        startTime = Util.getMillis();

        if (client.player != null) {
            client.player.playSound(OccasionalFlashbangClient.FLASHBANG_SOUND, 1.0f, 1.0f);
        }
    }

    public static float getOpacity() {
        if (startTime == 0)
            return 0;

        long elapsed = Util.getMillis() - startTime;
        if (elapsed >= DURATION) {
            startTime = 0;
            return 0;
        }

        return 1.0f - ((float) elapsed / DURATION);
    }
}
