package folk.sisby.portable_crafting_standalone;

import folk.sisby.portable_crafting_standalone.screens.PortableCraftingScreenHandler;
import folk.sisby.portable_crafting_standalone.compat.inventory_tabs.PortableCraftingTab;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortableCraftingStandalone implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Portable Crafting Standalone");

	public static final Text LABEL = new TranslatableText("container.portable_crafting_standalone.portable_crafting_table");

	public static final String ID = "portable_crafting_standalone";
	public static final Identifier ID_OPEN_CRAFTING_TABLE = new Identifier("portable_crafting_standalone", "open_crafting_table");

	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Initializing {}!", mod.metadata().name());

		if (QuiltLoader.isModLoaded("inventorytabs")) {
			PortableCraftingTab.touch();
		}

		PortableCraftingScreenHandler.touch();

		ServerPlayNetworking.registerGlobalReceiver(ID_OPEN_CRAFTING_TABLE, (server, player, handler, buf, sender) -> server.execute(() -> {
			if (player.getInventory().contains(new ItemStack(Blocks.CRAFTING_TABLE))) {
				player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, inv, p) -> new PortableCraftingScreenHandler(i, inv, ScreenHandlerContext.create(p.getWorld(), p.getBlockPos())), LABEL));
			}
		}));
	}
}
