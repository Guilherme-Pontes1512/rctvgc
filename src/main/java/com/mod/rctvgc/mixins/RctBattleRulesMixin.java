package com.mod.rctvgc.mixins;

import com.gitlab.srcmc.rctapi.api.battle.BattleRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = com.gitlab.srcmc.rctmod.api.RCTMod.class, remap = false)
public class RctBattleRulesMixin {

    @ModifyArg(
            method = "makeBattle",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/gitlab/srcmc/rctapi/api/battle/BattleManager;startBattle(Ljava/util/List;Ljava/util/List;Lcom/gitlab/srcmc/rctapi/api/battle/BattleFormat;Lcom/gitlab/srcmc/rctapi/api/battle/BattleRules;)Ljava/util/UUID;"
            ),
            index = 3
    )
    private BattleRules forceAdjustLevels(BattleRules original) {
        // Preserva o que vier do datapack e só força as flags necessárias pro adjustLevel (do Cobblemon) ser aplicado
        return new BattleRules.Builder()
                .withMaxItemUses(original.getMaxItemUses())
                .withHealPlayers(original.getHealPlayers())
                .withAdjustPlayerLevels(true)
                .withAdjustNPCLevels(true)
                .build();
    }
}

