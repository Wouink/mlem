package io.github.wouink.mlem;

import net.minecraft.command.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Mlem.MODID)
@OnlyIn(Dist.CLIENT)
public class Mlem {
	public static final String MODID = "mlem";

	public Mlem() {
		MinecraftForge.EVENT_BUS.register(this.getClass());
	}

	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("modlist").executes(new ListModsCommand()));
	}
}
