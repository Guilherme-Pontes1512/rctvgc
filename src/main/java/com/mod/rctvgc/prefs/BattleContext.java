package com.mod.rctvgc.prefs;

import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public final class BattleContext {
    private static final ThreadLocal<UUID> CURRENT_PLAYER = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> FORCE_SINGLES = new ThreadLocal<>();

    private BattleContext() {}

    public static void set(ServerPlayer p, boolean forceSingles) {
        CURRENT_PLAYER.set(p.getUUID());
        FORCE_SINGLES.set(forceSingles);
    }

    public static UUID getPlayerId() { return CURRENT_PLAYER.get(); }

    public static boolean forceSingles() {
        Boolean v = FORCE_SINGLES.get();
        return v != null && v;
    }

    public static void clear() {
        CURRENT_PLAYER.remove();
        FORCE_SINGLES.remove();
    }
}