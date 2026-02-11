package com.mod.rctvgc.mixins;

import com.gitlab.srcmc.rctapi.api.battle.BattleRules;
import com.mod.rctvgc.Config;
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
        // Se não quiser level set, não mexe nas rules
        if (!Config.ENABLE_LEVEL_SET.get()) return original;

        return new BattleRules.Builder()
                .withMaxItemUses(original.getMaxItemUses())
                .withHealPlayers(original.getHealPlayers())
                .withAdjustPlayerLevels(true)
                .withAdjustNPCLevels(true)
                .build();
    }
}

