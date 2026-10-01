package com.mod.rctvgc;

import com.mod.rctvgc.commands.RctVgcCommands;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

@Mod(RCTTrainersVGC.MODID)
public class RCTTrainersVGC {
    public static final String MODID = "rcttrainersvgc";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RCTTrainersVGC(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("RCTTrainersVGC loaded");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent e) {
        RctVgcCommands.register(e);
    }
}
