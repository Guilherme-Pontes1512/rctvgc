package com.mod.rctvgc.mixins;

import com.cobblemon.mod.common.Cobblemon;
import com.gitlab.srcmc.rctapi.api.battle.BattleRules;
import com.gitlab.srcmc.rctmod.api.RCTMod;
import com.gitlab.srcmc.rctmod.world.entities.TrainerMob;
import com.mod.rctvgc.Config;
import com.mod.rctvgc.prefs.BattleContext;
import com.mod.rctvgc.prefs.VgcPrefs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(value = com.gitlab.srcmc.rctmod.api.RCTMod.class, remap = false)
public class RctBattleRulesMixin {

    @Inject(method = "makeBattle", at = @At("HEAD"))
    private void rctvgc$setCtx(TrainerMob mob, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (!(player instanceof ServerPlayer sp)) return;

        int playerCount = 0;
        for (var pk : Cobblemon.INSTANCE.getStorage().getParty(sp)) {
            if (pk != null) playerCount++;
        }

        // Pega o time do NPC do mesmo jeito que o RCT pega
        var team = RCTMod.getInstance()
                .getTrainerManager()
                .getData(mob)
                .getTrainerTeam();

        int npcCount = team.getTeam().isEmpty() ? 0 : team.getTeam().size();

        boolean forceSingles = playerCount < 2 || npcCount < 2;

        BattleContext.set(sp, forceSingles);
    }

    @Inject(method = "makeBattle", at = @At("RETURN"))
    private void rctvgc$clearCtx(CallbackInfoReturnable<Boolean> cir) {
        BattleContext.clear();
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
        UUID playerId = BattleContext.getPlayerId();
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

