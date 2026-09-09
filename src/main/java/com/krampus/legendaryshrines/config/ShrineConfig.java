package com.krampus.legendaryshrines.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ShrineConfig {

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue BIND_RADIUS;
    public static final ForgeConfigSpec.IntValue BREAK_DISTANCE;
    public static final ForgeConfigSpec.BooleanValue BIND_ON_USE;
    public static final ForgeConfigSpec.IntValue RESPAWN_COOLDOWN;
    public static final ForgeConfigSpec.BooleanValue OVERRIDE_VANILLA_RESPAWN;
    public static final ForgeConfigSpec.BooleanValue SOUNDS_ENABLED;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("binding");

        BIND_RADIUS = builder
                .comment("How close a player must get to a shrine to bind to it, in blocks.")
                .defineInRange("bindRadius", 16, 1, 128);

        BREAK_DISTANCE = builder
                .comment("How far a player can travel from their shrine before the link breaks, in blocks.",
                        "Set to 0 to make links never break by distance.")
                .defineInRange("breakDistance", 1000, 0, 30000000);

        BIND_ON_USE = builder
                .comment("Require players to right click a shrine to bind to it instead of binding by proximity.",
                        "bindRadius is ignored while this is enabled.")
                .define("bindOnUse", false);

        builder.pop();
        builder.push("respawn");

        RESPAWN_COOLDOWN = builder
                .comment("Seconds before a player may respawn at a shrine again after using one.",
                        "Set to 0 to disable the cooldown.")
                .defineInRange("cooldownSeconds", 0, 0, 86400);

        OVERRIDE_VANILLA_RESPAWN = builder
                .comment("Make the vanilla Respawn button send bound players to their shrine.",
                        "The extra Respawn at the Shrine button is hidden, and the shrine takes priority",
                        "over beds and world spawn. Players without a link, or on cooldown, respawn normally.")
                .define("overrideVanillaRespawn", false);

        builder.pop();
        builder.push("effects");

        SOUNDS_ENABLED = builder
                .comment("Play shrine sounds on binding and on losing a link.")
                .define("soundsEnabled", true);

        builder.pop();

        SPEC = builder.build();
    }
}
