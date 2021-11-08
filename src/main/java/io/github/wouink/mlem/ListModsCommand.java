package io.github.wouink.mlem;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListModsCommand implements Command<CommandSource> {
	@Override
	public int run(CommandContext<CommandSource> context) {
		List<ModInfo> mods = ModList.get().getMods();
		ArrayList<String> modList = new ArrayList<String>();
		for(ModInfo mod : mods) {
			modList.add(String.format("%s;%s;%s;%s;%s%s",
					mod.getModId(),
					mod.getVersion(),
					mod.getDisplayName(),
					mod.getConfigElement("authors").isPresent() ? mod.getConfigElement("authors").get() : "",
					mod.getConfigElement("displayURL").isPresent() ? mod.getConfigElement("displayURL").get() : "",
					System.lineSeparator())
			);
		}

		try {
			final DateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
			File output = FMLPaths.GAMEDIR.get().resolve(String.format("modlist_%s.csv", dateFmt.format(new Date()))).toFile();
			output.getParentFile().mkdirs();
			output.createNewFile();
			FileWriter fr = new FileWriter(output);

			modList.sort(Collator.getInstance());
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("#modid;version;modname;authors;url" + System.lineSeparator());
			for(String line : modList) {
				stringBuilder.append(line);
			}

			fr.write(stringBuilder.toString());
			fr.close();

			ITextComponent fileComponent = new StringTextComponent(output.getName()).withStyle(TextFormatting.UNDERLINE).withStyle((style) ->
				style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, output.getAbsolutePath()))
			);
			context.getSource().sendSuccess(new TranslationTextComponent("modlist.saved").append(fileComponent), false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return SINGLE_SUCCESS;
	}
}
