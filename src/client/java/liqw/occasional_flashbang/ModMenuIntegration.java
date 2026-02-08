package liqw.occasional_flashbang;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import liqw.occasional_flashbang.config.FlashbangConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.literal("Occasional Flashbang Config"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));

            ConfigHolder<FlashbangConfig> configHolder = AutoConfig.getConfigHolder(FlashbangConfig.class);
            FlashbangConfig config = configHolder.getConfig();

            general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enabled"), config.enabled)
                    .setTooltip(Component.literal("Completely enable or disable the mod"))
                    .setDefaultValue(true).setSaveConsumer(value -> {
                        config.enabled = value;
                        configHolder.save();
                    }).build());

            general.addEntry(
                    entryBuilder
                            .startIntField(Component.literal("Flashbang Chance"), config.chance)
                            .setTooltip(Component.literal("The chance for a flashbang to randomly occur"),
                                    Component.literal("Set to -1 or 0 to disable"))
                            .setDefaultValue(1000)
                            .setMin(-1)
                            .setSaveConsumer(value -> {
                                config.chance = value;
                                configHolder.save();
                            })
                            .build());

            general.addEntry(entryBuilder.startIntField(Component.literal("Damage Threshold"), config.damage)
                    .setTooltip(Component.literal("The minimum damage required to trigger a flashbang"),
                            Component.literal("Set to -1 or 0 to disable"))
                    .setDefaultValue(8)
                    .setMin(-1)
                    .setMax(20)
                    .setSaveConsumer(value -> {
                        config.damage = value;
                        configHolder.save();
                    })
                    .build());

            return builder.build();
        };
    }
}