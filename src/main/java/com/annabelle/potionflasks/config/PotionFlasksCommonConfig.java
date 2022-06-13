package com.annabelle.potionflasks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PotionFlasksCommonConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> FLASK_MAX_FILL_LEVEL;

    static {
        BUILDER.push("Configs for Potion Flasks");

        FLASK_MAX_FILL_LEVEL = BUILDER.define("Flask max fill level",9);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
