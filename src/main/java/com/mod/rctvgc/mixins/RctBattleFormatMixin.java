package com.mod.rctvgc.mixins;

import com.cobblemon.mod.common.battles.BattleFormat;
import com.mod.rctvgc.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;

@Mixin(value = com.gitlab.srcmc.rctapi.api.battle.BattleFormat.class, remap = false)
public class RctBattleFormatMixin {

    @Inject(method = "getCobblemonBattleFormat", at = @At("RETURN"), cancellable = true)
    public void forceFormat(CallbackInfoReturnable<BattleFormat> cir) {
        BattleFormat original = cir.getReturnValue();

        boolean doublesEnabled = Config.ENABLE_DOUBLES.get();
        boolean levelSetEnabled = Config.ENABLE_LEVEL_SET.get();
        int level = Config.LEVEL_SET.get();

        // Se nada está habilitado, não mexe
        if (!doublesEnabled && !levelSetEnabled) return;

        // Decide base (doubles ou mantém original)
        BattleFormat base = doublesEnabled
                ? BattleFormat.Companion.getGEN_9_DOUBLES()
                : original;

        // Merge rule sets (mantém regras que já existiam)
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
