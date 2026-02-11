package com.mod.rctvgc.prefs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public final class VgcPrefs {
    private static final String ROOT = "rcttrainersvgc";
    private static final String DOUBLES = "doublesEnabled";
    private static final String LEVEL_SET_ENABLED = "levelSetEnabled";
    private static final String LEVEL_SET = "levelSet";

    private VgcPrefs() {}

    private static CompoundTag root(Player p) {
        CompoundTag data = p.getPersistentData();
        if (!data.contains(ROOT, CompoundTag.TAG_COMPOUND)) {
            data.put(ROOT, new CompoundTag());
        }
        return data.getCompound(ROOT);
    }

    public static boolean doublesEnabled(Player p, boolean defaultValue) {
        CompoundTag r = root(p);
        return r.contains(DOUBLES) ? r.getBoolean(DOUBLES) : defaultValue;
    }

    public static void setDoublesEnabled(Player p, boolean value) {
        root(p).putBoolean(DOUBLES, value);
    }

    public static boolean levelSetEnabled(Player p, boolean defaultValue) {
        CompoundTag r = root(p);
        return r.contains(LEVEL_SET_ENABLED) ? r.getBoolean(LEVEL_SET_ENABLED) : defaultValue;
    }

    public static void setLevelSetEnabled(Player p, boolean value) {
        root(p).putBoolean(LEVEL_SET_ENABLED, value);
    }

    public static int levelSet(Player p, int defaultValue) {
        CompoundTag r = root(p);
        return r.contains(LEVEL_SET) ? r.getInt(LEVEL_SET) : defaultValue;
    }

    public static void setLevelSet(Player p, int value) {
        root(p).putInt(LEVEL_SET, value);
    }
}