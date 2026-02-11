package com.mod.rctvgc.mixins;

import com.cobblemon.mod.common.battles.BattleFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = com.gitlab.srcmc.rctapi.api.battle.BattleFormat.class, remap = false)
public class RctBattleFormatMixin {

    @Inject(method = "getCobblemonBattleFormat", at = @At("RETURN"), cancellable = true)
    public void forceVgcFormat(CallbackInfoReturnable<BattleFormat> cir) {
        // Formato que o RCT ia usar originalmente (talvez singles/doubles etc)
        BattleFormat original = cir.getReturnValue();

        // Base doubles gen 9 do Cobblemon
        BattleFormat doubles = BattleFormat.Companion.getGEN_9_DOUBLES();

        // Cria um novo BattleFormat (NÃO muta o singleton GEN_9_DOUBLES!)
        BattleFormat vgc = new BattleFormat(
                doubles.getMod(),
                doubles.getBattleType(),
                original.getRuleSet(),
                doubles.getGen(),
                50 // adjustLevel = 50
        );

        cir.setReturnValue(vgc);
    }
}
