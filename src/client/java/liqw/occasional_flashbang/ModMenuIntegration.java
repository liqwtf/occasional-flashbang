package liqw.occasional_flashbang;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import liqw.occasional_flashbang.config.FlashbangConfig;
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

            general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enabled"), FlashbangConfig.enabled)
                    .setTooltip(Component.literal("Completely enable or disable the mod"))
                    .setDefaultValue(true).setSaveConsumer(value -> FlashbangConfig.enabled = value).build());

            general.addEntry(
                    entryBuilder
                            .startIntField(Component.literal("Flashbang Chance"), FlashbangConfig.chance)
                            .setTooltip(Component.literal("The chance for a flashbang to randomly occur"),
                                    Component.literal("Set to -1 or 0 to disable"))
                            .setDefaultValue(1000)
                            .setMin(-1)
                            .setSaveConsumer(value -> FlashbangConfig.chance = value)
                            .build());

            general.addEntry(entryBuilder.startIntField(Component.literal("Damage Threshold"), FlashbangConfig.damage)
                    .setTooltip(Component.literal("The minimum damage required to trigger a flashbang"),
                            Component.literal("Set to -1 or 0 to disable"))
                    .setDefaultValue(8)
                    .setMin(-1)
                    .setMax(20)
                    .setSaveConsumer(value -> FlashbangConfig.damage = value)
                    .build());

            return builder.build();
        };
    }
}