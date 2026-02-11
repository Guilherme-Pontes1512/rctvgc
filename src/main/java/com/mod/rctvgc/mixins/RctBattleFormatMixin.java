package com.mod.rctvgc.mixins;

import com.cobblemon.mod.common.battles.BattleFormat;
import com.mod.rctvgc.prefs.BattleContext;
import com.mod.rctvgc.prefs.VgcPrefs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.UUID;

@Mixin(value = com.gitlab.srcmc.rctapi.api.battle.BattleFormat.class, remap = false)
public class RctBattleFormatMixin {

    @Inject(method = "getCobblemonBattleFormat", at = @At("RETURN"), cancellable = true)
    public void forceFormat(CallbackInfoReturnable<BattleFormat> cir) {
        BattleFormat original = cir.getReturnValue();

        UUID playerId = com.mod.rctvgc.prefs.BattleContext.getPlayerId();
        if (playerId == null) return;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        Player player = server.getPlayerList().getPlayer(playerId);
        if (player == null) return;

        boolean doublesEnabled = VgcPrefs.doublesEnabled(player, true);
        boolean levelSetEnabled = VgcPrefs.levelSetEnabled(player, true);
        int level = VgcPrefs.levelSet(player, 50);

        if (BattleContext.forceSingles()) {
            doublesEnabled = false;
        }

        if (!doublesEnabled && !levelSetEnabled) return;

        BattleFormat base = doublesEnabled
                ? BattleFormat.Companion.getGEN_9_DOUBLES()
                : original;

        var mergedRules = new HashSet<>(base.getRuleSet());
        mergedRules.addAll(original.getRuleSet());

        int adjustLevel = levelSetEnabled ? level : base.getAdjustLevel();

        BattleFormat out = new BattleFormat(
                base.getMod(),
                base.getBattleType(),
                mergedRules,
                base.getGen(),
                adjustLevel
        );

        cir.setReturnValue(out);
    }
}
