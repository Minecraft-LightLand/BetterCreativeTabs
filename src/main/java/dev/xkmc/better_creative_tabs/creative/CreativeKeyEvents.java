package dev.xkmc.bettercreativetabs.creative;

import dev.xkmc.bettercreativetabs.init.BetterCreativeTabs;
import dev.xkmc.bettercreativetabs.init.BCTClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = BetterCreativeTabs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CreativeKeyEvents {

	@SubscribeEvent
	public static void inputEvent(InputEvent.Key event) {
		if (event.getKey() != BCTClient.CREATIVE.getKey().getValue()) return;
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		if (!player.isCreative()) return;
		var screen = Minecraft.getInstance().screen;
		if (screen != null && !(screen instanceof CreativeModeInventoryScreen)) return;
		CreativeTabGroup.GROUPS = null;
		CreativeTabList.TABS = null;
		Minecraft.getInstance().setScreen(new CreativeIndexScreen(0));
	}

}
