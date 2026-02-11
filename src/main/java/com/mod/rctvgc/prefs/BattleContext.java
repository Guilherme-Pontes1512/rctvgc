package com.mod.rctvgc.prefs;

import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public final class BattleContext {
    private static final ThreadLocal<UUID> CURRENT_PLAYER = new ThreadLocal<>();

    private BattleContext() {}

    public static void set(ServerPlayer p) { CURRENT_PLAYER.set(p.getUUID()); }
    public static UUID get() { return CURRENT_PLAYER.get(); }
    public static void clear() { CURRENT_PLAYER.remove(); }
}