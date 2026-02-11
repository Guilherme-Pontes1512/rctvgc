package com.mod.rctvgc;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class Config {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue ENABLE_DOUBLES;
    public static final ModConfigSpec.BooleanValue ENABLE_LEVEL_SET;
    public static final ModConfigSpec.IntValue LEVEL_SET;

    static {
        ModConfigSpec.Builder b = new ModConfigSpec.Builder();

        b.push("vgc");

        ENABLE_DOUBLES = b
                .comment("Force RCT trainer battles to be Doubles.")
                .define("enableDoubles", true);

        ENABLE_LEVEL_SET = b
                .comment("Enable flat level adjustment for RCT trainer battles.")
                .define("enableLevelSet", true);

        LEVEL_SET = b
                .comment("Level to use when enableLevelSet=true. Typical VGC is 50.")
                .defineInRange("levelSet", 50, 1, 100);

        b.pop();
        SPEC = b.build();
    }

    private Config() {}
}
