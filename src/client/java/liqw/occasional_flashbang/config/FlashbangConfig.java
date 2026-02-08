package liqw.occasional_flashbang.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "occasional-flashbang")
public class FlashbangConfig implements ConfigData {
    public boolean enabled = true;
    public int chance = 1000;
    public int damage = 8;

}
