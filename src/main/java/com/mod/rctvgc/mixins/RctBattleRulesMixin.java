package com.mod.rctvgc.mixins;

import com.gitlab.srcmc.rctapi.api.battle.BattleRules;
import com.gitlab.srcmc.rctmod.world.entities.TrainerMob;
import com.mod.rctvgc.Config;
import com.mod.rctvgc.prefs.BattleContext;
import com.mod.rctvgc.prefs.VgcPrefs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.UUID;

@Mixin(value = com.gitlab.srcmc.rctmod.api.RCTMod.class, remap = false)
public class RctBattleRulesMixin {

    @Inject(method = "makeBattle", at = @At("HEAD"))
    private void rctvgc$setCtx(TrainerMob mob, net.minecraft.world.entity.player.Player player, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir) {
        if (player instanceof net.minecraft.server.level.ServerPlayer sp) {
            com.mod.rctvgc.prefs.BattleContext.set(sp);
        }
    }

    @Inject(method = "makeBattle", at = @At("RETURN"))
    private void rctvgc$clearCtx(org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir) {
        com.mod.rctvgc.prefs.BattleContext.clear();
    }

    @ModifyArg(
            method = "makeBattle",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/gitlab/srcmc/rctapi/api/battle/BattleManager;startBattle(Ljava/util/List;Ljava/util/List;Lcom/gitlab/srcmc/rctapi/api/battle/BattleFormat;Lcom/gitlab/srcmc/rctapi/api/battle/BattleRules;)Ljava/util/UUID;"
            ),
            index = 3
    )
    private BattleRules forceAdjustLevels(BattleRules original) {
        UUID playerId = BattleContext.get();
        if (playerId == null) return original;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return original;

        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) return original;

        boolean levelSetEnabled = VgcPrefs.levelSetEnabled(player, true);
        if (!levelSetEnabled) return original; // não força adjust

        return new BattleRules.Builder()
                .withMaxItemUses(original.getMaxItemUses())
                .withHealPlayers(original.getHealPlayers())
                .withAdjustPlayerLevels(true)
                .withAdjustNPCLevels(true)
                .build();
    }
}

