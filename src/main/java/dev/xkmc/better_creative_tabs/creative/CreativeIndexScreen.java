package dev.xkmc.better_creative_tabs.creative;

import dev.xkmc.better_creative_tabs.mixin.CreativeModeInventoryScreenAccessor;
import dev.xkmc.better_creative_tabs.util.GuiHelper;
import dev.xkmc.better_creative_tabs.util.MenuLayoutConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CreativeIndexScreen extends Screen {

	private static final MenuLayoutConfig MANAGER = CreativeIndexLayout.get();
	private static final Component TITLE = Component.translatable("better-creative-tabs.creative_index");
	private static final int MAX = 9 * 9;

	public CreativeIndexScreen(int page) {
		this(page, "");
	}

	public CreativeIndexScreen(int page, String text) {
		this(TITLE, MANAGER, page, text, "grid");
	}

	private final List<CreativeModeTab> allTabs = CreativeTabList.getTabs();
	private final MenuLayoutConfig manager;
	private final String[] slots;

	private int page;
	private String initialText;
	private List<CreativeModeTab> tabs = new ArrayList<>(allTabs);
	private EditBox editBox;
	private boolean completeInit = false;

	private int imageWidth, imageHeight, leftPos, topPos;

	private CreativeModeTab hovered = null;

	protected CreativeIndexScreen(Component title, MenuLayoutConfig manager, int page, String text, String... slots) {
		super(title);
		this.page = page;
		this.manager = manager;
		this.slots = slots;
		this.initialText = text;

	}

	@Override
	protected void init() {
		completeInit = false;
		this.imageWidth = 176;
		this.imageHeight = manager.getHeight();
		this.leftPos = (this.width - imageWidth) / 2;
		this.topPos = (this.height - imageHeight) / 2;

		if (editBox == null) {
			editBox = new EditBox(font, leftPos + 8, topPos + 19, 80, 9,
					Component.translatable("itemGroup.search"));
			editBox.setValue(initialText);
			editBox.setMaxLength(50);
			editBox.setBordered(true);
			editBox.setVisible(true);
			editBox.setTextColor(16777215);
		}
		addRenderableWidget(editBox);
		rebuildTabList();

		int totalPage = (tabs.size() - 1) / MAX + 1;
		int x = (this.width + this.imageWidth) / 2 - 16,
				y = (this.height - this.imageHeight) / 2 + 4;
		int w = 10;
		int h = 11;
		page = Math.min(totalPage - 1, Math.max(0, page));
		if (page > 0) {
			addRenderableWidget(Button.builder(Component.literal("<"), e -> click(-1))
					.pos(x - w - 1, y).size(w, h).build());
		}
		if (page < totalPage - 1) {
			addRenderableWidget(Button.builder(Component.literal(">"), e -> click(1))
					.pos(x, y).size(w, h).build());
		}
		completeInit = true;
	}

	private void rebuildTabList() {
		String val = editBox.getValue().toLowerCase(Locale.ROOT);
		int oldPage = (tabs.size() - 1) / MAX + 1;
		tabs = allTabs.stream().filter(e ->
						e.getDisplayName().getString().toLowerCase(Locale.ROOT).contains(val) ||
								BuiltInRegistries.CREATIVE_MODE_TAB.getKey(e).toString().contains(val))
				.toList();
		int newPage = (tabs.size() - 1) / MAX + 1;
		if (completeInit && (page > 0 || newPage != oldPage)) {
			page = 0;
			reinit();
		}
	}

	@Override
	public void resize(int width, int height) {
		initialText = editBox.getValue();
		boolean isFocused = editBox.isFocused();
		super.resize(width, height);
		if (isFocused) setFocused(editBox);
	}

	private void reinit() {
		initialText = editBox.getValue();
		boolean isFocused = editBox.isFocused();
		init();
		if (isFocused) setFocused(editBox);
	}

	@Override
	public boolean charTyped(CharacterEvent event) {
		if (!editBox.isFocused()) return false;
		String s = editBox.getValue();
		if (editBox.charTyped(event)) {
			if (!Objects.equals(s, editBox.getValue())) {
				rebuildTabList();
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		String s = editBox.getValue();
		if (editBox.keyPressed(event)) {
			if (!Objects.equals(s, editBox.getValue())) {
				rebuildTabList();
			}
			return true;
		}
		if (editBox.isFocused() && editBox.isVisible()) {
			if (event.key() == 256) {
				editBox.setFocused(false);
			}
			return true;
		}
		return super.keyPressed(event);
	}

	private void click(int btn) {
		page += btn;
		reinit();
	}

	protected void renderLabels(GuiGraphicsExtractor g, int mx, int my) {
		int maxPage = (tabs.size() - 1) / MAX + 1;
		String count = " (" + (page + 1) + "/" + maxPage + ")";
		g.text(font, TITLE.copy().append(count), 8, 6, 0xFF404040, false);
	}

	@Nullable
	protected CreativeModeTab getStack(String comp, int x, int y) {
		int ind = x + y * 9 + page * MAX;
		if (ind >= tabs.size()) return null;
		return tabs.get(ind);
	}

	protected void renderTooltip(GuiGraphicsExtractor g, Font font, CreativeModeTab hovered, int mx, int my) {
		GuiHelper.tooltip(g, List.of(
				hovered.getDisplayName(),
				Component.literal(BuiltInRegistries.CREATIVE_MODE_TAB.getKey(hovered).toString())
						.withStyle(ChatFormatting.DARK_GRAY)
		), mx, my);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		if (super.mouseReleased(event)) {
			return true;
		}
		SlotResult result = findSlot(event.x(), event.y());
		if (result == null) return false;
		int ind = result.x() + result.y() * 9 + page * MAX;
		if (ind >= tabs.size()) return false;
		var screen = new CreativeModeInventoryScreen(this.minecraft.player,
				this.minecraft.player.connection.enabledFeatures(),
				this.minecraft.options.operatorItemsTab().get());
		Minecraft.getInstance().setScreen(screen);

		var scr = ((CreativeModeInventoryScreenAccessor) screen);
		var pages = scr.getPages();
		var tab = tabs.get(ind);
		for (var e : pages)
			if (e.getVisibleTabs().contains(tab)) {
				screen.setCurrentPage(e);
				scr.callSelectTab(tab);
				return true;
			}
		return true;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor g, int mx, int my, float pTick) {
		super.extractBackground(g, mx, my, pTick);
		this.renderBg(g, pTick, mx, my);
		g.pose().pushMatrix();
		g.pose().translate(leftPos, topPos);
		hovered = null;
		for (String c : slots) {
			renderSlotComp(g, c, mx, my);
		}
		this.renderLabels(g, mx, my);
		if (hovered != null) {
			g.pose().pushMatrix();
			g.pose().translate(-leftPos, -topPos);
			renderTooltip(g, font, hovered, mx, my);
			g.pose().popMatrix();
		}
		g.pose().popMatrix();
	}

	private static final Identifier SLOT_HIGHLIGHT_FRONT_SPRITE = Identifier.withDefaultNamespace("container/slot_highlight_front");

	private void renderSlotComp(GuiGraphicsExtractor g, String name, int mx, int my) {
		var comp = manager.getComp(name);
		for (int i = 0; i < comp.rx; i++) {
			for (int j = 0; j < comp.ry; j++) {
				int sx = comp.x + comp.w * i;
				int sy = comp.y + comp.h * j;
				var stack = getStack(name, i, j);
				if (stack == null) continue;
				this.renderSlot(g, sx, sy, stack.getIconItem());
				if (this.isHovering(name, i, j, mx, my)) {
					g.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_FRONT_SPRITE, sx - 4, sy - 4, 24, 24);
					hovered = stack;
				}
			}
		}
	}

	private void renderSlot(GuiGraphicsExtractor g, int x, int y, ItemStack stack) {
		String s = null;
		assert this.minecraft != null;
		assert this.minecraft.player != null;
		g.item(stack, x, y, x + y * this.imageWidth);
		g.itemDecorations(this.font, stack, x, y, s);
	}

	private void renderBg(GuiGraphicsExtractor stack, float pt, int mx, int my) {
		var sr = manager.new ScreenRenderer(this, leftPos, topPos, imageWidth, imageHeight);
		sr.start(stack);
	}

	private boolean isHovering(String slot, int i, int j, double mx, double my) {
		var comp = manager.getComp(slot);
		return this.isHovering(comp.x + comp.w * i, comp.y + comp.h * j, 16, 16, mx, my);
	}

	private boolean isHovering(int x, int y, int w, int h, double mx, double my) {
		int i = this.leftPos;
		int j = this.topPos;
		mx -= i;
		my -= j;
		return mx >= (x - 1) && mx < (x + w + 1) && my >= (y - 1) && my < (y + h + 1);
	}

	@Nullable
	protected SlotResult findSlot(double mx, double my) {
		for (String c : slots) {
			var comp = manager.getComp(c);
			for (int i = 0; i < comp.rx; i++) {
				for (int j = 0; j < comp.ry; j++) {
					if (this.isHovering(c, i, j, mx, my)) {
						return new SlotResult(c, i, j);
					}
				}
			}
		}
		return null;
	}

	public record SlotResult(String name, int x, int y) {
	}


}
