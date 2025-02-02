package folk.sisby.portable_crafting;

import folk.sisby.portable_crafting.tabs.PortableCraftingTabProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

import static folk.sisby.portable_crafting.PortableCrafting.C2S_OPEN_PORTABLE_CRAFTING;

public class PortableCraftingClient implements ClientModInitializer {
	public static KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
		"key.portable_crafting.open_crafting_table",
		InputUtil.Type.KEYSYM,
		GLFW.GLFW_KEY_V,
		"key.categories.inventory"
	));

	public static boolean openCraftingTable() {
		if (ClientPlayNetworking.canSend(C2S_OPEN_PORTABLE_CRAFTING) && PortableCrafting.openCrafting(MinecraftClient.getInstance().player, Items.CRAFTING_TABLE.getDefaultStack(), false)) {
			ClientPlayNetworking.send(C2S_OPEN_PORTABLE_CRAFTING, PacketByteBufs.empty());
			return true;
		}
		return false;
	}

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(world -> {
			if (keyBinding == null || world == null) return;
			while (keyBinding.wasPressed()) {
				openCraftingTable();
			}
		});
		if (FabricLoader.getInstance().isModLoaded("inventory-tabs")) {
			PortableCraftingTabProvider.register();
		}
		PortableCrafting.LOGGER.info("[Portable Crafting Client] Initialised!");
	}
}
