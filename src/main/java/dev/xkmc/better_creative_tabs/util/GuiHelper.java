package dev.xkmc.better_creative_tabs.util;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class GuiHelper {
    public GuiHelper() {
    }

    public static void tooltip(GuiGraphicsExtractor g, Component text, int x, int y) {
        tooltip(g, List.of(text), null, x, y);
    }

    public static void tooltip(GuiGraphicsExtractor g, List<Component> text, int x, int y) {
        tooltip(g, text, null, x, y);
    }

    public static void tooltip(GuiGraphicsExtractor g, List<Component> text, @Nullable ClientTooltipComponent comp, int x, int y) {
        tooltip(g, text, comp, ItemStack.EMPTY, x, y);
    }

    public static void tooltip(GuiGraphicsExtractor g, List<Component> text, @Nullable ClientTooltipComponent comp, ItemStack stack, int x, int y) {
        List<ClientTooltipComponent> list = new ArrayList();

        for(Component e : text) {
            list.add(ClientTooltipComponent.create(e.getVisualOrderText()));
        }

        if (comp != null) {
            list.add(comp);
        }

        g.tooltip(Minecraft.getInstance().font, list, x, y, DefaultTooltipPositioner.INSTANCE, (Identifier)null, stack);
    }

    public static void blit(GuiGraphicsExtractor g, Identifier id, int x, int y, int u, int v, int w, int h) {
        g.blit(RenderPipelines.GUI_TEXTURED, id, x, y, (float)u, (float)v, w, h, 256, 256);
    }
}
